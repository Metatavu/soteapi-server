package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentData_;

/**
 * DAO class for Content content
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ContentDataDAO extends AbstractDAO<ContentData> {

  
  /**
   * Creates new content content
   *  
   * @param language content language
   * @param value content value
   * @param content content that content is linked to
   * @return created content content
   */
  public ContentData create(String language, String value, Content content) {
    ContentData contentData = new ContentData();
    contentData.setLanguage(language);
    contentData.setContent(content);
    contentData.setValue(value);
    return persist(contentData);
  }
  
  /**
   * Lists all content contents for single content
   * 
   * @param content Content which contents are listed
   * @return List of content contents
   */
  public List<ContentData> listByContent(Content content) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ContentData> criteria = criteriaBuilder.createQuery(ContentData.class);
    Root<ContentData> root = criteria.from(ContentData.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(ContentData_.content), content)
    );
    
    TypedQuery<ContentData> query = entityManager.createQuery(criteria);
    
    return query.getResultList();
  }
  
  /**
   * Updates language
   *
   * @param language language
   * @return updated contentContent
   */
   public ContentData updateLanguage(ContentData contentData, String language) {
     contentData.setLanguage(language);
     return persist(contentData);
   }

  /**
   * Updates value
   *
   * @param value value
   * @return updated contentContent
   */
   public ContentData updateValue(ContentData contentData, String value) {
     contentData.setValue(value);
     return persist(contentData);
   }

  /**
   * Updates content
   *
   * @param content content
   * @return updated contentContent
   */
   public ContentData updateContent(ContentData contentData, Content content) {
     contentData.setContent(content);
     return persist(contentData);
   }
  
}
