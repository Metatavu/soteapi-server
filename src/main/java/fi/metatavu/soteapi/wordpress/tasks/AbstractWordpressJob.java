package fi.metatavu.soteapi.wordpress.tasks;

import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.model.Term;

import fi.metatavu.soteapi.tasks.AbstractUpdateJob;

public abstract class AbstractWordpressJob extends AbstractUpdateJob {

  @Inject
  private Wordpress wordpressClient;

  protected Term findCategoryById(Long categoryId) {
    return wordpressClient.getCategory(categoryId);
  }
  
  /**
   * Returns Wordpress client
   * 
   * @return Wordpress client
   */
  protected Wordpress getWordpressClient() {
    return wordpressClient;
  }
  
  @Override
  protected boolean isEnabled() {
    if (wordpressClient == null) {
      return false;
    }
    
    return super.isEnabled();
  }
}
