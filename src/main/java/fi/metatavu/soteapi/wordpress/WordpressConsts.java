package fi.metatavu.soteapi.wordpress;

public class WordpressConsts {

  public static final String URL_SETTING = "wordpress.url";
  public static final String PAGES_SYNC_ENABLED = "wordpress.pages-sync-enabled";
  public static final String POSTS_SYNC_ENABLED = "wordpress.posts-sync-enabled";
  public static final String EVENTS_SYNC_ENABLED = "wordpress.events-sync-enabled";
  public static final String CATEGORIES_SYNC_ENABLED = "wordpress.categories-sync-enabled";
  public static final String EMERGENCY_CONGESTION_STATUS_SYNC_ENABLED = "wordpress.emergency-congestion-status-sync-enabled";
  public static final String DEFAULT_LANGUAGE = "FI";
  public static final boolean USE_PERMALINK_ENDPOINT = false;
  public static final boolean DEBUG_CLIENT = false;
  public static final int PAGE_SIZE = 3;
  public static final String ORIGIN = "WORDPRESS";
  
  private WordpressConsts() {
  }
  
}
