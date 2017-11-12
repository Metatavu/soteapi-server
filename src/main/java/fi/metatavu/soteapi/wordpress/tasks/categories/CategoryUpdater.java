package fi.metatavu.soteapi.wordpress.tasks.categories;

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
public class CategoryUpdater {

  @Inject
  private CategoryUpdateJob categoryUpdateJob;
  
  @Resource
  private ManagedScheduledExecutorService managedScheduledExecutorService;
  
  @PostConstruct
  public void categoryConstruct() {
    startTimer(60000, 1000);
  }
  
  private void startTimer(long warmup, long delay) {
    managedScheduledExecutorService.scheduleWithFixedDelay(categoryUpdateJob, warmup, delay, TimeUnit.MILLISECONDS);
  }

}
