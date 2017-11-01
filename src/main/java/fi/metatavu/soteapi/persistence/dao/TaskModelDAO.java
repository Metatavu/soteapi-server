package fi.metatavu.soteapi.persistence.dao;

import java.time.OffsetDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.metatavu.soteapi.persistence.model.TaskModel;
import fi.metatavu.soteapi.persistence.model.TaskModel_;
import fi.metatavu.soteapi.persistence.model.TaskQueue;

/**
 * DAO class for Task entity
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class TaskModelDAO extends AbstractDAO<TaskModel> {
  
  /**
   * Creates new task entity
   * 
   * @param queue queue the task belongs to
   * @param priority whether the task is a priority task or not
   * @param data serialized task data
   * @param uniqueId unique id for task
   * @param created creation time
   * @return created task
   */
  public TaskModel create(TaskQueue queue, Boolean priority, byte[] data, String uniqueId, OffsetDateTime created) {
    TaskModel taskModel = new TaskModel();
    taskModel.setCreated(created);
    taskModel.setUniqueId(uniqueId);
    taskModel.setData(data);
    taskModel.setPriority(priority);
    taskModel.setQueue(queue);
    
    return persist(taskModel);
  }
  
  /**
   * Returns next scheduled task for the specified queue
   * 
   * @param queue queue
   * @return Next scheduled task for the specified queue
   */
  public TaskModel findNextInQueue(TaskQueue queue) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<TaskModel> criteria = criteriaBuilder.createQuery(TaskModel.class);
    Root<TaskModel> root = criteria.from(TaskModel.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(TaskModel_.queue), queue));
    criteria.orderBy(criteriaBuilder.desc(root.get(TaskModel_.priority)), criteriaBuilder.asc(root.get(TaskModel_.created)), criteriaBuilder.asc(root.get(TaskModel_.id)));
    TypedQuery<TaskModel> query = entityManager.createQuery(criteria);
    query.setMaxResults(1);
    
    return getSingleResult(query);
  }

  /**
   * Finds task by queue and unique id
   * 
   * @param queue queue
   * @param uniqueId unique id
   * @return count of tasks by queue and unique id
   */
  public TaskModel findByQueueAndUniqueId(TaskQueue queue, String uniqueId) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<TaskModel> criteria = criteriaBuilder.createQuery(TaskModel.class);
    Root<TaskModel> root = criteria.from(TaskModel.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(TaskModel_.queue), queue),
        criteriaBuilder.equal(root.get(TaskModel_.uniqueId), uniqueId)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  /**
   * Counts tasks by queue and unique id
   * 
   * @param queue queue
   * @param uniqueId unique id
   * @return count of tasks by queue and unique id
   */
  public Long countByQueueAndUniqueId(TaskQueue queue, String uniqueId) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<TaskModel> root = criteria.from(TaskModel.class);
    criteria.select(criteriaBuilder.count(root.get(TaskModel_.id)));
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(TaskModel_.queue), queue),
        criteriaBuilder.equal(root.get(TaskModel_.uniqueId), uniqueId)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  
  public Long countByQueue(TaskQueue queue) {
    EntityManager entityManager = getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<TaskModel> root = criteria.from(TaskModel.class);
    criteria.select(criteriaBuilder.count(root));
    criteria.where(criteriaBuilder.equal(root.get(TaskModel_.queue), queue));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public TaskModel updatePriority(TaskModel taskModel, boolean priority) {
    taskModel.setPriority(priority);
    return persist(taskModel);
  }
  
}
