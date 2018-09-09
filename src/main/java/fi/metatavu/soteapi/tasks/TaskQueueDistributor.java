package fi.metatavu.soteapi.tasks;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import fi.metatavu.soteapi.cluster.ClusterController;
import fi.metatavu.soteapi.persistence.model.TaskQueue;
import fi.metatavu.soteapi.settings.RunMode;
import fi.metatavu.soteapi.settings.SystemSettingController;

/**
 * Task queue distributor
 * 
 * @author Antti Leppä
 */
@Startup
@Singleton
@ApplicationScoped
public class TaskQueueDistributor {

  private static final int UPDATE_INTERVAL = 1000 * 60;
  private static final int TEST_UPDATE_INTERVAL = 5000;

  @Inject
  private Logger logger;
  
  @Inject
  private TaskController taskController;

  @Inject
  private ClusterController clusterController;

  @Inject
  private SystemSettingController systemSettingController;
  
  @Resource
  private ManagedScheduledExecutorService managedScheduledExecutorService;
  
  @Resource
  private EJBContext ejbContext;

  @PostConstruct
  public void postConstruct() {
    startTimer();
  }
  
  private void selfAssignQueues() {
    String localNodeName = clusterController.getLocalNodeName();
    List<String> nodeNames = clusterController.getNodeNames();
    int myIndex = nodeNames.indexOf(localNodeName);
    int nodeCount = nodeNames.size();
    Long queueCount = taskController.countTaskQueues();
    
    if (logger.isInfoEnabled()) {
      logger.info(String.format("Reassigning queues, found %d workers online. My index is %d", nodeCount, myIndex));
    }
    
    for (int i = 0; i < queueCount; i++) {
      if ((i % nodeCount) == myIndex) {
        TaskQueue taskQueue = taskController.findTaskQueueByIndex(i);
        if (!StringUtils.equals(taskQueue.getResponsibleNode(), localNodeName)) {
          if (logger.isInfoEnabled()) {
            logger.info(String.format("Worker %s reserved queue %s", localNodeName, taskQueue.getName()));
          }
          
          taskController.updateTaskQueueResponsibleNode(taskQueue, localNodeName);
        }
      }
    }
  }
  
  private void startTimer() {
    boolean testMode = systemSettingController.getRunMode() == RunMode.TEST;
    long interval = testMode ? TEST_UPDATE_INTERVAL : UPDATE_INTERVAL;
    startTimer(interval, interval);
  }
  
  private void startTimer(long warmup, long delay) {
    managedScheduledExecutorService.scheduleWithFixedDelay(() -> {
      UserTransaction userTransaction = ejbContext.getUserTransaction();
      try {
        userTransaction.begin();
        selfAssignQueues();
        userTransaction.commit();
      } catch (Throwable ex) {
        logger.error("TaskQueueDistributor crashed", ex);
        try {
          if (userTransaction != null) {
            userTransaction.rollback();
          }
        } catch (SystemException e1) {
          logger.error("Failed to rollback transaction", e1);
        }
      }
    }, warmup, delay, TimeUnit.MILLISECONDS);
  }
  
}
