package fi.metatavu.soteapi.wordpress.tasks.pages;

import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.model.Page;

import fi.metatavu.soteapi.wordpress.tasks.AbstractListJob;

public class PageListJob extends AbstractListJob<Page> {
  
  @Inject
  private PageListQueue pageListQueue;
  
  @Inject
  private PageUpdateQueue pageUpdateQueue;
  
  @Override
  protected void execute() {
    PageListTask pageListTask = pageListQueue.next();
    if (pageListTask != null) {
      performTask(pageListTask);
    } else if (pageListQueue.isEmptyAndLocalNodeResponsible()) {
      fillQueue();
    }
  }
  
  private void performTask(PageListTask task) {
    getDataFromPage(Page.class, task.getPage()).forEach(this::processPage);
  }
  
  private void processPage(Page pageData) {
    if (pageData == null) {
      return;
    }
    
    PageUpdateTaskModel pageModel = new PageUpdateTaskModel();
    pageModel.setOriginId(pageData.getId().toString());
    pageModel.setSlug(pageData.getSlug());
    pageModel.setTitle(pageData.getTitle().getRendered());
    pageModel.setContent(pageData.getContent().getRendered());
    
    PageUpdateTask pageEntityTask = new PageUpdateTask();
    pageEntityTask.setPageModel(pageModel);
    pageEntityTask.setPriority(Boolean.FALSE);
    pageEntityTask.setUniqueId(String.format("wp-page-update-%d", pageData.getId()));
    pageUpdateQueue.enqueueTask(pageEntityTask);
  }
  
  private void fillQueue() {
    int numberOfPages = getNumberOfPages(Page.class);
    for (int i = 1; i <= numberOfPages; i++) {
      PageListTask newTask = new PageListTask();
      newTask.setPriority(Boolean.FALSE);
      newTask.setUniqueId(String.format("wp-page-list-%d", i));
      newTask.setPage(i);
      pageListQueue.enqueueTask(newTask);
    }
    
  }
  
}
