package fi.metatavu.soteapi.wordpress.tasks.posts;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class PostListQueue extends AbstractSoteApiTaskQueue<PostListTask> {

  @Override
  public String getName() {
    return "WORDPRESS-POSTS-LISTS";
  }

}
