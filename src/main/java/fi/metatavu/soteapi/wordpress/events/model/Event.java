/*
 * The Events Calendar REST API
 * The Events Calendar REST API allows accessing upcoming events information easily and conveniently.
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package fi.metatavu.soteapi.wordpress.events.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.metatavu.soteapi.wordpress.events.model.deserialization.EventImageDeserializer;
import io.swagger.annotations.ApiModelProperty;

/**
 * Event
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-11-11T23:28:38.367+02:00")
public class Event {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("global_id")
  private String globalId = null;

  @JsonProperty("global_id_lineage")
  private List<String> globalIdLineage = null;

  @JsonProperty("author")
  private Integer author = null;

  @JsonProperty("date")
  private String date = null;

  @JsonProperty("date_utc")
  private String dateUtc = null;

  @JsonProperty("modified")
  private String modified = null;

  @JsonProperty("modified_utc")
  private String modifiedUtc = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("rest_url")
  private String restUrl = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("excerpt")
  private String excerpt = null;

  @JsonProperty("image")
  @JsonDeserialize (using = EventImageDeserializer.class)
  private Image image = null;

  @JsonProperty("all_day")
  private Boolean allDay = null;

  @JsonProperty("start_date")
  private String startDate = null;

  @JsonProperty("start_date_details")
  private DateDetails startDateDetails = null;

  @JsonProperty("end_date")
  private String endDate = null;

  @JsonProperty("end_date_details")
  private DateDetails endDateDetails = null;

  @JsonProperty("utc_start_date")
  private String utcStartDate = null;

  @JsonProperty("utc_start_date_details")
  private DateDetails utcStartDateDetails = null;

  @JsonProperty("utc_end_date")
  private String utcEndDate = null;

  @JsonProperty("utc_end_date_details")
  private DateDetails utcEndDateDetails = null;

  @JsonProperty("timezone")
  private String timezone = null;

  @JsonProperty("timezone_abbr")
  private String timezoneAbbr = null;

  @JsonProperty("cost")
  private String cost = null;

  @JsonProperty("cost_details")
  private CostDetails costDetails = null;

  @JsonProperty("website")
  private String website = null;

  @JsonProperty("show_map")
  private Boolean showMap = null;

  @JsonProperty("show_map_link")
  private Boolean showMapLink = null;

  @JsonProperty("hide_from_listings")
  private Boolean hideFromListings = null;

  @JsonProperty("sticky")
  private Boolean sticky = null;

  @JsonProperty("featured")
  private Boolean featured = null;

  @JsonProperty("categories")
  private List<Term> categories = null;

  @JsonProperty("tags")
  private List<Term> tags = null;

  @JsonProperty("venue")
  private List<Venue> venue = null;

  @JsonProperty("organizer")
  private List<Organizer> organizer = null;

  public Event id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * The event WordPress post ID
   * @return id
  **/
  @ApiModelProperty(value = "The event WordPress post ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Event globalId(String globalId) {
    this.globalId = globalId;
    return this;
  }

   /**
   * The event ID used to globally identify in Event Aggregator
   * @return globalId
  **/
  @ApiModelProperty(value = "The event ID used to globally identify in Event Aggregator")
  public String getGlobalId() {
    return globalId;
  }

  public void setGlobalId(String globalId) {
    this.globalId = globalId;
  }

  public Event globalIdLineage(List<String> globalIdLineage) {
    this.globalIdLineage = globalIdLineage;
    return this;
  }

  public Event addGlobalIdLineageItem(String globalIdLineageItem) {
    if (this.globalIdLineage == null) {
      this.globalIdLineage = new ArrayList<String>();
    }
    this.globalIdLineage.add(globalIdLineageItem);
    return this;
  }

   /**
   * An Array containing the lineage of where this event comes from, this should not change after the event is created.
   * @return globalIdLineage
  **/
  @ApiModelProperty(value = "An Array containing the lineage of where this event comes from, this should not change after the event is created.")
  public List<String> getGlobalIdLineage() {
    return globalIdLineage;
  }

  public void setGlobalIdLineage(List<String> globalIdLineage) {
    this.globalIdLineage = globalIdLineage;
  }

  public Event author(Integer author) {
    this.author = author;
    return this;
  }

   /**
   * The event author WordPress post ID
   * @return author
  **/
  @ApiModelProperty(value = "The event author WordPress post ID")
  public Integer getAuthor() {
    return author;
  }

  public void setAuthor(Integer author) {
    this.author = author;
  }

  public Event date(String date) {
    this.date = date;
    return this;
  }

   /**
   * The event creation date in the site timezone
   * @return date
  **/
  @ApiModelProperty(value = "The event creation date in the site timezone")
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Event dateUtc(String dateUtc) {
    this.dateUtc = dateUtc;
    return this;
  }

   /**
   * The event creation date in UTC time
   * @return dateUtc
  **/
  @ApiModelProperty(value = "The event creation date in UTC time")
  public String getDateUtc() {
    return dateUtc;
  }

  public void setDateUtc(String dateUtc) {
    this.dateUtc = dateUtc;
  }

  public Event modified(String modified) {
    this.modified = modified;
    return this;
  }

   /**
   * The event last modification date in the site timezone
   * @return modified
  **/
  @ApiModelProperty(value = "The event last modification date in the site timezone")
  public String getModified() {
    return modified;
  }

  public void setModified(String modified) {
    this.modified = modified;
  }

  public Event modifiedUtc(String modifiedUtc) {
    this.modifiedUtc = modifiedUtc;
    return this;
  }

   /**
   * The event last modification date in UTC time
   * @return modifiedUtc
  **/
  @ApiModelProperty(value = "The event last modification date in UTC time")
  public String getModifiedUtc() {
    return modifiedUtc;
  }

  public void setModifiedUtc(String modifiedUtc) {
    this.modifiedUtc = modifiedUtc;
  }

  public Event status(String status) {
    this.status = status;
    return this;
  }

   /**
   * The event status
   * @return status
  **/
  @ApiModelProperty(value = "The event status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Event url(String url) {
    this.url = url;
    return this;
  }

   /**
   * The URL to the event page
   * @return url
  **/
  @ApiModelProperty(value = "The URL to the event page")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Event restUrl(String restUrl) {
    this.restUrl = restUrl;
    return this;
  }

   /**
   * The TEC REST API link to fetch this event
   * @return restUrl
  **/
  @ApiModelProperty(value = "The TEC REST API link to fetch this event")
  public String getRestUrl() {
    return restUrl;
  }

  public void setRestUrl(String restUrl) {
    this.restUrl = restUrl;
  }

  public Event title(String title) {
    this.title = title;
    return this;
  }

   /**
   * The event name
   * @return title
  **/
  @ApiModelProperty(value = "The event name")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Event description(String description) {
    this.description = description;
    return this;
  }

   /**
   * The event long description
   * @return description
  **/
  @ApiModelProperty(value = "The event long description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Event excerpt(String excerpt) {
    this.excerpt = excerpt;
    return this;
  }

   /**
   * The event short description
   * @return excerpt
  **/
  @ApiModelProperty(value = "The event short description")
  public String getExcerpt() {
    return excerpt;
  }

  public void setExcerpt(String excerpt) {
    this.excerpt = excerpt;
  }

  public Event image(Image image) {
    this.image = image;
    return this;
  }

   /**
   * The event featured image details if set
   * @return image
  **/
  @ApiModelProperty(value = "The event featured image details if set")
  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public Event allDay(Boolean allDay) {
    this.allDay = allDay;
    return this;
  }

   /**
   * Whether or not this event is an all day Event
   * @return allDay
  **/
  @ApiModelProperty(value = "Whether or not this event is an all day Event")
  public Boolean isAllDay() {
    return allDay;
  }

  public void setAllDay(Boolean allDay) {
    this.allDay = allDay;
  }

  public Event startDate(String startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * The event start date in the event or site timezone
   * @return startDate
  **/
  @ApiModelProperty(value = "The event start date in the event or site timezone")
  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public Event startDateDetails(DateDetails startDateDetails) {
    this.startDateDetails = startDateDetails;
    return this;
  }

   /**
   * An array of each component of the event start date
   * @return startDateDetails
  **/
  @ApiModelProperty(value = "An array of each component of the event start date")
  public DateDetails getStartDateDetails() {
    return startDateDetails;
  }

  public void setStartDateDetails(DateDetails startDateDetails) {
    this.startDateDetails = startDateDetails;
  }

  public Event endDate(String endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * The event end date in the event or site timezone
   * @return endDate
  **/
  @ApiModelProperty(value = "The event end date in the event or site timezone")
  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Event endDateDetails(DateDetails endDateDetails) {
    this.endDateDetails = endDateDetails;
    return this;
  }

   /**
   * An array of each component of the event end date
   * @return endDateDetails
  **/
  @ApiModelProperty(value = "An array of each component of the event end date")
  public DateDetails getEndDateDetails() {
    return endDateDetails;
  }

  public void setEndDateDetails(DateDetails endDateDetails) {
    this.endDateDetails = endDateDetails;
  }

  public Event utcStartDate(String utcStartDate) {
    this.utcStartDate = utcStartDate;
    return this;
  }

   /**
   * The event start date in UTC time
   * @return utcStartDate
  **/
  @ApiModelProperty(value = "The event start date in UTC time")
  public String getUtcStartDate() {
    return utcStartDate;
  }

  public void setUtcStartDate(String utcStartDate) {
    this.utcStartDate = utcStartDate;
  }

  public Event utcStartDateDetails(DateDetails utcStartDateDetails) {
    this.utcStartDateDetails = utcStartDateDetails;
    return this;
  }

   /**
   * An array of each component of the event start date in UTC time
   * @return utcStartDateDetails
  **/
  @ApiModelProperty(value = "An array of each component of the event start date in UTC time")
  public DateDetails getUtcStartDateDetails() {
    return utcStartDateDetails;
  }

  public void setUtcStartDateDetails(DateDetails utcStartDateDetails) {
    this.utcStartDateDetails = utcStartDateDetails;
  }

  public Event utcEndDate(String utcEndDate) {
    this.utcEndDate = utcEndDate;
    return this;
  }

   /**
   * The event end date in UTC time
   * @return utcEndDate
  **/
  @ApiModelProperty(value = "The event end date in UTC time")
  public String getUtcEndDate() {
    return utcEndDate;
  }

  public void setUtcEndDate(String utcEndDate) {
    this.utcEndDate = utcEndDate;
  }

  public Event utcEndDateDetails(DateDetails utcEndDateDetails) {
    this.utcEndDateDetails = utcEndDateDetails;
    return this;
  }

   /**
   * An array of each component of the event end date in UTC time
   * @return utcEndDateDetails
  **/
  @ApiModelProperty(value = "An array of each component of the event end date in UTC time")
  public DateDetails getUtcEndDateDetails() {
    return utcEndDateDetails;
  }

  public void setUtcEndDateDetails(DateDetails utcEndDateDetails) {
    this.utcEndDateDetails = utcEndDateDetails;
  }

  public Event timezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

   /**
   * The event timezone string
   * @return timezone
  **/
  @ApiModelProperty(value = "The event timezone string")
  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public Event timezoneAbbr(String timezoneAbbr) {
    this.timezoneAbbr = timezoneAbbr;
    return this;
  }

   /**
   * The abbreviated event timezone string
   * @return timezoneAbbr
  **/
  @ApiModelProperty(value = "The abbreviated event timezone string")
  public String getTimezoneAbbr() {
    return timezoneAbbr;
  }

  public void setTimezoneAbbr(String timezoneAbbr) {
    this.timezoneAbbr = timezoneAbbr;
  }

  public Event cost(String cost) {
    this.cost = cost;
    return this;
  }

   /**
   * The event cost including the currency symbol
   * @return cost
  **/
  @ApiModelProperty(value = "The event cost including the currency symbol")
  public String getCost() {
    return cost;
  }

  public void setCost(String cost) {
    this.cost = cost;
  }

  public Event costDetails(CostDetails costDetails) {
    this.costDetails = costDetails;
    return this;
  }

   /**
   * The event cost details
   * @return costDetails
  **/
  @ApiModelProperty(value = "The event cost details")
  public CostDetails getCostDetails() {
    return costDetails;
  }

  public void setCostDetails(CostDetails costDetails) {
    this.costDetails = costDetails;
  }

  public Event website(String website) {
    this.website = website;
    return this;
  }

   /**
   * The event website URL
   * @return website
  **/
  @ApiModelProperty(value = "The event website URL")
  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public Event showMap(Boolean showMap) {
    this.showMap = showMap;
    return this;
  }

   /**
   * Whether the map should be shown for the event or not
   * @return showMap
  **/
  @ApiModelProperty(value = "Whether the map should be shown for the event or not")
  public Boolean isShowMap() {
    return showMap;
  }

  public void setShowMap(Boolean showMap) {
    this.showMap = showMap;
  }

  public Event showMapLink(Boolean showMapLink) {
    this.showMapLink = showMapLink;
    return this;
  }

   /**
   * Whether the map link should be shown for the event or not
   * @return showMapLink
  **/
  @ApiModelProperty(value = "Whether the map link should be shown for the event or not")
  public Boolean isShowMapLink() {
    return showMapLink;
  }

  public void setShowMapLink(Boolean showMapLink) {
    this.showMapLink = showMapLink;
  }

  public Event hideFromListings(Boolean hideFromListings) {
    this.hideFromListings = hideFromListings;
    return this;
  }

   /**
   * Whether an event should be hidden from the calendar view or not
   * @return hideFromListings
  **/
  @ApiModelProperty(value = "Whether an event should be hidden from the calendar view or not")
  public Boolean isHideFromListings() {
    return hideFromListings;
  }

  public void setHideFromListings(Boolean hideFromListings) {
    this.hideFromListings = hideFromListings;
  }

  public Event sticky(Boolean sticky) {
    this.sticky = sticky;
    return this;
  }

   /**
   * Whether an event is sticky in the calendar view or not
   * @return sticky
  **/
  @ApiModelProperty(value = "Whether an event is sticky in the calendar view or not")
  public Boolean isSticky() {
    return sticky;
  }

  public void setSticky(Boolean sticky) {
    this.sticky = sticky;
  }

  public Event featured(Boolean featured) {
    this.featured = featured;
    return this;
  }

   /**
   * Whether the event is featured in the calendar or not
   * @return featured
  **/
  @ApiModelProperty(value = "Whether the event is featured in the calendar or not")
  public Boolean isFeatured() {
    return featured;
  }

  public void setFeatured(Boolean featured) {
    this.featured = featured;
  }

  public Event categories(List<Term> categories) {
    this.categories = categories;
    return this;
  }

   /**
   * The event categories
   * @return categories
  **/
  @ApiModelProperty(value = "The event categories")
  public List<Term> getCategories() {
    return categories;
  }

  public void setCategories(List<Term> categories) {
    this.categories = categories;
  }

  public Event tags(List<Term> tags) {
    this.tags = tags;
    return this;
  }

   /**
   * The event tags
   * @return tags
  **/
  @ApiModelProperty(value = "The event tags")
  public List<Term> getTags() {
    return tags;
  }

  public void setTags(List<Term> tags) {
    this.tags = tags;
  }

  public Event venue(List<Venue> venue) {
    this.venue = venue;
    return this;
  }

   /**
   * The event venue
   * @return venue
  **/
  @ApiModelProperty(value = "The event venue")
  public List<Venue> getVenue() {
    return venue;
  }

  public void setVenue(List<Venue> venue) {
    this.venue = venue;
  }

  public Event organizer(List<Organizer> organizer) {
    this.organizer = organizer;
    return this;
  }

   /**
   * The event organizers
   * @return organizer
  **/
  @ApiModelProperty(value = "The event organizers")
  public List<Organizer> getOrganizer() {
    return organizer;
  }

  public void setOrganizer(List<Organizer> organizer) {
    this.organizer = organizer;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(this.id, event.id) &&
        Objects.equals(this.globalId, event.globalId) &&
        Objects.equals(this.globalIdLineage, event.globalIdLineage) &&
        Objects.equals(this.author, event.author) &&
        Objects.equals(this.date, event.date) &&
        Objects.equals(this.dateUtc, event.dateUtc) &&
        Objects.equals(this.modified, event.modified) &&
        Objects.equals(this.modifiedUtc, event.modifiedUtc) &&
        Objects.equals(this.status, event.status) &&
        Objects.equals(this.url, event.url) &&
        Objects.equals(this.restUrl, event.restUrl) &&
        Objects.equals(this.title, event.title) &&
        Objects.equals(this.description, event.description) &&
        Objects.equals(this.excerpt, event.excerpt) &&
        Objects.equals(this.image, event.image) &&
        Objects.equals(this.allDay, event.allDay) &&
        Objects.equals(this.startDate, event.startDate) &&
        Objects.equals(this.startDateDetails, event.startDateDetails) &&
        Objects.equals(this.endDate, event.endDate) &&
        Objects.equals(this.endDateDetails, event.endDateDetails) &&
        Objects.equals(this.utcStartDate, event.utcStartDate) &&
        Objects.equals(this.utcStartDateDetails, event.utcStartDateDetails) &&
        Objects.equals(this.utcEndDate, event.utcEndDate) &&
        Objects.equals(this.utcEndDateDetails, event.utcEndDateDetails) &&
        Objects.equals(this.timezone, event.timezone) &&
        Objects.equals(this.timezoneAbbr, event.timezoneAbbr) &&
        Objects.equals(this.cost, event.cost) &&
        Objects.equals(this.costDetails, event.costDetails) &&
        Objects.equals(this.website, event.website) &&
        Objects.equals(this.showMap, event.showMap) &&
        Objects.equals(this.showMapLink, event.showMapLink) &&
        Objects.equals(this.hideFromListings, event.hideFromListings) &&
        Objects.equals(this.sticky, event.sticky) &&
        Objects.equals(this.featured, event.featured) &&
        Objects.equals(this.categories, event.categories) &&
        Objects.equals(this.tags, event.tags) &&
        Objects.equals(this.venue, event.venue) &&
        Objects.equals(this.organizer, event.organizer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, globalId, globalIdLineage, author, date, dateUtc, modified, modifiedUtc, status, url, restUrl, title, description, excerpt, image, allDay, startDate, startDateDetails, endDate, endDateDetails, utcStartDate, utcStartDateDetails, utcEndDate, utcEndDateDetails, timezone, timezoneAbbr, cost, costDetails, website, showMap, showMapLink, hideFromListings, sticky, featured, categories, tags, venue, organizer);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Event {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    globalId: ").append(toIndentedString(globalId)).append("\n");
    sb.append("    globalIdLineage: ").append(toIndentedString(globalIdLineage)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    dateUtc: ").append(toIndentedString(dateUtc)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    modifiedUtc: ").append(toIndentedString(modifiedUtc)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    restUrl: ").append(toIndentedString(restUrl)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    excerpt: ").append(toIndentedString(excerpt)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    allDay: ").append(toIndentedString(allDay)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    startDateDetails: ").append(toIndentedString(startDateDetails)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    endDateDetails: ").append(toIndentedString(endDateDetails)).append("\n");
    sb.append("    utcStartDate: ").append(toIndentedString(utcStartDate)).append("\n");
    sb.append("    utcStartDateDetails: ").append(toIndentedString(utcStartDateDetails)).append("\n");
    sb.append("    utcEndDate: ").append(toIndentedString(utcEndDate)).append("\n");
    sb.append("    utcEndDateDetails: ").append(toIndentedString(utcEndDateDetails)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    timezoneAbbr: ").append(toIndentedString(timezoneAbbr)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    costDetails: ").append(toIndentedString(costDetails)).append("\n");
    sb.append("    website: ").append(toIndentedString(website)).append("\n");
    sb.append("    showMap: ").append(toIndentedString(showMap)).append("\n");
    sb.append("    showMapLink: ").append(toIndentedString(showMapLink)).append("\n");
    sb.append("    hideFromListings: ").append(toIndentedString(hideFromListings)).append("\n");
    sb.append("    sticky: ").append(toIndentedString(sticky)).append("\n");
    sb.append("    featured: ").append(toIndentedString(featured)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    venue: ").append(toIndentedString(venue)).append("\n");
    sb.append("    organizer: ").append(toIndentedString(organizer)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

