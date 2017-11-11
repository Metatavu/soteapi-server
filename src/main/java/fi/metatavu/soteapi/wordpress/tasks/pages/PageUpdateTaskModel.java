package fi.metatavu.soteapi.wordpress.tasks.pages;

import fi.metatavu.soteapi.wordpress.tasks.AbstractPostUpdateModel;

public class PageUpdateTaskModel extends AbstractPostUpdateModel {

  private static final long serialVersionUID = 8224934651920150851L;

  public PageUpdateTaskModel() {
    super();
  }

  public PageUpdateTaskModel(String title, String content, String slug, String originId, String parentOriginId, Long categoryId, Long orderIndex) {
    super(title, content, slug, originId, parentOriginId, categoryId, orderIndex);
  }
  
}
