package fi.metatavu.soteapi.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class ContentImageData {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  @NotNull
  @Lob
  private byte[] data;
  
  @OneToOne
  private ContentImageMeta contentImageMeta;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public ContentImageMeta getContentImageMeta() {
    return contentImageMeta;
  }

  public void setContentImageMeta(ContentImageMeta contentImageMeta) {
    this.contentImageMeta = contentImageMeta;
  }

}
