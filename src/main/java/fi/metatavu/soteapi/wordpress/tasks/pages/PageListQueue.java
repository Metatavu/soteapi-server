package fi.metatavu.soteapi.wordpress.tasks.pages;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class PageListQueue extends AbstractSoteApiTaskQueue<PageListTask> {

  @Override
  public String getName() {
    return "WORDPRESS-PAGE-LISTS";
  }

}
