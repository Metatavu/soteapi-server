package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Page;
import fi.metatavu.soteapi.persistence.model.Page_;

/**
 * DAO class for Pages
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class PageDAO extends AbstractDAO<Page> {

  /**
   * Creates new page entity
   * 
   * @param originId Pages origin Id
   * @param slug Pages slug
   * @return created page
   */
  public Page create(String originId, String slug) {
    Page page = new Page();
    page.setOriginId(originId);
    page.setSlug(slug);
    return persist(page);
  }
  

  /**
   * Finds page by origin id
   * 
   * @param originId origin id
   * @return Page or null if not found
   */
  public List<Page> listByOriginId(String originId) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Page> criteria = criteriaBuilder.createQuery(Page.class);
    Root<Page> root = criteria.from(Page.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Page_.originId), originId)
    );
    
    TypedQuery<Page> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
  /**
   * Updates originId
   *
   * @param originId originId
   * @return updated page
   */
   public Page updateOriginId(Page page, String originId) {
     page.setOriginId(originId);
     return persist(page);
   }

  /**
   * Updates slug
   *
   * @param slug slug
   * @return updated page
   */
   public Page updateSlug(Page page, String slug) {
     page.setSlug(slug);
     return persist(page);
   }
  
}
