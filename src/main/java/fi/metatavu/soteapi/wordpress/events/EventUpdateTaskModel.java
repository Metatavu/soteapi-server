package fi.metatavu.soteapi.wordpress.events;

import java.io.Serializable;

public class EventUpdateTaskModel implements Serializable {

  private static final long serialVersionUID = -8595848061651103341L;
  
  private String title;
  
  private String description;

  private String originId;

  private String slug;

  private String category;

  private String startTime;

  private String endTime;

  private Boolean allDay;
  
  public EventUpdateTaskModel() {
  }

  public EventUpdateTaskModel(String originId, String title, String description, String slug, String category, String startTime, String endTime, Boolean allDay) {
    super();
    this.title = title;
    this.description = description;
    this.originId = originId;
    this.slug = slug;
    this.category = category;
    this.startTime = startTime;
    this.endTime = endTime;
    this.allDay = allDay;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getDescription() {
    return description;
  }

  public String getOriginId() {
    return originId;
  }

  public String getSlug() {
    return slug;
  }

  public String getCategory() {
    return category;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public Boolean getAllDay() {
    return allDay;
  }

}
