package fi.metatavu.soteapi.wordpress.tasks.pages;

import java.util.List;

import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.model.Page;
import com.afrozaar.wordpress.wpapi.v2.request.Request;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractListJob;

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
    PageUpdateTaskModel pageModel = new PageUpdateTaskModel();
    
    
    if (pageData.getAdditionalProperties().containsKey("categories")) {
      categoriesObject = pageData.getAdditionalProperties().get("categories");
    }
    
    if (categoriesObject != null && categoriesObject instanceof List) {
      List<Integer> categoriesList = (List<Integer>) categoriesObject;
      if (!categoriesList.isEmpty()) {
        Long categoryId = categoriesList.get(0).longValue();
        pageModel.setCategoryId(categoryId);
      }
    }
    
    String parentOriginId = pageData.getParent() != null && pageData.getParent() > 0 ? pageData.getParent().toString() : null;
    
    pageModel.setOriginId(pageData.getId().toString());
    pageModel.setSlug(pageData.getSlug());
    pageModel.setTitle(pageData.getTitle().getRendered());
    pageModel.setContent(pageData.getContent().getRendered());
    pageModel.setParentOriginId(parentOriginId);
    
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
