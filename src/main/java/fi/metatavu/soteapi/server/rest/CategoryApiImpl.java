package fi.metatavu.soteapi.server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.soteapi.category.CategoryController;
import fi.metatavu.soteapi.persistence.model.Category;
import fi.metatavu.soteapi.persistence.model.CategoryTitle;
import fi.metatavu.soteapi.rest.translate.CategoryTranslator;
import fi.metatavu.soteapi.server.rest.api.CategoriesApi;

@RequestScoped
@Stateful
public class CategoryApiImpl implements CategoriesApi {

  @Inject
  private CategoryController categoryController;
  
  @Inject
  private ResponseController responseController;
  
  @Inject
  private CategoryTranslator categoryTranslator;
  
  @Override
  public Response listCategories(Long firstResult, Long maxResults) {
    
    Integer from = firstResult == null ? null : firstResult.intValue();
    Integer max = maxResults == null ? null : maxResults.intValue();
    
    List<Category> categoryEntities = categoryController.listCategories(from, max);
    List<List<CategoryTitle>> categoryTitleEntities = new ArrayList<>(categoryEntities.size());
    for (Category categoryEntity : categoryEntities) {
      categoryTitleEntities.add(categoryController.listCategoryTitlesByCategory(categoryEntity));
    }
    
    return responseController.respondOk(categoryTranslator.translateCategories(categoryEntities, categoryTitleEntities));
  }

}
