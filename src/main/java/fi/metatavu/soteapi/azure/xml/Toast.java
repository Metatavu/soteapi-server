package fi.metatavu.soteapi.azure.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Toast {
  public Toast() {
    super();
  }

  public Toast(Visual visual) {
    super();
    this.visual = visual;
  }

  @XmlElement
  public Visual getVisual() {
    return visual;
  }

  public void setVisual(Visual visual) {
    this.visual = visual;
  }
  
  private Visual visual;
}
