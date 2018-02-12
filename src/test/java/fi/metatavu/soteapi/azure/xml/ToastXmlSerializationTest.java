package fi.metatavu.soteapi.azure.xml;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import fi.metatavu.soteapi.wnspusher.xml.Binding;
import fi.metatavu.soteapi.wnspusher.xml.Text;
import fi.metatavu.soteapi.wnspusher.xml.Toast;
import fi.metatavu.soteapi.wnspusher.xml.Visual;

public class ToastXmlSerializationTest {
  
  @Test
  public void testToastXmlSerialization()
      throws JAXBException {
    String expected = "<toast><visual><binding template=\"ToastText01\">" + 
                      "<text id=\"1\">Toast text</text></binding>" + 
                      "</visual></toast>";
    Toast subject = new Toast(
        new Visual(
            new Binding("ToastText01",
                new Text("1", "Toast text"))));
    
    JAXBContext jaxbContext = JAXBContext.newInstance(
        Toast.class,
        Visual.class,
        Binding.class,
        Text.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    StringWriter writer = new StringWriter();
    jaxbMarshaller.marshal(subject, writer);
    String actual = writer.toString();

    assertEquals(expected, actual);
  }

}
