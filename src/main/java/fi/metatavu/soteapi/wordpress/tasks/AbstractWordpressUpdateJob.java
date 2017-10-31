package fi.metatavu.soteapi.wordpress.tasks;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import org.springframework.util.ReflectionUtils;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.request.SearchRequest;
import com.afrozaar.wordpress.wpapi.v2.response.PagedResponse;

import fi.metatavu.soteapi.tasks.AbstractUpdateJob;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

/**
 * Abstract base class for wordpress update jobs
 * 
 * @author Heikki Kurhinen
 * 
 * @param <T> Wordpress post type
 */
public abstract class AbstractWordpressUpdateJob<T> extends AbstractUpdateJob {
  
  @Inject
  private Wordpress wordpressClient;
  
  /**
   * Get total number of pages
   * 
   * @param postType type of wordpress post
   * @return total number of pages for post type
   */
  protected int getNumberOfPages(Class<T> postType) {
    PagedResponse<T> searchResponse = wordpressClient.search(SearchRequest.Builder
        .aSearchRequest(postType)
        .withPagination(WordpressConsts.PAGE_SIZE, 1)
        .build());

    Field pagesField = ReflectionUtils.findField(PagedResponse.class, "pages");
    ReflectionUtils.makeAccessible(pagesField);
    return (int) ReflectionUtils.getField(pagesField, searchResponse);
  }
  
  /**
   * Get data from page
   * 
   * @param postType Type of wordpress post
   * @param page Page to extract the data from
   * @return list of posts
   */
  protected List<T> getDataFromPage(Class<T> postType, int page) {
    PagedResponse<T> searchResponse = wordpressClient.search(SearchRequest.Builder
        .aSearchRequest(postType)
        .withPagination(WordpressConsts.PAGE_SIZE, page)
        .build());
    
    return searchResponse.getList();
  }

}
