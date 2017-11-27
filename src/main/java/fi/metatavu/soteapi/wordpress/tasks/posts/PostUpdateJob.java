package fi.metatavu.soteapi.wordpress.tasks.posts;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.afrozaar.wordpress.wpapi.v2.model.Term;

import fi.metatavu.soteapi.content.ContentController;
import fi.metatavu.soteapi.firebase.FirebaseController;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.utils.HtmlUtils;
import fi.metatavu.soteapi.utils.TimeUtils;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractWordpressJob;

public class PostUpdateJob extends AbstractWordpressJob {

  @Inject
  private FirebaseController firebaseController;
  
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

  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.POSTS_SYNC_ENABLED;
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
    Long categoryId = postUpdateModel.getCategoryId();
    String categorySlug = null;
    Long orderIndex = postUpdateModel.getOrderIndex();
    OffsetDateTime created = TimeUtils.parseOffsetDateTime(postUpdateModel.getCreated());
    OffsetDateTime modified = TimeUtils.parseOffsetDateTime(postUpdateModel.getModified());
    
    if (categoryId != null) {
      Term category = findCategoryById(categoryId);
      if (category != null) {
        categorySlug = category.getSlug();
      }
    }
    
    Content contentEntity = contentController.createContent(WordpressConsts.ORIGIN, originId, slug, ContentType.NEWS, null, categorySlug, created, modified, orderIndex);
    
    if (StringUtils.isNotEmpty(contentTitle)) {
      contentController.createContentTitle(WordpressConsts.DEFAULT_LANGUAGE, contentTitle, contentEntity);
    }

    if (StringUtils.isNotEmpty(contentData)) {
      contentController.createContentData(WordpressConsts.DEFAULT_LANGUAGE, contentData, contentEntity);
    }
    
    sendPushNotification(contentEntity.getId(), contentTitle, contentData);
  }
  
  private void updateExistingPage(Content contentEntity, PostUpdateTaskModel postUpdateModel) {

    Long categoryId = postUpdateModel.getCategoryId();
    String categorySlug = null;
    if (categoryId != null) {
      Term category = findCategoryById(categoryId);
      if (category != null) {
        categorySlug = category.getSlug();
      }
    }
    
    Long orderIndex = postUpdateModel.getOrderIndex();
    if (contentEntity.getArchived()) {
      contentController.updateContentArchived(contentEntity, false);
    }
    
    OffsetDateTime created = TimeUtils.parseOffsetDateTime(postUpdateModel.getCreated());
    OffsetDateTime modified = TimeUtils.parseOffsetDateTime(postUpdateModel.getModified());
    
    contentController.updateContent(contentEntity, postUpdateModel.getOriginId(), postUpdateModel.getSlug(), null, ContentType.NEWS, categorySlug, created, modified, orderIndex);
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

  private void sendPushNotification(Long id, String title, String content) {
    Map<String, Object> data = new HashMap<>();
    data.put("soteApiEvent", "ITEM-CREATED");
    data.put("itemId", String.valueOf(id));
    data.put("itemType", "NEWS");
    
    String body = StringUtils.abbreviate(HtmlUtils.html2text(content), 200);
    firebaseController.sendNotifcationToTopic("news", title, body, data);
  }
  
}
