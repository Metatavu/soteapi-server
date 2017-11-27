package fi.metatavu.soteapi.system;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;

import fi.metatavu.soteapi.settings.RunMode;
import fi.metatavu.soteapi.settings.SystemSettingController;

@ApplicationScoped
public class SystemHealthCheck {

  @Inject
  private Logger logger;
  
  @Inject
  private SystemSettingController systemSettingController;

  public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
    RunMode runMode = systemSettingController.getRunMode();
    switch (runMode) {
      case DEVELOPMENT:
        logger.warn("System is running in development mode");
      break;
      case TEST:
        logger.warn("System is running in test mode");
      break;
      case PRODUCTION:
        logger.info("System is running in production mode");
      break;
    }
  }
  
}
