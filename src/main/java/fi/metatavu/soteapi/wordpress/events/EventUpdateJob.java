package fi.metatavu.soteapi.wordpress.events;

import java.time.OffsetDateTime;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.event.EventController;
import fi.metatavu.soteapi.persistence.model.Event;
import fi.metatavu.soteapi.persistence.model.EventDescription;
import fi.metatavu.soteapi.persistence.model.EventTitle;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractWordpressJob;

public class EventUpdateJob extends AbstractWordpressJob {
  
  @Inject
  private EventUpdateQueue eventUpdateQueue;
  
  @Inject
  private EventController eventController;
  
  @Override
  protected void execute() {
    EventUpdateTask eventUpdateTask = eventUpdateQueue.next();
    if (eventUpdateTask != null) {
      performTask(eventUpdateTask);
    }
  }
  
  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.EVENTS_SYNC_ENABLED;
  }
  
  private void performTask(EventUpdateTask task) {
    EventUpdateTaskModel eventUpdateModel = task.getPostUpdateModel();

    if (eventUpdateModel == null) {
      return;
    }

    String originId = eventUpdateModel.getOriginId();
    Event eventEntity = eventController.findEventByOriginId(originId);
    
    if (eventEntity != null) {
      updateExistingPage(eventEntity, eventUpdateModel);
      return;
    }
    
    createNewEvent(eventUpdateModel);
  }
  
  private void createNewEvent(EventUpdateTaskModel eventUpdateModel) {
    if (eventUpdateModel == null) {
      return;
    }
    
    String originId = eventUpdateModel.getOriginId();
    String slug = eventUpdateModel.getSlug();
    String eventTitle = eventUpdateModel.getTitle();
    String eventDescription = eventUpdateModel.getDescription();
    String category = eventUpdateModel.getCategory();
    OffsetDateTime startTime = parseDateTime(eventUpdateModel.getStartTime());
    OffsetDateTime endTime = parseDateTime(eventUpdateModel.getEndTime());
    Boolean allDay = eventUpdateModel.getAllDay();

    Event eventEntity = eventController.createEvent(originId, slug, category, startTime, endTime, allDay);
    
    if (eventEntity.getArchived()) {
      eventController.updateEventArchived(eventEntity, false);
    }
    
    if (StringUtils.isNotEmpty(eventTitle)) {
      eventController.createEventTitle(WordpressConsts.DEFAULT_LANGUAGE, eventTitle, eventEntity);
    }

    if (StringUtils.isNotEmpty(eventDescription)) {
      eventController.createEventDescription(WordpressConsts.DEFAULT_LANGUAGE, eventDescription, eventEntity);
    }
  }
  
  private OffsetDateTime parseDateTime(String time) {
    if (time == null) {
      return null;
    }
    
    return OffsetDateTime.parse(time);
  }

  private void updateExistingPage(Event eventEntity, EventUpdateTaskModel eventUpdateModel) {
    String originId = eventUpdateModel.getOriginId();
    String slug = eventUpdateModel.getSlug();
    String eventTitle = eventUpdateModel.getTitle();
    String eventDescription = eventUpdateModel.getDescription();
    String category = eventUpdateModel.getCategory();
    OffsetDateTime startTime = parseDateTime(eventUpdateModel.getStartTime());
    OffsetDateTime endTime = parseDateTime(eventUpdateModel.getEndTime());
    Boolean allDay = eventUpdateModel.getAllDay();
    
    if (StringUtils.isNotEmpty(eventDescription)) {
      EventDescription eventDataEntity = eventController.listEventDescriptionByEvent(eventEntity)
        .stream()
        .filter(event -> event.getLanguage().equals(WordpressConsts.DEFAULT_LANGUAGE))
        .findAny()
        .orElse(null);
      
      if (eventDataEntity != null) {
        eventController.updateEventDescription(eventDataEntity, WordpressConsts.DEFAULT_LANGUAGE, eventDescription, eventEntity);
      } else {
        eventController.createEventDescription(WordpressConsts.DEFAULT_LANGUAGE, eventDescription, eventEntity);
      }
    }
    
    if (StringUtils.isNotEmpty(eventTitle)) {
      updateExistingPageTitle(eventEntity, eventTitle);
    }

    eventController.updateEvent(eventEntity, originId, slug, category, startTime, endTime, allDay);
  }

  private void updateExistingPageTitle(Event eventEntity, String eventTitleEvent) {
    EventTitle eventTitleEntity = eventController.listEventTitlesByEvent(eventEntity)
      .stream()
      .filter(title -> title.getLanguage().equals(WordpressConsts.DEFAULT_LANGUAGE))
      .findAny()
      .orElse(null);
    
    if (eventTitleEntity != null) {
      eventController.updateEventTitle(eventTitleEntity, WordpressConsts.DEFAULT_LANGUAGE, eventTitleEvent, eventEntity);
    } else {
      eventController.createEventTitle(WordpressConsts.DEFAULT_LANGUAGE, eventTitleEvent, eventEntity);
    }
  }
  
}
