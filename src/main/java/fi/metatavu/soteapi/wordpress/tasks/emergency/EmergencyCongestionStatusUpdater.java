package fi.metatavu.soteapi.wordpress.tasks.emergency;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Updater for emergency congestion status
 * 
 * @author Antti Leppä
 */
@Singleton
@ApplicationScoped
@Startup
public class EmergencyCongestionStatusUpdater {

  @Inject
  private EmergencyCongestionStatusJob emergencyCongestionStatusJob;
  
  @Resource
  private ManagedScheduledExecutorService managedScheduledExecutorService;
  
  @PostConstruct
  public void emergencyCongestionStatusConstruct() {
    startTimer(10000, 10000);
  }
  
  private void startTimer(long warmup, long delay) {
    managedScheduledExecutorService.scheduleWithFixedDelay(emergencyCongestionStatusJob, warmup, delay, TimeUnit.MILLISECONDS);
  }

}
