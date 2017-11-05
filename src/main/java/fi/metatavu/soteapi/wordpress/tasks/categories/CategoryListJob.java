package fi.metatavu.soteapi.wordpress.tasks.categories;

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
  
  protected void process(Term categoryData) {
    if (categoryData == null) {
      return;
    }
    
    CategoryUpdateTaskModel categoryModel = new CategoryUpdateTaskModel();
    categoryModel.setOriginId(categoryData.getId().toString());
    categoryModel.setTitle(categoryData.getName());
    categoryModel.setSlug(categoryData.getSlug());
    
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
      wordpressClient.getCategories()
        .forEach(this::process);
    } else if (categoryListQueue.isEmptyAndLocalNodeResponsible()) {
      createTask();
    }
    
  }
    
}
