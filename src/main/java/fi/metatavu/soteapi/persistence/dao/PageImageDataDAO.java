package fi.metatavu.soteapi.persistence.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.PageImageData;
import fi.metatavu.soteapi.persistence.model.PageImageData_;
import fi.metatavu.soteapi.persistence.model.PageImageMeta;

/**
 * DAO class for Page image data
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class PageImageDataDAO extends AbstractDAO<PageImageData> {

  /**
   * Finds page image data with page image meta
   * 
   * @param pageImageMeta Image metadata describing the image
   * @return Image data
   */
  public PageImageData findByPageImageMeta(PageImageMeta pageImageMeta) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PageImageData> criteria = criteriaBuilder.createQuery(PageImageData.class);
    Root<PageImageData> root = criteria.from(PageImageData.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(PageImageData_.pageImageMeta), pageImageMeta)
    );
    
    TypedQuery<PageImageData> query = entityManager.createQuery(criteria);
    
    return query.getSingleResult();
  }

}
