package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentImageMeta;
import fi.metatavu.soteapi.persistence.model.ContentImageMeta_;

/**
 * DAO class for Content image metadata
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ContentImageMetaDAO extends AbstractDAO<ContentImageMeta> {

  /**
   * Find content image metadata with image id and content
   * 
   * @param imageId Id of the image
   * @param content Content the image belongs to
   * @return Image metadata
   */
  public ContentImageMeta findByIdAndContent(Long imageId, Content content) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ContentImageMeta> criteria = criteriaBuilder.createQuery(ContentImageMeta.class);
    Root<ContentImageMeta> root = criteria.from(ContentImageMeta.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ContentImageMeta_.id), imageId),
        criteriaBuilder.equal(root.get(ContentImageMeta_.content), content)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  /**
   * List all image metadatas from single content
   * 
   * @param content Content to list the images from
   * @return List of images metadatas
   */
  public List<ContentImageMeta> listByContent(Content content) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ContentImageMeta> criteria = criteriaBuilder.createQuery(ContentImageMeta.class);
    Root<ContentImageMeta> root = criteria.from(ContentImageMeta.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(ContentImageMeta_.content), content)
    );
    
    TypedQuery<ContentImageMeta> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
  /**
   * List images with content and type
   * 
   * @param content Content to list the images from
   * @param type Image type
   * @return List of image metadatas
   */
  public List<ContentImageMeta> listByContentAndType(Content content, String type) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ContentImageMeta> criteria = criteriaBuilder.createQuery(ContentImageMeta.class);
    Root<ContentImageMeta> root = criteria.from(ContentImageMeta.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ContentImageMeta_.type), type),
        criteriaBuilder.equal(root.get(ContentImageMeta_.content), content)
      )
    );
    
    TypedQuery<ContentImageMeta> query = entityManager.createQuery(criteria);
    return query.getResultList();
  }
  
}
