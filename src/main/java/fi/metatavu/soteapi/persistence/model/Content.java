package fi.metatavu.soteapi.persistence.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Content {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  @NotNull
  private Long orderIndex;
  
  @Column(nullable = false)
  @NotNull
  @NotEmpty
  private String originId;

  @Column(nullable = false)
  @NotNull
  @NotEmpty
  private String origin;
  
  @Column(nullable = false)
  @NotNull
  @NotEmpty
  private String slug;
  
  @Column(nullable = true)
  private String category;
  
  @Enumerated (EnumType.STRING)
  @Column(nullable = false)
  private ContentType contentType;

  @ManyToOne(optional = true)
  private Content parent;

  @NotNull
  @Column(nullable = true)
  private Boolean archived;

  @NotNull
  @Column(nullable = true)
  private OffsetDateTime created;

  @NotNull
  @Column(nullable = true)
  private OffsetDateTime modified;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public String getOrigin() {
    return origin;
  }
  
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getOriginId() {
    return originId;
  }

  public void setOriginId(String originId) {
    this.originId = originId;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }
  
  public Content getParent() {
    return parent;
  }
  
  public void setParent(Content parent) {
    this.parent = parent;
  }
  
  public ContentType getContentType() {
    return contentType;
  }
  
  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }
  
  public String getCategory() {
    return category;
  }
  
  public void setCategory(String category) {
    this.category = category;
  }
  
  public Long getOrderIndex() {
    return orderIndex;
  }
  
  public void setOrderIndex(Long orderIndex) {
    this.orderIndex = orderIndex;
  }
  
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }
  
  public Boolean getArchived() {
    return archived;
  }
  
  public OffsetDateTime getCreated() {
    return created;
  }
  
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }
  
  public OffsetDateTime getModified() {
    return modified;
  }
  
  public void setModified(OffsetDateTime modified) {
    this.modified = modified;
  }
  
}
