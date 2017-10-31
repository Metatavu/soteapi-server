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
   * Creates new page content
   *  
   * @param language content language
   * @param value content value
   * @param page page that content is linked to
   * @return created page content
   */
  public PageContent create(String language, String value, Page page) {
    PageContent pageContent = new PageContent();
    pageContent.setLanguage(language);
    pageContent.setPage(page);
    pageContent.setValue(value);
    return persist(pageContent);
  }
  
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
  
  /**
   * Updates language
   *
   * @param language language
   * @return updated pageContent
   */
   public PageContent updateLanguage(PageContent pageContent, String language) {
     pageContent.setLanguage(language);
     return persist(pageContent);
   }

  /**
   * Updates value
   *
   * @param value value
   * @return updated pageContent
   */
   public PageContent updateValue(PageContent pageContent, String value) {
     pageContent.setValue(value);
     return persist(pageContent);
   }

  /**
   * Updates page
   *
   * @param page page
   * @return updated pageContent
   */
   public PageContent updatePage(PageContent pageContent, Page page) {
     pageContent.setPage(page);
     return persist(pageContent);
   }
  
}
