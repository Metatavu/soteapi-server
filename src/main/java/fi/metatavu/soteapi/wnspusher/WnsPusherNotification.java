package fi.metatavu.soteapi.wnspusher;

public class WnsPusherNotification {
  public WnsPusherNotification() {
  }
  
  public WnsPusherNotification(String app,
                               String content,
                               String contentType,
                               String wnsType) {
    super();
    this.app = app;
    this.content = content;
    this.contentType = contentType;
    this.wnsType = wnsType;
  }

  public String getApp() {
    return app;
  }
  public void setApp(String app) {
    this.app = app;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getContentType() {
    return contentType;
  }
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
  public String getWnsType() {
    return wnsType;
  }
  public void setWnsType(String wnsType) {
    this.wnsType = wnsType;
  }

  private String app;
  private String content;
  private String contentType;
  private String wnsType;
}
