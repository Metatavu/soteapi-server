package fi.metatavu.soteapi.rss.news;

import fi.metatavu.metaflow.tasks.Task;

public class NewsListTask implements Task {
  
  private static final long serialVersionUID = -6126874371978835414L;
  
  private String uniqueId;
  private boolean priority;
  private int page;
  
  public NewsListTask() {
    super();
  }
  
  public NewsListTask(String uniqueId, boolean priority, int page) {
    super();
    this.uniqueId = uniqueId;
    this.priority = priority;
    this.page = page;
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