package fi.metatavu.soteapi.persistence.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  @NotNull
  private Long originId;

  @Column(nullable = false)
  @NotNull
  private Long productId;

  @Column(nullable = false)
  @NotNull
  private Integer rating;

  @Column
  private String review;

  @Column
  private String location;

  @NotNull
  @Column(nullable = true)
  private OffsetDateTime created;

  @NotNull
  @Column(nullable = true)
  private OffsetDateTime modified;

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the originId
   */
  public Long getOriginId() {
    return originId;
  }

  /**
   * @param originId the originId to set
   */
  public void setOriginId(Long originId) {
    this.originId = originId;
  }

  /**
   * @return the productId
   */
  public Long getProductId() {
    return productId;
  }

  /**
   * @param productId the productId to set
   */
  public void setProductId(Long productId) {
    this.productId = productId;
  }

  /**
   * @return the rating
   */
  public Integer getRating() {
    return rating;
  }

  /**
   * @param rating the rating to set
   */
  public void setRating(Integer rating) {
    this.rating = rating;
  }

  /**
   * @return the review
   */
  public String getReview() {
    return review;
  }

  /**
   * @param review the review to set
   */
  public void setReview(String review) {
    this.review = review;
  }

  /**
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * @return the created
   */
  public OffsetDateTime getCreated() {
    return created;
  }

  /**
   * @param created the created to set
   */
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  /**
   * @return the modified
   */
  public OffsetDateTime getModified() {
    return modified;
  }

  /**
   * @param modified the modified to set
   */
  public void setModified(OffsetDateTime modified) {
    this.modified = modified;
  }
}
