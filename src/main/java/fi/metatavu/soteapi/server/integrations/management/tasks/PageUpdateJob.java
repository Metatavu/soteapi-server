package fi.metatavu.soteapi.server.integrations.management.tasks;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.tasks.AbstractUpdateJob;

@ApplicationScoped
public class PageUpdateJob extends AbstractUpdateJob{

  @Inject
  private PageTaskQueue pageTaskQueue;
  
  @Override
  protected void execute() {
    PageTask pageTask = pageTaskQueue.next();
    if (pageTask != null) {
      updatePage(pageTask);
    }
  }

  private void updatePage(PageTask task) {
    //TODO: update management page
  }
  
}
