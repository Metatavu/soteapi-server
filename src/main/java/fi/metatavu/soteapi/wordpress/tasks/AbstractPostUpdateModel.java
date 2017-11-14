package fi.metatavu.soteapi.wordpress.tasks;

import java.io.Serializable;

public abstract class AbstractPostUpdateModel implements Serializable {

  private static final long serialVersionUID = -94277011788449153L;

  private String title;
  private String content;
  private String slug;
  private String originId;
  private String parentOriginId;
  private Long categoryId;
  private String created;
  private String modified;
  private Long orderIndex;

  public AbstractPostUpdateModel() {
  }

  @SuppressWarnings ("squid:S00107")
  public AbstractPostUpdateModel(String title, String content, String slug, String originId, String parentOriginId,
      Long categoryId, String created, String modified, Long orderIndex) {
    super();
    this.title = title;
    this.content = content;
    this.slug = slug;
    this.originId = originId;
    this.parentOriginId = parentOriginId;
    this.categoryId = categoryId;
    this.created = created;
    this.modified = modified;
    this.orderIndex = orderIndex;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getSlug() {
    return slug;
  }

  public String getOriginId() {
    return originId;
  }

  public String getParentOriginId() {
    return parentOriginId;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public Long getOrderIndex() {
    return orderIndex;
  }
  
  public String getCreated() {
    return created;
  }
  
  public String getModified() {
    return modified;
  }

}
