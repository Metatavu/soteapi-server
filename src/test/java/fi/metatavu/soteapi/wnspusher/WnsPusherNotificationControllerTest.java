package fi.metatavu.soteapi.wnspusher;

import static org.junit.Assert.assertEquals;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.metatavu.soteapi.settings.SystemSettingController;

public class WnsPusherNotificationControllerTest {

  private static final String TEST_MESSAGE = "TEST_MESSAGE";
  private static final String TEST_TITLE = "TEST_TITLE";
  private static final String TEST_APP = "TEST_APP";
  private static final String TEST_API_KEY = "TEST_API_KEY";
  private static final String TEST_ENDPOINT = "TEST_ENDPOINT";

  private SystemSettingController systemSettingController;
  private Logger logger;
  private CloseableHttpClient httpClient;
  private ArgumentCaptor<HttpPost> httpClientArguments;
  private CloseableHttpResponse httpResponse;
  private WnsPusherNotificationController subject;

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
      .thenReturn(TEST_ENDPOINT);

    Mockito
      .when(systemSettingController.getSettingValue(WnsPusherNotificationConsts.API_KEY_SETTING))
      .thenReturn(TEST_API_KEY);

    Mockito
      .when(systemSettingController.getSettingValue(WnsPusherNotificationConsts.APP_SETTING))
      .thenReturn(TEST_APP);

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
    subject.sendNotificationToTopic(TEST_TITLE, TEST_MESSAGE);
  }

  @Test
  public void testSendNotificationHeader() {
    subject.sendNotificationToTopic(TEST_TITLE, TEST_MESSAGE);
    assertEquals(httpClientArguments
                  .getValue()
                  .getLastHeader("Authorization")
                  .getValue(),
                 "APIKEY TEST_API_KEY");
  }

  @Test
  public void testSendNotificationApp()
      throws ParseException, IOException {
    subject.sendNotificationToTopic(TEST_TITLE, TEST_MESSAGE);
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost httpPost = httpClientArguments.getValue();
    JsonNode notification = objectMapper.readTree(EntityUtils.toString(httpPost.getEntity()));
    assertEquals(notification.get("app").asText(), TEST_APP);
  }

  @Test
  public void testSendNotificationUrl()
      throws ParseException, IOException {
    subject.sendNotificationToTopic(TEST_TITLE, TEST_MESSAGE);
    HttpPost httpPost = httpClientArguments.getValue();
    assertEquals(httpPost.getURI().toString(), TEST_ENDPOINT);
  }

  @Test
  public void testSendNotificationContentType()
      throws ParseException, IOException {
    subject.sendNotificationToTopic(TEST_TITLE, TEST_MESSAGE);
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost httpPost = httpClientArguments.getValue();
    JsonNode notification = objectMapper.readTree(EntityUtils.toString(httpPost.getEntity()));
    assertEquals(notification.get("contentType").asText(), "application/xml");
  }

  @Test
  public void testSendNotificationWnsType()
      throws ParseException, IOException {
    subject.sendNotificationToTopic(TEST_TITLE, TEST_MESSAGE);
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost httpPost = httpClientArguments.getValue();
    JsonNode notification = objectMapper.readTree(EntityUtils.toString(httpPost.getEntity()));
    assertEquals(notification.get("wnsType").asText(), "wns/toast");
  }

  @Test
  public void testSendNotificationContent()
      throws ParseException, IOException {
    subject.sendNotificationToTopic(TEST_TITLE, TEST_MESSAGE);
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost httpPost = httpClientArguments.getValue();
    JsonNode notification = objectMapper.readTree(EntityUtils.toString(httpPost.getEntity()));

    String expectedXml = "<toast launch=\"null\"><visual>" +
                         "<binding template=\"ToastText02\">" + 
                         "<text id=\"1\">" + TEST_TITLE + "</text>" + 
                         "<text id=\"2\">" + TEST_MESSAGE + "</text></binding>" + 
                         "</visual></toast>";
    assertEquals(notification.get("content").asText(), expectedXml);
  }

  @Test
  public void testSendNotificationNonAsciiChars()
      throws ParseException, IOException {
    subject.sendNotificationToTopic("ÄÖäö", "ÄÖäö");
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost httpPost = httpClientArguments.getValue();
    JsonNode notification = objectMapper.readTree(EntityUtils.toString(httpPost.getEntity()));

    String expectedXml = "<toast launch=\"null\"><visual>" +
                         "<binding template=\"ToastText02\">" + 
                         "<text id=\"1\">&#196;&#214;&#228;&#246;</text>" + 
                         "<text id=\"2\">&#196;&#214;&#228;&#246;</text></binding>" + 
                         "</visual></toast>";
    assertEquals(expectedXml, notification.get("content").asText());
  }
}
