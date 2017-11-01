package fi.metatavu.soteapi.wordpress.tasks;

import fi.metatavu.metaflow.tasks.AbstractTaskQueue;

public class PageUpdateQueue extends AbstractTaskQueue<PageUpdateTask> {

  @Override
  public String getName() {
    return "WORDPRESS-PAGE-UPDATES";
  }

}
