package fi.metatavu.soteapi.wnspusher;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import fi.metatavu.soteapi.settings.SystemSettingController;

public class WnsPusherNotificationControllerTest {
  SystemSettingController systemSettingController;
  Logger logger;
  CloseableHttpClient httpClient;
  CloseableHttpResponse httpResponse;
  WnsPusherNotificationController subject;

  @Before
  void setUp() throws ClientProtocolException, IOException {
    systemSettingController = Mockito.mock(SystemSettingController.class);
    logger = Mockito.mock(Logger.class);
    httpClient = Mockito.mock(CloseableHttpClient.class);
    httpResponse = Mockito.mock(CloseableHttpResponse.class);

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
      .when(httpClient.execute(Mockito.any()))
      .thenReturn(httpResponse);
    
    StatusLine statusLine = Mockito.mock(StatusLine.class);
    Mockito.when(statusLine.getStatusCode()).thenReturn(200);
    Mockito
      .when(httpResponse.getStatusLine())
      .thenReturn(statusLine);
  }

  @Test
  public void testCorrectEndpoint() {
  }
}
