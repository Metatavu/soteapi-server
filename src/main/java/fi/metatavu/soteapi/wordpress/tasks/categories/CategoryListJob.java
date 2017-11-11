package fi.metatavu.soteapi.wordpress.tasks.categories;

import java.util.List;

import javax.inject.Inject;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.model.Term;

import fi.metatavu.soteapi.tasks.AbstractUpdateJob;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

public class CategoryListJob extends AbstractUpdateJob {
  
  @Inject
  private CategoryListQueue categoryListQueue;
  
  @Inject
  private CategoryUpdateQueue categoryUpdateQueue;
  
  @Inject
  private Wordpress wordpressClient;
  
  protected void createTask() {
    CategoryListTask newTask = new CategoryListTask();
    newTask.setPriority(Boolean.FALSE);
    newTask.setUniqueId("wp-category-list");
    categoryListQueue.enqueueTask(newTask);
  }

  @Override
  protected boolean isEnabled() {
    if (wordpressClient == null) {
      return false;
    }
    
    return super.isEnabled();
  }
  
  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.CATEGORIES_SYNC_ENABLED;
  }
  
  protected void process(Term categoryData, Long originIndex) {
    if (categoryData == null) {
      return;
    }
    
    String originId = categoryData.getId().toString();
    String title = categoryData.getName();
    String slug = categoryData.getSlug();
        
    CategoryUpdateTaskModel categoryModel = new CategoryUpdateTaskModel(title, null, slug, originId, null, null, originIndex);
    
    CategoryUpdateTask categoryUpdateTask = new CategoryUpdateTask();
    categoryUpdateTask.setPostUpdateModel(categoryModel);
    categoryUpdateTask.setPriority(Boolean.FALSE);
    categoryUpdateTask.setUniqueId(String.format("wp-category-update-%d", categoryData.getId()));
    categoryUpdateQueue.enqueueTask(categoryUpdateTask);
  }

  @Override
  protected void execute() {
    CategoryListTask listTask = categoryListQueue.next();
    if (listTask != null) {
      List<Term> categories = wordpressClient.getCategories();
      for (int i = 0; i < categories.size(); i++) {
        Term term = categories.get(i);
        process(term, (long) i);
      }
    } else if (categoryListQueue.isEmptyAndLocalNodeResponsible()) {
      createTask();
    }
    
  }
    
}
