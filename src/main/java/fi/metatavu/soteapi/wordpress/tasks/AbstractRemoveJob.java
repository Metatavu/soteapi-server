package fi.metatavu.soteapi.wordpress.tasks;

import java.util.List;

import javax.inject.Inject;

import fi.metatavu.soteapi.content.ContentController;
import fi.metatavu.soteapi.persistence.model.Content;
import fi.metatavu.soteapi.persistence.model.ContentType;
import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

public abstract class AbstractRemoveJob<T extends AbstractRemoveTask, Q extends AbstractSoteApiTaskQueue<T>> extends AbstractWordpressJob {
  
  @Inject
  private ContentController contentController;
  
  @Override
  protected void execute() {
    AbstractRemoveTask removeTask = getQueue().next();
    if (removeTask != null) {
      performTask(removeTask);
    } else {
      if (getQueue().isEmptyAndLocalNodeResponsible()) {
        fillQueue();  
      }
    }
  }
  
  /**
   * Returns queue responsible of removes
   * 
   * @return queue responsible of removes
   */
  protected abstract Q getQueue();
  
  /**
   * Creates new task
   * 
   * @param originId origin id
   * @return created task
   */
  protected abstract T createTask(String originId);
  
  /**
   * Checks whether entity is archived or not
   * 
   * @param originId originId
   * @return whether entity is archived or not
   */
  protected abstract boolean isArchived(String originId);

  /**
   * Returns content's content types
   * 
   * @return content's content types
   */
  protected abstract ContentType[] getContentTypes();
  
  private void performTask(AbstractRemoveTask task) {
    String originId = task.getOriginId();
    Content contentEntity = contentController.findContentByOriginId(originId);
    if (contentEntity != null && !contentEntity.getArchived() && isArchived(originId)) {
      contentController.updateContentArchived(contentEntity, Boolean.TRUE);
    }
  }

  private void fillQueue() {
    for (ContentType contentType : getContentTypes()) {    
      List<String> originIds = contentController.listOriginIds(contentType, WordpressConsts.ORIGIN);
      for (String originId : originIds) {
        getQueue().enqueueTask(createTask(originId));
      }
    }
  }


  
}
