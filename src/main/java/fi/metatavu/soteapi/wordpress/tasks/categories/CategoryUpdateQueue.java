package fi.metatavu.soteapi.wordpress.tasks.categories;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class CategoryUpdateQueue extends AbstractSoteApiTaskQueue<CategoryUpdateTask> {

  @Override
  public String getName() {
    return "WORDPRESS-CATEGORY-UPDATES";
  }

}
