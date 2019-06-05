package fi.metatavu.soteapi.bisnode.tasks.reviews;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import com.vdurmont.emoji.EmojiParser;

import org.slf4j.Logger;

import fi.metatavu.soteapi.bisnode.BisnodeConsts;
import fi.metatavu.soteapi.bisnode.BisnodeController;
import fi.metatavu.soteapi.bisnode.model.BisnodeReview;
import fi.metatavu.soteapi.bisnode.model.BisnodeReviewContainer;
import fi.metatavu.soteapi.persistence.model.Review;
import fi.metatavu.soteapi.review.ReviewController;
import fi.metatavu.soteapi.review.ReviewProductController;
import fi.metatavu.soteapi.server.rest.model.ReviewProduct;
import fi.metatavu.soteapi.tasks.AbstractUpdateJob;

/**
 * Class for Bisnode update jobs
 * 
 * @author Heikki Kurhinen
 */
public class ReviewListJob extends AbstractUpdateJob {

  @Inject
  private Logger logger;

  @Inject
  private ReviewListQueue reviewListQueue;

  @Inject
  private BisnodeController bisnodeController;

  @Inject
  private ReviewProductController reviewProductController;

  @Inject
  private ReviewController reviewController;

  @Override
  protected void execute() {
    ReviewListTask task = reviewListQueue.next();
    if (task != null) {
      performTask(task);
    } else if (reviewListQueue.isEmptyAndLocalNodeResponsible()) {
      fillQueue();
    }
  }

  /**
   * Creates new task based on the page index
   * 
   * @param reviewProduct review product
   * @param page page index
   * @return created task
   */
  private ReviewListTask createTask(ReviewProduct reviewProduct, long page) {
    String uniqueId = String.format("bisnode-review-%s-%d", reviewProduct.getName(), page);
    ReviewListTask task = new ReviewListTask(uniqueId, Boolean.FALSE);
    task.setPage(page);
    task.setProductName(reviewProduct.getName());
    task.setProductId(reviewProduct.getId());
    return task;
  }

  /**
   * Performs existing task
   * 
   * @param task task to perform
   */
  private void performTask(ReviewListTask task) {
    BisnodeReviewContainer bisnodeReviewContainer = bisnodeController.listReviews(task.getProductName(), task.getPage());
    if (bisnodeReviewContainer == null) {
      return;
    }

    List<BisnodeReview> reviews = bisnodeReviewContainer.getReviews();
    reviews.stream().forEach(bisnodeReview -> updateOrCreateReview(bisnodeReview, task.getProductId()));
  }

  /**
   * Updates or creates review
   * 
   * @param bisnodeReview Bisnode review
   * @param reviewProductId SoteAPi review product id
   */
  private void updateOrCreateReview(BisnodeReview bisnodeReview, long reviewProductId) {
    Review existingReview = reviewController.findReviewByOriginId(bisnodeReview.getId());
    String reviewContent = bisnodeReview.getReview() != null ? EmojiParser.parseToAliases(bisnodeReview.getReview()) : null;
    if (existingReview == null) {
      reviewController.createReview(bisnodeReview.getId(), reviewProductId, bisnodeReview.getRating(), reviewContent, bisnodeReview.getCreated(), bisnodeReview.getLocation());
    } else {
      reviewController.updateReview(existingReview, bisnodeReview.getId(), reviewProductId, bisnodeReview.getRating(), reviewContent, bisnodeReview.getCreated(), bisnodeReview.getLocation());
    }
  }

  /**
   * Fills the queue
   */
  private void fillQueue() {
    try {
      List<ReviewProduct> reviewProducts = reviewProductController.listReviewProducts();
      for (ReviewProduct reviewProduct : reviewProducts) {
        long numberOfPages = getNumberOfPages(reviewProduct);
        for (long i = 0l; i <= numberOfPages; i++) {
          reviewListQueue.enqueueTask(createTask(reviewProduct, i));
        }
      }
    } catch (IOException e) {
      logger.error("Error filling Bisnode review tasks queue", e);
    }
  }
  
  /**
   * Get total number of pages per product
   * 
   * @param reviewProduct product to get the number of pages for
   * @return number of pages
   */
  private long getNumberOfPages(ReviewProduct reviewProduct) {
    BisnodeReviewContainer bisnodeReviewContainer = bisnodeController.listReviews(reviewProduct.getName(), 0l);
    if(bisnodeReviewContainer == null) {
      return -1;
    }

    return bisnodeReviewContainer.getTotal() / BisnodeConsts.PAGE_SIZE;
  }

  @Override
  protected String getEnabledSetting() {
    return BisnodeConsts.REVIEW_SYNC_ENABLED;
  }

}
