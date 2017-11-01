package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Page;
import fi.metatavu.soteapi.persistence.model.PageTitle;
import fi.metatavu.soteapi.persistence.model.PageTitle_;

/**
 * DAO class for Page titles
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class PageTitleDAO extends AbstractDAO<PageTitle> {

  /**
   * Creates new page title entity
   * 
   * @param language Page title language
   * @param value Page title value
   * @param page Page that title is for
   * @return Created page title
   */
  public PageTitle create(String language, String value, Page page) {
    PageTitle pageTitle = new PageTitle();
    pageTitle.setLanguage(language);
    pageTitle.setValue(value);
    pageTitle.setPage(page);
    return persist(pageTitle);
  }
  
  /**
   * Lists all page titles for single page
   * 
   * @param page Page which titles are listed
   * @return List of page titles
   */
  public List<PageTitle> listByPage(Page page) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<PageTitle> criteria = criteriaBuilder.createQuery(PageTitle.class);
    Root<PageTitle> root = criteria.from(PageTitle.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(PageTitle_.page), page)
    );
    
    TypedQuery<PageTitle> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
  /**
   * Updates language
   *
   * @param language language
   * @return updated pageTitle
   */
   public PageTitle updateLanguage(PageTitle pageTitle, String language) {
     pageTitle.setLanguage(language);
     return persist(pageTitle);
   }

  /**
   * Updates value
   *
   * @param value value
   * @return updated pageTitle
   */
   public PageTitle updateValue(PageTitle pageTitle, String value) {
     pageTitle.setValue(value);
     return persist(pageTitle);
   }

  /**
   * Updates page
   *
   * @param page page
   * @return updated pageTitle
   */
   public PageTitle updatePage(PageTitle pageTitle, Page page) {
     pageTitle.setPage(page);
     return persist(pageTitle);
   }
  
}
