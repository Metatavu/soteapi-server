package fi.metatavu.soteapi.wordpress.tasks;

import fi.metatavu.metaflow.tasks.Task;

public class PageUpdateTask implements Task {

  private static final long serialVersionUID = 8471408662465136413L;

  private String uniqueId;
  private boolean priority;
  private PageUpdateTaskModel pageModel;
  
  public PageUpdateTask() {
    super();
  }
  
  public PageUpdateTask(String uniqueId, boolean priority) {
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
  
  public PageUpdateTaskModel getPageModel() {
    return pageModel;
  }
  
  public void setPageModel(PageUpdateTaskModel pageModel) {
    this.pageModel = pageModel;
  }
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public void setPriority(boolean priority) {
    this.priority = priority;
  }

}
