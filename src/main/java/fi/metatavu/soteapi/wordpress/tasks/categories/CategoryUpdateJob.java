package fi.metatavu.soteapi.wordpress.tasks.categories;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.category.CategoryController;
import fi.metatavu.soteapi.persistence.model.Category;
import fi.metatavu.soteapi.persistence.model.CategoryTitle;
import fi.metatavu.soteapi.wordpress.WordpressConsts;
import fi.metatavu.soteapi.wordpress.tasks.AbstractWordpressJob;

public class CategoryUpdateJob extends AbstractWordpressJob {
  
  @Inject
  private CategoryUpdateQueue categoryUpdateQueue;
  
  @Inject
  private CategoryController categoryController;
  
  @Override
  protected void execute() {
    CategoryUpdateTask categoryUpdateTask = categoryUpdateQueue.next();
    if (categoryUpdateTask != null) {
      performTask(categoryUpdateTask);
    }
  }
  
  private void performTask(CategoryUpdateTask task) {
    CategoryUpdateTaskModel categoryUpdateModel = task.getPostUpdateModel();

    if (categoryUpdateModel == null) {
      return;
    }

    String originId = categoryUpdateModel.getOriginId();
    Category categoryEntity = categoryController.findCategoryByOriginId(originId);
    
    if (categoryEntity != null) {
      updateExistingCategory(categoryEntity, categoryUpdateModel);
      return;
    }
    
    createNewCategory(categoryUpdateModel);
  }
  
  private void createNewCategory(CategoryUpdateTaskModel categoryUpdateModel) {
    if (categoryUpdateModel == null) {
      return;
    }
    
    Category category = categoryController.createCategory(categoryUpdateModel.getOriginId(), categoryUpdateModel.getSlug());
    
    String categoryTitleContent = categoryUpdateModel.getTitle();
    
    if (StringUtils.isNotEmpty(categoryTitleContent)) {
      categoryController.createCategoryTitle(category, WordpressConsts.DEFAULT_LANGUAGE, categoryTitleContent);
    }
  }
  
  private void updateExistingCategory(Category categoryEntity, CategoryUpdateTaskModel categoryUpdateModel) {
    if (categoryUpdateModel == null) {
      return;
    }
    
    String categoryTitleContent = categoryUpdateModel.getTitle();

    categoryController.updateCategory(categoryEntity, categoryUpdateModel.getOriginId(), categoryUpdateModel.getSlug());
    
    if (StringUtils.isNotEmpty(categoryTitleContent)) {
      CategoryTitle categoryTitle = categoryController.listCategoryTitlesByCategory(categoryEntity)
        .stream()
        .filter(title -> title.getLanguage().equals(WordpressConsts.DEFAULT_LANGUAGE))
        .findAny()
        .orElse(null);

      if (categoryTitle != null) {
        categoryController.updateCategoryTitle(categoryTitle, WordpressConsts.DEFAULT_LANGUAGE, categoryTitleContent, categoryEntity);
      } else {
        categoryController.createCategoryTitle(categoryEntity, WordpressConsts.DEFAULT_LANGUAGE, categoryTitleContent);
      }
    }

  }
  
}
