package fi.metatavu.soteapi.persistence.dao;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Event;
import fi.metatavu.soteapi.persistence.model.Event_;

/**
 * DAO class for Events
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class EventDAO extends AbstractDAO<Event> {

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
  public Event create(String originId, String slug, String category, OffsetDateTime startTime, OffsetDateTime endTime, Boolean allDay) {
   Event event = new Event();
   event.setOriginId(originId);
   event.setSlug(slug);
   event.setCategory(category);
   event.setStartTime(startTime);
   event.setEndTime(endTime);
   event.setAllDay(allDay);
   return persist(event);
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
  public List<Event> listByStartLessThanOrEqualToEndGreaterThanOrEqualToAndCategory(OffsetDateTime startTime, OffsetDateTime endTime, String category, Long firstResult, Long maxResults) {
    EntityManager entityManager = getEntityManager();
  
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Event> criteria = criteriaBuilder.createQuery(Event.class);
    Root<Event> root = criteria.from(Event.class);
    
    List<Predicate> restrictions = new ArrayList<>();
  
    if (startTime != null) {
      restrictions.add(criteriaBuilder.lessThanOrEqualTo(root.get(Event_.startTime), startTime));
    }
  
    if (endTime != null) {
      restrictions.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Event_.endTime), endTime));
    }
    
    if (category != null) {
      restrictions.add(criteriaBuilder.equal(root.get(Event_.category), category));
    }
    
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(restrictions.toArray(new Predicate[0]))
    );
  
    criteria.orderBy(criteriaBuilder.asc(root.get(Event_.startTime)));
    
    TypedQuery<Event> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult.intValue());
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults.intValue());
    }
  
    return query.getResultList();
  }
  
  /**
   * Finds event by origin id
   * 
   * @param originId origin id
   * @return Event or null if not found
   */
  public Event findByOriginId(String originId) {
    EntityManager entityManager = getEntityManager();
  
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Event> criteria = criteriaBuilder.createQuery(Event.class);
    Root<Event> root = criteria.from(Event.class);
  
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Event_.originId), originId)
    );
    
    TypedQuery<Event> query = entityManager.createQuery(criteria);
    
    return getSingleResult(query);
  }
  
  /**
  * Updates originId
  *
  * @param originId originId
  * @return updated event
  */
  public Event updateOriginId(Event event, String originId) {
    event.setOriginId(originId);
    return persist(event);
  }
  
  /**
  * Updates slug
  *
  * @param slug slug
  * @return updated event
  */
  public Event updateSlug(Event event, String slug) {
    event.setSlug(slug);
    return persist(event);
  }
  
  /**
  * Updates category
  *
  * @param category category
  * @return updated event
  */
  public Event updateCategory(Event event, String category) {
    event.setCategory(category);
    return persist(event);
  }
  
  /**
  * Updates startTime
  *
  * @param startTime startTime
  * @return updated event
  */
  public Event updateStartTime(Event event, OffsetDateTime startTime) {
    event.setStartTime(startTime);
    return persist(event);
  }
  
  /**
  * Updates endTime
  *
  * @param endTime endTime
  * @return updated event
  */
  public Event updateEndTime(Event event, OffsetDateTime endTime) {
    event.setEndTime(endTime);
    return persist(event);
  }
  
  /**
  * Updates allDay
  *
  * @param allDay allDay
  * @return updated event
  */
  public Event updateAllDay(Event event, Boolean allDay) {
    event.setAllDay(allDay);
    return persist(event);
  }

}
