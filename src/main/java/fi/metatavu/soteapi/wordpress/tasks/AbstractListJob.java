package fi.metatavu.soteapi.wordpress.tasks;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import org.springframework.util.ReflectionUtils;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.request.SearchRequest;
import com.afrozaar.wordpress.wpapi.v2.response.PagedResponse;

import fi.metatavu.soteapi.persistence.dao.AbstractDAO;
import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.tasks.AbstractUpdateJob;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

/**
 * Abstract base class for wordpress update jobs
 * 
 * @author Heikki Kurhinen
 * 
 * @param <W> Wordpress post type
 */
public abstract class AbstractListJob<W, T extends AbstractListTask> extends AbstractUpdateJob {
  
  @Inject
  private Wordpress wordpressClient;

  @Override
  protected void execute() {
    T task = getQueue().next();
    if (task != null) {
      performTask(task);
    } else if (getQueue().isEmptyAndLocalNodeResponsible()) {
      fillQueue();
    }
  }
  
  @Override
  protected boolean isEnabled() {
    if (getWordpressClient() == null) {
      return false;
    }
    
    return super.isEnabled();
  }

  /**
   * Processes the post
   * 
   * @param post post to be processed
   */
  protected abstract void process(W post);

  /**
   * Get data from page
   * 
   * @param postType Type of wordpress post
   * @param page Page to extract the data from
   * @return list of posts
   */
  protected List<W> getDataFromPage(int page) {

    @SuppressWarnings("unchecked")
    PagedResponse<W> searchResponse = (PagedResponse<W>) getWordpressClient().search(SearchRequest.Builder
        .aSearchRequest(getWordpressTypeClass())
        .withPagination(WordpressConsts.PAGE_SIZE, page)
        .withUri(getEndPointUri())
        .build());

    return searchResponse.getList();
  }
  
  /**
   * Creates new task based on the page index
   * 
   * @param page page index
   * @return created task
   */
  protected abstract T createTask(int page);
  
  /**
   * Returns queue
   * 
   * @return queue
   */
  protected abstract AbstractSoteApiTaskQueue<T> getQueue();
  
  /**
   * Returns uri for search endpoint
   * 
   * @return uri of search endpoint
   */
  protected abstract String getEndPointUri();
  
  /**
   * Returns Wordpress client
   * 
   * @return Wordpress client
   */
  protected Wordpress getWordpressClient() {
    return wordpressClient;
  }
  
  private void performTask(T task) {
    getDataFromPage(task.getPage()).forEach(this::process);
  }
  
  /**
   * Fills the queue
   */
  private void fillQueue() {
    int numberOfPages = getNumberOfPages();
    for (int i = 1; i <= numberOfPages; i++) {
      getQueue().enqueueTask(createTask(i));
    }
  }
  
  /**
   * Get total number of pages
   * 
   * @param postType type of wordpress post
   * @return total number of pages for post type
   */
  protected int getNumberOfPages() {
    PagedResponse<?> searchResponse = getWordpressClient().search(SearchRequest.Builder
        .aSearchRequest(getWordpressTypeClass())
        .withPagination(WordpressConsts.PAGE_SIZE, 1)
        .withUri(getEndPointUri())
        .build());

    Field pagesField = ReflectionUtils.findField(PagedResponse.class, "pages");
    ReflectionUtils.makeAccessible(pagesField);
    return (int) ReflectionUtils.getField(pagesField, searchResponse);
  }
  
  @SuppressWarnings("unchecked")
  private Class<? extends W> getWordpressTypeClass() {
    Type genericSuperclass = getClass().getGenericSuperclass();

    if (genericSuperclass instanceof ParameterizedType) {
      return (Class<? extends W>) getTypeArgument((ParameterizedType) genericSuperclass, 0);
    } else {
      if ((genericSuperclass instanceof Class<?>) && (AbstractDAO.class.isAssignableFrom((Class<?>) genericSuperclass))) {
        return (Class<? extends W>) getTypeArgument((ParameterizedType) ((Class<?>) genericSuperclass).getGenericSuperclass(), 0);
      }
    }

    return null;
  }

  private Class<?> getTypeArgument(ParameterizedType parameterizedType, int index) {
    return (Class<?>) parameterizedType.getActualTypeArguments()[index];
  }

}
