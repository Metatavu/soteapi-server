package fi.metatavu.soteapi.wordpress.tasks.posts;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.content.ContentController;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.tasks.AbstractUpdateJob;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

public class PostUpdateJob extends AbstractUpdateJob {
  
  @Inject
  private PostUpdateQueue postUpdateQueue;
  
  @Inject
  private ContentController contentController;
  
  @Override
  protected void execute() {
    PostUpdateTask postUpdateTask = postUpdateQueue.next();
    if (postUpdateTask != null) {
      performTask(postUpdateTask);
    }
  }
  
  private void performTask(PostUpdateTask task) {
    PostUpdateTaskModel postUpdateModel = task.getPostUpdateModel();

    if (postUpdateModel == null) {
      return;
    }

    String originId = postUpdateModel.getOriginId();
    Content contentEntity = contentController.findContentByOriginId(originId);
    
    if (contentEntity != null) {
      updateExistingPage(contentEntity, postUpdateModel);
      return;
    }
    
    createNewContent(postUpdateModel);
  }
  
  private void createNewContent(PostUpdateTaskModel postUpdateModel) {
    if (postUpdateModel == null) {
      return;
    } 
    
    String originId = postUpdateModel.getOriginId();
    String slug = postUpdateModel.getSlug();
    String contentTitle = postUpdateModel.getTitle();
    String contentData = postUpdateModel.getContent();

    Content contentEntity = contentController.createContent(originId, slug, ContentType.NEWS, null);
    
    if (StringUtils.isNotEmpty(contentTitle)) {
      contentController.createContentTitle(WordpressConsts.DEFAULT_LANGUAGE, contentTitle, contentEntity);
    }

    if (StringUtils.isNotEmpty(contentData)) {
      contentController.createContentData(WordpressConsts.DEFAULT_LANGUAGE, contentData, contentEntity);
    }
  }
  
  private void updateExistingPage(Content contentEntity, PostUpdateTaskModel postUpdateModel) {

    contentController.updateContent(contentEntity, postUpdateModel.getOriginId(), postUpdateModel.getSlug(), ContentType.NEWS, null);
    String contentTitleContent = postUpdateModel.getTitle();
    String contentData = postUpdateModel.getContent();

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
