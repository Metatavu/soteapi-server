package fi.metatavu.soteapi.rest.translate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.persistence.model.PageContent;
import fi.metatavu.soteapi.persistence.model.PageImageMeta;
import fi.metatavu.soteapi.persistence.model.PageTitle;
import fi.metatavu.soteapi.server.rest.model.LocalizedValue;
import fi.metatavu.soteapi.server.rest.model.Page;
import fi.metatavu.soteapi.server.rest.model.PageImage;

/**
 * Translator for page related translations
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class PageTranslator {
  
  /**
   * Translates database page entity to REST page 
   * 
   * @param pageEntity Database page entity to be translated
   * @param pageTitleEntities Database entities for titles related to the page
   * @return Page translated to the REST page
   */
  public Page translatePage(fi.metatavu.soteapi.persistence.model.Page pageEntity, List<PageTitle> pageTitleEntities) {
    if (pageEntity == null) {
      return null;
    }

    Page page = new Page();
    page.setId(pageEntity.getId());
    page.setSlug(pageEntity.getSlug());
    page.setTitle(translatePageTitles(pageTitleEntities));
    return page;
  }
  
  /**
   * Translates list of pages to REST entities
   * 
   * @param pageEntities List of database page enties
   * @param pageTitleEntities List of list of page title entities
   * @return List of translated page entities
   */
  public List<Page> translatePages(List<fi.metatavu.soteapi.persistence.model.Page> pageEntities, List<List<PageTitle>> pageTitleEntities) {
    List<Page> results = new ArrayList<>(pageEntities.size());
    Iterator<fi.metatavu.soteapi.persistence.model.Page> pageEntityIterator = pageEntities.iterator();
    Iterator<List<PageTitle>> pageTitleEntitiesIterator = pageTitleEntities.iterator();
    while (pageEntityIterator.hasNext() && pageTitleEntitiesIterator.hasNext()) {
      results.add(translatePage(pageEntityIterator.next(), pageTitleEntitiesIterator.next()));
    }
    
    return results;
  }
  
  /**
   * Translates database page content entities to REST entities
   * 
   * @param pageContentEntities Entities to be translates
   * @return translated entities
   */
  public List<LocalizedValue> translatePageContents(List<PageContent> pageContentEntities) {
    List<LocalizedValue> results = new ArrayList<>(pageContentEntities.size());
    for (PageContent pageContentEntity : pageContentEntities) {
      results.add(translatePageContent(pageContentEntity));
    }
    return results;
  }
  
  /**
   * Translates page image metadata
   * 
   * @param pageImageMetaEntity Page image metadata entity to be translated
   * @return REST entity for image metadata
   */
  public PageImage translatePageImage(PageImageMeta pageImageMetaEntity) {
    if (pageImageMetaEntity == null) {
      return null;
    }

    PageImage pageImage = new PageImage();
    pageImage.setId(pageImageMetaEntity.getId());
    pageImage.setName(pageImageMetaEntity.getName());
    pageImage.setContentType(pageImageMetaEntity.getContentType());
    pageImage.setSize(0l); //TODO: add size or calculate from data
    pageImage.setType(pageImageMetaEntity.getType());
    return pageImage;
  }
  
  /**
   * Translates list of page image metadata entities
   * 
   * @param pageImageMetaEntitys Entities to be translated
   * @return Translated entities
   */
  public List<PageImage> translatePageImages(List<PageImageMeta> pageImageMetaEntitys) {
    List<PageImage> results = new ArrayList<>(pageImageMetaEntitys.size());
    for (PageImageMeta pageImageMetaEntity : pageImageMetaEntitys) {
      results.add(translatePageImage(pageImageMetaEntity));
    }
    return results;
  }
  
  private LocalizedValue translatePageContent(PageContent pageContentEntity) {
    LocalizedValue localizedValue = new LocalizedValue();
    localizedValue.setLanguage(pageContentEntity.getLanguage());
    localizedValue.setValue(pageContentEntity.getValue());
    return localizedValue;
  }
  
  private LocalizedValue translatePageTitle(PageTitle pageTitleEntity) {
    LocalizedValue localizedValue = new LocalizedValue();
    localizedValue.setLanguage(pageTitleEntity.getLanguage());
    localizedValue.setValue(pageTitleEntity.getValue());
    return localizedValue;
  }
  
  private List<LocalizedValue> translatePageTitles(List<PageTitle> pageTitleEntities) {
    List<LocalizedValue> results = new ArrayList<>(pageTitleEntities.size());
    for (PageTitle pageTitleEntity : pageTitleEntities) {
      results.add(translatePageTitle(pageTitleEntity));
    }
    return results;
  }
  
}
