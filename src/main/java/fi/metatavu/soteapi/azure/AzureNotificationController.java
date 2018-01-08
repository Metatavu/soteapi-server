package fi.metatavu.soteapi.azure;

import java.io.StringWriter;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;

import com.windowsazure.messaging.Notification;
import com.windowsazure.messaging.NotificationHub;
import com.windowsazure.messaging.NotificationHubsException;

import fi.metatavu.soteapi.azure.xml.Binding;
import fi.metatavu.soteapi.azure.xml.Text;
import fi.metatavu.soteapi.azure.xml.Toast;
import fi.metatavu.soteapi.azure.xml.Visual;
import fi.metatavu.soteapi.settings.SystemSettingController;

@ApplicationScoped
public class AzureNotificationController {
  
  @Inject
  private SystemSettingController systemSettingController;
  
  @Inject
  private Logger logger;

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   * @param data message data
   */
  public void sendNotifcationToTopic(String topic, String title, String message, Map<String, Object> data) {
    sendNotification(topic, title, message, data);
  }

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   */
  public void sendNotifcationToTopic(String topic, String title, String message) {
    sendNotification(topic, title, message, null);
  }

  private void sendNotification(String to, String title, String message, Map<String, Object> data) {
    String connectionString = systemSettingController.getSettingValue(AzureNotificationConsts.CONNECTION_STRING_SETTING);
    if (connectionString == null) {
      logger.warn("Azure notification hub connection string not set");
    }
    String hubName = systemSettingController.getSettingValue(AzureNotificationConsts.HUB_NAME_SETTING);
    if (hubName == null) {
      logger.warn("Azure notification hub name not set");
    }
    
    try {
      NotificationHub hub = new NotificationHub(connectionString, hubName);
      String notificationContent = createWindowsToastNotification(message);
      Notification notification = Notification.createWindowsNotification(notificationContent);
      hub.sendNotification(notification);
    } catch (JAXBException | NotificationHubsException ex) {
      logger.warn("Couldn't send Windows notification.", ex);
    }
  }
  
  private String createWindowsToastNotification(String message)
      throws JAXBException {
    Toast toast = new Toast(
        new Visual(
            new Binding(
                "ToastText01",
                new Text(
                    "1",
                    message))));
    
    JAXBContext jaxbContext = JAXBContext.newInstance(
        Toast.class,
        Visual.class,
        Binding.class,
        Text.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    StringWriter writer = new StringWriter();
    jaxbMarshaller.marshal(toast, writer);
    return writer.toString();
  }
}
