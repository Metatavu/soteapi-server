package fi.metatavu.soteapi.notification;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fi.metatavu.soteapi.firebase.FirebaseController;

@ApplicationScoped
public class NotificationController {
  
  @Inject
  private FirebaseController firebaseController;

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   * @param data message data
   */
  public void sendNotificationToTopic(String topic, String title, String message, Map<String, Object> data) {
    firebaseController.sendNotifcationToTopic(topic, title, message, data);
  }

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   */
  public void sendNotificationToTopic(String topic, String title, String message) {
    firebaseController.sendNotifcationToTopic(topic, title, message);
  }
}
