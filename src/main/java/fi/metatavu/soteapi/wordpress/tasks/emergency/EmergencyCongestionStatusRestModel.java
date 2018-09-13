package fi.metatavu.soteapi.wordpress.tasks.emergency;

import java.time.OffsetDateTime;

/**
 * Rest model for Wordpress emergency congestion status endpoint response
 * 
 * @author Antti Leppä
 */
public class EmergencyCongestionStatusRestModel {

  private Integer value;
  private OffsetDateTime modified;
  
  public OffsetDateTime getModified() {
    return modified;
  }
  
  public void setModified(OffsetDateTime modified) {
    this.modified = modified;
  }
  
  public Integer getValue() {
    return value;
  }
  
  public void setValue(Integer value) {
    this.value = value;
  }
  
}
