package fi.metatavu.soteapi.wordpress.tasks;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.page.PageController;
import fi.metatavu.soteapi.persistence.model.Page;
import fi.metatavu.soteapi.persistence.model.PageContent;
import fi.metatavu.soteapi.persistence.model.PageTitle;
import fi.metatavu.soteapi.tasks.AbstractUpdateJob;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

public class PageUpdateJob extends AbstractUpdateJob {
  
  @Inject
  private PageUpdateQueue pageUpdateQueue;
  
  @Inject
  private PageController pageController;
  
  @Override
  protected void execute() {
    PageUpdateTask pageUpdateTask = pageUpdateQueue.next();
    if (pageUpdateTask != null) {
      performTask(pageUpdateTask);
    }
  }
  
  private void performTask(PageUpdateTask task) {
    PageUpdateTaskModel pageUpdateModel = task.getPageModel();

    if (pageUpdateModel == null) {
      return;
    }

    String originId = pageUpdateModel.getOriginId();
    Page pageEntity = pageController.findPageByOriginId(originId);
    
    if (pageEntity != null) {
      updateExistingPage(pageEntity, pageUpdateModel);
      return;
    }
    
    createNewPage(pageUpdateModel);
  }
  
  private void createNewPage(PageUpdateTaskModel pageUpdateModel) {
    if (pageUpdateModel == null) {
      return;
    } 
    
    String originId = pageUpdateModel.getOriginId();
    String slug = pageUpdateModel.getSlug();
    String pageTitle = pageUpdateModel.getTitle();
    String pageContent = pageUpdateModel.getContent();
    Page pageEntity = pageController.createPage(originId, slug);
    
    if (StringUtils.isNotEmpty(pageTitle)) {
      pageController.createPageTitle(WordpressConsts.DEFAULT_LANGUAGE, pageTitle, pageEntity);
    }

    if (StringUtils.isNotEmpty(pageContent)) {
      pageController.createPageContent(WordpressConsts.DEFAULT_LANGUAGE, pageContent, pageEntity);
    }
  }
  
  private void updateExistingPage(Page pageEntity, PageUpdateTaskModel pageUpdateModel) {
    pageController.updatePage(pageEntity, pageUpdateModel.getOriginId(), pageUpdateModel.getSlug());
    String pageTitleContent = pageUpdateModel.getTitle();
    String pageContent = pageUpdateModel.getContent();

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
}
