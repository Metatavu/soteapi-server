package fi.metatavu.soteapi.wordpress.tasks.posts;

import java.util.List;

import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.request.Request;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.wordpress.tasks.AbstractListJob;

public class PostListJob extends AbstractListJob<Post, PostListTask> {
  
  @Inject
  private PostListQueue postListQueue;
  
  @Inject
  private PostUpdateQueue postUpdateQueue;
  
  @Override
  protected AbstractSoteApiTaskQueue<PostListTask> getQueue() {
    return postListQueue;
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
    
    PostUpdateTaskModel postModel = new PostUpdateTaskModel();
    
    List<Long> categoryIds = postData.getCategoryIds();
    if (!categoryIds.isEmpty()) {
      postModel.setCategoryId(categoryIds.get(0));
    }
    
    postModel.setOriginId(postData.getId().toString());
    postModel.setSlug(postData.getSlug());
    postModel.setTitle(postData.getTitle().getRendered());
    postModel.setContent(postData.getContent().getRendered());
    
    PostUpdateTask postUpdateTask = new PostUpdateTask();
    postUpdateTask.setPostUpdateModel(postModel);
    postUpdateTask.setPriority(Boolean.FALSE);
    postUpdateTask.setUniqueId(String.format("wp-post-update-%d", postData.getId()));
    postUpdateQueue.enqueueTask(postUpdateTask);
  }

  @Override
  protected String getEndPointUri() {
    return Request.POSTS;
  }
    
}
