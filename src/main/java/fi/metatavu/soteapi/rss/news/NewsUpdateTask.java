package fi.metatavu.soteapi.rss.news;

import fi.metatavu.metaflow.tasks.Task;

public class NewsUpdateTask implements Task {

  private static final long serialVersionUID = 4725085938716681192L;

  private String uniqueId;
  private boolean priority;
  private NewsUpdateModel model;
  
  public NewsUpdateTask() {
    super();
  }
  
  public NewsUpdateTask(String uniqueId, boolean priority, NewsUpdateModel model) {
    super();
    this.uniqueId = uniqueId;
    this.priority = priority;
    this.model = model;
  }

  @Override
  public String getUniqueId() {
    return uniqueId;
  }

  @Override
  public boolean getPriority() {
    return priority;
  }

  public NewsUpdateModel getModel() {
    return model;
  }
  
  public void setModel(NewsUpdateModel model) {
    this.model = model;
  }
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public void setPriority(boolean priority) {
    this.priority = priority;
  }
  
}
