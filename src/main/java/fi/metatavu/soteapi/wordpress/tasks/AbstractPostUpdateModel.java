package fi.metatavu.soteapi.wordpress.tasks;

import java.io.Serializable;

public abstract class AbstractPostUpdateModel implements Serializable{
  
  private static final long serialVersionUID = -5258379017640768560L;

  private String title;
  private String content;
  private String slug;
  private String originId;
  private String parentOriginId;
  
  public AbstractPostUpdateModel() {
  }
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getOriginId() {
    return originId;
  }

  public void setOriginId(String originId) {
    this.originId = originId;
  }
  
  public String getParentOriginId() {
    return parentOriginId;
  }
  
  public void setParentOriginId(String parentOriginId) {
    this.parentOriginId = parentOriginId;
  }
}
