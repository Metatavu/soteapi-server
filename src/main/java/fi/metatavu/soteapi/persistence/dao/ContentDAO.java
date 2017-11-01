package fi.metatavu.soteapi.persistence.dao;

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
   * @return created content
   */
  public Content create(String originId, String slug, ContentType contentType, Content parent) {
    Content content = new Content();
    content.setOriginId(originId);
    content.setSlug(slug);
    content.setContentType(contentType);
    content.setParent(parent);
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
  
}
