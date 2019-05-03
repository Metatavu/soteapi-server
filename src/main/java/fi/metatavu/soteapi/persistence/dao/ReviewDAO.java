package fi.metatavu.soteapi.persistence.dao;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

import fi.metatavu.soteapi.persistence.model.Review;
import fi.metatavu.soteapi.persistence.model.Review_;
import fi.metatavu.soteapi.server.rest.model.ReviewListSort;

/**
 * DAO class for reviews
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ReviewDAO extends AbstractDAO<Review> {

  /**
   * Creates new review
   * 
   * @param originId origin id
   * @param productId product id
   * @param rating rating
   * @param reviewText review text
   * @param created created
   * @param location location
   *
   * @return created review
   */
  public Review create(Long originId, Long productId, Integer rating, String reviewText, OffsetDateTime created, String location) {
    Review review = new Review();
    review.setOriginId(originId);
    review.setProductId(productId);
    review.setLocation(location);
    review.setCreated(created);
    review.setModified(OffsetDateTime.now());
    review.setRating(rating);
    review.setReview(reviewText);

    return persist(review);
  }

  /**
   * Lists reviews
   * 
   * @param productId product id
   * @param maxRating max rating
   * @param minRating min rating
   * @param sort sort
   * @param firstResult first result
   * @param maxResults max results
   * @return list of reviews
   */
  public List<Review> listReviews(Long productId, Integer maxRating, Integer minRating, ReviewListSort sort, Long firstResult, Long maxResults) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Review> criteria = criteriaBuilder.createQuery(Review.class);
    Root<Review> root = criteria.from(Review.class);

    List<Predicate> restrictions = buildRestrictions(root, criteriaBuilder, productId, maxRating, minRating);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(restrictions.toArray(new Predicate[0]))
    );

    if (sort.equals(ReviewListSort.DESC)) {
      criteria.orderBy(criteriaBuilder.desc(root.get(Review_.created)));
    } else {
      criteria.orderBy(criteriaBuilder.asc(root.get(Review_.created)));
    }

    TypedQuery<Review> query = entityManager.createQuery(criteria);
    if (firstResult != null) {
      query.setFirstResult(firstResult.intValue());
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults.intValue());
    }
  
    return query.getResultList();
  }

  /**
   * Counts reviews by filter
   * 
   * @param productId product id
   * @param maxRating max rating
   * @param minRating min rating
   * @return count of filtered reviews
   */
  public Long countReviews(Long productId, Integer maxRating, Integer minRating) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<Review> root = criteria.from(Review.class);

    List<Predicate> restrictions = buildRestrictions(root, criteriaBuilder, productId, maxRating, minRating);

    criteria.select(criteriaBuilder.count(root.get(Review_.id)));
    criteria.where(
      criteriaBuilder.and(restrictions.toArray(new Predicate[0]))
    );

    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Counts all reviews
   * 
   * @return count of all reviews
   */
  public Long countAllReviews() {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<Review> root = criteria.from(Review.class);

    criteria.select(criteriaBuilder.count(root.get(Review_.id)));

    return getSingleResult(entityManager.createQuery(criteria));
  }

  /**
   * Finds review by origin id
   * 
   * @param originId origin id
   * @return review or null if not found
   */
  public Review findByOriginId(Long originId) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Review> criteria = criteriaBuilder.createQuery(Review.class);
    Root<Review> root = criteria.from(Review.class);

    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Review_.originId), originId)
    );
    
    TypedQuery<Review> query = entityManager.createQuery(criteria);
    
    return getSingleResult(query);
  }
  
  /**
   * Updates originId
   *
   * @param review review to update
   * @param originId originId
   * @return updated review
   */
  public Review updateOriginId(Review review, Long originId) {
    review.setOriginId(originId);
    return persist(review);
  }

  /**
   * Updates productId
   *
   * @param review review to update
   * @param productId productId
   * @return updated review
   */
  public Review updateProductId(Review review, Long productId) {
    review.setProductId(productId);
    return persist(review);
  }


  /**
   * Updates location
   *
   * @param review review to update
   * @param location location
   * @return updated review
   */
  public Review updateLocation(Review review, String location) {
    review.setLocation(location);
    return persist(review);
  }

  /**
   * Updates created
   *
   * @param review review to update
   * @param created created
   * @return updated review
   */
  public Review updateCreated(Review review, OffsetDateTime created) {
    review.setCreated(created);
    return persist(review);
  }

  /**
   * Updates modified
   *
   * @param review review to update
   * @param modified modified
   * @return updated review
   */
  public Review updateModified(Review review, OffsetDateTime modified) {
    review.setModified(modified);
    return persist(review);
  }

  /**
   * Updates rating
   *
   * @param review review to update
   * @param rating rating
   * @return updated review
   */
  public Review updateRating(Review review, Integer rating) {
    review.setRating(rating);
    return persist(review);
  }

  /**
   * Updates review
   *
   * @param review review to update
   * @param review review
   * @return updated review
   */
  public Review updateReview(Review entity, String review) {
    entity.setReview(review);
    return persist(entity);
  }

  /**
   * Creates list of filters
   * 
   * @param root root
   * @param criteriaBuilder criteria builder
   * @param productId product id
   * @param maxRating max rating
   * @param minRating min rating
   * @return list of filters
   */
  private List<Predicate> buildRestrictions(Root<Review> root, CriteriaBuilder criteriaBuilder, Long productId, Integer maxRating, Integer minRating) {
    List<Predicate> restrictions = new ArrayList<>();
    
    if (productId != null) {
      restrictions.add(criteriaBuilder.equal(root.get(Review_.productId), productId));
    }
    
    if (maxRating != null) {
      restrictions.add(criteriaBuilder.lessThanOrEqualTo(root.get(Review_.rating), maxRating));
    }
  
    if (minRating != null) {
      restrictions.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Review_.rating), minRating));
    }

    return restrictions;
  }
}