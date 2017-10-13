package fi.metatavu.soteapi.persistence.dao;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.persistence.model.Page;

/**
 * DAO class for Pages
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class PageDAO extends AbstractDAO<Page> {

  /**
   * Creates new page entity
   * 
   * @param originId Pages origin Id
   * @param slug Pages slug
   * @return created page
   */
  public Page create(String originId, String slug) {
    Page page = new Page();
    page.setOriginId(originId);
    page.setSlug(slug);
    return persist(page);
  }
  
}
