import java.io.Serializable;

public class Contact implements Serializable {
    private String name;
    private String surname;
    private String mail;
    private int phone;
    private String description;

    public Contact(String name, String surname, String mail, int phone, String description){
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.phone = phone;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
