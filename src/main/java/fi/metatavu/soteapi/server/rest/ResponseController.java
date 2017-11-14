package fi.metatavu.soteapi.server.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;

@ApplicationScoped
public class ResponseController {

  private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
  private static final String FAILED_TO_STREAM_DATA_TO_CLIENT = "Failed to stream data to client";
  private static final String UNSUPPORTED_ENCODING = "Unsupported encoding";
  
  @Inject
  private Logger logger;
  
  /**
   * Creates streamed response from string using a UTF-8 encoding
   * 
   * @param data data
   * @param type content type
   * @return Response
   */
  public Response streamResponse(String data, String type) {
    return streamResponse(data, "UTF-8", type);
  }
  
  /**
   * Creates streamed response from string using specified encoding
   * 
   * @param data data
   * @param type content type
   * @return Response
   */
  public Response streamResponse(String data, String charsetName, String type) {
    try {
      return streamResponse(data.getBytes(charsetName), type);
    } catch (UnsupportedEncodingException e) {
      logger.error(UNSUPPORTED_ENCODING, e);
      return Response.status(Status.INTERNAL_SERVER_ERROR)
        .entity(INTERNAL_SERVER_ERROR)
        .build();
    }
  }
  
  /**
   * Creates streamed response from byte array
   * 
   * @param data data
   * @param type content type
   * @return Response
   */
  public Response streamResponse(byte[] data, String type) {
    try (InputStream byteStream = new ByteArrayInputStream(data)) {
      return streamResponse(type, byteStream, data.length);
    } catch (IOException e) {
      logger.error(FAILED_TO_STREAM_DATA_TO_CLIENT, e);
      return Response.status(Status.INTERNAL_SERVER_ERROR)
        .entity(INTERNAL_SERVER_ERROR)
        .build();
    }
  }

  /**
   * Creates streamed response from input stream
   * 
   * @param inputStream data
   * @param type content type
   * @param contentLength content length
   * @return Response
   */
  public Response streamResponse(String type, InputStream inputStream, int contentLength) {
    return Response.ok(new Stream(inputStream), type)
      .header("Content-Length", contentLength)
      .build();
  }
  
  private class Stream implements StreamingOutput {
    
    private InputStream inputStream;
    
    public Stream(InputStream inputStream) {
      this.inputStream = inputStream;
    }

    @Override
    public void write(OutputStream output) throws IOException {
      byte[] buffer = new byte[1024 * 100];
      int bytesRead;
      
      while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
        output.write(buffer, 0, bytesRead);
        output.flush();
      }
      
      output.flush();
    }
    
  }

  /**
   * Responds with ok 200
   * 
   * @param entity response entity
   * @return response
   */
  public Response respondOk(Object entity) {
    return Response.ok(entity).build();
  }

  /**
   * Responds with ok 200 or 404 if given entity is null
   * 
   * @param entity response entity
   * @return response
   */
  public Response respondOkOrNotFound(Object entity) {
    if (entity == null) {
      return respondNotFound();
    }
    
    return respondOk(entity);
  }

  /**
   * Responds with not found 404
   * 
   * @return response
   */
  public Response respondNotFound() {
    return Response.status(Response.Status.NOT_FOUND).build();
  }

  /**
   * Responds with not found 410
   * 
   * @return response
   */
  public Response responseGone() {
    return Response.status(Response.Status.GONE).build();
  }
  
  /**
   * Responds with no content 204
   * 
   * @return response
   */
  public Response respondNoContent() {
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  /**
   * Respond with bad request 400
   * 
   * @param message message
   * @return
   */
  public Response respondBadRequest(String message) {
    return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
  }

}
