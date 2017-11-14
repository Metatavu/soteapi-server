package fi.metatavu.soteapi.event;

import java.time.OffsetDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.persistence.dao.EventDAO;
import fi.metatavu.soteapi.persistence.dao.EventDescriptionDAO;
import fi.metatavu.soteapi.persistence.dao.EventTitleDAO;
import fi.metatavu.soteapi.persistence.model.Event;
import fi.metatavu.soteapi.persistence.model.EventDescription;
import fi.metatavu.soteapi.persistence.model.EventTitle;

/**
 * Controller for operations related to events
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class EventController {

  @Inject
  private EventDAO eventDAO;
  
  @Inject
  private EventTitleDAO eventTitleDAO;
  
  @Inject
  private EventDescriptionDAO eventDescriptionDAO;
  
  /**
   * Creates new event
   *
   * @param originId originId
   * @param slug slug
   * @param category category
   * @param startTime startTime
   * @param endTime endTime
   * @param allDay allDay
   * @return created event
   */
   public Event createEvent(String originId, String slug, String category, OffsetDateTime startTime, OffsetDateTime endTime, Boolean allDay) {
     return eventDAO.create(originId, slug, category, startTime, endTime, allDay, false);
   }
  
  /**
   * Creates new event title
   * 
   * @param language title language
   * @param value title value
   * @param event event that title is found from
   * @return create event title
   */
  public EventTitle createEventTitle(String language, String value, Event event) {
    return eventTitleDAO.create(language, value, event);
  }
  
  /**
   * Creates new event event
   * 
   * @param language event language
   * @param value event value
   * @param event event that event is found from
   * @return created event event
   */
  public EventDescription createEventDescription(String language, String value, Event event) {
    return eventDescriptionDAO.create(language, value, event);
  }
  
  /**
   * Finds event with eventId
   * 
   * @param eventId Events id
   * @return Event
   */
  public Event findEventById(Long eventId) {
    return eventDAO.findById(eventId);
  }
  
  /**
   * Finds event with origin id
   * 
   * @param originId Events origin id
   * @return Event
   */
  public Event findEventByOriginId(String originId) {
    return eventDAO.findByOriginId(originId);
  }
  
  /**
   * Lists events that have start less than equal to, end time greater or equal to specified time and have a specified category. 
   * 
   * All parameters may be nulled. Nulled parameters will be ignored.
   * 
   * @param startTime start date. If null is specified the criteria is ignored
   * @param endTime end date. If null is specified the criteria is ignored
   * @param category catogory slug. If null is specified the criteria is ignored
   * @param firstResult first result
   * @param maxResults max results
   * @return List of events that have end time greater or equal to specified time
   */
  public List<Event> listEventsByStartLessThanOrEqualToEndGreaterThanOrEqualToAndCategory(OffsetDateTime startTime, OffsetDateTime endTime, String category, Long firstResult, Long maxResults) {
    return eventDAO.listByStartLessThanOrEqualToEndGreaterThanOrEqualToCategoryAndArchived(startTime, endTime, category, Boolean.FALSE, firstResult, maxResults);
  }

  /**
   * Lists event titles by event
   * 
   * @param event which titles are to be listed
   * @return List of event titles in different languages
   */
  public List<EventTitle> listEventTitlesByEvent(Event event) {
    return eventTitleDAO.listByEvent(event);
  }
  
  /**
   * Lists event event by event in all available languages
   * 
   * @param event which events are to be listed
   * @return List of event events in all available languages
   */
  public List<EventDescription> listEventDescriptionByEvent(Event event) {
    return eventDescriptionDAO.listByEvent(event);
  }

  /**
   * Update event
   *
   * @param originId originId
   * @param slug slug
   * @param category category
   * @param startTime startTime
   * @param endTime endTime
   * @param allDay allDay
   * @param modifier modifier
   * @return updated event
   */
  public Event updateEvent(Event event, String originId, String slug, String category, OffsetDateTime startTime, OffsetDateTime endTime, Boolean allDay) {
    eventDAO.updateOriginId(event, originId);
    eventDAO.updateSlug(event, slug);
    eventDAO.updateCategory(event, category);
    eventDAO.updateStartTime(event, startTime);
    eventDAO.updateEndTime(event, endTime);
    eventDAO.updateAllDay(event, allDay);
    return event;
  }

  /**
   * Updates event archived
   * 
   * @param eventEntity event
   * @param archived archived
   * @return updated event
   */
  public Event updateEventArchived(Event eventEntity, Boolean archived) {
    return eventDAO.updateArchived(eventEntity, archived);
  }

  /**
   * Update eventTitle
   *
   * @param eventTitle event title to update
   * @param language language
   * @param value value
   * @param event event
   * @return updated eventTitle
   */
  public EventTitle updateEventTitle(EventTitle eventTitle, String language, String value, Event event) {
    eventTitleDAO.updateLanguage(eventTitle, language);
    eventTitleDAO.updateValue(eventTitle, value);
    eventTitleDAO.updateEvent(eventTitle, event);
    return eventTitle;
  }
  
  /**
   * Update eventEvent
   *
   * @param eventDescription event data to update
   * @param language language
   * @param value value
   * @param event event
   * @return updated eventEvent
   */
  public EventDescription updateEventDescription(EventDescription eventDescription, String language, String value, Event event) {
    eventDescriptionDAO.updateLanguage(eventDescription, language);
    eventDescriptionDAO.updateValue(eventDescription, value);
    eventDescriptionDAO.updateEvent(eventDescription, event);
    return eventDescription;
  }
}
