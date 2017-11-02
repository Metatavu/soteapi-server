package fi.metatavu.soteapi.server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EnumType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.EnumUtils;

import fi.metatavu.soteapi.content.ContentController;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentData;
import fi.metatavu.soteapi.persistence.model.ContentImageData;
import fi.metatavu.soteapi.persistence.model.ContentImageMeta;
import fi.metatavu.soteapi.persistence.model.ContentTitle;
import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.rest.translate.ContentTranslator;

@RequestScoped
@Stateful
public class ContentApiImpl implements ContentsApi {

  @Inject
  private ContentTranslator contentTranslator;
  
  @Inject
  private ContentController contentController;
  
  @Inject
  private ResponseController responseController;
  
  @Override
  public Response findContent(Long contentId) throws Exception {
    Content content = contentController.findContentById(contentId);
    if (content == null) {
      return responseController.respondNotFound();
    }
    
    List<ContentTitle> contentTitles = contentController.listContentTitlesByContent(content);
    return responseController.respondOk(contentTranslator.translateContent(content, contentTitles));
  }

  @Override
  public Response findContentData(Long contentId) throws Exception {
    Content content = contentController.findContentById(contentId);
    if (content == null) {
      return responseController.respondNotFound();
    }
    
    List<ContentData> contentDatas = contentController.listContentDataByContent(content);
    return responseController.respondOk(contentTranslator.translateContentDatas(contentDatas));
  }

  @Override
  public Response findContentImage(Long contentId, Long imageId) throws Exception {
    Content content = contentController.findContentById(contentId);
    if (content == null) {
      return responseController.respondNotFound();
    }
    
    ContentImageMeta contentImageMeta = contentController.findContentImageMetaByIdAndContent(contentId, content);
    return responseController.respondOkOrNotFound(contentTranslator.translateContentImage(contentImageMeta));
  }

  @Override
  public Response getContentImageData(Long contentId, Long imageId, Integer size) throws Exception {
    //TODO: scale image if size is specified
    
    Content content = contentController.findContentById(contentId);
    if (content == null) {
      return responseController.respondNotFound();
    }
    
    ContentImageMeta contentImageMeta = contentController.findContentImageMetaByIdAndContent(imageId, content);
    if (contentImageMeta == null) {
      return responseController.respondNotFound();
    }
    
    ContentImageData contentImageData = contentController.findContentImageDatabyContentImageMeta(contentImageMeta);
    if (contentImageData == null) {
      return responseController.respondNotFound();
    }
    
    return responseController.streamResponse(contentImageData.getData(), contentImageMeta.getContentType());
  }

  @Override
  public Response listContentImages(Long contentId, String type) throws Exception {
    Content content = contentController.findContentById(contentId);
    if (content == null) {
      return responseController.respondNotFound();
    }
    
    List<ContentImageMeta> contentImageMetas = contentController.listContentImageMetaByContentFilterByType(content, type);
    return responseController.respondOk(contentTranslator.translateContentImages(contentImageMetas));
  }

  @Override
  public Response listContents(Long parentId, String type, Long firstResult, Long maxResults) throws Exception {
    /*ContentType contentType = null;
    if (type != null) {
      contentType = EnumUtils.getEnum(ContentType.class, type);
      if (contentType == null) {
        return responseController.respondBadRequest("Invalid type filter");
      }
    }*/
    
    Integer from = firstResult == null ? null : firstResult.intValue();
    Integer to = maxResults == null ? null : maxResults.intValue();
    List<Content> contentEntities = contentController.listContents(from, to);
    List<List<ContentTitle>> contentTitleEntities = new ArrayList<>(contentEntities.size());
    for (Content contentEntity : contentEntities) {
      contentTitleEntities.add(contentController.listContentTitlesByContent(contentEntity));
    }

    return responseController.respondOk(contentTranslator.translateContents(contentEntities, contentTitleEntities));
  }
}
