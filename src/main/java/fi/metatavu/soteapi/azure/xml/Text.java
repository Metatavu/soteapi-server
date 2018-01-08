package fi.metatavu.soteapi.azure.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class Text {
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

  String id = null;
  String content = null;
}
