package fi.metatavu.soteapi.wordpress.tasks.posts;

import java.time.OffsetDateTime;
import java.util.List;

import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.request.Request;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.utils.TimeUtils;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
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
  protected String getEnabledSetting() {
    return WordpressConsts.POSTS_SYNC_ENABLED;
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
    
    Long categoryId = null;
    List<Long> categoryIds = postData.getCategoryIds();
    if (!categoryIds.isEmpty()) {
      categoryId = categoryIds.get(0);
    }
    
    String parentOriginId = null;
    String title = postData.getTitle().getRendered();
    String content = postData.getContent().getRendered();
    String slug = postData.getSlug();
    String originId = postData.getId().toString();
    OffsetDateTime createdDate = TimeUtils.parseOffsetDateTimeLocal(postData.getDateGmt());
    Long orderIndex = -createdDate.toEpochSecond();
    String created = TimeUtils.toIsoString(createdDate);
    String modified = TimeUtils.toIsoString(TimeUtils.parseOffsetDateTimeLocal(postData.getModifiedGmt()));
    
    PostUpdateTaskModel postModel = new PostUpdateTaskModel(title, content, slug, originId, parentOriginId, categoryId, created, modified, orderIndex);
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
