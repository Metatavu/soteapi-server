package fi.metatavu.soteapi.tasks;

import javax.inject.Inject;

import fi.metatavu.metaflow.tasks.Task;
import fi.metatavu.metaflow.tasks.TaskProvider;
import fi.metatavu.soteapi.cluster.ClusterController;

public class TaskProviderImpl implements TaskProvider {
  
  @Inject
  private ClusterController clusterController;

  @Inject
  private TaskController taskController;

  @Override
  public <T extends Task> T getNextTask(String queueName) {
    return taskController.getNextTask(queueName, clusterController.getLocalNodeName());
  }

  @Override
  public <T extends Task> T createTask(String queueName, T task) {
    taskController.createTask(queueName, task);
    return task;
  }

  @Override
  public boolean isQueueEmpty(String queueName) {
    return taskController.isQueueEmpty(queueName);
  }

  @Override
  public <T extends Task> T findTask(String queueName, String uniqueId) {
    return taskController.findTask(queueName, uniqueId);
  }
}
