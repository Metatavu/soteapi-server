package fi.metatavu.soteapi.wordpress.tasks.emergency;

import fi.metatavu.metaflow.tasks.Task;

public class EmergencyCongestionStatusTask implements Task {

  private static final long serialVersionUID = 7070692754391739204L;
  
  private String uniqueId;
  private boolean priority;
  
  @Override
  public String getUniqueId() {
    return uniqueId;
  }
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  @Override
  public boolean getPriority() {
    return priority;
  }

  public void setPriority(boolean priority) {
    this.priority = priority;
  }

}
