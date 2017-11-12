package fi.metatavu.soteapi.rest.translate;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.persistence.model.EventDescription;
import fi.metatavu.soteapi.persistence.model.EventTitle;
import fi.metatavu.soteapi.server.rest.model.Event;
import fi.metatavu.soteapi.server.rest.model.LocalizedValue;

/**
 * Translator for event related translations
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class EventTranslator extends AbstractTranslator {
  
  /**
   * Translates database event entity to REST event 
   * 
   * @param eventEntity Database event entity to be translated
   * @param eventTitleEntities Database entities for titles related to the event
   * @return Event translated to the REST event
   */
  public Event translateEvent(fi.metatavu.soteapi.persistence.model.Event eventEntity, List<EventTitle> eventTitleEntities, List<EventDescription> eventDescriptionEntities) {
    if (eventEntity == null) {
      return null;
    }

    LocalDate startDate = this.getDate(eventEntity.getStartTime());
    LocalDate endDate = this.getDate(eventEntity.getEndTime());
    String startTime = eventEntity.getAllDay() ? null : this.formatTime(eventEntity.getStartTime());
    String endTime = eventEntity.getAllDay() ? null : this.formatTime(eventEntity.getStartTime());

    Event event = new Event();
    event.setAllDay(eventEntity.getAllDay());
    event.setCategory(eventEntity.getCategory());
    event.setDescription(translateEventDescriptions(eventDescriptionEntities));
    event.setEndDate(endDate);
    event.setEndTime(endTime);
    event.setId(eventEntity.getId());
    event.setSlug(eventEntity.getSlug());
    event.setStartDate(startDate);
    event.setStartTime(startTime);
    event.setTitle(translateEventTitles(eventTitleEntities));
    
    return event;
  }

  /**
   * Translates list of events to REST entities
   * 
   * @param eventEntities List of database event enties
   * @param eventTitleEntities List of list of event title entities
   * @return List of translated event entities
   */
  public List<Event> translateEvents(List<fi.metatavu.soteapi.persistence.model.Event> eventEntities, List<List<EventTitle>> eventTitleEntities, List<List<EventDescription>> eventDescriptionEntities) {
    List<Event> results = new ArrayList<>(eventEntities.size());
    Iterator<fi.metatavu.soteapi.persistence.model.Event> eventEntityIterator = eventEntities.iterator();
    Iterator<List<EventTitle>> eventTitleEntitiesIterator = eventTitleEntities.iterator();
    Iterator<List<EventDescription>> eventDescriptionEntitiesIterator = eventDescriptionEntities.iterator();
    while (eventEntityIterator.hasNext() && eventTitleEntitiesIterator.hasNext()) {
      results.add(translateEvent(eventEntityIterator.next(), eventTitleEntitiesIterator.next(), eventDescriptionEntitiesIterator.next()));
    }
    
    return results;
  }
  
  /**
   * Translates database event event entities to REST entities
   * 
   * @param eventEventEntities Entities to be translates
   * @return translated entities
   */
  private List<LocalizedValue> translateEventDescriptions(List<EventDescription> eventEventEntities) {
    List<LocalizedValue> results = new ArrayList<>(eventEventEntities.size());
    for (EventDescription eventEventEntity : eventEventEntities) {
      results.add(translateEventDescription(eventEventEntity));
    }
    return results;
  }
  
  private String formatTime(OffsetDateTime time) {
    if (time != null) {
      return null;
    }
    
    return DateTimeFormatter.ISO_OFFSET_TIME.format(time);
  }

  private LocalDate getDate(OffsetDateTime time) {
    if (time != null) {
      return time.toLocalDate();
    }
    
    return null;
  }
  
  private LocalizedValue translateEventDescription(EventDescription eventEventEntity) {
    LocalizedValue localizedValue = new LocalizedValue();
    localizedValue.setLanguage(eventEventEntity.getLanguage());
    localizedValue.setValue(eventEventEntity.getValue());
    return localizedValue;
  }
  
  private LocalizedValue translateEventTitle(EventTitle eventTitleEntity) {
    LocalizedValue localizedValue = new LocalizedValue();
    localizedValue.setLanguage(eventTitleEntity.getLanguage());
    localizedValue.setValue(eventTitleEntity.getValue());
    return localizedValue;
  }
  
  private List<LocalizedValue> translateEventTitles(List<EventTitle> eventTitleEntities) {
    List<LocalizedValue> results = new ArrayList<>(eventTitleEntities.size());
    for (EventTitle eventTitleEntity : eventTitleEntities) {
      results.add(translateEventTitle(eventTitleEntity));
    }
    return results;
  }
  
}
