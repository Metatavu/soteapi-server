package fi.metatavu.soteapi.wordpress.tasks.pages;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class PageUpdateQueue extends AbstractSoteApiTaskQueue<PageUpdateTask> {

  @Override
  public String getName() {
    return "WORDPRESS-PAGE-UPDATES";
  }

}
