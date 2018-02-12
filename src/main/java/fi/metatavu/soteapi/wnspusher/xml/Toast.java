package fi.metatavu.soteapi.wnspusher.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Toast {
  public Toast() {
    super();
  }

  public Toast(Visual visual, String launch) {
    super();
    this.visual = visual;
    this.launch = launch;
  }

  @XmlElement
  public Visual getVisual() {
    return visual;
  }

  public void setVisual(Visual visual) {
    this.visual = visual;
  }
  
  @XmlAttribute
  public String getLaunch() {
    return launch;
  }
  
  public void setLaunch(String launch) {
    this.launch = launch;
  }
  
  private Visual visual;
  private String launch;
}
