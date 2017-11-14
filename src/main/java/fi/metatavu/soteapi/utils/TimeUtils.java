package fi.metatavu.soteapi.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtils {
  
  private TimeUtils() {
  }

  /**
   * Parses offset date time from iso string
   * 
   * @param isoString iso string
   * @return parsed offset date time
   */
  public static OffsetDateTime parseOffsetDateTime(String isoString) {
    return OffsetDateTime.parse(isoString);
  }
  
  /**
   * Parses offset date time from local date string. Time zone is assumed to be UTC
   * 
   * @param isoString iso string
   * @return parsed offset date time
   */
  public static OffsetDateTime parseOffsetDateTimeLocal(String isoString) {
    return LocalDateTime.parse(isoString).atOffset(ZoneOffset.UTC);
  }
  
  /**
   * Returns offset date time at the instant of date
   * 
   * @param date date
   * @return offset date time
   */
  public static OffsetDateTime fromDate(Date date) {
    return OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
  }
  
  /**
   * Formats offset date time as iso string
   * 
   * @param offsetDateTime offset date time
   * @return iso string
   */
  public static String toIsoString(OffsetDateTime offsetDateTime) {
    return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime);
  }
  
}
