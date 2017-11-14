package fi.metatavu.soteapi.wordpress.tasks;

import fi.metatavu.metaflow.tasks.Task;

/**
 * Abstract base class for Wordpress remove checks
 * 
 * @author Antti Leppä
 */
public abstract class AbstractRemoveTask implements Task {
  
  private static final long serialVersionUID = 1838391427906990813L;
  
  private String originId;
  private Boolean priority;
  
  public AbstractRemoveTask() {
  }
  
  public AbstractRemoveTask(String originId, Boolean priority) {
    super();
    this.originId = originId;
    this.priority = priority;
  }

  @Override
  public boolean getPriority() {
    return priority;
  }
  
  public String getOriginId() {
    return originId;
  }
  
}
