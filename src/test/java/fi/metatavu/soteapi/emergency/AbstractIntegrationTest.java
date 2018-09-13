package fi.metatavu.soteapi.emergency;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.metatavu.soteapi.itest.AbstractMocker;
import fi.metatavu.soteapi.itest.WordpressEmergencyMocker;

/**
 * Abstract base class for integration tests
 * 
 * @author Antti Leppä
 */
public class AbstractIntegrationTest {
  
  private static Logger logger = LoggerFactory.getLogger(AbstractIntegrationTest.class);
  
  private WordpressEmergencyMocker wordpressEmergencyMocker = new WordpressEmergencyMocker();
  private List<AbstractMocker> mockers = Arrays.asList(wordpressEmergencyMocker);
  
  /**
   * Starts all mockers
   */
  @Before
  public void startMocks() {
    mockers.stream().forEach(mocker -> mocker.startMock());
  }
  
  /**
   * Ends all mockers
   */
  @After
  public void endMocks() {
    mockers.stream().forEach(mocker -> mocker.clearAndEnd());
  }
  
  /**
   * Returns Wordpress emergency mocker
   * 
   * @return Wordpress emergency mocker
   */
  protected WordpressEmergencyMocker getWordpressEmergencyMocker() {
    return wordpressEmergencyMocker;
  }

  /**
   * Flush JPA cache
   */
  protected void flushJpaCache() {
    given()
      .baseUri(getApiBasePath())
      .contentType(io.restassured.http.ContentType.JSON)
      .get("/system/jpa/cache/flush")
      .then();
  }
  
  /**
   * Returns port where integration test Wildfly is running
   * 
   * @return port where integration test Wildfly is running
   */
  protected int getHttpPort() {
    return Integer.parseInt(System.getProperty("it.port.http"));
  }

  /**
   * Returns host where integration test Wildfly is running
   * 
   * @return host where integration test Wildfly is running
   */
  protected String getHost() {
    return System.getProperty("it.host");
  }

  /**
   * Returns port where WireMock is running
   * 
   * @return port where WireMock is running
   */  
  protected int getWireMockPort() {
    return getHttpPort() + 1;
  }
  
  /**
   * Returns base path of WireMock
   * 
   * @return base path of WireMock
   */
  protected String getWireMockBasePath() {
    return String.format("http://%s:%d", getHost(), getWireMockPort());
  }

  /**
   * Returns API base path for application running in integration test mode
   * 
   * @return API base path for application running in integration test mode
   */
  protected String getApiBasePath() {
    return String.format("http://%s:%d/v1", getHost(), getHttpPort());
  }

  /**
   * Inserts a system setting
   * 
   * @param key key
   * @param value value
   * @return created id
   */
  protected long insertSystemSetting(String key, String value) {
    return executeInsert("insert into SystemSetting (settingKey, value) values (?, ?)", key, value);
  }
  
  /**
   * Removes system setting
   * 
   * @param key key
   */
  protected void deleteSystemSetting(String key) {
    executeDelete("delete from SystemSetting where settingKey = ?", key);
  }
  
  /**
   * Executes an insert statement
   * 
   * @param sql sql
   * @param params parameters
   * @return created id
   */
  protected long executeInsert(String sql, Object... params) {
    try (Connection connection = getJdbcConnection()) {
      connection.setAutoCommit(true);
      PreparedStatement statement = connection.prepareStatement(sql);
      try {
        applyStatementParams(statement, params);
        statement.execute();
        
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
          return getGeneratedKey(generatedKeys);
        }
      } finally {
        statement.close();
      }
    } catch (Exception e) {
      logger.error("Failed to execute insert", e);
      fail(e.getMessage());
    }
    
    return -1;
  }
  
  /**
   * Executes an delete statement
   * 
   * @param sql sql
   * @param params parameters
   */
  protected void executeDelete(String sql, Object... params) {
    try (Connection connection = getJdbcConnection()) {
      connection.setAutoCommit(true);
      PreparedStatement statement = connection.prepareStatement(sql);
      try {
        applyStatementParams(statement, params);
        statement.execute();
      } finally {
        statement.close();
      }
    } catch (Exception e) {
      logger.error("Failed to execute delete", e);
      fail(e.getMessage());
    }
  }

  /**
   * Matcher for matching offset date time instants
   * 
   * @param offsetDateTime offset date time
   * @return matcher
   */
  protected Matcher<Instant> sameInstant(OffsetDateTime offsetDateTime) {
    Instant instant = offsetDateTime != null ? offsetDateTime.toInstant() : null;
    return sameInstant(instant);
  }

  /**
   * Matcher for Java time instant
   * 
   * @param instant instant
   * @return matcher
   */
  @SuppressWarnings ({"squid:S1188", "squid:MethodCyclomaticComplexity"})
  protected static Matcher<Instant> sameInstant(final Instant instant) {
    
    return new BaseMatcher<Instant>(){

      @Override
      public void describeTo(Description description) {
        description.appendText("same instant: ").appendValue(instant.toString());
      }

      @Override
      public boolean matches(Object item) {
        if (item == null && instant == null) {
          return true;
        }
        
        if (item == null || instant == null) {
          return false;
        }
        
        Instant itemInstant = toInstant(item);
        if (itemInstant == null) {
          return false;
        }
        
        return itemInstant.getEpochSecond() == instant.getEpochSecond();
      }
      
      private Instant toInstant(Object item) {
        if (item instanceof String) {
          return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse((String) item));
        } else if (item instanceof Instant) {
          return (Instant) item;
        }
        
        return null;
      }
      
    };
    
  }
  
  /**
   * Applies JDBC statement parameters
   * 
   * @param statement statement
   * @param params parameters
   * @throws SQLException thrown when applying fails
   */
  private void applyStatementParams(PreparedStatement statement, Object... params)
      throws SQLException {
    for (int i = 0, l = params.length; i < l; i++) {
      Object param = params[i];
      if (param instanceof List) {
        statement.setObject(i + 1, ((List<?>) param).toArray());
      } else {
        statement.setObject(i + 1, params[i]);
      }
    }
  }
  
  /**
   * Returns generated key
   * 
   * @param generatedKeys generated keys
   * @return id
   * @throws SQLException thrown when operation fails
   */
  private long getGeneratedKey(ResultSet generatedKeys) throws SQLException {
    if (generatedKeys.next()) {
      return generatedKeys.getLong(1);
    }
    
    return -1;
  }
  
  /**
   * Returns JDBC connection
   * 
   * @return JDBC connection
   */
  private Connection getJdbcConnection() {
    String username = System.getProperty("it.jdbc.username");
    String password = System.getProperty("it.jdbc.password");
    String url = System.getProperty("it.jdbc.url");
    try {
      Class.forName(System.getProperty("it.jdbc.driver")).newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      logger.error("Failed to load JDBC driver", e);
      fail(e.getMessage());
    }

    try {
      return DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      logger.error("Failed to get connection", e);
      fail(e.getMessage());
    }
    
    return null;
  }
  
}
