package fi.metatavu.soteapi.wordpress.tasks.pages;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.math.NumberUtils;

import com.afrozaar.wordpress.wpapi.v2.exception.PageNotFoundException;
import com.afrozaar.wordpress.wpapi.v2.model.Page;

import fi.metatavu.soteapi.content.ContentController;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractWordpressJob;

public class PageRemoveJob extends AbstractWordpressJob {
  
  @Inject
  private PageRemoveQueue pageRemoveQueue;
  
  @Inject
  private ContentController contentController;
  
  @Override
  protected void execute() {
    PageRemoveTask pageRemoveTask = pageRemoveQueue.next();
    if (pageRemoveTask != null) {
      performTask(pageRemoveTask);
    } else {
      if (pageRemoveQueue.isEmptyAndLocalNodeResponsible()) {
        fillQueue();  
      }
    }
  }
  
  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.PAGES_SYNC_ENABLED;
  }
  
  private void performTask(PageRemoveTask task) {
    String originId = task.getOriginId();
    Content contentEntity = contentController.findContentByOriginId(originId);
    if (contentEntity != null && !contentEntity.getArchived() && isPageArchived(originId)) {
      contentController.updateContentArchived(contentEntity, Boolean.TRUE);
    }
  }
  
  @SuppressWarnings ("squid:S1166")
  private boolean isPageArchived(String originId) {
    Long pageId = NumberUtils.createLong(originId);
    
    try {
      Page page = getWordpressClient().getPage(pageId);
      if (page == null || !"publish".equals(page.getStatus())) {
        return true;        
      }
    } catch (PageNotFoundException e) {
      return true;
    }
    
    return false;
  }

  private void fillQueue() {
    List<String> originIds = contentController.listOriginIds(WordpressConsts.ORIGIN);
    for (String originId : originIds) {
      pageRemoveQueue.enqueueTask(new PageRemoveTask(originId));
    }
  }
  
}
