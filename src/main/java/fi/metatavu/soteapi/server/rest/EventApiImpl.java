package fi.metatavu.soteapi.server.rest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.event.EventController;
import fi.metatavu.soteapi.persistence.model.Event;
import fi.metatavu.soteapi.persistence.model.EventDescription;
import fi.metatavu.soteapi.persistence.model.EventTitle;
import fi.metatavu.soteapi.rest.translate.EventTranslator;

@RequestScoped
@Stateful
public class EventApiImpl implements EventsApi {

  @Inject
  private EventTranslator eventTranslator;
  
  @Inject
  private EventController eventController;
  
  @Inject
  private ResponseController responseController;

  @Override
  public Response findEvent(Long eventId) throws Exception {
    Event event = eventController.findEventById(eventId);
    if (event == null) {
      return responseController.respondNotFound();
    }

    if (event.getArchived()) {
      return responseController.responseGone();
    }
    
    List<EventTitle> eventTitles = eventController.listEventTitlesByEvent(event);
    List<EventDescription> eventDatas = eventController.listEventDescriptionByEvent(event);
    return responseController.respondOk(eventTranslator.translateEvent(event, eventTitles, eventDatas));
  }
  
  @Override
  public Response listEvents(String endsAfter, String startsBefore, String categorySlug, Long firstResult, Long maxResults) throws Exception {
    List<Event> eventEntities = eventController.listEventsByStartLessThanOrEqualToEndGreaterThanOrEqualToAndCategory(parseTime(startsBefore), parseTime(endsAfter), categorySlug, firstResult, maxResults);
  
    List<List<EventTitle>> eventTitleEntities = new ArrayList<>(eventEntities.size());
    List<List<EventDescription>> eventDescriptionEntities = new ArrayList<>(eventEntities.size());
    for (Event eventEntity : eventEntities) {
      eventTitleEntities.add(eventController.listEventTitlesByEvent(eventEntity));
      eventDescriptionEntities.add(eventController.listEventDescriptionByEvent(eventEntity));
    }
  
    return responseController.respondOk(eventTranslator.translateEvents(eventEntities, eventTitleEntities, eventDescriptionEntities));
  }

  private OffsetDateTime parseTime(String timeString) {
    if (StringUtils.isEmpty(timeString)) {
      return null;
    }
    
    return OffsetDateTime.parse(timeString);
  }

}
