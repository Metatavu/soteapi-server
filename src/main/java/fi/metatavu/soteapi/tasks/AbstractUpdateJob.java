package fi.metatavu.soteapi.tasks;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import fi.metatavu.soteapi.settings.SystemSettingController;

/**
 * Abstract base class for scheduled update jobs
 * 
 * @author Heikki Kurhinen
 *
 */
public abstract class AbstractUpdateJob implements Runnable {

  @Inject
  private Logger logger;

  @Inject
  private SystemSettingController systemSettingController;
  
  @Resource
  private EJBContext ejbContext;
  
  @Override
  public void run() {
    UserTransaction userTransaction = ejbContext.getUserTransaction();
    try {
      userTransaction.begin();
      
      if (isEnabled()) {
        execute();
      }
      
      userTransaction.commit();
    } catch (Exception ex) {
      logger.error("Timer throw an exception", ex);
      try {
        userTransaction.rollback();
      } catch (SystemException e1) {
        logger.error("Failed to rollback transaction", e1);
      }
    }
  }
  
  /**
   * Executes job
   */
  protected abstract void execute();
    
  /**
   * Returns setting key for determining whether the job is enabled or not
   * 
   * @return setting key for determining whether the job is enabled or not
   */
  protected abstract String getEnabledSetting();
  
  /**
   * Returns whether job is enabled. If enabled setting is not defined the job is interpret as enabled
   * 
   * @return whether job is enabled
   */
  protected boolean isEnabled() {
    if (StringUtils.isEmpty(getEnabledSetting())) {
      return true;
    }
    
    return systemSettingController.getSettingValueBoolean(getEnabledSetting(), true);
  }
  
}
