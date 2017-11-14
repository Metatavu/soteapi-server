package fi.metatavu.soteapi.wordpress.tasks.posts;

import javax.inject.Inject;

import org.apache.commons.lang3.math.NumberUtils;

import com.afrozaar.wordpress.wpapi.v2.exception.PostNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Post;

import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractRemoveJob;

public class PostRemoveJob extends AbstractRemoveJob<PostRemoveTask, PostRemoveQueue> {
  
  @Inject
  private PostRemoveQueue postRemoveQueue;

  @Override
  protected PostRemoveQueue getQueue() {
    return postRemoveQueue;
  }
  
  @Override
  protected PostRemoveTask createTask(String originId) {
    return new PostRemoveTask(originId);
  }

  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.POSTS_SYNC_ENABLED;
  }

  @SuppressWarnings ("squid:S1166")
  protected boolean isArchived(String originId) {
    Long pageId = NumberUtils.createLong(originId);
    
    try {
      Post post = getWordpressClient().getPost(pageId);
      if (post == null || !"publish".equals(post.getStatus())) {
        return true;        
      }
    } catch (PostNotFoundException e) {
      return true;
    }
    
    return false;
  }

}
