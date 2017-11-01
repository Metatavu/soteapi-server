package fi.metatavu.soteapi.persistence.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.ContentImageData;
import fi.metatavu.soteapi.persistence.model.ContentImageData_;
import fi.metatavu.soteapi.persistence.model.ContentImageMeta;

/**
 * DAO class for Content image data
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ContentImageDataDAO extends AbstractDAO<ContentImageData> {

  /**
   * Finds content image data with content image meta
   * 
   * @param contentImageMeta Image metadata describing the image
   * @return Image data
   */
  public ContentImageData findByContentImageMeta(ContentImageMeta contentImageMeta) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ContentImageData> criteria = criteriaBuilder.createQuery(ContentImageData.class);
    Root<ContentImageData> root = criteria.from(ContentImageData.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(ContentImageData_.contentImageMeta), contentImageMeta)
    );
    
    TypedQuery<ContentImageData> query = entityManager.createQuery(criteria);
    
    return query.getSingleResult();
  }

}
