package fi.metatavu.soteapi.wordpress.tasks.posts;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import com.afrozaar.wordpress.wpapi.v2.exception.PostNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Post;

import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractRemoveJob;

@ApplicationScoped
public class PostRemoveJob extends AbstractRemoveJob<PostRemoveTask, PostRemoveQueue> {

  @Inject
  private Logger logger;

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

  @Override
  protected ContentType getContentType() {
    return ContentType.NEWS;
  }

  @SuppressWarnings ("squid:S1166")
  protected boolean isArchived(String originId) {
    Long pageId = NumberUtils.createLong(originId);
    
    try {
      Post post = getWordpressClient().getPost(pageId);
      if (post == null) {
        if (logger.isWarnEnabled()) {
          logger.warn(String.format("Could not check whether page %d was removed because of null response", pageId));
        }
        
        return false;
      }
      
      if (!"publish".equals(post.getStatus()) && logger.isWarnEnabled()) {
        logger.warn(String.format("Page %d status was %s", pageId, post.getStatus()));
        return false;
      }
    } catch (PostNotFoundException e) {
      logger.info(String.format("Page %d was removed with status %s (%s)", pageId, e.getCode(), e.getErrorMessage()));
      return true;
    }
    
    return false;
  }

}
