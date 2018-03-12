package fi.metatavu.soteapi.wordpress.events;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.afrozaar.wordpress.wpapi.v2.Client;
import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.metatavu.soteapi.category.CategoryController;
import fi.metatavu.soteapi.persistence.model.Category;
import fi.metatavu.soteapi.settings.SystemSettingController;
import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.events.model.DateDetails;
import fi.metatavu.soteapi.wordpress.events.model.Event;
import fi.metatavu.soteapi.wordpress.events.model.Events;
import fi.metatavu.soteapi.wordpress.events.model.Term;
import fi.metatavu.soteapi.wordpress.tasks.AbstractListJob;

@ApplicationScoped
public class EventListJob extends AbstractListJob<Event, EventListTask> {
  
  private static final String CONTEXT = "/tribe/events/v1";

  @Inject
  private Logger logger;
  
  @Inject
  private EventListQueue eventListQueue;
  
  @Inject
  private EventUpdateQueue eventUpdateQueue;

  @Inject
  private SystemSettingController systemSettingController;

  @Inject
  private CategoryController categoryController;
  
  @Override
  protected AbstractSoteApiTaskQueue<EventListTask> getQueue() {
    return eventListQueue;
  }
  
  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.EVENTS_SYNC_ENABLED;
  }
  
  @Override
  protected EventListTask createTask(int page) {
    EventListTask newTask = new EventListTask();
    newTask.setPriority(Boolean.FALSE);
    newTask.setUniqueId(String.format("wp-events-list-%d", page));
    newTask.setPage(page);
    return newTask;
  }
  
  @Override
  protected Wordpress getWordpressClient() {
    String url = systemSettingController.getSettingValue(WordpressConsts.URL_SETTING);
    if (StringUtils.isNotBlank(url)) {
      return new Client(CONTEXT, url, "", "", WordpressConsts.USE_PERMALINK_ENDPOINT, WordpressConsts.DEBUG_CLIENT);
    }
    
    return null;
  }

  /**
   * Get total number of pages
   * 
   * @param postType type of wordpress post
   * @return total number of pages for post type
   */
  @Override
  protected int getNumberOfPages() {
    Events events = listEvents(1);
    if (events != null) {
      return events.getTotalPages();
    }
    
    return 0;
  }

  private Events listEvents(int page) {
    String url = String.format("%s?page=%d", getEndPointUri(), page);
    
    try {
      ResponseEntity<String> responseEntity = getWordpressClient().doCustomExchange(url, HttpMethod.GET, String.class, new Object[0], null, null, null);
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(responseEntity.getBody(), Events.class);
    } catch (HttpClientErrorException e) {
      HttpStatus statusCode = e.getStatusCode();
      if (statusCode != null && statusCode == HttpStatus.NOT_FOUND) {
        if (logger.isDebugEnabled()) {
          logger.debug(String.format("Could not find event's page %d", page));
        }
      } else {
        if (logger.isErrorEnabled()) {
          logger.error(String.format("Failed to list events from URL %s", url), e);
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error(String.format("Failed to list events from URL %s", url), e);
      }
    }
    
    return null;
  }
  
  /**
   * Get data from page
   * 
   * @param postType Type of wordpress post
   * @param page Page to extract the data from
   * @return list of posts
   */
  @Override
  protected List<Event> getDataFromPage(int page) {
    Events events = listEvents(page);
    if (events != null) {
      return events.getEvents();
    }
    
    return Collections.emptyList();
  }
  
  @Override
  protected void process(Event eventData) {
    if (eventData == null) {
      return;
    }

    String title = eventData.getTitle();
    String description = eventData.getDescription();
    String slug = extractSlug(eventData.getUrl());
    String originId = eventData.getId().toString();
    Term category = getCategory(eventData.getCategories());
    String categorySlug = getCategorySlug(category);
    String categoryOriginId = getCategoryOriginId(category);
    OffsetDateTime startTime = getDateTime(eventData.getStartDateDetails(), eventData.getTimezone());
    OffsetDateTime endTime = getDateTime(eventData.getEndDateDetails(), eventData.getTimezone());
    Boolean allDay = eventData.isAllDay();
    
    if (categoryOriginId != null && categoryController.findCategoryByOriginId(categoryOriginId) == null) {
      Category createdCategory = categoryController.createCategory(categoryOriginId, categorySlug);
      categoryController.createCategoryTitle(createdCategory, "FI", getCategoryName(category));
    }
    
    EventUpdateTaskModel updateTaskModel = new EventUpdateTaskModel(originId, title, description, slug, categorySlug, formatTime(startTime), formatTime(endTime), allDay);
    
    EventUpdateTask task = new EventUpdateTask();
    task.setPostUpdateModel(updateTaskModel);
    task.setPriority(false);
    task.setUniqueId(String.format("wp-event-update-%d", eventData.getId()));
    
    eventUpdateQueue.enqueueTask(task);
  }

  private String formatTime(OffsetDateTime time) {
    if (time == null) {
      return null;
    }
    
    return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(time);
  }

  private OffsetDateTime getDateTime(DateDetails startDateDetails, String timeZone) {
    Integer year = startDateDetails.getYear();
    Integer month = startDateDetails.getMonth();
    Integer dayOfMonth = startDateDetails.getDay();
    Integer hour = startDateDetails.getHour();
    Integer minute = startDateDetails.getMinutes();
    
    LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
    if (StringUtils.isNotBlank(timeZone)) {
      ZoneId zoneId = ZoneId.of(timeZone);
      if (zoneId != null) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime.toOffsetDateTime();
      }
    }

    return OffsetDateTime.of(year, month, dayOfMonth, hour, minute, 0, 0, ZoneOffset.UTC);
  }

  @Override
  protected String getEndPointUri() {
    return "/events";
  }
  
  private Term getCategory(List<Term> categories) {
    if (categories == null || categories.isEmpty()) {
      return null;
    }
    
    return categories.get(0);
  }
  
  private String getCategoryOriginId(Term term) {
    if (term == null) {
      return null;
    }
    
    return term.getId().toString();
    
  }

  private String getCategorySlug(Term term) {
    if (term == null) {
      return null;
    }
    
    return term.getSlug();
  }
  
  private String getCategoryName(Term term) {
    if (term == null) {
      return null;
    }
    
    return term.getName();
  }
  
  /**
   * Extracts slug from url
   * 
   * @param url url
   * @return slug
   */
  private String extractSlug(String url) {
    try {
      return FilenameUtils.getName(FilenameUtils.normalizeNoEndSeparator(new java.net.URL(url).getPath()));
    } catch (MalformedURLException e) {
      logger.error(String.format("Failed to resolve slug from url %s", url));
    }
    
    return null;
  }
    
}
