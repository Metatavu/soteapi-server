package fi.metatavu.soteapi.wordpress.tasks;

import fi.metatavu.metaflow.tasks.AbstractTaskQueue;

public class PageListQueue extends AbstractTaskQueue<PageListTask> {

  @Override
  public String getName() {
    return "WORDPRESS-PAGE-LISTS";
  }

}
