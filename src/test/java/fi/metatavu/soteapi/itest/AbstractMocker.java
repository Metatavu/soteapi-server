package fi.metatavu.soteapi.itest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;

/**
 * Abstract base class for all WireMock mockers
 * 
 * @author Antti Leppä
 */
public abstract class AbstractMocker {

  protected static final String APPLICATION_JSON = "application/json";
  protected static final String CONTENT_SIZE = "Content-Size";
  protected static final String CONTENT_TYPE = "Content-Type";

  private boolean mocking = false;
  
  /**
   * Resets mocker state
   */
  public abstract void clear();

  /**
   * Starts mocking
   */
  public void startMock() {
    mocking = true;
  }
  
  /**
   * Ends mocking
   */
  public void endMock() {
    mocking = false;
  }

  /**
   * Clears mocker and ends mocking
   */
  public void clearAndEnd() {
    clear();
    endMock();
  }
  
  /**
   * Returns whether mocker is mocking or not
   * 
   * @return whether mocker is mocking or not
   */
  public boolean isMocking() {
    return mocking;
  }

  /**
   * Serializes object into JSON string
   * 
   * @param object object
   * @return serialized JSON string
   * @throws JsonProcessingException thrown when serialization fails
   */
  protected String toJSON(Object object) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper.writeValueAsString(object);
  }

  /**
   * Creates successful JSON GET response
   * 
   * @param resource resource
   * @return response
   * @throws JsonProcessingException thrown when JSON serialization fails
   */
  protected ResponseDefinitionBuilder createOkGetReturn(Object resource) throws JsonProcessingException {
    return aResponse()
      .withHeader(CONTENT_TYPE, APPLICATION_JSON)
      .withBody(toJSON(resource));
  }

}
