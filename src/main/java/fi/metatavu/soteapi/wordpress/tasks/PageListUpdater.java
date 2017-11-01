package fi.metatavu.soteapi.wordpress.tasks;

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
public class PageListUpdater {

  @Inject
  private PageListJob pageListJob;
  
  @Resource
  private ManagedScheduledExecutorService managedScheduledExecutorService;
  
  @PostConstruct
  public void postConstruct() {
    startTimer(10000, 10000);
  }
  
  private void startTimer(long warmup, long delay) {
    managedScheduledExecutorService.scheduleWithFixedDelay(pageListJob, warmup, delay, TimeUnit.MILLISECONDS);
  }

}
