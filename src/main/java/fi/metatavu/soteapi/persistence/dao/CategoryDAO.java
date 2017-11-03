package fi.metatavu.soteapi.persistence.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.Category;
import fi.metatavu.soteapi.persistence.model.Category_;

/**
 * DAO class for Content titles
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class CategoryDAO extends AbstractDAO<Category> {

  /**
   * Creates new category
   * 
   * @param originId origin id
   * @param slug slug
   * @return created category
   */
  public Category create(String originId, String slug) {
    Category category = new Category();
    category.setOriginId(originId);
    category.setSlug(slug);
    return persist(category);
  }


  /**
   * Finds category by origin id
   * 
   * @param originId origin id
   * @return category or null if not found
   */
  public Category findByOriginId(String originId) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Category> criteria = criteriaBuilder.createQuery(Category.class);
    Root<Category> root = criteria.from(Category.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Category_.originId), originId)
    );
    
    TypedQuery<Category> query = entityManager.createQuery(criteria);
    
    return getSingleResult(query);
  }
  
  /**
   * Updates originId
   *
   * @param category category to update
   * @param originId originId
   * @return updated category
   */
   public Category updateOriginId(Category category, String originId) {
     category.setOriginId(originId);
     return persist(category);
   }

  /**
   * Updates slug
   *
   * @param category category to update
   * @param slug slug
   * @return updated category
   */
   public Category updateSlug(Category category, String slug) {
     category.setSlug(slug);
     return persist(category);
   }  
}
