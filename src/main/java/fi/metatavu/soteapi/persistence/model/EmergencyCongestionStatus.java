package fi.metatavu.soteapi.persistence.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class EmergencyCongestionStatus {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotNull
  @Column(nullable = true)
  private Integer value;
  
  @NotNull
  @Column(nullable = true)
  private OffsetDateTime created;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Integer getValue() {
    return value;
  }
  
  public void setValue(Integer value) {
    this.value = value;
  }
  
  public OffsetDateTime getCreated() {
    return created;
  }
  
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }
  
  
}
