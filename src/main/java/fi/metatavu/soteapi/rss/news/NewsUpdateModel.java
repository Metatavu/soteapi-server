package fi.metatavu.soteapi.rss.news;

import java.io.Serializable;

public class NewsUpdateModel implements Serializable{

  private static final long serialVersionUID = -8317527358332397258L;
  
  private String title;
  private String content;
  private String slug;
  private String originId;
  private String categorySlug;
  
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
  
  public String getCategorySlug() {
    return categorySlug;
  }
  
  public void setCategorySlug(String categorySlug) {
    this.categorySlug = categorySlug;
  }
  
}
