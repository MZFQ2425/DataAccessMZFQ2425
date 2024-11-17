import java.io.*;
import java.util.List;

public class ContactToObject {

    //function to seralize the list of contacts (object) to a file
    public static void saveContactsToFile(List<Contact> contacts, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(contacts);
        }
    }

    //functions to get the contact list from a file
    public static List<Contact> loadContactsFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Contact>) ois.readObject();
        }
    }
}
