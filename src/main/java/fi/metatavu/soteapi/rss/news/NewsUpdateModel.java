package fi.metatavu.soteapi.rss.news;

import java.io.Serializable;

public class NewsUpdateModel implements Serializable{

  private static final long serialVersionUID = -8317527358332397258L;
  
  private String title;
  private String content;
  private String slug;
  private String originId;
  private String categorySlug;
  private String created;
  private String modified;
  private Long orderIndex;
  
  public NewsUpdateModel() {
  }
  
  public NewsUpdateModel(String title, String content, String slug, String originId, String categorySlug,
      String created, String modified, Long orderIndex) {
    super();
    this.title = title;
    this.content = content;
    this.slug = slug;
    this.originId = originId;
    this.categorySlug = categorySlug;
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

  public String getCategorySlug() {
    return categorySlug;
  }
  
  public String getCreated() {
    return created;
  }
  
  public String getModified() {
    return modified;
  }
  
  public Long getOrderIndex() {
    return orderIndex;
  }
  
  
}
