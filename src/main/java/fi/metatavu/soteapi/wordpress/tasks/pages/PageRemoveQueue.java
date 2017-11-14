package fi.metatavu.soteapi.wordpress.tasks.pages;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class PageRemoveQueue extends AbstractSoteApiTaskQueue<PageRemoveTask> {

  @Override
  public String getName() {
    return "WORDPRESS-PAGE-REMOVES";
  }

}
