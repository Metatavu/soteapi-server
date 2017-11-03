package fi.metatavu.soteapi.rest.translate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.persistence.model.CategoryTitle;
import fi.metatavu.soteapi.server.rest.model.Category;
import fi.metatavu.soteapi.server.rest.model.LocalizedValue;

/**
 * Translator for category related translations
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class CategoryTranslator extends AbstractTranslator {
  
  /**
   * Translates database category entity to REST content 
   * 
   * @param categoryEntity Database category entity to be translated
   * @param categoryTitleEntities Database entities for titles related to the category
   * @return Category translated to the REST content
   */
  public Category translateContent(fi.metatavu.soteapi.persistence.model.Category categoryEntity, List<CategoryTitle> categoryTitleEntities) {
    if (categoryEntity == null) {
      return null;
    }

    Category category = new Category();
    category.setId(categoryEntity.getId());
    category.setSlug(categoryEntity.getSlug());
    category.setTitle(translateCategoryTitles(categoryTitleEntities));

    return category;
  }
  
  /**
   * Translates list of categories to REST entities
   * 
   * @param categoryEntities List of database category enties
   * @param categoryTitleEntities List of list of category title entities
   * @return List of translated category entities
   */
  public List<Category> translateCategories(List<fi.metatavu.soteapi.persistence.model.Category> categoryEntities, List<List<CategoryTitle>> categoryTitleEntities) {
    List<Category> results = new ArrayList<>(categoryEntities.size());
    Iterator<fi.metatavu.soteapi.persistence.model.Category> categoryEntityIterator = categoryEntities.iterator();
    Iterator<List<CategoryTitle>> categoryTitleEntitiesIterator = categoryTitleEntities.iterator();
    while (categoryEntityIterator.hasNext() && categoryTitleEntitiesIterator.hasNext()) {
      results.add(translateContent(categoryEntityIterator.next(), categoryTitleEntitiesIterator.next()));
    }
    
    return results;
  }
  
  private List<LocalizedValue> translateCategoryTitles(List<CategoryTitle> categoryTitleEntities) {
    List<LocalizedValue> results = new ArrayList<>(categoryTitleEntities.size());
    for (CategoryTitle categoryTitleEntity : categoryTitleEntities) {
      results.add(translateContentTitle(categoryTitleEntity));
    }
    return results;
  }
  
  private LocalizedValue translateContentTitle(CategoryTitle categoryTitleEntity) {
    LocalizedValue localizedValue = new LocalizedValue();
    localizedValue.setLanguage(categoryTitleEntity.getLanguage());
    localizedValue.setValue(categoryTitleEntity.getValue());
    return localizedValue;
  }
  
}
