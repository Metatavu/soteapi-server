package fi.metatavu.soteapi.bisnode.model;

import java.util.List;

/**
 * POJO for storing response from Bisnode's yes-no summary API
 */
public class BisnodeReviewContainer {

  private Long total;
  private List<BisnodeReview> reviews;

  public BisnodeReviewContainer() {
    //Default constructor
  }

  public BisnodeReviewContainer(Long total, List<BisnodeReview> reviews) {
    this.total = total;
    this.reviews = reviews;
  }

  /**
   * @return the total
   */
  public long getTotal() {
    return total;
  }

  /**
   * @param total the total to set
   */
  public void setTotal(long total) {
    this.total = total;
  }

  /**
   * @return the reviews
   */
  public List<BisnodeReview> getReviews() {
    return reviews;
  }

  /**
   * @param reviews the reviews to set
   */
  public void setReviews(List<BisnodeReview> reviews) {
    this.reviews = reviews;
  }
}