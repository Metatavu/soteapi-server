package fi.metatavu.soteapi.firebase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.settings.SystemSettingController;
import us.raudi.pushraven.Notification;
import us.raudi.pushraven.Pushraven;

@ApplicationScoped
public class FirebaseController {
  
  @Inject
  private SystemSettingController systemSettingController;

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   */
  public void sendNotifcationToTopic(String topic, String title, String message) {
    sendNotification(String.format("/topics/%s", topic), title, message);
  }

  private void sendNotification(String to, String title, String message) {
    Pushraven.setKey(systemSettingController.getSettingValue(FirebaseConsts.SERVER_KEY_SETTING));
    
    Pushraven.push(new Notification()
      .title(title)
      .text(message)
      .icon("push")
      .color("#005eb8")
      .to(to));
  }
  
}
