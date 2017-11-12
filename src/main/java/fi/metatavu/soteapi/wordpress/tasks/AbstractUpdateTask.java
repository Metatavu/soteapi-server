package fi.metatavu.soteapi.wordpress.tasks;

import java.io.Serializable;

import fi.metatavu.metaflow.tasks.Task;

public class AbstractUpdateTask<M extends Serializable> implements Task {

  private static final long serialVersionUID = 4725085938716681192L;

  private String uniqueId;
  private boolean priority;
  private M postUpdateModel;
  
  public AbstractUpdateTask() {
    super();
  }
  
  public AbstractUpdateTask(String uniqueId, boolean priority) {
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

  public M getPostUpdateModel() {
    return postUpdateModel;
  }
  
  public void setPostUpdateModel(M postUpdateModel) {
    this.postUpdateModel = postUpdateModel;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public void setPriority(boolean priority) {
    this.priority = priority;
  }
  
}
