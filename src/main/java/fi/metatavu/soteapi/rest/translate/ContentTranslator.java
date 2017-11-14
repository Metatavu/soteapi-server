package fi.metatavu.soteapi.rest.translate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentImageMeta;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.server.rest.model.Content;
import fi.metatavu.soteapi.server.rest.model.ContentImage;
import fi.metatavu.soteapi.server.rest.model.LocalizedValue;

/**
 * Translator for content related translations
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ContentTranslator extends AbstractTranslator {
  
  /**
   * Translates database content entity to REST content 
   * 
   * @param contentEntity Database content entity to be translated
   * @param contentTitleEntities Database entities for titles related to the content
   * @return Content translated to the REST content
   */
  public Content translateContent(fi.metatavu.soteapi.persistence.model.Content contentEntity, List<ContentTitle> contentTitleEntities, List<ContentData> contentDataEntities) {
    if (contentEntity == null) {
      return null;
    }

    Long parentId = contentEntity.getParent() != null ? contentEntity.getParent().getId() : null;
    
    Content content = new Content();
    content.setId(contentEntity.getId());
    content.setSlug(contentEntity.getSlug());
    content.setTitle(translateContentTitles(contentTitleEntities));
    content.setContent(translateContentDatas(contentDataEntities));
    content.setType(translateEnum(Content.TypeEnum.class, contentEntity.getContentType()));
    content.setParentId(parentId);
    content.setCategory(contentEntity.getCategory());
    content.setLevel(getContentLevel(contentEntity));
    content.setCreated(contentEntity.getCreated());
    content.setModified(contentEntity.getModified());

    return content;
  }
  
  /**
   * Translates list of contents to REST entities
   * 
   * @param contentEntities List of database content enties
   * @param contentTitleEntities List of list of content title entities
   * @return List of translated content entities
   */
  public List<Content> translateContents(List<fi.metatavu.soteapi.persistence.model.Content> contentEntities, List<List<ContentTitle>> contentTitleEntities, List<List<ContentData>> contentDataEntities) {
    List<Content> results = new ArrayList<>(contentEntities.size());
    Iterator<fi.metatavu.soteapi.persistence.model.Content> contentEntityIterator = contentEntities.iterator();
    Iterator<List<ContentTitle>> contentTitleEntitiesIterator = contentTitleEntities.iterator();
    Iterator<List<ContentData>> contentDataEntitiesIterator = contentDataEntities.iterator();
    while (contentEntityIterator.hasNext() && contentTitleEntitiesIterator.hasNext()) {
      results.add(translateContent(contentEntityIterator.next(), contentTitleEntitiesIterator.next(), contentDataEntitiesIterator.next()));
    }
    
    return results;
  }
  
  /**
   * Translates database content content entities to REST entities
   * 
   * @param contentContentEntities Entities to be translates
   * @return translated entities
   */
  public List<LocalizedValue> translateContentDatas(List<ContentData> contentContentEntities) {
    List<LocalizedValue> results = new ArrayList<>(contentContentEntities.size());
    for (ContentData contentContentEntity : contentContentEntities) {
      results.add(translateContentData(contentContentEntity));
    }
    return results;
  }
  
  /**
   * Translates content image metadata
   * 
   * @param contentImageMetaEntity Content image metadata entity to be translated
   * @return REST entity for image metadata
   */
  public ContentImage translateContentImage(ContentImageMeta contentImageMetaEntity) {
    if (contentImageMetaEntity == null) {
      return null;
    }

    ContentImage contentImage = new ContentImage();
    contentImage.setId(contentImageMetaEntity.getId());
    contentImage.setName(contentImageMetaEntity.getName());
    contentImage.setContentType(contentImageMetaEntity.getContentType());
    contentImage.setSize(0l); //TODO: add size or calculate from data
    contentImage.setType(contentImageMetaEntity.getType());
    return contentImage;
  }
  
  /**
   * Translates list of content image metadata entities
   * 
   * @param contentImageMetaEntitys Entities to be translated
   * @return Translated entities
   */
  public List<ContentImage> translateContentImages(List<ContentImageMeta> contentImageMetaEntitys) {
    List<ContentImage> results = new ArrayList<>(contentImageMetaEntitys.size());
    for (ContentImageMeta contentImageMetaEntity : contentImageMetaEntitys) {
      results.add(translateContentImage(contentImageMetaEntity));
    }
    return results;
  }
  
  private LocalizedValue translateContentData(ContentData contentContentEntity) {
    LocalizedValue localizedValue = new LocalizedValue();
    localizedValue.setLanguage(contentContentEntity.getLanguage());
    localizedValue.setValue(contentContentEntity.getValue());
    return localizedValue;
  }
  
  private LocalizedValue translateContentTitle(ContentTitle contentTitleEntity) {
    LocalizedValue localizedValue = new LocalizedValue();
    localizedValue.setLanguage(contentTitleEntity.getLanguage());
    localizedValue.setValue(contentTitleEntity.getValue());
    return localizedValue;
  }
  
  private int getContentLevel(fi.metatavu.soteapi.persistence.model.Content contentEntity) {
    int level = 0;
    while(contentEntity.getParent() != null) {
      level++;
      contentEntity = contentEntity.getParent();
    }
    return level;
  }
  
  private List<LocalizedValue> translateContentTitles(List<ContentTitle> contentTitleEntities) {
    List<LocalizedValue> results = new ArrayList<>(contentTitleEntities.size());
    for (ContentTitle contentTitleEntity : contentTitleEntities) {
      results.add(translateContentTitle(contentTitleEntity));
    }
    return results;
  }
  
}
