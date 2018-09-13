package fi.metatavu.soteapi.wordpress.tasks.emergency;

import java.io.IOException;
import java.time.OffsetDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.afrozaar.wordpress.wpapi.v2.Client;
import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fi.metatavu.soteapi.emergency.EmergencyCongestionStatusController;
import fi.metatavu.soteapi.settings.SystemSettingController;
import fi.metatavu.soteapi.tasks.AbstractUpdateJob;
import fi.metatavu.soteapi.wordpress.WordpressConsts;

/**
 * Emergency congestion status update job
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class EmergencyCongestionStatusJob extends AbstractUpdateJob {
  
  private static final String CONTEXT = "/emergency";
  private static final String STATUS_ENDPOINT = "/congestion/status";

  @Inject
  private Logger logger;

  @Inject
  private SystemSettingController systemSettingController;

  @Inject
  private EmergencyCongestionStatusController emergencyCongestionStatusController;

  @Inject
  private EmergencyCongestionStatusQueue emergencyCongestionStatusQueue;
  
  @Override
  protected String getEnabledSetting() {
    return WordpressConsts.EMERGENCY_CONGESTION_STATUS_SYNC_ENABLED;
  }
  
  @Override
  protected void execute() {
    EmergencyCongestionStatusTask task = emergencyCongestionStatusQueue.next();
    if (task != null) {
      try {
        EmergencyCongestionStatusRestModel model = doRestRequest();
        OffsetDateTime modified = model.getModified();
        Integer value = model.getValue();
        if (value == null) {
          return;
        }
  
        int currentValue = emergencyCongestionStatusController.getCurrentEmergencyCongestionStatus();
        if (currentValue != value) {
          emergencyCongestionStatusController.createEmergencyCongestionStatus(value, modified);
        }
      } catch (IOException e) {
        logger.error("Failed to fetch emergency congestion status", e);
      }
    } else if (emergencyCongestionStatusQueue.isEmptyAndLocalNodeResponsible()) {
      createTask();
    }
  }

  /**
   * Creates new task
   */
  protected void createTask() {
    EmergencyCongestionStatusTask newTask = new EmergencyCongestionStatusTask();
    newTask.setPriority(Boolean.FALSE);
    newTask.setUniqueId("wp-emergency-congestion-status");
    emergencyCongestionStatusQueue.enqueueTask(newTask);
  }

  /**
   * Creates and request into status REST endpoint
   * 
   * @return response
   * @throws IOException thrown when request fails
   */
  private EmergencyCongestionStatusRestModel doRestRequest() throws IOException {
    ResponseEntity<String> responseEntity = getWordpressClient().doCustomExchange(STATUS_ENDPOINT, HttpMethod.GET, String.class, new Object[0], null, null, null);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper.readValue(responseEntity.getBody(), EmergencyCongestionStatusRestModel.class);
  }
  
  /**
   * Returns initialized Wordpress client
   * 
   * @return initialized Wordpress client
   */
  private Wordpress getWordpressClient() {
    String url = systemSettingController.getSettingValue(WordpressConsts.URL_SETTING);
    if (StringUtils.isNotBlank(url)) {
      return new Client(CONTEXT, url, "", "", WordpressConsts.USE_PERMALINK_ENDPOINT, WordpressConsts.DEBUG_CLIENT);
    }
    
    return null;
  }
    
}
