package fi.metatavu.soteapi.server.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.soteapi.persistence.model.Review;
import fi.metatavu.soteapi.rest.translate.ReviewTranslator;
import fi.metatavu.soteapi.review.ReviewController;
import fi.metatavu.soteapi.server.rest.api.ReviewsApi;
import fi.metatavu.soteapi.server.rest.model.ReviewListSort;

/**
 * Api for reviews
 * 
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
public class ReviewsApiImpl implements ReviewsApi {

  @Inject
  private ResponseController responseController;

  @Inject
  private ReviewController reviewController;

  @Inject
  private ReviewTranslator reviewTranslator;

  @Override
  public Response findReview(Long reviewId) {
    Review review = reviewController.findReviewById(reviewId);
    return responseController.respondOkOrNotFound(reviewTranslator.translateReview(review));
  }

  @Override
  public Response listReviews(Long productId, Integer minRating, Integer maxRating, Integer minReviewLength, Long firstResult, Long maxResults, ReviewListSort sort) {
    ReviewListSort sortOrder = sort == null ? ReviewListSort.DESC : sort;
    List<Review> reviews = reviewController.listReviews(productId, maxRating, minRating, minReviewLength, sortOrder, firstResult, maxResults);
    Long reviewCount = reviewController.countReviews(productId, maxRating, minRating, minReviewLength);
    Long unfilteredCount = reviewController.countAllReviews();

    Response response = responseController.respondOk(reviewTranslator.translateReviews(reviews));
    response.getHeaders().add("total-hits", reviewCount);
    response.getHeaders().add("total-unfiltered-hits", unfilteredCount);

    return response;
  }
  
}
