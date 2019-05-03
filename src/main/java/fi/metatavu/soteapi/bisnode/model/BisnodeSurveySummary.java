package fi.metatavu.soteapi.bisnode.model;

/**
 * POJO for storing response from Bisnode's yes-no summary API
 */
public class BisnodeSurveySummary {

  private long yes;
  private long total;

  public BisnodeSurveySummary() {
    //Default constructor
  }

  public BisnodeSurveySummary(long yes, long total) {
    this.yes = yes;
    this.total = total;
  }

  /**
   * @return the yes
   */
  public long getYes() {
    return yes;
  }

  /**
   * @param yes the yes to set
   */
  public void setYes(long yes) {
    this.yes = yes;
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
}