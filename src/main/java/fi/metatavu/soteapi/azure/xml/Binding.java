package fi.metatavu.soteapi.azure.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class Binding {
  public Binding() {
    super();
  }
  
  public Binding(String template, Text text) {
    super();
    this.template = template;
    this.text = text;
  }

  @XmlElement
  public Text getText() {
    return text;
  }

  public void setText(Text text) {
    this.text = text;
  }

  @XmlAttribute
  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  private String template = null;
  private Text text = null;
}
