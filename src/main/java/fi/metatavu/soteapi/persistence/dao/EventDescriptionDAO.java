package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Event;
import fi.metatavu.soteapi.persistence.model.EventDescription;
import fi.metatavu.soteapi.persistence.model.EventDescription_;

/**
 * DAO class for Event event
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class EventDescriptionDAO extends AbstractDAO<EventDescription> {

  /**
   * Creates new event event
   *  
   * @param language event language
   * @param value event value
   * @param event event that event is linked to
   * @return created event event
   */
  public EventDescription create(String language, String value, Event event) {
    EventDescription eventDescription = new EventDescription();
    eventDescription.setLanguage(language);
    eventDescription.setEvent(event);
    eventDescription.setValue(value);
    return persist(eventDescription);
  }
  
  /**
   * Lists all event events for single event
   * 
   * @param event Event which events are listed
   * @return List of event events
   */
  public List<EventDescription> listByEvent(Event event) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EventDescription> criteria = criteriaBuilder.createQuery(EventDescription.class);
    Root<EventDescription> root = criteria.from(EventDescription.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(EventDescription_.event), event)
    );
    
    TypedQuery<EventDescription> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
  /**
   * Updates language
   *
   * @param language language
   * @return updated eventEvent
   */
   public EventDescription updateLanguage(EventDescription eventDescription, String language) {
     eventDescription.setLanguage(language);
     return persist(eventDescription);
   }

  /**
   * Updates value
   *
   * @param value value
   * @return updated eventEvent
   */
   public EventDescription updateValue(EventDescription eventDescription, String value) {
     eventDescription.setValue(value);
     return persist(eventDescription);
   }

  /**
   * Updates event
   *
   * @param event event
   * @return updated eventEvent
   */
   public EventDescription updateEvent(EventDescription eventDescription, Event event) {
     eventDescription.setEvent(event);
     return persist(eventDescription);
   }
  
}
