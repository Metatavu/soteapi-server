package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.persistence.model.ContentTitle_;

/**
 * DAO class for Content titles
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ContentTitleDAO extends AbstractDAO<ContentTitle> {

  /**
   * Creates new content title entity
   * 
   * @param language Content title language
   * @param value Content title value
   * @param content Content that title is for
   * @return Created content title
   */
  public ContentTitle create(String language, String value, Content content) {
    ContentTitle contentTitle = new ContentTitle();
    contentTitle.setLanguage(language);
    contentTitle.setValue(value);
    contentTitle.setContent(content);
    return persist(contentTitle);
  }
  
  /**
   * Lists all content titles for single content
   * 
   * @param content Content which titles are listed
   * @return List of content titles
   */
  public List<ContentTitle> listByContent(Content content) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ContentTitle> criteria = criteriaBuilder.createQuery(ContentTitle.class);
    Root<ContentTitle> root = criteria.from(ContentTitle.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(ContentTitle_.content), content)
    );
    
    TypedQuery<ContentTitle> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
  /**
   * Updates language
   *
   * @param language language
   * @return updated contentTitle
   */
   public ContentTitle updateLanguage(ContentTitle contentTitle, String language) {
     contentTitle.setLanguage(language);
     return persist(contentTitle);
   }

  /**
   * Updates value
   *
   * @param value value
   * @return updated contentTitle
   */
   public ContentTitle updateValue(ContentTitle contentTitle, String value) {
     contentTitle.setValue(value);
     return persist(contentTitle);
   }

  /**
   * Updates content
   *
   * @param content content
   * @return updated contentTitle
   */
   public ContentTitle updateContent(ContentTitle contentTitle, Content content) {
     contentTitle.setContent(content);
     return persist(contentTitle);
   }
  
}
