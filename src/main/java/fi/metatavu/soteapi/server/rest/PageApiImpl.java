package fi.metatavu.soteapi.server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.soteapi.page.PageController;
import fi.metatavu.soteapi.persistence.model.Page;
import fi.metatavu.soteapi.persistence.model.PageContent;
import fi.metatavu.soteapi.persistence.model.PageImageData;
import fi.metatavu.soteapi.persistence.model.PageImageMeta;
import fi.metatavu.soteapi.persistence.model.PageTitle;
import fi.metatavu.soteapi.rest.translate.PageTranslator;

@RequestScoped
@Stateful
public class PageApiImpl implements PagesApi {

  @Inject
  private PageTranslator pageTranslator;
  
  @Inject
  private PageController pageController;
  
  @Inject
  private ResponseController responseController;
  
  @Override
  public Response findPage(Long pageId) throws Exception {
    Page page = pageController.findPageById(pageId);
    if (page == null) {
      return responseController.respondNotFound();
    }
    
    List<PageTitle> pageTitles = pageController.listPageTitlesByPage(page);
    return responseController.respondOk(pageTranslator.translatePage(page, pageTitles));
  }

  @Override
  public Response findPageContent(Long pageId) throws Exception {
    Page page = pageController.findPageById(pageId);
    if (page == null) {
      return responseController.respondNotFound();
    }
    
    List<PageContent> pageContents = pageController.listPageContentByPage(page);
    return responseController.respondOk(pageTranslator.translatePageContents(pageContents));
  }

  @Override
  public Response findPageImage(Long pageId, Long imageId) throws Exception {
    Page page = pageController.findPageById(pageId);
    if (page == null) {
      return responseController.respondNotFound();
    }
    
    PageImageMeta pageImageMeta = pageController.findPageImageMetaByIdAndPage(pageId, page);
    return responseController.respondOkOrNotFound(pageTranslator.translatePageImage(pageImageMeta));
  }

  @Override
  public Response getPageImageData(Long pageId, Long imageId, Integer size) throws Exception {
    //TODO: scale image if size is specified
    
    Page page = pageController.findPageById(pageId);
    if (page == null) {
      return responseController.respondNotFound();
    }
    
    PageImageMeta pageImageMeta = pageController.findPageImageMetaByIdAndPage(imageId, page);
    if (pageImageMeta == null) {
      return responseController.respondNotFound();
    }
    
    PageImageData pageImageData = pageController.findPageImageDatabyPageImageMeta(pageImageMeta);
    if (pageImageData == null) {
      return responseController.respondNotFound();
    }
    
    return responseController.streamResponse(pageImageData.getData(), pageImageMeta.getContentType());
  }

  @Override
  public Response listPageImages(Long pageId, String type) throws Exception {
    Page page = pageController.findPageById(pageId);
    if (page == null) {
      return responseController.respondNotFound();
    }
    
    List<PageImageMeta> pageImageMetas = pageController.listPageImageMetaByPageFilterByType(page, type);
    return responseController.respondOk(pageTranslator.translatePageImages(pageImageMetas));
  }

  @Override
  public Response listPages(Long parentId, String path, Long firstResult, Long maxResults) throws Exception {
    // TODO: filter by path and parentId
    Integer from = firstResult == null ? null : firstResult.intValue();
    Integer to = maxResults == null ? null : maxResults.intValue();
    List<Page> pageEntities = pageController.listPages(from, to);
    List<List<PageTitle>> pageTitleEntities = new ArrayList<>(pageEntities.size());
    for (Page pageEntity : pageEntities) {
      pageTitleEntities.add(pageController.listPageTitlesByPage(pageEntity));
    }

    return responseController.respondOk(pageTranslator.translatePages(pageEntities, pageTitleEntities));
  }
}
