package fi.metatavu.soteapi.wordpress.tasks.pages;

import javax.inject.Inject;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import com.afrozaar.wordpress.wpapi.v2.exception.PageNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Page;

import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractRemoveJob;

public class PageRemoveJob extends AbstractRemoveJob<PageRemoveTask, PageRemoveQueue> {

  @Inject
  private Logger logger;

  @Inject
  private PageRemoveQueue pageRemoveQueue;

  @Override
  protected PageRemoveQueue getQueue() {
    return pageRemoveQueue;
  }
  
  @Override
  protected PageRemoveTask createTask(String originId) {
    return new PageRemoveTask(originId);
  }

  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.PAGES_SYNC_ENABLED;
  }
  
  @SuppressWarnings ("squid:S1166")
  protected boolean isArchived(String originId) {
    Long pageId = NumberUtils.createLong(originId);
    
    try {
      Page page = getWordpressClient().getPage(pageId);
      if (page == null) {
        if (logger.isWarnEnabled()) {
          logger.warn(String.format("Could not check whether page %d was removed because of null response", pageId));
        }
        
        return false;
      }
      
      if (!"publish".equals(page.getStatus()) && logger.isWarnEnabled()) {
        logger.warn(String.format("Page %d status was %s", pageId, page.getStatus()));
        return false;
      }
    } catch (PageNotFoundException e) {
      logger.info(String.format("Page %d was removed", pageId));
      return true;
    }
    
    return false;
  }

}
