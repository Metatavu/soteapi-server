package fi.metatavu.soteapi.wordpress.events;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.afrozaar.wordpress.wpapi.v2.Client;
import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.metatavu.soteapi.settings.SystemSettingController;
import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.events.model.DateDetails;
import fi.metatavu.soteapi.wordpress.events.model.Event;
import fi.metatavu.soteapi.wordpress.events.model.Events;
import fi.metatavu.soteapi.wordpress.events.model.Term;
import fi.metatavu.soteapi.wordpress.tasks.AbstractListJob;

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
    try {
      ResponseEntity<String> responseEntity = getWordpressClient().doCustomExchange(String.format("%s&%d", getEndPointUri(), page), HttpMethod.GET, String.class, new Object[0], null, null, null);
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(responseEntity.getBody(), Events.class);
    } catch (IOException e) {
      logger.error("Failed to list events", e);
    };
    
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

    ZoneOffset timeZone = getTimeZone(eventData.getTimezone());
    String title = eventData.getTitle();
    String description = eventData.getDescription();
    String slug = extractSlug(eventData.getUrl());
    String originId = eventData.getId().toString();
    String category = getCategorySlug(eventData.getCategories());
    OffsetDateTime startTime = getDateTime(eventData.getStartDateDetails(), timeZone);
    OffsetDateTime endTime = getDateTime(eventData.getEndDateDetails(), timeZone);
    Boolean allDay = eventData.isAllDay();
    
    EventUpdateTaskModel updateTaskModel = new EventUpdateTaskModel(originId, title, description, slug, category, formatTime(startTime), formatTime(endTime), allDay);
    
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

  private ZoneOffset getTimeZone(String timezone) {
    if (StringUtils.isBlank(timezone)) {
      return ZoneOffset.UTC;
    }

    return ZoneOffset.of(timezone);
  }

  private OffsetDateTime getDateTime(DateDetails startDateDetails, ZoneOffset timeZone) {
    Integer year = startDateDetails.getYear();
    Integer month = startDateDetails.getMonth();
    Integer dayOfMonth = startDateDetails.getDay();
    Integer hour = startDateDetails.getHour();
    Integer minute = startDateDetails.getMinutes();
    
    return OffsetDateTime.of(year, month, dayOfMonth, hour, minute, 0, 0, timeZone);
  }

  @Override
  protected String getEndPointUri() {
    return "/events";
  }

  private String getCategorySlug(List<Term> categories) {
    if (categories == null || categories.isEmpty()) {
      return null;
    }
    
    return categories.get(0).getSlug();
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
