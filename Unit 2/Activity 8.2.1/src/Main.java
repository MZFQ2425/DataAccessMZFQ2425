import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
class myXMLContactsHandler extends DefaultHandler {
    protected String tagContent;
    protected String fullName;
    protected String phone = null;
    protected boolean isPhones = false;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("phones")){
            isPhones = true;
        }
    }

    public void characters( char ch[], int start, int length )
            throws SAXException {

        tagContent = new String( ch, start, length );
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if ( !qName.isBlank() ) {
            if (qName.equals("name")) {
                fullName = tagContent;
            } else if (qName.equals("surname")) {
                fullName += " "+tagContent;
            } else if ((qName.equals("cell") || qName.equals("work") || qName.equals("home")) && isPhones) {
                if(qName.equals("cell")){
                    phone = tagContent;
                }else if(qName.equals("work") && phone == null){
                    phone = tagContent;
                } else if(qName.equals("home") && phone == null){
                    phone = tagContent;
                }
            } else if (qName.equals("contact")) {
                System.out.println("Full Name: " + fullName);
                if (phone != null) {
                    System.out.println("Phone: " + phone);
                } else {
                    System.out.println("! This contact has no phone info");
                }
                System.out.println("-----");

                fullName = null;
                phone = null;
            } else if (qName.equals("phones")) {
                isPhones = false;
            }
        }
    }
}

public class Main {
    public static void main( String[] args ) {
        try {
            SAXParser saxParser = SAXParserFactory.
                    newInstance().newSAXParser();
            saxParser.parse("files//contacts.xml", new
                    myXMLContactsHandler());
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}

