package fi.metatavu.soteapi.server.integrations.management.tasks;

import fi.metatavu.metaflow.tasks.Task;

public class PageTask implements Task {

  private static final long serialVersionUID = 8471408662465136413L;

  public PageTask() {
    super();
  }
  
  public PageTask(String uniqueId, boolean priority) {
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
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public void setPriority(boolean priority) {
    this.priority = priority;
  }

  private String uniqueId;
  private boolean priority;

}
