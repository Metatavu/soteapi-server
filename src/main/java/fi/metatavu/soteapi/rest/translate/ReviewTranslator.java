package fi.metatavu.soteapi.rest.translate;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.server.rest.model.Review;

/**
 * Translator for review related translations
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ReviewTranslator extends AbstractTranslator {
  
  /**
   * Translates database review entity to REST content 
   * 
   * @param reviewEntity Database review entity to be translated
   * 
   * @return Review translated to the REST content
   */
  public Review translateReview(fi.metatavu.soteapi.persistence.model.Review reviewEntity) {
    if (reviewEntity == null) {
      return null;
    }

    Review review = new Review();
    review.setId(reviewEntity.getId());
    review.setCreated(reviewEntity.getCreated());
    review.setLocation(reviewEntity.getLocation());
    review.setModified(reviewEntity.getModified());
    review.setProductId(reviewEntity.getProductId());
    review.setRating(reviewEntity.getRating());
    review.setReview(reviewEntity.getReview());

    return review;
  }
  
  /**
   * Translates list of review entities to rest entities
   * 
   * @param reviewEntities entitites to translate
   * @return list of translated entities
   */
  public List<Review> translateReviews(List<fi.metatavu.soteapi.persistence.model.Review> reviewEntities) {
    return reviewEntities.stream().map(this::translateReview).collect(Collectors.toList());
  }
}
