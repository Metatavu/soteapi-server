package fi.metatavu.soteapi.wordpress.tasks.pages;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.afrozaar.wordpress.wpapi.v2.model.Term;

import fi.metatavu.soteapi.content.ContentController;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractWordpressJob;

public class PageUpdateJob extends AbstractWordpressJob {
  
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
  
  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.PAGES_SYNC_ENABLED;
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
    Long orderIndex = pageUpdateModel.getOrderIndex();
    
    Content parent = null;
    
    if (StringUtils.isNotEmpty(parentOriginId)) {
      parent = contentController.findContentByOriginId(parentOriginId);
      if (parent == null) {
        return;
      }
    }
    
    Long categoryId = pageUpdateModel.getCategoryId();
    String categorySlug = null;
    if (categoryId != null) {
      Term category = findCategoryById(categoryId);
      if (category != null) {
        categorySlug = category.getSlug();
      }
    }
    
    ContentType contentType = ContentType.PAGE;
    
    if (StringUtils.isNotEmpty(contentData)) {
      String pageLink = getPageLink(contentData);
      if (pageLink != null) {
        contentData = pageLink;
        contentType = ContentType.LINK;
      }
    }
    
    Content contentEntity = contentController.createContent(originId, slug, contentType, parent, categorySlug, orderIndex);
    
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
    
    Long categoryId = pageUpdateModel.getCategoryId();
    String categorySlug = null;
    if (categoryId != null) {
      Term category = findCategoryById(categoryId);
      if (category != null) {
        categorySlug = category.getSlug();
      }
    }
    
    String contentTitleContent = pageUpdateModel.getTitle();
    String contentData = pageUpdateModel.getContent();
    ContentType contentType = ContentType.PAGE;
    Long orderIndex = pageUpdateModel.getOrderIndex();
    
    if (StringUtils.isNotEmpty(contentData)) {
      String pageLink = getPageLink(contentData);
      if (pageLink != null) {
        contentData = pageLink;
        contentType = ContentType.LINK;
      }
      
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
    
    if (StringUtils.isNotEmpty(contentTitleContent)) {
      updateExistingPageTitle(contentEntity, contentTitleContent);
    }
    
    contentController.updateContent(contentEntity, pageUpdateModel.getOriginId(), pageUpdateModel.getSlug(), parent, contentType, categorySlug, orderIndex);
  }

  private void updateExistingPageTitle(Content contentEntity, String contentTitleContent) {
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
  
  private String getPageLink(String contentData) {
    Document document = Jsoup.parse(contentData);
    Elements children = document.body().children();
    if (children.size() == 1) {
      Element child = children.get(0);
      Tag tag = child.tag();
      String tagName = tag.getName();
      if ("a".equals(tagName)) {
        return child.attr("href");
      }

      if ("p".equals(tagName) && child.children().size() == 1) {
        Element childOfChild = child.children().get(0);
        if ("a".equals(childOfChild.tag().getName())) {
          return childOfChild.attr("href");
        }
      }
    }

    return null;
  }
  
}
