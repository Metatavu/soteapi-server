package fi.metatavu.soteapi.persistence.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * JPA entity representing queued task
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
@Entity
@Table (
  uniqueConstraints = @UniqueConstraint (name = "UN_TASKMODEL_QUEUE_TASK_ID", columnNames = { "queue_id", "uniqueId" })    
)
public class TaskModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Boolean priority;
  
  @ManyToOne 
  private TaskQueue queue;
  
  @Column(nullable = false)
  @NotNull
  @NotEmpty
  @Lob
  private byte[] data;

  @Column(nullable = false)
  private OffsetDateTime created;
  
  @Column(nullable = false)
  @NotNull
  @NotEmpty
  private String uniqueId;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Boolean getPriority() {
    return priority;
  }

  public void setPriority(Boolean priority) {
    this.priority = priority;
  }

  public TaskQueue getQueue() {
    return queue;
  }
  
  public void setQueue(TaskQueue queue) {
    this.queue = queue;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }
  
  public String getUniqueId() {
    return uniqueId;
  }
  
  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }
  
}
