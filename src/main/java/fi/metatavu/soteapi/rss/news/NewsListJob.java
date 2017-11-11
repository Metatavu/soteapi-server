package fi.metatavu.soteapi.rss.news;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;

import com.github.slugify.Slugify;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import fi.metatavu.soteapi.settings.SystemSettingController;
import fi.metatavu.soteapi.tasks.AbstractUpdateJob;

public class NewsListJob extends AbstractUpdateJob {
  
  @Inject
  private Logger logger;

  @Inject
  private NewsListQueue newsListQueue;

  @Inject
  private NewsUpdateQueue newsUpdateQueue;

  @Inject
  private SystemSettingController systemSettingController;
  
  @Override
  protected void execute() {
    String url = getUrl();
    if (StringUtils.isNotEmpty(url)) {
      NewsListTask task = newsListQueue.next();
      if (task != null) {
        performTask(url, task);
      } else if (newsListQueue.isEmptyAndLocalNodeResponsible()) {
        fillQueue();
      }
    }
  }
  
  @Override
  protected String getEnabledSetting() {
    return NewsConsts.NEWS_SYNC_ENABLED;
  }
  
  /**
   * Executes list task
   * 
   * @param task task
   */
  private void performTask(String url, NewsListTask task) {
    loadEntries(url, task.getPage()).forEach(this::process);
  }
  
  /**
   * Loads page feed entries from URL
   * 
   * @param url feed URL
   * @param page page
   * @return feed entries
   */
  private Iterable<SyndEntry> loadEntries(String url, int page) {
    SyndFeed syndFeed = loadFeed(url, page);
    if (syndFeed != null) {
      return syndFeed.getEntries();
    }
    
    return Collections.emptyList();
  }

  /**
   * Loads RSS feed page from URL
   * 
   * @param url feed URL
   * @param page page
   * @return feed
   */
  private SyndFeed loadFeed(String url, int page) {
    SyndFeedInput input = new SyndFeedInput();
    try {
      URIBuilder uriBuilder = new URIBuilder(url);
      uriBuilder.addParameter("paged", String.valueOf(page));
      
      return input.build(new XmlReader(uriBuilder.build().toURL()));
    } catch (IllegalArgumentException | FeedException | IOException | URISyntaxException e) {
      logger.warn(String.format("Failed to load feed entries from %s page %d", url, page), e);
    }
    
    return null;
  }

  /**
   * Processes a feed entry
   * 
   * @param syndEntry feed entry
   */
  private void process(SyndEntry syndEntry) {
    if (syndEntry == null) {
      return;
    }

    String uniqueId = String.format("rss-news-update-%s", syndEntry.getUri());
    boolean priority = false;
    newsUpdateQueue.enqueueTask(new NewsUpdateTask(uniqueId, priority, createModel(syndEntry)));
  }

  /**
   * Creates update model from feed entry
   * 
   * @param syndEntry feed entry
   * @return update model
   */
  private NewsUpdateModel createModel(SyndEntry syndEntry) {
    List<SyndCategory> entryCategories = syndEntry.getCategories();
    String title = syndEntry.getTitle();
    String slug = extractEntrySlug(syndEntry);
    String content = null;
    String categorySlug = null;
    String originId = syndEntry.getUri();
    Long orderIndex = -syndEntry.getPublishedDate().getTime();
    
    if (!entryCategories.isEmpty()) {
      Iterator<SyndCategory> iterator = entryCategories.iterator();
      while (StringUtils.isEmpty(categorySlug) && iterator.hasNext()) {
        String name = iterator.next().getName();
        categorySlug = new Slugify().slugify(name);
      }
    }
    
    List<SyndContent> entryContents = syndEntry.getContents();
    for (SyndContent entryContent : entryContents) {
      String type = entryContent.getType();
      if (type == null) {
        type = "text/plain";
      }
      
      if ("html".equals(type) || "text/plain".equals(type)) {
        content = entryContent.getValue();
      } else {
        if (logger.isWarnEnabled()) {
          logger.warn(String.format("Don't know how to handle RSS content with type %s", type));
        }
      }
    }
    
    NewsUpdateModel result = new NewsUpdateModel(title, content, slug, originId, categorySlug, orderIndex);
    
    return result;
  }

  /**
   * Extracts slug from feed entry
   * 
   * @param syndEntry feed entry
   * @return slug from feed entry
   */
  private String extractEntrySlug(SyndEntry syndEntry) {
    java.net.URL url;
    try {
      url = new java.net.URL(syndEntry.getLink());
      return FilenameUtils.getName(FilenameUtils.normalizeNoEndSeparator(url.getPath()));
    } catch (MalformedURLException e) {
      logger.error(String.format("Failed to resolve slug from entry %s", syndEntry.getUri()));
    }
    
    return null;
  }

  /**
   * Fills the queue
   */
  private void fillQueue() {
    for (int page = 0; page < getPages(); page++) {
      newsListQueue.enqueueTask(new NewsListTask(String.format("rss-news-list-%d", page), false, page));
    }
  }

  /**
   * Returns number of pages to sync or 1 if not specified
   * 
   * @return number of pages to sync or 1 if not specified
   */
  private int getPages() {
    return systemSettingController.getSettingValueInteger(NewsConsts.RSS_PAGES_SETTING, 1);
  }
  
  /**
   * Returns URL to RSS feed or null if not defined
   * 
   * @return URL to RSS feed or null if not defined
   */
  private String getUrl() {
    return systemSettingController.getSettingValue(NewsConsts.RSS_URL_SETTING);
  }

}
