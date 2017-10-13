package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Page;
import fi.metatavu.soteapi.persistence.model.PageImageMeta;
import fi.metatavu.soteapi.persistence.model.PageImageMeta_;

/**
 * DAO class for Page image metadata
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class PageImageMetaDAO extends AbstractDAO<PageImageMeta> {

  /**
   * Find page image metadata with image id and page
   * 
   * @param imageId Id of the image
   * @param page Page the image belongs to
   * @return Image metadata
   */
  public PageImageMeta findByIdAndPage(Long imageId, Page page) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PageImageMeta> criteria = criteriaBuilder.createQuery(PageImageMeta.class);
    Root<PageImageMeta> root = criteria.from(PageImageMeta.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(PageImageMeta_.id), imageId),
        criteriaBuilder.equal(root.get(PageImageMeta_.page), page)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  /**
   * List all image metadatas from single page
   * 
   * @param page Page to list the images from
   * @return List of images metadatas
   */
  public List<PageImageMeta> listByPage(Page page) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PageImageMeta> criteria = criteriaBuilder.createQuery(PageImageMeta.class);
    Root<PageImageMeta> root = criteria.from(PageImageMeta.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(PageImageMeta_.page), page)
    );
    
    TypedQuery<PageImageMeta> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
  /**
   * List images with page and type
   * 
   * @param page Page to list the images from
   * @param type Image type
   * @return List of image metadatas
   */
  public List<PageImageMeta> listByPageAndType(Page page, String type) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PageImageMeta> criteria = criteriaBuilder.createQuery(PageImageMeta.class);
    Root<PageImageMeta> root = criteria.from(PageImageMeta.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(PageImageMeta_.type), type),
        criteriaBuilder.equal(root.get(PageImageMeta_.page), page)
      )
    );
    
    TypedQuery<PageImageMeta> query = entityManager.createQuery(criteria);
    return query.getResultList();
  }
  
}
