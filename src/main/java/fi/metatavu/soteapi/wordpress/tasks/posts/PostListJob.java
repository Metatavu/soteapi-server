package fi.metatavu.soteapi.wordpress.tasks.posts;

import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.model.Post;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.wordpress.tasks.AbstractListJob;

public class PostListJob extends AbstractListJob<Post, PostListTask> {
  
  @Inject
  private PostListQueue pageListQueue;
  
  @Inject
  private PostUpdateQueue pageUpdateQueue;
  
  @Override
  protected AbstractSoteApiTaskQueue<PostListTask> getQueue() {
    return pageListQueue;
  }
  
  @Override
  protected PostListTask createTask(int page) {
    PostListTask newTask = new PostListTask();
    newTask.setPriority(Boolean.FALSE);
    newTask.setUniqueId(String.format("wp-post-list-%d", page));
    newTask.setPage(page);
    return newTask;
  }
  
  @Override
  protected void process(Post postData) {
    if (postData == null) {
      return;
    }
    
    PostUpdateTaskModel pageModel = new PostUpdateTaskModel();
    pageModel.setOriginId(postData.getId().toString());
    pageModel.setSlug(postData.getSlug());
    pageModel.setTitle(postData.getTitle().getRendered());
    pageModel.setContent(postData.getContent().getRendered());
    
    PostUpdateTask pageEntityTask = new PostUpdateTask();
    pageEntityTask.setPostUpdateModel(pageModel);
    pageEntityTask.setPriority(Boolean.FALSE);
    pageEntityTask.setUniqueId(String.format("wp-post-update-%d", postData.getId()));
    pageUpdateQueue.enqueueTask(pageEntityTask);
  }
    
}
