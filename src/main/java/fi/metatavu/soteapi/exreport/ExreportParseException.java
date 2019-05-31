package fi.metatavu.soteapi.exreport;

/**
 * Exception for Exreport parsing failures
 * 
 * @author Antti Leppä
 */
public class ExreportParseException extends Exception {
  
  private static final long serialVersionUID = -1609783333930728292L;

  public ExreportParseException(Throwable cause) {
    super(cause);
  }
  
  public ExreportParseException(String message) {
    super(message);
  }

}
