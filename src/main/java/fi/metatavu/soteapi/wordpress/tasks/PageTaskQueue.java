package fi.metatavu.soteapi.wordpress.tasks;

import fi.metatavu.metaflow.tasks.AbstractTaskQueue;

public class PageTaskQueue extends AbstractTaskQueue<PageTask> {

  @Override
  public String getName() {
    return "MANAGEMENT-PAGE-TASKS";
  }

}
