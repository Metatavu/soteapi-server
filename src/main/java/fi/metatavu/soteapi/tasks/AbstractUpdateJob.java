package fi.metatavu.soteapi.tasks;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

/**
 * Abstract base class for scheduled update jobs
 * 
 * @author Heikki Kurhinen
 *
 */
public abstract class AbstractUpdateJob implements Runnable {

  @Inject
  private Logger logger;
  
  @Resource
  private EJBContext ejbContext;
  
  @Override
  public void run() {
    UserTransaction userTransaction = ejbContext.getUserTransaction();
    try {
      userTransaction.begin();
      
      doJob();
      
      userTransaction.commit();
    } catch (Exception ex) {
      logger.error("Timer throw an exception", ex);
      try {
        if (userTransaction != null) {
          userTransaction.rollback();
        }
      } catch (SystemException e1) {
        logger.error("Failed to rollback transaction", e1);
      }
    }
  }
  
  protected abstract void doJob();
  
}
