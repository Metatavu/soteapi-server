package fi.metatavu.soteapi.wordpress.tasks.pages;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.content.ContentController;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.tasks.AbstractUpdateJob;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

public class PageUpdateJob extends AbstractUpdateJob {
  
  @Inject
  private PageUpdateQueue pageUpdateQueue;
  
  @Inject
  private ContentController contentController;
  
  @Override
  protected void execute() {
    PageUpdateTask pageUpdateTask = pageUpdateQueue.next();
    if (pageUpdateTask != null) {
      performTask(pageUpdateTask);
    }
  }
  
  private void performTask(PageUpdateTask task) {
    PageUpdateTaskModel pageUpdateModel = task.getPostUpdateModel();

    if (pageUpdateModel == null) {
      return;
    }

    String originId = pageUpdateModel.getOriginId();
    Content contentEntity = contentController.findContentByOriginId(originId);
    
    if (contentEntity != null) {
      updateExistingPage(contentEntity, pageUpdateModel);
      return;
    }
    
    createNewContent(pageUpdateModel);
  }
  
  private void createNewContent(PageUpdateTaskModel pageUpdateModel) {
    if (pageUpdateModel == null) {
      return;
    } 
    
    String originId = pageUpdateModel.getOriginId();
    String slug = pageUpdateModel.getSlug();
    String contentTitle = pageUpdateModel.getTitle();
    String contentData = pageUpdateModel.getContent();
    String parentOriginId = pageUpdateModel.getParentOriginId();
    Content parent = null;
    
    if (StringUtils.isNotEmpty(parentOriginId)) {
      parent = contentController.findContentByOriginId(parentOriginId);
      if (parent == null) {
        return;
      }
    }
    
    Content contentEntity = contentController.createContent(originId, slug, ContentType.PAGE, parent);
    
    if (StringUtils.isNotEmpty(contentTitle)) {
      contentController.createContentTitle(WordpressConsts.DEFAULT_LANGUAGE, contentTitle, contentEntity);
    }

    if (StringUtils.isNotEmpty(contentData)) {
      contentController.createContentData(WordpressConsts.DEFAULT_LANGUAGE, contentData, contentEntity);
    }
  }
  
  private void updateExistingPage(Content contentEntity, PageUpdateTaskModel pageUpdateModel) {
    String parentOriginId = pageUpdateModel.getParentOriginId();
    Content parent = null;
    
    if (StringUtils.isNotEmpty(parentOriginId)) {
      parent = contentController.findContentByOriginId(parentOriginId);
      if (parent == null) {
        return;
      }
    }
    
    contentController.updateContent(contentEntity, pageUpdateModel.getOriginId(), pageUpdateModel.getSlug(), ContentType.PAGE, parent);
    String contentTitleContent = pageUpdateModel.getTitle();
    String contentData = pageUpdateModel.getContent();

    if (StringUtils.isNotEmpty(contentTitleContent)) {
      ContentTitle contentTitleEntity = contentController.listContentTitlesByContent(contentEntity)
        .stream()
        .filter(title -> title.getLanguage().equals(WordpressConsts.DEFAULT_LANGUAGE))
        .findAny()
        .orElse(null);
      
      if (contentTitleEntity != null) {
        contentController.updateContentTitle(contentTitleEntity, WordpressConsts.DEFAULT_LANGUAGE, contentTitleContent, contentEntity);
      } else {
        contentController.createContentTitle(WordpressConsts.DEFAULT_LANGUAGE, contentTitleContent, contentEntity);
      }
    }

    if (StringUtils.isNotEmpty(contentData)) {
      ContentData contentDataEntity = contentController.listContentDataByContent(contentEntity)
        .stream()
        .filter(content -> content.getLanguage().equals(WordpressConsts.DEFAULT_LANGUAGE))
        .findAny()
        .orElse(null);
      
      if (contentDataEntity != null) {
        contentController.updateContentData(contentDataEntity, WordpressConsts.DEFAULT_LANGUAGE, contentData, contentEntity);
      } else {
        contentController.createContentData(WordpressConsts.DEFAULT_LANGUAGE, contentData, contentEntity);
      } 
    }
  }
  
}
