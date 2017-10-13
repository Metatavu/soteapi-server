package fi.metatavu.soteapi.server.integrations.management.tasks;

import javax.inject.Inject;

import fi.metatavu.soteapi.tasks.AbstractUpdateJob;

public class PageUpdateJob extends AbstractUpdateJob{

  @Inject
  private PageTaskQueue pageTaskQueue;
  
  @Override
  protected void doJob() {
    PageTask pageTask = pageTaskQueue.next();
    if (pageTask != null) {
      updatePage(pageTask);
    }
  }

  public void updatePage(PageTask task) {
    //TODO: update management page
  }
  
}
