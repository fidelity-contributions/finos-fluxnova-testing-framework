package org.finos.fluxnova.bpm.test.mockito.function;


import jakarta.annotation.Nonnull;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URL;
import java.util.function.Function;


/**
 * Return DOM document for given resource.
 *
 * @author Jan Galinski, Holisticon AG
 */
public class ReadXmlDocumentFromResource implements Function<URL, Document> {

  public static Function<Document, String> TO_STRING = new Function<Document, String>() {
    @Nonnull
    @Override
    public String apply(final Document document) {
      try {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        final StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.getBuffer().toString();
      } catch (javax.xml.transform.TransformerException e) {
        throw new RuntimeException(e);
      }

    }
  };

  private final DocumentBuilderFactory factory;

  public ReadXmlDocumentFromResource() {
    this.factory =  DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
  }

  @Override
  public Document apply(final URL xmlResource) {
    try {
      final Document document = factory.newDocumentBuilder().parse(xmlResource.openStream());
      document.getDocumentElement().normalize();

      return document;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}

