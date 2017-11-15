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

import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.persistence.model.Content_;

/**
 * DAO class for Contents
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ContentDAO extends AbstractDAO<Content> {

  /**
   * Creates new content entity
   * 
   * @param origin Contents origin
   * @param originId Contents origin Id
   * @param slug Contents slug
   * @param contentType content type
   * @param parent parent content
   * @param category category slug
   * @param created created
   * @param modified modified
   * @param orderIndex order index
   * @param archived archived
   * @return created content
   */
  @SuppressWarnings ("squid:S00107")
  public Content create(String origin, String originId, String slug, ContentType contentType, Content parent, String category, OffsetDateTime created, OffsetDateTime modified, Long orderIndex, Boolean archived) {
    Content content = new Content();
    content.setOrigin(origin);
    content.setOriginId(originId);
    content.setSlug(slug);
    content.setContentType(contentType);
    content.setParent(parent);
    content.setCategory(category);
    content.setCreated(created);
    content.setModified(modified);
    content.setOrderIndex(orderIndex);
    content.setArchived(archived);
    return persist(content);
  }

  /**
   * Finds content by origin id
   * 
   * @param originId origin id
   * @return Content or null if not found
   */
  public Content findByOriginId(String originId) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Content> criteria = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = criteria.from(Content.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Content_.originId), originId)
    );
    
    TypedQuery<Content> query = entityManager.createQuery(criteria);
    
    return getSingleResult(query);
  }
  
  /**
   * Lists contents by type and parent, filtered by first result and max results.
   * 
   * All parameters can be nulled. Nulled parameters will be ignored.
   *
   * @param parent parent content
   * @param types content types
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listByParentAndTypesAndArchived(Content parent, List<ContentType> types, Boolean archived, Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Content> criteria = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = criteria.from(Content.class);

    List<Predicate> restrictions = new ArrayList<>();
    
    if (archived != null) {
      restrictions.add(criteriaBuilder.equal(root.get(Content_.archived), archived));
    }
    
    if (parent != null) {
      restrictions.add(criteriaBuilder.equal(root.get(Content_.parent), parent)); 
    }
    
    if (types != null && !types.isEmpty()) {
      restrictions.add(root.get(Content_.contentType).in(types));
    }

    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(restrictions.toArray(new Predicate[0]))
    );
    
    criteria.orderBy(criteriaBuilder.asc(root.get(Content_.orderIndex)));
    
    TypedQuery<Content> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }

    return query.getResultList();
  }
  

  /**
   * Lists contents by type and null parent, filtered by first result and max results.
   * 
   * All parameters can be nulled. Nulled parameters will be ignored.
   *
   * @param parent parent content
   * @param types content types
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listByNullParentAndTypesAndArchived(List<ContentType> types, Boolean archived, Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Content> criteria = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = criteria.from(Content.class);

    List<Predicate> restrictions = new ArrayList<>();
    
    restrictions.add(criteriaBuilder.equal(root.get(Content_.archived), archived));
    restrictions.add(criteriaBuilder.isNull(root.get(Content_.parent))); 
    
    if (types != null && !types.isEmpty()) {
      restrictions.add(root.get(Content_.contentType).in(types));
    }

    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(restrictions.toArray(new Predicate[0]))
    );
    
    criteria.orderBy(criteriaBuilder.asc(root.get(Content_.orderIndex)));
    
    TypedQuery<Content> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }

    return query.getResultList();
  }

  /**
   * Lists origin ids by origin and archived
   *
   * @param origin origin
   * @return list of origin ids
   */
  public List<String> listOriginIdsByContentTypeOriginAndArchived(ContentType contentType, String origin, Boolean archived) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<String> criteria = criteriaBuilder.createQuery(String.class);
    Root<Content> root = criteria.from(Content.class);
    
    criteria.select(root.get(Content_.originId));
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(Content_.archived), archived),
        criteriaBuilder.equal(root.get(Content_.contentType), contentType),
        criteriaBuilder.equal(root.get(Content_.origin), origin)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  /**
   * Updates origin
   * 
   * @param content content to update
   * @param origin origin
   * @return updated content
   */
   public Content updateOrigin(Content content, String origin) {
     content.setOrigin(origin);
     return persist(content);
   }
  
  /**
   * Updates originId
   * 
   * @param content content to update
   * @param originId originId
   * @return updated content
   */
   public Content updateOriginId(Content content, String originId) {
     content.setOriginId(originId);
     return persist(content);
   }

  /**
   * Updates slug
   *
   * @param content content to update
   * @param slug slug
   * @return updated content
   */
   public Content updateSlug(Content content, String slug) {
     content.setSlug(slug);
     return persist(content);
   }

  /**
   * Updates parent
   *
   * @param content content to update
   * @param parent parent
   * @return updated content
   */
   public Content updateParent(Content content, Content parent) {
     content.setParent(parent);
     return persist(content);
   }

  /**
   * Updates contentType
   *
   * @param content content to update
   * @param contentType contentType
   * @return updated content
   */
   public Content updateContentType(Content content, ContentType contentType) {
     content.setContentType(contentType);
     return persist(content);
   }
  
  /**
   * Updates category
   *
   * @param content content to update
   * @param category category
   * @return updated content
   */
   public Content updateCategory(Content content, String category) {
     content.setCategory(category);
     return persist(content);
   }
   
  /**
   * Updates orderIndex
   *
   * @param orderIndex orderIndex
   * @return updated content
   */
  public Content updateOrderIndex(Content content, Long orderIndex) {
    content.setOrderIndex(orderIndex);
    return persist(content);
  }
    
  /**
   * Updates orderIndex
   *
   * @param orderIndex orderIndex
   * @return updated content
   */
   public Content updateArchived(Content content, Boolean archived) {
     content.setArchived(archived);
     return persist(content);
  }
   
  /**
   * Updates created
   *
   * @param created created
   * @return updated content
   */
   public Content updateCreated(Content content, OffsetDateTime created) {
     content.setCreated(created);
     return persist(content);
   }
   
  /**
   * Updates modified
   *
   * @param modified modified
   * @return updated content
   */
   public Content updateModified(Content content, OffsetDateTime modified) {
     content.setModified(modified);
     return persist(content);
   }
}
