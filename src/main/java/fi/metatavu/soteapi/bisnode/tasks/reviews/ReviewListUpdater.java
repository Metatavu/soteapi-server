package fi.metatavu.soteapi.bisnode.tasks.reviews;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Updater for review lists
 * 
 * @author Heikki Kurhinen
 */
@Singleton
@ApplicationScoped
@Startup
public class ReviewListUpdater {

  @Inject
  private ReviewListJob reviewListJob;
  
  @Resource
  private ManagedScheduledExecutorService managedScheduledExecutorService;
  
  @PostConstruct
  public void postConstruct() {
    startTimer(60000, 10000);
  }
  
  /**
   * Starts timer
   * 
   * @param warmup warmup
   * @param delay delay
   */
  private void startTimer(long warmup, long delay) {
    managedScheduledExecutorService.scheduleWithFixedDelay(reviewListJob, warmup, delay, TimeUnit.MILLISECONDS);
  }

}
