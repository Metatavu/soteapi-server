package fi.metatavu.soteapi.wordpress.tasks.pages;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.model.Page;
import com.afrozaar.wordpress.wpapi.v2.request.Request;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.utils.TimeUtils;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractListJob;

@ApplicationScoped
public class PageListJob extends AbstractListJob<Page, PageListTask> {
  
  @Inject
  private PageListQueue pageListQueue;
  
  @Inject
  private PageUpdateQueue pageUpdateQueue;
  
  @Override
  protected AbstractSoteApiTaskQueue<PageListTask> getQueue() {
    return pageListQueue;
  }
  
  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.PAGES_SYNC_ENABLED;
  }
  
  @Override
  protected PageListTask createTask(int page) {
    PageListTask newTask = new PageListTask();
    newTask.setPriority(Boolean.FALSE);
    newTask.setUniqueId(String.format("wp-page-list-%d", page));
    newTask.setPage(page);
    return newTask;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected void process(Page pageData) {
    if (pageData == null) {
      return;
    }

    Object categoriesObject = null;
    Long categoryId = null;
    
    if (pageData.getAdditionalProperties().containsKey("categories")) {
      categoriesObject = pageData.getAdditionalProperties().get("categories");
    }
    
    if (categoriesObject != null && categoriesObject instanceof List) {
      List<Integer> categoriesList = (List<Integer>) categoriesObject;
      if (!categoriesList.isEmpty()) {
        categoryId = categoriesList.get(0).longValue();
      }
    }
    
    String parentOriginId = pageData.getParent() != null && pageData.getParent() > 0 ? pageData.getParent().toString() : null;
    String title = pageData.getTitle().getRendered();
    String content = pageData.getContent().getRendered();
    String slug = pageData.getSlug();
    String originId = pageData.getId().toString();
    Long orderIndex = pageData.getMenuOrder();
    String created = TimeUtils.toIsoString(TimeUtils.parseOffsetDateTimeLocal(pageData.getDateGmt()));
    String modified = TimeUtils.toIsoString(TimeUtils.parseOffsetDateTimeLocal(pageData.getModifiedGmt()));
    
    PageUpdateTaskModel pageModel = new PageUpdateTaskModel(title, content, slug, originId, parentOriginId, categoryId, created, modified, orderIndex);
    PageUpdateTask pageEntityTask = new PageUpdateTask();
    pageEntityTask.setPostUpdateModel(pageModel);
    pageEntityTask.setPriority(Boolean.FALSE);
    pageEntityTask.setUniqueId(String.format("wp-page-update-%d", pageData.getId()));
    pageUpdateQueue.enqueueTask(pageEntityTask);
  }

  @Override
  protected String getEndPointUri() {
    return Request.PAGES;
  }
    
}
