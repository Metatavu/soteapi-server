package fi.metatavu.soteapi.wordpress.tasks.categories;

import fi.metatavu.soteapi.wordpress.tasks.AbstractPostUpdateModel;

public class CategoryUpdateTaskModel extends AbstractPostUpdateModel {

  private static final long serialVersionUID = 9183678443351585239L;

  public CategoryUpdateTaskModel() {
  }

  public CategoryUpdateTaskModel(String title, String content, String slug, String originId, String parentOriginId, Long categoryId, Long orderIndex) {
    super(title, content, slug, originId, parentOriginId, categoryId, orderIndex);
  }

}
