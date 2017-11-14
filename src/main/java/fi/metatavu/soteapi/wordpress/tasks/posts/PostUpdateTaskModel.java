package fi.metatavu.soteapi.wordpress.tasks.posts;

import fi.metatavu.soteapi.wordpress.tasks.AbstractPostUpdateModel;

public class PostUpdateTaskModel extends AbstractPostUpdateModel {

  private static final long serialVersionUID = -8955929424049867006L;

  public PostUpdateTaskModel() {
    super();
  }

  public PostUpdateTaskModel(String title, String content, String slug, String originId, String parentOriginId, Long categoryId, String created, String modified, Long orderIndex) {
    super(title, content, slug, originId, parentOriginId, categoryId, created, modified, orderIndex);
  }
  
}
