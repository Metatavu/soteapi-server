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
   * Creates new page
   * 
   * @param originId origin id
   * @param slug slug
   * @return created page
   */
  public Page createPage(String originId, String slug) {
    return pageDAO.create(originId, slug);
  }
  
  /**
   * Creates new page title
   * 
   * @param language title language
   * @param value title value
   * @param page page that title is found from
   * @return create page title
   */
  public PageTitle createPageTitle(String language, String value, Page page) {
    return pageTitleDAO.create(language, value, page);
  }
  
  /**
   * Creates new page content
   * 
   * @param language content language
   * @param value content value
   * @param page page that content is found from
   * @return created page content
   */
  public PageContent createPageContent(String language, String value, Page page ) {
    return pageContentDAO.create(language, value, page);
  }

  /**
   * Update page
   *
   * @param originId originId
   * @param slug slug
   * @return updated page
   */
  public Page updatePage(Page page, String originId, String slug) {
    pageDAO.updateOriginId(page, originId);
    pageDAO.updateSlug(page, slug);
    return page;
  }

  /**
   * Update pageTitle
   *
   * @param language language
   * @param value value
   * @param page page
   * @return updated pageTitle
   */
  public PageTitle updatePageTitle(PageTitle pageTitle, String language, String value, Page page) {
    pageTitleDAO.updateLanguage(pageTitle, language);
    pageTitleDAO.updateValue(pageTitle, value);
    pageTitleDAO.updatePage(pageTitle, page);
    return pageTitle;
  }
  
  /**
   * Update pageContent
   *
   * @param language language
   * @param value value
   * @param page page
   * @return updated pageContent
   */
  public PageContent updatePageContent(PageContent pageContent, String language, String value, Page page) {
    pageContentDAO.updateLanguage(pageContent, language);
    pageContentDAO.updateValue(pageContent, value);
    pageContentDAO.updatePage(pageContent, page);
    return pageContent;
  }
  
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
   * Finds page with origin id
   * 
   * @param originId Pages origin id
   * @return Page
   */
  public List<Page> listPagesByOriginId(String originId) {
    return pageDAO.listByOriginId(originId);
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
