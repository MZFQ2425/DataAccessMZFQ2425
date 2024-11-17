import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XMLtoContacts extends DefaultHandler {
    private List<Contact> contacts = new ArrayList<>();
    private Contact currentContact;
    private String currentTag;
    private List<String> currentPhones = new ArrayList<>();
    private List<String> currentEmails = new ArrayList<>();

    public List<Contact> getContacts() {
        return contacts;
    }

    @Override
    //function to start reading each element from the XML
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTag = qName;

        //if is contact, create new instance of contact with empty values (but id)
        if (qName.equalsIgnoreCase("contact")) {
            currentContact = new Contact(attributes.getValue("id"), "", "", new ArrayList<>(),  new ArrayList<>());
        }
    }

    @Override
    //set value of contacts iterating the XML
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length).trim();

        if (!content.isEmpty()) {
            switch (currentTag) {
                case "name":
                    currentContact.name = content;
                    break;
                case "surname":
                    currentContact.surname = content;
                    break;
                case "cell":
                case "home":
                case "work":
                    currentPhones.add(content);
                    break;
                case "email":
                    currentEmails.add(content);
                    break;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("contact")) {
            currentContact.phones = currentPhones;
            currentContact.emails = currentEmails;
            contacts.add(currentContact);

            currentPhones = new ArrayList<>();
            currentEmails = new ArrayList<>();
        }
    }
}
