import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // 1 - XML to .obj

            // XML to Object (contacts list)
            List<Contact> contacts = XMLParse.parseXMLToContacts("files\\contacts.xml");
            //save list of contacts to .obj file
            ContactToObject.saveContactsToFile(contacts, "files\\contacts.obj");

            // 2- .obj to XML
            // read from .obj to list of contact
            List<Contact> loadedContacts = ContactToObject.loadContactsFromFile("files\\contacts.obj");

            // change the .obj to a output XML
            convertContactsToXML(loadedContacts, "files\\contacts_output.xml");

        } catch (IOException | ClassNotFoundException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static void convertContactsToXML(List<Contact> contacts, String xmlFilename) {
        try {
            // create a xml file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // craete the root contactlist on XML
            Element rootElement = doc.createElement("contactlist");
            doc.appendChild(rootElement);

            //read all constacts from the list
            for (Contact contact : contacts) {
                // create node contact
                Element contactElement = doc.createElement("contact");
                contactElement.setAttribute("id", contact.getId());

                // add name
                Element nameElement = doc.createElement("name");
                nameElement.appendChild(doc.createTextNode(contact.getName()));
                contactElement.appendChild(nameElement);

                //add surname
                Element surnameElement = doc.createElement("surname");
                surnameElement.appendChild(doc.createTextNode(contact.getSurname()));
                contactElement.appendChild(surnameElement);

                // add emails
                Element emailsElement = doc.createElement("emails");
                for (String email : contact.getEmails()) {
                    Element emailElement = doc.createElement("email");
                    emailElement.appendChild(doc.createTextNode(email));
                    emailsElement.appendChild(emailElement);
                }
                contactElement.appendChild(emailsElement);

                // add phones
                Element phonesElement = doc.createElement("phones");
                for (String phone : contact.getPhones()) {
                    Element phoneElement = doc.createElement("phone");
                    phoneElement.appendChild(doc.createTextNode(phone));
                    phonesElement.appendChild(phoneElement);
                }
                contactElement.appendChild(phonesElement);

                // add the now full contact node to the root
                rootElement.appendChild(contactElement);
            }

            // create the XML
            FileOutputStream fileOutputStream = new FileOutputStream(new File(xmlFilename));

            // parse object doc to XML output
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(fileOutputStream));
            fileOutputStream.close();

            System.out.println("XML successfully generated: " + xmlFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
