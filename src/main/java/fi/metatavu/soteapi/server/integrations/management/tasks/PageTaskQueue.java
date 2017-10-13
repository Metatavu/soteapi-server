package fi.metatavu.soteapi.server.integrations.management.tasks;

import fi.metatavu.metaflow.tasks.AbstractTaskQueue;

public class PageTaskQueue extends AbstractTaskQueue<PageTask> {

  @Override
  public String getName() {
    return "FINVOICE-TASKS";
  }

}
