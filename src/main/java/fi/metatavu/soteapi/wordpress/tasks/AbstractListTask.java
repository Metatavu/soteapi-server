package fi.metatavu.soteapi.wordpress.tasks;

import fi.metatavu.metaflow.tasks.Task;

public class AbstractListTask implements Task {
  
  private static final long serialVersionUID = 7795741722570626557L;
  
  private String uniqueId;
  private boolean priority;
  private int page;
  
  public AbstractListTask() {
    super();
  }
  
  public AbstractListTask(String uniqueId, boolean priority) {
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
  
  public int getPage() {
    return page;
  }
  
  public void setPage(int page) {
    this.page = page;
  }
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public void setPriority(boolean priority) {
    this.priority = priority;
  }

}
