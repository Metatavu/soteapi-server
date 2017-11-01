package fi.metatavu.soteapi.wordpress.tasks.posts;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class PostUpdateQueue extends AbstractSoteApiTaskQueue<PostUpdateTask> {

  @Override
  public String getName() {
    return "WORDPRESS-POST-UPDATES";
  }

}
