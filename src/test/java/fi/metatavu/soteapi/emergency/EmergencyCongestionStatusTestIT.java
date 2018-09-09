package fi.metatavu.soteapi.emergency;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.Is.is;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

/**
 * Tests for emergency congestion status
 * 
 * @author Antti Leppä
 */
public class EmergencyCongestionStatusTestIT extends AbstractIntegrationTest {
  
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(getWireMockPort());
  
  @Before
  public void setupWordpress() {
    insertSystemSetting("wordpress.url", String.format("%s/wp-json/", getWireMockBasePath()));
    flushJpaCache();    
  }

  @After
  public void teardownWordpress() {
    deleteSystemSetting("wordpress.url");
    flushJpaCache();    
  }
  
  @Test
  public void testEmergencyCongestionStatusSync() throws JsonProcessingException {
    OffsetDateTime modified = OffsetDateTime.of(2018, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
    int value = 20;

    getWordpressEmergencyMocker().mockValue(modified, value);    
    
    awaitEmergencyCongestionStatusModified(modified);
    
    given()
      .baseUri(getApiBasePath())
      .contentType(io.restassured.http.ContentType.JSON)
      .get("/emergencyCongestionStatuses?maxResults=1")
      .then()
      .body("created[0]", sameInstant(modified))
      .body("value[0]", is(value));
  }

  @Test
  public void testEmergencyCongestionStatusChange() throws JsonProcessingException {
    OffsetDateTime modified1 = OffsetDateTime.of(2018, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
    int value1 = 20;

    getWordpressEmergencyMocker().mockValue(modified1, value1);    
    awaitEmergencyCongestionStatusModified(modified1);
    
    given()
      .baseUri(getApiBasePath())
      .contentType(io.restassured.http.ContentType.JSON)
      .get("/emergencyCongestionStatuses?maxResults=1")
      .then()
      .body("created[0]", sameInstant(modified1))
      .body("value[0]", is(value1));
    
    OffsetDateTime modified2 = OffsetDateTime.of(2018, 1, 2, 1, 1, 1, 1, ZoneOffset.UTC);
    int value2 = 50;

    getWordpressEmergencyMocker().mockValue(modified2, value2);    
    awaitEmergencyCongestionStatusModified(modified2);
    
    given()
      .baseUri(getApiBasePath())
      .contentType(io.restassured.http.ContentType.JSON)
      .get("/emergencyCongestionStatuses?maxResults=1")
      .then()
      .body("created[0]", sameInstant(modified2))
      .body("value[0]", is(value2));
    
  }

  /**
   * Awaits that emergency congestion status has been modified in specified time
   * 
   * @param modified modification time
   */
  private void awaitEmergencyCongestionStatusModified(OffsetDateTime modified) {
    await().atMost(1, TimeUnit.MINUTES).until(() -> {
      OffsetDateTime statusModified = getEmergencyCongestionStatusModified();
      return statusModified != null && statusModified.toEpochSecond() == modified.toEpochSecond();
    });    
  }
  
  /**
   * Returns when emergency congestion status has last time been modified
   * 
   * @return when emergency congestion status has last time been modified
   */
  private OffsetDateTime getEmergencyCongestionStatusModified() {
    String created = given()
      .baseUri(getApiBasePath())
      .contentType(io.restassured.http.ContentType.JSON)
      .get("/emergencyCongestionStatuses?maxResults=1")
      .body()
      .jsonPath()
      .getString("created[0]");
    
    if (StringUtils.isBlank(created)) {
      return null;
    }
    
    return OffsetDateTime.parse(created);
  }

  
}
