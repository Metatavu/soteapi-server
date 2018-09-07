package fi.metatavu.soteapi.wordpress.tasks.emergency;

import java.time.OffsetDateTime;

public class EmergencyCongestionStatusRestModel {

  private Integer value;
  private OffsetDateTime created;
  
  public OffsetDateTime getCreated() {
    return created;
  }
  
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }
  
  public Integer getValue() {
    return value;
  }
  
  public void setValue(Integer value) {
    this.value = value;
  }
  
}
