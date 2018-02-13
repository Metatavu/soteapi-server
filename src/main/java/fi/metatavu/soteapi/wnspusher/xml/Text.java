package fi.metatavu.soteapi.wnspusher.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class Text {
  private String id = null;
  private String content = null;

  public Text() {
    super();
  }
  
  public Text(String id, String content) {
    super();
    this.id = id;
    this.content = content;
  }

  @XmlAttribute
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  @XmlValue
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
