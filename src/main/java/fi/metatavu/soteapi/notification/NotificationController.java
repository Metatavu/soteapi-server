package fi.metatavu.soteapi.notification;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.firebase.FirebaseNotificationController;
import fi.metatavu.soteapi.wnspusher.WnsPusherNotificationController;

@ApplicationScoped
public class NotificationController {
  
  @Inject
  private FirebaseNotificationController firebaseController;

  @Inject
  private WnsPusherNotificationController azureController;

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   * @param data message data
   */
  public void sendNotificationToTopic(String topic, String title, String message, Map<String, Object> data) {
    firebaseController.sendNotificationToTopic(topic, title, message, data);
    azureController.sendNotificationToTopic(title, message, data);
  }

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   */
  public void sendNotificationToTopic(String topic, String title, String message) {
    firebaseController.sendNotificationToTopic(topic, title, message);
    azureController.sendNotificationToTopic(title, message);
  }
}
