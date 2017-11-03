package fi.metatavu.soteapi.persistence.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Category;
import fi.metatavu.soteapi.persistence.model.CategoryTitle;
import fi.metatavu.soteapi.persistence.model.CategoryTitle_;

/**
 * DAO class for Content titles
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class CategoryTitleDAO extends AbstractDAO<CategoryTitle> {

  /**
   * Creates new category title
   * 
   * @param category category this title is connected to
   * @param language language
   * @param value value
   * @return created category title
   */
  public CategoryTitle create(Category category, String language, String value) {
    CategoryTitle categoryTitle = new CategoryTitle();
    categoryTitle.setCategory(category);
    categoryTitle.setLanguage(language);
    categoryTitle.setValue(value);
    return persist(categoryTitle);
  }

  /**
   * Updates language
   * 
   * @param categoryTitle category title to update
   * @param language language
   * @return updated categoryTitle
   */
  public CategoryTitle updateLanguage(CategoryTitle categoryTitle, String language) {
    categoryTitle.setLanguage(language);
    return persist(categoryTitle);
  }

  /**
   * Updates value
   * 
   * @param categoryTitle category title to update
   * @param value value
   * @return updated categoryTitle
   */
  public CategoryTitle updateValue(CategoryTitle categoryTitle, String value) {
    categoryTitle.setValue(value);
    return persist(categoryTitle);
  }

  /**
   * Updates category
   *
   * @param categoryTitle category title to update
   * @param category category
   * @return updated categoryTitle
   */
  public CategoryTitle updateCategory(CategoryTitle categoryTitle, Category category) {
    categoryTitle.setCategory(category);
    return persist(categoryTitle);
  }

  /**
   * lists category titles by category
   * 
   * @param category category
   * @return list of category titles
   */
  public List<CategoryTitle> listByCategory(Category category) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CategoryTitle> criteria = criteriaBuilder.createQuery(CategoryTitle.class);
    Root<CategoryTitle> root = criteria.from(CategoryTitle.class);

    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(CategoryTitle_.category), category));

    TypedQuery<CategoryTitle> query = entityManager.createQuery(criteria);

    return query.getResultList();
  }
}
