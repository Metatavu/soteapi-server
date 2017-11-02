package fi.metatavu.soteapi.category;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.persistence.dao.CategoryDAO;
import fi.metatavu.soteapi.persistence.dao.CategoryTitleDAO;
import fi.metatavu.soteapi.persistence.model.Category;
import fi.metatavu.soteapi.persistence.model.CategoryTitle;

/**
 * Controller for operations related to categories
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class CategoryController {

  @Inject
  private CategoryDAO categoryDAO;
  
  @Inject
  private CategoryTitleDAO categoryTitleDAO;

  
  /**
   * Creates new category
   * 
   * @param originId origin id
   * @param slug slug
   * @return created category
   */
  public Category createCategory(String originId, String slug) {
    return categoryDAO.create(originId, slug);
  }
  
  /**
   * Creates new category title
   * 
   * @param category category the title is connected to
   * @param language language
   * @param value value
   * @return created category title
   */
  public CategoryTitle createCategoryTitle(Category category, String language, String value) {
    return categoryTitleDAO.create(category, language, value);
  }
  
  /**
   * Finds category by origin id
   * 
   * @param originId origin id
   * @return category or null if not found
   */
  public Category findCategoryByOriginId(String originId) {
    return categoryDAO.findByOriginId(originId);
  }
  
  /**
   * Lists categories optionally filtered by first result and max results
   * 
   * @param firstResult first result
   * @param maxResults max results
   * @return list of categories
   */
  public List<Category> listCategories(Integer firstResult, Integer maxResults) {
    return categoryDAO.listAll(firstResult, maxResults);
  }
  
  /**
   * Lists category titles by category
   * 
   * @param category category
   * @return list of category titles
   */
  public List<CategoryTitle> listCategoryTitlesByCategory(Category category) {
    return categoryTitleDAO.listByCategory(category);
  }
  
  /**
   * Update category
   *
   * @param category category to update
   * @param originId originId
   * @param slug slug
   * @return updated category
   */
  public Category updateCategory(Category category, String originId, String slug) {
    categoryDAO.updateOriginId(category, originId);
    categoryDAO.updateSlug(category, slug);
    return category;
  }
  
  /**
   * Update categoryTitle
   *
   * @param categoryTitle category title to update
   * @param language language
   * @param value value
   * @param category category
   * @return updated categoryTitle
   */
  public CategoryTitle updateCategoryTitle(CategoryTitle categoryTitle, String language, String value, Category category) {
    categoryTitleDAO.updateLanguage(categoryTitle, language);
    categoryTitleDAO.updateValue(categoryTitle, value);
    categoryTitleDAO.updateCategory(categoryTitle, category);
    return categoryTitle;
  }

  
}
