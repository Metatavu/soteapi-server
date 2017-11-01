package fi.metatavu.soteapi.wordpress.tasks;

import java.io.Serializable;

public class PageUpdateTaskModel implements Serializable{
  
  private static final long serialVersionUID = -2309431277184013627L;

  private String title;
  private String content;
  private String slug;
  private String originId;
  
  public PageUpdateTaskModel() {
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
}
