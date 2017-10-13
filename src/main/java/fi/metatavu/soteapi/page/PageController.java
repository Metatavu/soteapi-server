package fi.metatavu.soteapi.page;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.persistence.dao.PageContentDAO;
import fi.metatavu.soteapi.persistence.dao.PageDAO;
import fi.metatavu.soteapi.persistence.dao.PageImageDataDAO;
import fi.metatavu.soteapi.persistence.dao.PageImageMetaDAO;
import fi.metatavu.soteapi.persistence.dao.PageTitleDAO;
import fi.metatavu.soteapi.persistence.model.Page;
import fi.metatavu.soteapi.persistence.model.PageContent;
import fi.metatavu.soteapi.persistence.model.PageImageData;
import fi.metatavu.soteapi.persistence.model.PageImageMeta;
import fi.metatavu.soteapi.persistence.model.PageTitle;

/**
 * Controller for operations related to pages
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class PageController {

  @Inject
  private PageDAO pageDAO;
  
  @Inject
  private PageTitleDAO pageTitleDAO;
  
  @Inject
  private PageContentDAO pageContentDAO;
  
  @Inject
  private PageImageMetaDAO pageImageMetaDAO;
  
  @Inject
  private PageImageDataDAO pageImageDataDAO;
  
  /**
   * Finds page with pageId
   * 
   * @param pageId Pages id
   * @return Page
   */
  public Page findPageById(Long pageId) {
    return pageDAO.findById(pageId);
  }
  
  /**
   * Finds page with pageId
   * 
   * @param pageId Pages id
   * @return Page
   */
  public List<Page> listPages(Integer firstResult, Integer maxResults) {
    return pageDAO.listAll(firstResult, maxResults);
  }
  
  /**
   * Lists page titles by page
   * 
   * @param page which titles are to be listed
   * @return List of page titles in different languages
   */
  public List<PageTitle> listPageTitlesByPage(Page page) {
    return pageTitleDAO.listByPage(page);
  }
  
  /**
   * Lists page content by page in all available languages
   * 
   * @param page which contents are to be listed
   * @return List of page contents in all available languages
   */
  public List<PageContent> listPageContentByPage(Page page) {
    return pageContentDAO.listByPage(page);
  }
  
  /**
   * Finds page image metadata by imageId and page
   *  
   * @param imageId Images id
   * @param page Page image is related to
   * @return Page image metadata
   */
  public PageImageMeta findPageImageMetaByIdAndPage(Long imageId, Page page) {
    return pageImageMetaDAO.findByIdAndPage(imageId, page);
  }
  
  /**
   * Finds page images data with image metadata
   * 
   * @param pageImageMeta Related image metadata
   * @return page image data
   */
  public PageImageData findPageImageDatabyPageImageMeta(PageImageMeta pageImageMeta) {
    return pageImageDataDAO.findByPageImageMeta(pageImageMeta);
  }
  
  /**
   * List page image metadatas by page or by page and type
   * 
   * @param page Page to list image metadatas from
   * @param type Filter with type (can be null)
   * @return List of page image metadatas
   */
  public List<PageImageMeta> listPageImageMetaByPageFilterByType(Page page, String type) {
    if (type == null) {
      return pageImageMetaDAO.listByPage(page);
    }
    
    return pageImageMetaDAO.listByPageAndType(page, type);
  }
  
}
