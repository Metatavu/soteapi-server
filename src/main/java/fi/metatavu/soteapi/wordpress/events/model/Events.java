package fi.metatavu.soteapi.wordpress.events.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties (ignoreUnknown = true)
public class Events {

  private Integer total;
  
  @JsonProperty ("total_pages")
  private Integer totalPages;
  
  @SuppressWarnings ("squid:S1700")
  private List<Event> events;
  
  public List<Event> getEvents() {
    return events;
  }
  
  public void setEvents(List<Event> events) {
    this.events = events;
  }
  
  public Integer getTotal() {
    return total;
  }
  
  public void setTotal(Integer total) {
    this.total = total;
  }
  
  public Integer getTotalPages() {
    return totalPages;
  }
  
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }
  
}
