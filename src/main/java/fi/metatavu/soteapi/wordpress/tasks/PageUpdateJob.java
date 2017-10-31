package fi.metatavu.soteapi.wordpress.tasks;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.afrozaar.wordpress.wpapi.v2.model.Page;

import fi.metatavu.soteapi.page.PageController;
import fi.metatavu.soteapi.persistence.model.PageContent;
import fi.metatavu.soteapi.persistence.model.PageTitle;
import fi.metatavu.soteapi.tasks.TaskController;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

public class PageUpdateJob extends AbstractWordpressUpdateJob<Page> {
  
  @Inject
  private TaskController taskController;
  
  @Inject
  private PageTaskQueue pageTaskQueue;
  
  @Inject
  private PageController pageController;
  
  @Override
  protected void execute() {
    PageTask pageTask = pageTaskQueue.next();
    if (pageTask != null) {
      performTask(pageTask);
    } else if (taskController.isEmptyAndLocalNodeResponsible(pageTaskQueue.getName())) {
      fillQueue();
    }
  }
  
  private void performTask(PageTask task) {
    getDataFromPage(Page.class, task.getPage()).forEach(this::processPage);
  }
  
  private void processPage(Page pageData) {
    String originId = pageData.getId().toString();
    List<fi.metatavu.soteapi.persistence.model.Page> pageEntities = pageController.listPagesByOriginId(originId);
    
    if (pageEntities != null && !pageEntities.isEmpty()) {
      updateExistingPage(pageEntities.get(0), pageData);
      return;
    }
    
    createNewPage(pageData);
  }
  
  private void createNewPage(Page pageData) {
    if (pageData == null) {
      return;
    } 
    
    String originId = pageData.getId().toString();
    String slug = pageData.getSlug();
    String pageTitle = pageData.getTitle().getRendered();
    String pageContent = pageData.getContent().getRendered();
    fi.metatavu.soteapi.persistence.model.Page pageEntity = pageController.createPage(originId, slug);
    
    if (StringUtils.isNotEmpty(pageTitle)) {
      pageController.createPageTitle(WordpressConsts.DEFAULT_LANGUAGE, pageTitle, pageEntity);
    }

    if (StringUtils.isNotEmpty(pageContent)) {
      pageController.createPageContent(WordpressConsts.DEFAULT_LANGUAGE, pageContent, pageEntity);
    }
  }
  
  private void updateExistingPage(fi.metatavu.soteapi.persistence.model.Page pageEntity, Page pageData) {
    pageController.updatePage(pageEntity, pageData.getId().toString(), pageData.getSlug());
    String pageTitleContent = pageData.getTitle().getRendered();
    String pageContent = pageData.getContent().getRendered();

    if (StringUtils.isNotEmpty(pageTitleContent)) {
      PageTitle pageTitleEntity = pageController.listPageTitlesByPage(pageEntity)
        .stream()
        .filter(title -> title.getLanguage().equals(WordpressConsts.DEFAULT_LANGUAGE))
        .findAny()
        .orElse(null);
      
      if (pageTitleEntity != null) {
        pageController.updatePageTitle(pageTitleEntity, WordpressConsts.DEFAULT_LANGUAGE, pageTitleContent, pageEntity);
      } else {
        pageController.createPageTitle(WordpressConsts.DEFAULT_LANGUAGE, pageTitleContent, pageEntity);
      }
    }

    if (StringUtils.isNotEmpty(pageContent)) {
      PageContent pageContentEntity = pageController.listPageContentByPage(pageEntity)
        .stream()
        .filter(content -> content.getLanguage().equals(WordpressConsts.DEFAULT_LANGUAGE))
        .findAny()
        .orElse(null);
      
      if (pageContentEntity != null) {
        pageController.updatePageContent(pageContentEntity, WordpressConsts.DEFAULT_LANGUAGE, pageContent, pageEntity);
      } else {
        pageController.createPageContent(WordpressConsts.DEFAULT_LANGUAGE, pageContent, pageEntity);
      } 
    }
  }
  
  private void fillQueue() {
    int numberOfPages = getNumberOfPages(Page.class);
    for (int i = 1; i <= numberOfPages; i++) {
      PageTask newTask = new PageTask();
      newTask.setPriority(Boolean.FALSE);
      newTask.setUniqueId(String.format("wp-page-%d", i));
      newTask.setPage(i);
      pageTaskQueue.enqueueTask(newTask);
    }
    
  }
  
}
