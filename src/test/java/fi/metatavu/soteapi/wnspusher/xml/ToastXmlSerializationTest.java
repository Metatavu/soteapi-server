package fi.metatavu.soteapi.wnspusher.xml;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

public class ToastXmlSerializationTest {
  
  @Test
  public void testToastText01XmlSerialization()
      throws JAXBException {
    String expected = "<toast launch=\"launchArgs\"><visual>" +
                      "<binding template=\"ToastText01\">" + 
                      "<text id=\"1\">Toast text</text></binding>" + 
                      "</visual></toast>";
    Toast subject = new Toast(
        new Visual(
            new Binding("ToastText01",
                Arrays.asList(new Text("1", "Toast text")))),
        "launchArgs");
    
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

  @Test
  public void testToastText02XmlSerialization()
      throws JAXBException {
    String expected = "<toast launch=\"launchArgs\"><visual>" +
                      "<binding template=\"ToastText02\">" + 
                      "<text id=\"1\">Toast title</text>" + 
                      "<text id=\"2\">Toast text</text></binding>" + 
                      "</visual></toast>";
    Toast subject = new Toast(
        new Visual(
            new Binding("ToastText02",
                Arrays.asList(
                    new Text("1", "Toast title"),
                    new Text("2", "Toast text")))),
        "launchArgs");
    
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
