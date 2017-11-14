package fi.metatavu.soteapi.content;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.persistence.dao.ContentDAO;
import fi.metatavu.soteapi.persistence.dao.ContentDataDAO;
import fi.metatavu.soteapi.persistence.dao.ContentImageDataDAO;
import fi.metatavu.soteapi.persistence.dao.ContentImageMetaDAO;
import fi.metatavu.soteapi.persistence.dao.ContentTitleDAO;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentImageData;
import fi.metatavu.soteapi.persistence.model.ContentImageMeta;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.persistence.model.ContentType;

/**
 * Controller for operations related to contents
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ContentController {

  @Inject
  private ContentDAO contentDAO;
  
  @Inject
  private ContentTitleDAO contentTitleDAO;
  
  @Inject
  private ContentDataDAO contentDataDAO;
  
  @Inject
  private ContentImageMetaDAO contentImageMetaDAO;
  
  @Inject
  private ContentImageDataDAO contentImageDataDAO;
  
  /**
   * Creates new content
   * 
   * @param originId origin id
   * @param slug slug
   * @param contentType content type
   * @param parent parent content
   * @param category category slug
   * @return created content
   */
  @SuppressWarnings ("squid:S00107")
  public Content createContent(String origin, String originId, String slug, ContentType contentType, Content parent, String category, OffsetDateTime created, OffsetDateTime modified, Long orderIndex) {
    return contentDAO.create(origin, originId, slug, contentType, parent, category, created, modified, orderIndex, false);
  }
  
  /**
   * Creates new content title
   * 
   * @param language title language
   * @param value title value
   * @param content content that title is found from
   * @return create content title
   */
  public ContentTitle createContentTitle(String language, String value, Content content) {
    return contentTitleDAO.create(language, value, content);
  }
  
  /**
   * Creates new content content
   * 
   * @param language content language
   * @param value content value
   * @param content content that content is found from
   * @return created content content
   */
  public ContentData createContentData(String language, String value, Content content) {
    return contentDataDAO.create(language, value, content);
  }

  /**
   * Update content
   *
   * @param originId originId
   * @param slug slug
   * @param parent parent
   * @param contentType contentType
   * @param category category
   * @param orderIndex orderIndex
   * @param modifier modifier
   * @return updated content
   */
  @SuppressWarnings ("squid:S00107")
  public Content updateContent(Content content, String originId, String slug, Content parent, ContentType contentType, String category, OffsetDateTime created, OffsetDateTime modified, Long orderIndex) {
    contentDAO.updateOriginId(content, originId);
    contentDAO.updateSlug(content, slug);
    contentDAO.updateParent(content, parent);
    contentDAO.updateContentType(content, contentType);
    contentDAO.updateCategory(content, category);
    contentDAO.updateCreated(content, created);
    contentDAO.updateModified(content, modified);
    contentDAO.updateOrderIndex(content, orderIndex);
    return content;
  }

  /**
   * Update contentTitle
   *
   * @param contentTitle content title to update
   * @param language language
   * @param value value
   * @param content content
   * @return updated contentTitle
   */
  public ContentTitle updateContentTitle(ContentTitle contentTitle, String language, String value, Content content) {
    contentTitleDAO.updateLanguage(contentTitle, language);
    contentTitleDAO.updateValue(contentTitle, value);
    contentTitleDAO.updateContent(contentTitle, content);
    return contentTitle;
  }
  
  /**
   * Update contentContent
   *
   * @param contentData content data to update
   * @param language language
   * @param value value
   * @param content content
   * @return updated contentContent
   */
  public ContentData updateContentData(ContentData contentData, String language, String value, Content content) {
    contentDataDAO.updateLanguage(contentData, language);
    contentDataDAO.updateValue(contentData, value);
    contentDataDAO.updateContent(contentData, content);
    return contentData;
  }
  
  /**
   * Updates content archived
   * 
   * @param contentEntity content entity
   * @param archived archived
   * @return updated content
   */
  public Content updateContentArchived(Content contentEntity, Boolean archived) {
    return contentDAO.updateArchived(contentEntity, archived);
  }

  /**
   * Finds content with contentId
   * 
   * @param contentId Contents id
   * @return Content
   */
  public Content findContentById(Long contentId) {
    return contentDAO.findById(contentId);
  }
  
  /**
   * Finds content with origin id
   * 
   * @param originId Contents origin id
   * @return Content
   */
  public Content findContentByOriginId(String originId) {
    return contentDAO.findByOriginId(originId);
  }

  /**
   * List contents optionally filtered by parent, type, firsresults and/or max results 
   * 
   * @param parent parent content
   * @param types content types
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listContents(Content parent, List<ContentType> types, Integer firstResult, Integer maxResults) {
    return contentDAO.listByParentAndTypesAndArchived(parent, types, Boolean.FALSE, firstResult, maxResults);
  }
  
  /**
   * List root contents, optionally filtered by type, first result and max results
   * 
   * @param types content types
   * @param firstResult first result
   * @param maxResults max results
   * @return list of contents
   */
  public List<Content> listRootContents(List<ContentType> types, Integer firstResult, Integer maxResults) { 
    return contentDAO.listByNullParentAndTypesAndArchived(types, Boolean.FALSE, firstResult, maxResults);
  }
  
  /**
   * Filters content by category slug
   * 
   * @param contents content list to filter
   * @param categorySlug category slug
   * @return list of contents
   */
  public List<Content> filterByCategorySlug(List<Content> contents, String categorySlug) {
    return contents
        .stream()
        .filter(content -> content.getCategory().equals(categorySlug))
        .collect(Collectors.toList());
  }
  
  /**
   * Lists content titles by content
   * 
   * @param content which titles are to be listed
   * @return List of content titles in different languages
   */
  public List<ContentTitle> listContentTitlesByContent(Content content) {
    return contentTitleDAO.listByContent(content);
  }
  
  /**
   * Lists content content by content in all available languages
   * 
   * @param content which contents are to be listed
   * @return List of content contents in all available languages
   */
  public List<ContentData> listContentDataByContent(Content content) {
    return contentDataDAO.listByContent(content);
  }
  
  /**
   * Finds content image metadata by imageId and content
   *  
   * @param imageId Images id
   * @param content Content image is related to
   * @return Content image metadata
   */
  public ContentImageMeta findContentImageMetaByIdAndContent(Long imageId, Content content) {
    return contentImageMetaDAO.findByIdAndContent(imageId, content);
  }
  
  /**
   * Finds content images data with image metadata
   * 
   * @param contentImageMeta Related image metadata
   * @return content image data
   */
  public ContentImageData findContentImageDatabyContentImageMeta(ContentImageMeta contentImageMeta) {
    return contentImageDataDAO.findByContentImageMeta(contentImageMeta);
  }
  
  /**
   * List content image metadatas by content or by content and type
   * 
   * @param content Content to list image metadatas from
   * @param type Filter with type (can be null)
   * @return List of content image metadatas
   */
  public List<ContentImageMeta> listContentImageMetaByContentFilterByType(Content content, String type) {
    if (type == null) {
      return contentImageMetaDAO.listByContent(content);
    }
    
    return contentImageMetaDAO.listByContentAndType(content, type);
  }

  /**
   * Lists non archived origin ids by origin
   * 
   * @param origin origin
   * @return origin ids
   */
  public List<String> listOriginIds(String origin) {
    return contentDAO.listOriginIdsByOriginAndArchived(origin, Boolean.FALSE);
  }
  
}
