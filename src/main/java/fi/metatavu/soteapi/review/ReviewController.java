package fi.metatavu.soteapi.review;

import java.time.OffsetDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.persistence.dao.ReviewDAO;
import fi.metatavu.soteapi.persistence.model.Review;
import fi.metatavu.soteapi.server.rest.model.ReviewListSort;

/**
 * Controller for operations related to reviews
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ReviewController {

  @Inject
  private ReviewDAO reviewDAO;

  /**
   * Creates new review
   * 
   * @param originId
   * @param productId
   * @param rating
   * @param reviewText
   * @param created
   * @param location
   *
   * @return created review
   */
  public Review createReview(Long originId, Long productId, Integer rating, String reviewText, OffsetDateTime created,
      String location) {
    return reviewDAO.create(originId, productId, rating, reviewText, created, location);
  }

  /**
   * Finds review with reviewId
   * 
   * @param reviewId Reviews id
   * @return Review
   */
  public Review findReviewById(Long reviewId) {
    return reviewDAO.findById(reviewId);
  }

  /**
   * Finds review with origin id
   * 
   * @param originId Reviews origin id
   * @return Review
   */
  public Review findReviewByOriginId(Long originId) {
    return reviewDAO.findByOriginId(originId);
  }

  /**
   * Lists reviews 
   * 
   * @param productId product id
   * @param maxRating max rating
   * @param minRating min rating
   * @param minReviewLength return only reviews containing at least given amount of characters
   * @param sort sort 
   * @param firstResult first result
   * @param maxResults max results
   * 
   * @return list of reviews
   */
  public List<Review> listReviews(Long productId, Integer maxRating, Integer minRating, Integer minReviewLength, ReviewListSort sort, Long firstResult, Long maxResults) {
    return reviewDAO.listReviews(productId, maxRating, minRating, minReviewLength, sort, firstResult, maxResults);
  }

  /**
   * Counts reviews by filter
   * 
   * @param productId product id
   * @param maxRating max rating
   * @param minRating min rating
   * @param minReviewLength return only reviews containing at least given amount of characters
   * 
   * @return count of reviews with given filters
   */
  public Long countReviews(Long productId, Integer maxRating, Integer minRating, Integer minReviewLength) {
    return reviewDAO.countReviews(productId, maxRating, minRating, minReviewLength);
  }

  /**
   * Counts all reviews
   * 
   * @return count of all reviews
   */
  public Long countAllReviews() {
    return reviewDAO.countAllReviews();
  }

  /**
   * Updates review
   * 
   * @param review review to update
   * @param originId originId
   * @param productId productId
   * @param rating rating
   * @param reviewText review
   * @param created created
   * @param location location
   * 
   * @return updated review
   */
  public Review updateReview(Review review, Long originId, Long productId, Integer rating, String reviewText, OffsetDateTime created, String location) {
    reviewDAO.updateOriginId(review, originId);
    reviewDAO.updateProductId(review, productId);
    reviewDAO.updateRating(review, rating);
    reviewDAO.updateReview(review, reviewText);
    reviewDAO.updateCreated(review, created);
    reviewDAO.updateLocation(review, location);
    reviewDAO.updateModified(review, OffsetDateTime.now());

    return review;
  }

}
