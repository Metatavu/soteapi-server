package fi.metatavu.soteapi.wordpress.tasks.categories;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class CategoryListQueue extends AbstractSoteApiTaskQueue<CategoryListTask> {

  @Override
  public String getName() {
    return "WORDPRESS-CATEGORY-LISTS";
  }

}
