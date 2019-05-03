package fi.metatavu.soteapi.bisnode.tasks.reviews;

import fi.metatavu.metaflow.tasks.Task;

/**
 * Task for listing reviews
 * 
 * @author Heikki Kurhinen
 */
public class ReviewListTask implements Task {
  
  private static final long serialVersionUID = 1740897740852535739L;

  private String uniqueId;
  private String productName;
  private boolean priority;
  private long page;
  private long productId;
  
  public ReviewListTask() {
    super();
  }
  
  public ReviewListTask(String uniqueId, boolean priority) {
    super();
    this.uniqueId = uniqueId;
    this.priority = priority;
  }

  @Override
  public String getUniqueId() {
    return uniqueId;
  }

  @Override
  public boolean getPriority() {
    return priority;
  }
  
  /**
   * @return the page
   */
  public long getPage() {
    return page;
  }

  /**
   * @param page the page to set
   */
  public void setPage(long page) {
    this.page = page;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(boolean priority) {
    this.priority = priority;
  }

  /**
   * @param uniqueId the uniqueId to set
   */
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  /**
   * @return the productName
   */
  public String getProductName() {
    return productName;
  }

  /**
   * @param productName the productName to set
   */
  public void setProductName(String productName) {
    this.productName = productName;
  }

  /**
   * @return the productId
   */
  public long getProductId() {
    return productId;
  }

  /**
   * @param productId the productId to set
   */
  public void setProductId(long productId) {
    this.productId = productId;
  }
}
