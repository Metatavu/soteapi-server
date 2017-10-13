package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Page;
import fi.metatavu.soteapi.persistence.model.PageContent;
import fi.metatavu.soteapi.persistence.model.PageContent_;

/**
 * DAO class for Page content
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class PageContentDAO extends AbstractDAO<PageContent> {

  /**
   * Lists all page contents for single page
   * 
   * @param page Page which contents are listed
   * @return List of page contents
   */
  public List<PageContent> listByPage(Page page) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PageContent> criteria = criteriaBuilder.createQuery(PageContent.class);
    Root<PageContent> root = criteria.from(PageContent.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(PageContent_.page), page)
    );
    
    TypedQuery<PageContent> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
}
