package fi.metatavu.soteapi.firebase;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.firebase.FirebaseConsts;
import fi.metatavu.soteapi.settings.SystemSettingController;
import us.raudi.pushraven.Notification;
import us.raudi.pushraven.Pushraven;

@ApplicationScoped
public class FirebaseNotificationController {
  
  @Inject
  private SystemSettingController systemSettingController;

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   * @param data message data
   */
  public void sendNotificationToTopic(String topic, String title, String message, Map<String, Object> data) {
    sendNotification(String.format("/topics/%s", getTopicName(topic)), title, message, data);
  }

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   */
  public void sendNotificationToTopic(String topic, String title, String message) {
    sendNotification(String.format("/topics/%s", getTopicName(topic)), title, message, null);
  }

  private void sendNotification(String to, String title, String message, Map<String, Object> data) {
    Pushraven.setKey(systemSettingController.getSettingValue(FirebaseConsts.SERVER_KEY_SETTING));

    Notification notification = new Notification()
      .title(title)
      .text(message)
      .icon("push")
      .color("#005eb8")
      .to(to);
    
    if (data != null) {
      notification.data(data);
    }
    
    Pushraven.push(notification);
  }

  private String getTopicName(String topic) {
    return String.format("%s%s", getPrefix(), topic);
  }
  
  @SuppressWarnings ("squid:S1301")
  private String getPrefix() {
    switch (systemSettingController.getRunMode()) {
      case DEVELOPMENT:
        return "dev";
      case TEST:
        return "test";
      case PRODUCTION:
        return "";
    }
    
    return "";
  }
  
}
