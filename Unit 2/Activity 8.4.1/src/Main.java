import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args)throws ParserConfigurationException,
            SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.
                newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document doc = loader.parse("files//contacts.xml");

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("contact");

        for ( int i = 0; i < nList.getLength(); i++ ) {
            Node nNode = nList.item(i);
            if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {
                Element elem = (Element) nNode;
                Node node1 = elem.getElementsByTagName("name").item(0);
                String name = node1.getTextContent();
                Node node2 = elem.getElementsByTagName("surname").item(0);
                String surname = node2.getTextContent();

                String phone = "";

                System.out.printf("\nFullname: "+name + " " + surname);

                Element phonesElement = (Element) elem.getElementsByTagName("phones").item(0);
                NodeList allElements = phonesElement.getElementsByTagName("*");

                int size = allElements.getLength();

                if(size>0){
                    if(phonesElement.getElementsByTagName("cell").item(0) !=null){
                        phone = phonesElement.getElementsByTagName("cell").item(0).getTextContent();
                    }else if (phonesElement.getElementsByTagName("work").item(0) !=null){
                        phone = phonesElement.getElementsByTagName("work").item(0).getTextContent();
                    }else if (phonesElement.getElementsByTagName("home").item(0) !=null){
                        phone = phonesElement.getElementsByTagName("home").item(0).getTextContent();
                    }
                    System.out.printf("\nPhone: "+phone);
                }else{
                    System.out.println("\n! This contact has no phone info");
                }

                System.out.printf("\n--------------");
            }
        }
    }
}