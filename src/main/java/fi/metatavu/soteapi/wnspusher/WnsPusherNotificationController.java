package fi.metatavu.soteapi.wnspusher;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.metatavu.soteapi.settings.SystemSettingController;
import fi.metatavu.soteapi.wnspusher.xml.Binding;
import fi.metatavu.soteapi.wnspusher.xml.Text;
import fi.metatavu.soteapi.wnspusher.xml.Toast;
import fi.metatavu.soteapi.wnspusher.xml.Visual;

@ApplicationScoped
public class WnsPusherNotificationController {
  
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
  public void sendNotificationToTopic(String topic, String title, String message, Map<String, Object> data) {
    sendNotification(topic, title, message, data);
  }

  /**
   * Sends push notification
   * 
   * @param topic target topic
   * @param title title
   * @param message message
   */
  public void sendNotificationToTopic(String topic, String title, String message) {
    sendNotification(topic, title, message, null);
  }

  private void sendNotification(String to, String title, String message, Map<String, Object> data) {
    String endpoint = systemSettingController.getSettingValue(
        WnsPusherNotificationConsts.ENDPOINT_SETTING);
    if (endpoint == null) {
      logger.warn("Wnspusher endpoint not set");
      return;
    }
    String apiKey = systemSettingController.getSettingValue(
        WnsPusherNotificationConsts.API_KEY_SETTING);
    if (apiKey == null) {
      logger.warn("Wnspusher api key not set");
      return;
    }
    String app = systemSettingController.getSettingValue(
        WnsPusherNotificationConsts.APP_SETTING);
    if (app == null) {
      logger.warn("Wnspusher app name not set");
      return;
    }
    
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String launchArgs = objectMapper.writeValueAsString(data);
      String notificationContent = createWindowsToastNotification(message, launchArgs);
      WnsPusherNotification notification = new WnsPusherNotification(
          app,
          notificationContent,
          "application/xml",
          "wns/toast");
      String notificationString = objectMapper.writeValueAsString(notification);
      HttpPost httpPost = new HttpPost(endpoint);
      httpPost.setHeader("Authorization", "APIKEY " + apiKey);
      httpPost.setEntity(new StringEntity(notificationString, StandardCharsets.UTF_8));
      try (CloseableHttpClient client = HttpClients.createDefault();
           CloseableHttpResponse response = client.execute(httpPost)) {
        int statusCode = response.getStatusLine().getStatusCode();
        if (200 <= statusCode && statusCode < 300) {
          logger.warn(String.format("Failure when sending to wnspusher (status %d): %s",
              statusCode,
              EntityUtils.toString(response.getEntity())));
        }
      }
    } catch (JAXBException | IOException ex) {
      logger.warn("Couldn't send Windows notification.", ex);
    }
  }
  
  private String createWindowsToastNotification(String message, String launchArgs)
      throws JAXBException {
    Toast toast = new Toast(
        new Visual(
            new Binding(
                "ToastText01",
                new Text(
                    "1",
                    message))),
        launchArgs);
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
