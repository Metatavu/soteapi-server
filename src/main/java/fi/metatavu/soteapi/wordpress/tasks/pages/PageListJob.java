package fi.metatavu.soteapi.wordpress.tasks.pages;

import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.model.Page;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
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
  protected PageListTask createTask(int page) {
    PageListTask newTask = new PageListTask();
    newTask.setPriority(Boolean.FALSE);
    newTask.setUniqueId(String.format("wp-page-list-%d", page));
    newTask.setPage(page);
    return newTask;
  }
  
  @Override
  protected void process(Page pageData) {
    if (pageData == null) {
      return;
    }
    
    PageUpdateTaskModel pageModel = new PageUpdateTaskModel();
    pageModel.setOriginId(pageData.getId().toString());
    pageModel.setSlug(pageData.getSlug());
    pageModel.setTitle(pageData.getTitle().getRendered());
    pageModel.setContent(pageData.getContent().getRendered());
    
    PageUpdateTask pageEntityTask = new PageUpdateTask();
    pageEntityTask.setPostUpdateModel(pageModel);
    pageEntityTask.setPriority(Boolean.FALSE);
    pageEntityTask.setUniqueId(String.format("wp-page-update-%d", pageData.getId()));
    pageUpdateQueue.enqueueTask(pageEntityTask);
  }
    
}
