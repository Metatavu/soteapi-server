package fi.metatavu.soteapi.persistence.dao;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
   * @param originId Contents origin Id
   * @param slug Contents slug
   * @param contentType content type
   * @param parent parent content
   * @param category category slug
   * @return created content
   */
  public Content create(String originId, String slug, ContentType contentType, Content parent, String category) {
    Content content = new Content();
    content.setOriginId(originId);
    content.setSlug(slug);
    content.setContentType(contentType);
    content.setParent(parent);
    content.setCategory(category);
    return persist(content);
  }

  /**
   * List content by type, optionally filtered by first result and max results
   * 
   * @param types content type
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listByTypes(List<ContentType> types, Integer firstResult, Integer maxResults) {
    if (types == null || types.isEmpty()) {
      return Collections.emptyList();
    }
    
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Content> criteria = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = criteria.from(Content.class);

    criteria.select(root);
    criteria.where(
        root.get(Content_.contentType).in(types)
    );
    
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
   * Lists contents by parent, optionally filtered by first result and max results
   * 
   * @param parent parent content
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listByParent(Content parent, Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Content> criteria = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = criteria.from(Content.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Content_.parent), parent)
    );
    
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
   * Lists root contents optionally filtered by first result and max results
   * 
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listRootContents(Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Content> criteria = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = criteria.from(Content.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.isNull(root.get(Content_.parent))
    );
    
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
   * Lists root contents by type optionally filtered by first result and max results
   * 
   * @param types content types
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listRootContentsByType(List<ContentType> types, Integer firstResult, Integer maxResults) {
    if (types == null || types.isEmpty()) {
      return Collections.emptyList();
    }
    
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Content> criteria = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = criteria.from(Content.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.isNull(root.get(Content_.parent)),
        root.get(Content_.contentType).in(types)
      )
    );
    
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
   * Lists contents by type and parent, optionally filtered by first result and max results
   * 
   * @param parent parent content
   * @param types content types
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listByParentAndType(Content parent, List<ContentType> types, Integer firstResult, Integer maxResults) {
    if (types == null || types.isEmpty()) {
      return Collections.emptyList();
    }
    
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Content> criteria = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = criteria.from(Content.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        root.get(Content_.contentType).in(types),
        criteriaBuilder.equal(root.get(Content_.parent), parent)
      )
    );
    
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
}
