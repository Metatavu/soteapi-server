package fi.metatavu.soteapi.bisnode.model;

import java.time.OffsetDateTime;

/**
 * POJO for storing response from Bisnode's review API
 */
public class BisnodeReview {

  private String author;
  private OffsetDateTime created;
  private long id;
  private String location;
  private int rating;
  private String review;


  public BisnodeReview() {
    //Default constructor
  }

  /**
   * All params constructor
   * 
   * @param author author
   * @param created created
   * @param id id
   * @param location location
   * @param rating rating
   * @param review review
   */
  public BisnodeReview(String author, OffsetDateTime created, long id, String location, int rating, String review) {
    this.author = author;
    this.created = created;
    this.id = id;
    this.location = location;
    this.rating = rating;
    this.review = review;
  }

  /**
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * @param author the author to set
   */
  public void setAuthor(String author) {
    this.author = author;
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
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
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
   * @return the rating
   */
  public int getRating() {
    return rating;
  }

  /**
   * @param rating the rating to set
   */
  public void setRating(int rating) {
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

}