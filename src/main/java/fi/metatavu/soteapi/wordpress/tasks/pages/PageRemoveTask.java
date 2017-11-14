package fi.metatavu.soteapi.wordpress.tasks.pages;

import fi.metatavu.soteapi.wordpress.tasks.AbstractRemoveTask;

public class PageRemoveTask extends AbstractRemoveTask {

  private static final long serialVersionUID = -7070079728238538761L;
  
  public PageRemoveTask() {
    super();
  }
  
  public PageRemoveTask(String originId) {
    super(originId, false);
  }

  @Override
  public String getUniqueId() {
    return String.format("wp-page-remove-%s", getOriginId());
  }

}
