package fi.metatavu.soteapi.exreport;

/**
 * Exreport service unit wait time
 * 
 * @author Antti Leppä
 */
public class ServiceUnitWaitTime {
  
  private String unitName;
  private Double waitTime;
  
  public String getUnitName() {
    return unitName;
  }
  
  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }
  
  public Double getWaitTime() {
    return waitTime;
  }
  
  public void setWaitTime(Double waitTime) {
    this.waitTime = waitTime;
  }

}
