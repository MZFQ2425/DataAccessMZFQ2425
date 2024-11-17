import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XMLParse {

    //function tio parse the xml contacts to a list of objects
    public static List<Contact> parseXMLToContacts(String xmlFilename) throws SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        try {
            saxParser = factory.newSAXParser();
            XMLtoContacts handler = new XMLtoContacts();

            saxParser.parse(new File(xmlFilename), handler);
            return handler.getContacts();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
