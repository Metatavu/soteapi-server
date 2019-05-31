package fi.metatavu.soteapi.exreport;

import java.util.List;

/**
 * Exreport service wait times
 * 
 * @author Antti Leppä
 */
public class ServiceWaitTimes {
  
  private String serviceId;
  private String serviceName;
  private List<ServiceUnitWaitTime> waitTimes;
  
  public String getServiceId() {
    return serviceId;
  }
  
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }
  
  public String getServiceName() {
    return serviceName;
  }
  
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
  
  public List<ServiceUnitWaitTime> getWaitTimes() {
    return waitTimes;
  }
  
  public void setWaitTimes(List<ServiceUnitWaitTime> waitTimes) {
    this.waitTimes = waitTimes;
  }

}
