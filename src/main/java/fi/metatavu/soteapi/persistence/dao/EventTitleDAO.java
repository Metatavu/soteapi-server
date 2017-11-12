package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Event;
import fi.metatavu.soteapi.persistence.model.EventTitle;
import fi.metatavu.soteapi.persistence.model.EventTitle_;

/**
 * DAO class for Event titles
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class EventTitleDAO extends AbstractDAO<EventTitle> {

  /**
   * Creates new event title entity
   * 
   * @param language Event title language
   * @param value Event title value
   * @param event Event that title is for
   * @return Created event title
   */
  public EventTitle create(String language, String value, Event event) {
    EventTitle eventTitle = new EventTitle();
    eventTitle.setLanguage(language);
    eventTitle.setValue(value);
    eventTitle.setEvent(event);
    return persist(eventTitle);
  }
  
  /**
   * Lists all event titles for single event
   * 
   * @param event Event which titles are listed
   * @return List of event titles
   */
  public List<EventTitle> listByEvent(Event event) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EventTitle> criteria = criteriaBuilder.createQuery(EventTitle.class);
    Root<EventTitle> root = criteria.from(EventTitle.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(EventTitle_.event), event)
    );
    
    TypedQuery<EventTitle> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
  /**
   * Updates language
   *
   * @param language language
   * @return updated eventTitle
   */
   public EventTitle updateLanguage(EventTitle eventTitle, String language) {
     eventTitle.setLanguage(language);
     return persist(eventTitle);
   }

  /**
   * Updates value
   *
   * @param value value
   * @return updated eventTitle
   */
   public EventTitle updateValue(EventTitle eventTitle, String value) {
     eventTitle.setValue(value);
     return persist(eventTitle);
   }

  /**
   * Updates event
   *
   * @param event event
   * @return updated eventTitle
   */
   public EventTitle updateEvent(EventTitle eventTitle, Event event) {
     eventTitle.setEvent(event);
     return persist(eventTitle);
   }
  
}
