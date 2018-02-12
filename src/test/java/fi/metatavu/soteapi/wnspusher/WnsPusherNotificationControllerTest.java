package fi.metatavu.soteapi.wnspusher;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.metatavu.soteapi.settings.SystemSettingController;

public class WnsPusherNotificationControllerTest {
  SystemSettingController systemSettingController;
  Logger logger;
  CloseableHttpClient httpClient;
  ArgumentCaptor<HttpPost> httpClientArguments;
  CloseableHttpResponse httpResponse;
  WnsPusherNotificationController subject;

  @Before
  public void setUp() throws ClientProtocolException, IOException {
    systemSettingController = Mockito.mock(SystemSettingController.class);
    logger = Mockito.mock(Logger.class);
    httpClient = Mockito.mock(CloseableHttpClient.class);
    httpResponse = Mockito.mock(CloseableHttpResponse.class);
    httpClientArguments = ArgumentCaptor.forClass(HttpPost.class);

    Supplier<CloseableHttpClient> httpClientSupplier = () -> httpClient;
    
    subject = new WnsPusherNotificationController(
        systemSettingController,
        logger,
        httpClientSupplier
    );
    
    Mockito
      .when(systemSettingController.getSettingValue(WnsPusherNotificationConsts.ENDPOINT_SETTING))
      .thenReturn("TEST_ENDPOINT");

    Mockito
      .when(systemSettingController.getSettingValue(WnsPusherNotificationConsts.API_KEY_SETTING))
      .thenReturn("TEST_API_KEY");

    Mockito
      .when(systemSettingController.getSettingValue(WnsPusherNotificationConsts.APP_SETTING))
      .thenReturn("TEST_APP");

    Mockito
      .when(httpClient.execute(httpClientArguments.capture()))
      .thenReturn(httpResponse);
    
    StatusLine statusLine = Mockito.mock(StatusLine.class);
    Mockito.when(statusLine.getStatusCode()).thenReturn(200);
    Mockito
      .when(httpResponse.getStatusLine())
      .thenReturn(statusLine);
  }

  @Test
  public void testSendNotificationToTopic() {
    subject.sendNotificationToTopic("TEST_TOPIC", "TEST_TITLE", "TEST_MESSAGE");
  }

  @Test
  public void testSendNotificationHeader() {
    subject.sendNotificationToTopic("TEST_TOPIC", "TEST_TITLE", "TEST_MESSAGE");
    assertEquals(httpClientArguments
                  .getValue()
                  .getLastHeader("Authorization")
                  .getValue(),
                 "APIKEY TEST_API_KEY");
  }

  @Test
  public void testSendNotificationApp()
      throws JsonParseException, JsonMappingException, ParseException, IOException {
    subject.sendNotificationToTopic("TEST_TOPIC", "TEST_TITLE", "TEST_MESSAGE");
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost httpPost = httpClientArguments.getValue();
    WnsPusherNotification notification = objectMapper.readValue(
        EntityUtils.toString(httpPost.getEntity()),
        WnsPusherNotification.class);
    assertEquals(notification.getApp(), "TEST_APP");
  }

  @Test
  public void testSendNotificationContentType()
      throws JsonParseException, JsonMappingException, ParseException, IOException {
    subject.sendNotificationToTopic("TEST_TOPIC", "TEST_TITLE", "TEST_MESSAGE");
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost httpPost = httpClientArguments.getValue();
    WnsPusherNotification notification = objectMapper.readValue(
        EntityUtils.toString(httpPost.getEntity()),
        WnsPusherNotification.class);
    assertEquals(notification.getContentType(), "application/xml");
  }

  @Test
  public void testSendNotificationWnsType()
      throws JsonParseException, JsonMappingException, ParseException, IOException {
    subject.sendNotificationToTopic("TEST_TOPIC", "TEST_TITLE", "TEST_MESSAGE");
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost httpPost = httpClientArguments.getValue();
    WnsPusherNotification notification = objectMapper.readValue(
        EntityUtils.toString(httpPost.getEntity()),
        WnsPusherNotification.class);
    assertEquals(notification.getWnsType(), "wns/toast");
  }
}
