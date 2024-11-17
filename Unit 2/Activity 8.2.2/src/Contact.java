import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable {

    private String id;
    String name;
    String surname;
    List<String> emails;
    List<String> phones;

    public Contact(String id, String name, String surname, List<String> emails, List<String> phones) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.emails = emails;
        this.phones = phones;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<String> getEmails() {
        return emails;
    }

    public List<String> getPhones() {
        return phones;
    }
}
