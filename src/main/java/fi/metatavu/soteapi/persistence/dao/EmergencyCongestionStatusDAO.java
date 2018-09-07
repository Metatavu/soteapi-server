package fi.metatavu.soteapi.persistence.dao;

import java.time.OffsetDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.EmergencyCongestionStatus;
import fi.metatavu.soteapi.persistence.model.EmergencyCongestionStatus_;

/**
 * DAO class for EmergencyCongestionStatuses
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class EmergencyCongestionStatusDAO extends AbstractDAO<EmergencyCongestionStatus> {

  /**
   * Creates new emergencyCongestionStatus entity
   * @param value value
   * @param created created
   * 
   * @return created emergency congestion status
   */
  @SuppressWarnings ("squid:S00107")
  public EmergencyCongestionStatus create(Integer value, OffsetDateTime created) {
    EmergencyCongestionStatus emergencyCongestionStatus = new EmergencyCongestionStatus();
    emergencyCongestionStatus.setCreated(created);
    emergencyCongestionStatus.setValue(value);
    return persist(emergencyCongestionStatus);
  }
  
  /**
   * Lists emergencyCongestionStatuses. Sorted by created asc
   * 
   * @param firstResult first result
   * @param maxResults max results
   * @return list of emergency congestion statuses
   */
  public List<EmergencyCongestionStatus> listSortByCreatedAsc(Long firstResult, Long maxResults) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EmergencyCongestionStatus> criteria = criteriaBuilder.createQuery(EmergencyCongestionStatus.class);
    Root<EmergencyCongestionStatus> root = criteria.from(EmergencyCongestionStatus.class);

    criteria.select(root);
    criteria.orderBy(criteriaBuilder.asc(root.get(EmergencyCongestionStatus_.created)));
    TypedQuery<EmergencyCongestionStatus> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult.intValue());
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults.intValue());
    }

    return query.getResultList();
  }
  
  /**
   * Lists emergencyCongestionStatuses. Sorted by created asc
   * 
   * @param firstResult first result
   * @param maxResults max results
   * @return list of emergency congestion statuses
   */
  public List<EmergencyCongestionStatus> listSortByCreatedDesc(Long firstResult, Long maxResults) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<EmergencyCongestionStatus> criteria = criteriaBuilder.createQuery(EmergencyCongestionStatus.class);
    Root<EmergencyCongestionStatus> root = criteria.from(EmergencyCongestionStatus.class);

    criteria.select(root);
    criteria.orderBy(criteriaBuilder.desc(root.get(EmergencyCongestionStatus_.created)));
    TypedQuery<EmergencyCongestionStatus> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult.intValue());
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults.intValue());
    }

    return query.getResultList();
  }

}
