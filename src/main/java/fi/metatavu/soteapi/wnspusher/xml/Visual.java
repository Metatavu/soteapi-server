package fi.metatavu.soteapi.wnspusher.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class Visual {
  public Visual() {
    super();
  }

  public Visual(Binding binding) {
    super();
    this.binding = binding;
  }

  @XmlElement
  public Binding getBinding() {
    return binding;
  }

  public void setBinding(Binding visual) {
    this.binding = visual;
  }
  
  private Binding binding;
}
