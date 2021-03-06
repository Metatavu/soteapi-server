package fi.metatavu.soteapi.rss.news;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.content.ContentController;
import fi.metatavu.soteapi.notification.NotificationController;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.tasks.AbstractUpdateJob;
import fi.metatavu.soteapi.utils.HtmlUtils;
import fi.metatavu.soteapi.utils.TimeUtils;

@ApplicationScoped
public class NewsUpdateJob extends AbstractUpdateJob {
  
  @Inject
  private NotificationController firebaseController;
  
  @Inject
  private NewsUpdateQueue newsUpdateQueue;

  @Inject
  private ContentController contentController;

  @Override
  protected void execute() {
    NewsUpdateTask newsUpdateTask = newsUpdateQueue.next();
    if (newsUpdateTask != null) {
      performTask(newsUpdateTask);
    }
  }
  
  @Override
  protected String getEnabledSetting() {
    return NewsConsts.NEWS_SYNC_ENABLED;
  }

  private void performTask(NewsUpdateTask task) {
    NewsUpdateModel newsUpdateModel = task.getModel();
    if (newsUpdateModel == null) {
      return;
    }

    String originId = newsUpdateModel.getOriginId();
    Content contentEntity = contentController.findContentByOriginId(originId);
    
    if (contentEntity != null) {
      updateExistingPage(contentEntity, newsUpdateModel);
      return;
    }
    
    createNewContent(newsUpdateModel);
  }
  
  private void createNewContent(NewsUpdateModel newsUpdateModel) {
    if (newsUpdateModel == null) {
      return;
    } 
    
    String originId = newsUpdateModel.getOriginId();
    String slug = newsUpdateModel.getSlug();
    String contentTitle = newsUpdateModel.getTitle();
    String contentData = newsUpdateModel.getContent();
    String categorySlug = newsUpdateModel.getCategorySlug();
    Long orderIndex = newsUpdateModel.getOrderIndex();
    OffsetDateTime created = TimeUtils.parseOffsetDateTime(newsUpdateModel.getCreated());
    OffsetDateTime modified = TimeUtils.parseOffsetDateTime(newsUpdateModel.getModified());
    
    Content contentEntity = contentController.createContent(NewsConsts.ORIGIN, originId, slug, ContentType.NEWS, null, categorySlug, created, modified, orderIndex);
    
    if (StringUtils.isNotEmpty(contentTitle)) {
      contentController.createContentTitle(NewsConsts.DEFAULT_LANGUAGE, contentTitle, contentEntity);
    }

    if (StringUtils.isNotEmpty(contentData)) {
      contentController.createContentData(NewsConsts.DEFAULT_LANGUAGE, contentData, contentEntity);
    }
    
    sendPushNotification(contentEntity.getId(), contentTitle, contentData);
  }
  
  private void updateExistingPage(Content contentEntity, NewsUpdateModel newsUpdateModel) {
    String categorySlug = newsUpdateModel.getCategorySlug();
    OffsetDateTime created = TimeUtils.parseOffsetDateTime(newsUpdateModel.getCreated());
    OffsetDateTime modified = TimeUtils.parseOffsetDateTime(newsUpdateModel.getModified());
    contentController.updateContent(contentEntity, newsUpdateModel.getOriginId(), newsUpdateModel.getSlug(), null, ContentType.NEWS, categorySlug, created, modified, newsUpdateModel.getOrderIndex());
    String contentTitleContent = newsUpdateModel.getTitle();
    String contentData = newsUpdateModel.getContent();
    
    if (contentEntity.getArchived()) {
      contentController.updateContentArchived(contentEntity, false);
    }

    if (StringUtils.isNotEmpty(contentTitleContent)) {
      ContentTitle contentTitleEntity = contentController.listContentTitlesByContent(contentEntity)
        .stream()
        .filter(title -> title.getLanguage().equals(NewsConsts.DEFAULT_LANGUAGE))
        .findAny()
        .orElse(null);
      
      if (contentTitleEntity != null) {
        contentController.updateContentTitle(contentTitleEntity, NewsConsts.DEFAULT_LANGUAGE, contentTitleContent, contentEntity);
      } else {
        contentController.createContentTitle(NewsConsts.DEFAULT_LANGUAGE, contentTitleContent, contentEntity);
      }
    }

    if (StringUtils.isNotEmpty(contentData)) {
      ContentData contentDataEntity = contentController.listContentDataByContent(contentEntity)
        .stream()
        .filter(content -> content.getLanguage().equals(NewsConsts.DEFAULT_LANGUAGE))
        .findAny()
        .orElse(null);
      
      if (contentDataEntity != null) {
        contentController.updateContentData(contentDataEntity, NewsConsts.DEFAULT_LANGUAGE, contentData, contentEntity);
      } else {
        contentController.createContentData(NewsConsts.DEFAULT_LANGUAGE, contentData, contentEntity);
      } 
    }
  }
  
  private void sendPushNotification(Long id, String title, String content) {
    Map<String, Object> data = new HashMap<>();
    data.put("soteApiEvent", "ITEM-CREATED");
    data.put("itemId", String.valueOf(id));
    data.put("itemType", "NEWS");
    
    String body = StringUtils.abbreviate(HtmlUtils.html2text(content), 200);
    firebaseController.sendNotificationToTopic("news", title, body, data);
  }
  
}
