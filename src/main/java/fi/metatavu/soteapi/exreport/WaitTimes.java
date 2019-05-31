package fi.metatavu.soteapi.exreport;

import java.util.List;

/**
 * Exreport wait times
 * 
 * @author Antti Leppä
 */
public class WaitTimes {

  private List<ServiceWaitTimes> services;
  
  public List<ServiceWaitTimes> getServices() {
    return services;
  }
  
  public void setServices(List<ServiceWaitTimes> services) {
    this.services = services;
  }
  
}
