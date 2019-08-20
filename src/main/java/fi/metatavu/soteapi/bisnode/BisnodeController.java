package fi.metatavu.soteapi.bisnode;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import fi.metatavu.soteapi.bisnode.model.BisnodeReview;
import fi.metatavu.soteapi.bisnode.model.BisnodeReviewContainer;
import fi.metatavu.soteapi.bisnode.model.BisnodeSurveySummary;
import fi.metatavu.soteapi.server.rest.model.SurveyQuestionSummary;

/**
 * Controller for communicating with bisnode API
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class BisnodeController {

  @Inject
  private Logger logger;

  /**
   * Gets survey question summary from Bisnode API
   * 
   * @param surveyQuestion survey question to get the summary for
   * @return Summary for provided survey question or null if not available
   */
  public SurveyQuestionSummary getSurveyQuestionSummary(String surveyName, long questionNumber) {
    BisnodeResponse response = doRequest(String.format("https://%s/api/v%s/yes-no/%s/%d", getHost(), getApiVersion(), surveyName, questionNumber));
    if (response == null) {
      return null;
    }

    try {
      BisnodeSurveySummary bisnodeSurveySummary = getObjectMapper().readValue(response.getContent(), BisnodeSurveySummary.class);
      SurveyQuestionSummary result = new SurveyQuestionSummary();
      result.setTotal(bisnodeSurveySummary.getTotal());
      result.setPositive(bisnodeSurveySummary.getYes());
      result.setNegative(bisnodeSurveySummary.getTotal() - bisnodeSurveySummary.getYes());
      return result;
    } catch (IOException e) {
      logger.error("Error parsing response from Bisnode API", e);
    }

    return null;
  }

  /**
   * Lists reviews from Bisnode API
   * 
   * @param reviewProductName review product name to find the reviews for
   * @param page page number ( 0 - indexed )
   * 
   * @return List of reviews or null if not available
   */
  public BisnodeReviewContainer listReviews(String reviewProductName, Long page) {
    BisnodeResponse response = doRequest(String.format("https://%s/api/v%s/reviews/%s?page=%s&size=%d", getHost(), getApiVersion(), reviewProductName, page, BisnodeConsts.PAGE_SIZE));
    if (response == null) {
      return null;
    }

    try {
      List<BisnodeReview> reviews = getObjectMapper().readValue(response.getContent(), new TypeReference<List<BisnodeReview>>() {});
      return new BisnodeReviewContainer(response.getTotalCount(), reviews);
    } catch (IOException e) {
      logger.error("Error parsing response from Bisnode API", e);
    }

    return null;
  }

  /**
   * Makes http request to Bisnode's API
   * 
   * @param url url to make the request to
   * 
   * @return response as string or null if unsuccessful
   */
  private BisnodeResponse doRequest(String url) {
    HttpGet httpGet = new HttpGet(url);
    httpGet.setHeader("Authorization", String.format("Bearer %s", JwtUtil.createToken(getCustomerSecret(), getIssuer(), getAudience())));
    SSLContextBuilder builder = new SSLContextBuilder();
    try {
      builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
    
      try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build()) {
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
          StatusLine statusLine = response.getStatusLine();
          
          if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
            BisnodeResponse bisnodeResponse = new BisnodeResponse();
            bisnodeResponse.setContent(EntityUtils.toString(response.getEntity()));
            
            Header totalCountHeader = response.getFirstHeader("x-total-count");
            if (totalCountHeader != null) {
              bisnodeResponse.setTotalCount(NumberUtils.createLong(totalCountHeader.getValue()));
            }
            return bisnodeResponse;
          } else {
            logger.error("Received an error [{}] {} while communicating with Bisnode. Customer secret was {}, issuer {}, audience: {}", statusLine.getStatusCode(), statusLine.getReasonPhrase(), getCustomerSecret(),  getIssuer(), getAudience());
          }
        }
      }
    } catch (NoSuchAlgorithmException | KeyStoreException | IOException | KeyManagementException e) {
      logger.error("Error connecting to bisnode API", e);
    }

    return null;
  }

  /**
   * Gets the Bisnode API version
   * 
   * @return Bisnode API version
   */
  private String getApiVersion() {
    return System.getProperty("bisnode.apiVersion", "86");
  }

  /**
   * Gets the Bisnode API host
   * 
   * @return Bisnode API host
   */
  private String getHost() {
    return System.getProperty("bisnode.host", "prs.sn4.com");
  }

  /**
   * Gets customer secret
   * 
   * @return customer secret
   */
  private String getCustomerSecret() {
    return StringEscapeUtils.unescapeHtml4(System.getProperty("bisnode.customerSecret"));
  }

  /**
   * Gets issuer
   * 
   * @return issuer
   */
  private String getIssuer() {
    return System.getProperty("bisnode.issuer", "www.essote.fi");
  }

  /**
   * Gets audience
   * 
   * @return audience
   */
  private String getAudience() {
    return System.getProperty("bisnode.audience", "prs.sn4.com");
  }

  /**
   * Creates object mapper with java time module
   * @return created object mapper
   */
  private ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  /**
   * Class for connecting data from http response
   */
  private class BisnodeResponse {

    private Long totalCount;
    private String content;

    /**
     * @return the totalCount
     */
    public Long getTotalCount() {
      return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(Long totalCount) {
      this.totalCount = totalCount;
    }

    /**
     * @return the content
     */
    public String getContent() {
      return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
      this.content = content;
    }
  }
}