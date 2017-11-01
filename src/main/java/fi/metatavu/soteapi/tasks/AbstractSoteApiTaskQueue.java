package fi.metatavu.soteapi.tasks;

import javax.inject.Inject;

import fi.metatavu.metaflow.tasks.AbstractTaskQueue;
import fi.metatavu.metaflow.tasks.Task;

public abstract class AbstractSoteApiTaskQueue<T extends Task> extends AbstractTaskQueue<T> {

  @Inject
  private TaskController taskController;
  
  public boolean isEmptyAndLocalNodeResponsible() {
    return taskController.isEmptyAndLocalNodeResponsible(getName());
  }
  
}
