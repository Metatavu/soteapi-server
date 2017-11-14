package fi.metatavu.soteapi.server.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

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
    
    if (content.getArchived()) {
      return responseController.responseGone();
    }
    
    List<ContentTitle> contentTitles = contentController.listContentTitlesByContent(content);
    List<ContentData> contentDatas = contentController.listContentDataByContent(content);

    return responseController.respondOk(contentTranslator.translateContent(content, contentTitles, contentDatas));
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
  public Response listContents(String parentId, List<String> types, String categorySlug, Long firstResult, Long maxResults) throws Exception {
    List<ContentType> contentTypes = null;
    if (types != null && !types.isEmpty()) {
      contentTypes = new ArrayList<>();
      
      for (String type : processListParam(types)) {
        ContentType contentType = EnumUtils.getEnum(ContentType.class, type);
        if (contentType == null) {
          return responseController.respondBadRequest("Invalid type filter");
        }
        contentTypes.add(contentType);
      }
    }
    
    Content parent = null;
    if (parentId != null && !"ROOT".equals(parentId)) {
      parent = contentController.findContentById(Long.parseLong(parentId));
      if (parent == null) {
        return responseController.respondBadRequest("Invalid parent id");
      }
    }
    
    Integer from = firstResult == null ? null : firstResult.intValue();
    Integer to = maxResults == null ? null : maxResults.intValue();
    List<Content> contentEntities = "ROOT".equals(parentId) ? contentController.listRootContents(contentTypes, from, to) : contentController.listContents(parent, contentTypes, from, to);
    
    if (categorySlug != null) {
      contentEntities = contentController.filterByCategorySlug(contentEntities, categorySlug);
    }
    
    List<List<ContentTitle>> contentTitleEntities = new ArrayList<>(contentEntities.size());
    List<List<ContentData>> contentDataEntities = new ArrayList<>(contentEntities.size());
    for (Content contentEntity : contentEntities) {
      contentTitleEntities.add(contentController.listContentTitlesByContent(contentEntity));
      contentDataEntities.add(contentController.listContentDataByContent(contentEntity));
    }

    return responseController.respondOk(contentTranslator.translateContents(contentEntities, contentTitleEntities, contentDataEntities));
  }

  private List<String> processListParam(List<String> types) {
    if (types == null || types.isEmpty()) {
      return Collections.emptyList();
    }
    
    if (types.size() == 1) {
      return Arrays.asList(StringUtils.split(types.get(0), ",")); 
    }
    
    return types;
  }
}
