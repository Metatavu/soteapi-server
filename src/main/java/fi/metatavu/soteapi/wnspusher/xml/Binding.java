package fi.metatavu.soteapi.wnspusher.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class Binding {
  public Binding() {
    super();
  }
  
  public Binding(String template, List<Text> texts) {
    super();
    this.template = template;
    this.texts = texts;
  }

  @XmlElement(name="text")
  public List<Text> getTexts() {
    return texts;
  }

  public void setText(List<Text> texts) {
    this.texts = texts;
  }

  @XmlAttribute
  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  private String template = null;
  private List<Text> texts = null;
}
