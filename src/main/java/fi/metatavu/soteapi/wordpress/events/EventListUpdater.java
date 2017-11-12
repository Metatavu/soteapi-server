package fi.metatavu.soteapi.wordpress.events;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Singleton
@ApplicationScoped
@Startup
public class EventListUpdater {

  @Inject
  private EventListJob eventListJob;
  
  @Resource
  private ManagedScheduledExecutorService managedScheduledExecutorService;
  
  @PostConstruct
  public void postConstruct() {
    startTimer(60000, 10000);
  }
  
  private void startTimer(long warmup, long delay) {
    managedScheduledExecutorService.scheduleWithFixedDelay(eventListJob, warmup, delay, TimeUnit.MILLISECONDS);
  }

}
