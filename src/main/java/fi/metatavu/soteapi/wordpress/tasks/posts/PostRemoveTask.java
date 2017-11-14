package fi.metatavu.soteapi.wordpress.tasks.posts;

import fi.metatavu.soteapi.wordpress.tasks.AbstractRemoveTask;

public class PostRemoveTask extends AbstractRemoveTask {
  
  private static final long serialVersionUID = -5448144366489579790L;

  public PostRemoveTask() {
    super();
  }
  
  public PostRemoveTask(String originId) {
    super(originId, false);
  }

  @Override
  public String getUniqueId() {
    return String.format("wp-post-remove-%s", getOriginId());
  }

}
