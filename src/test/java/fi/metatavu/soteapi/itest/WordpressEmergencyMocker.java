package fi.metatavu.soteapi.itest;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;

import fi.metatavu.soteapi.wordpress.tasks.emergency.EmergencyCongestionStatusRestModel;

/**
 * Mocker that is used to mock post menu order Wordpress plugin results
 * 
 * @author Antti Leppä
 */
public class WordpressEmergencyMocker extends AbstractMocker {
  
  @SuppressWarnings ("squid:S1075")
  private static final String PATH = "/wp-json/";
  
  private MappingBuilder mapping;
  
  /**
   * Changes mocked status endpoint return value
   * 
   * @param modified modified
   * @param value value
   * @return self
   * @throws JsonProcessingException thrown when JSON serialization fails
   */
  public WordpressEmergencyMocker mockValue(OffsetDateTime modified, Integer value) throws JsonProcessingException {
    if (isMocking() && mapping != null) {
      WireMock.removeStub(mapping);
    }
    
    EmergencyCongestionStatusRestModel resource = new EmergencyCongestionStatusRestModel();
    resource.setModified(modified);
    resource.setValue(value);
    
    mapping = get(urlPathEqualTo(PATH))
      .withQueryParam("rest_route", WireMock.equalTo("/emergency/congestion/status"))  
      .willReturn(createOkGetReturn(resource));
    
    if (isMocking()) {
      WireMock.stubFor(mapping);
    }
    
    return this;
  }
  
  @Override
  public void clear() {
    if (mapping != null) {
      WireMock.removeStub(mapping);
    }
    
    mapping = null;
  }
  
  @Override
  public void startMock() {
    super.startMock();
    
    if (mapping != null) {
      WireMock.stubFor(mapping);
    }
  }
  
  @Override
  public void endMock() {
    if (mapping != null) {
      WireMock.removeStub(mapping);
    }
    
    super.endMock();
  }

}
