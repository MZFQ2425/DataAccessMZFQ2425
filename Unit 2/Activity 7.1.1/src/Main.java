import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Contact> contactList = new ArrayList<Contact>();

    public static void main(String[] args) {
        File contacts = new File("contacts.obj");

        //Create the file if it doesn't exist
        if(!contacts.exists()){
            try {
                contacts.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        readObj();
        int operation = 0;
        do {

            operation = Integer.valueOf(readMenuOption());
            switch (operation) {
                case 1:
                    //create new contact
                    Contact c = addContact();
                    writeContactToFile(c);
                    break;
                case 2:
                    //show all contacts
                    if (contactList.size() > 0) {
                        showContacts();
                    } else {
                        System.out.println("There's no contacts on the list yet");
                    }
                    break;
                case 3:
                    System.out.println("If you'd like to search by full name introduce 'n'");
                    System.out.println("If you'd like to search by phone number introduce 'p'");
                    String answer = scanner.nextLine();

                    while (!answer.equalsIgnoreCase("n") && !answer.equalsIgnoreCase("p")) {
                        System.out.println("Please, write a correct type of search");
                        answer = scanner.nextLine();
                    }

                    String match = "";
                    boolean isOk = false;

                    if (answer.equalsIgnoreCase("n")) {
                        while(!isOk){
                            System.out.println("Please, enter the word you'd like to search on the contact's full name: ");
                            match = scanner.nextLine();
                            isOk = validationVar (match, "description", false);
                        }
                        searchBy(answer, match);
                    } else if (answer.equalsIgnoreCase("p")) {
                        while(!isOk){
                            System.out.println("Please, enter the exact phone number you'd like to search");
                            match = scanner.nextLine();
                            isOk = validationVar (match, "description", true);
                        }
                        searchBy(answer, match);
                    }

                    break;
                case 4:
                    //exit
                    System.out.println("Bye!");
                    break;
            }
        }while (operation != 4);
    }
    public static void readObj(){
        File file = new File("contacts.obj");

        if (file.length() != 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("contacts.obj"))) {
                contactList = (ArrayList<Contact>) in.readObject();
            } catch (FileNotFoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String readMenuOption(){
        System.out.println("What would you like to do? Type a number to do an operation:");
        System.out.println("1 - Add a new contact");
        System.out.println("2 - Show contact list");
        System.out.println("3 - Search a contact");
        System.out.println("4 - Exit");
        String operation = scanner.nextLine();
        boolean isOk = false;
        while(!isOk){
            try {
                int res = Integer.valueOf(operation);
                if(res>0 && res <5){
                    break;
                }else{
                    isOk = false;
                }
            }catch(Exception e){
                isOk = false;
            }
            System.out.println("Please, enter a valid operation");
            operation = scanner.nextLine();
        }

        return operation;
    }

    public static boolean validationVar (String stringToCheck, String nameOfVar, boolean isNumber){
        boolean isOk = false;

        if(!stringToCheck.isEmpty()){
            if(isNumber){
                try{
                    int value = Integer.valueOf(stringToCheck);
                    isOk = true;
                }catch (Exception e){
                    //couldn't parse the string to number
                    isOk = false;
                }
            }else{
                isOk = true;
            }
        }else{
            isOk = false;
            System.out.println("Please, write a valid "+nameOfVar);
        }

        return isOk;
    }

    public static void showContacts(){
        System.out.println("Here's the current list of contacts");
        System.out.println("---------");
        for (Contact contact : contactList) {
            System.out.println("Full name: "+contact.getName()+" "+contact.getSurname());
            System.out.println("E-mail: "+contact.getMail());
            System.out.println("Phone: "+contact.getPhone());
            System.out.println("Description: "+contact.getDescription());
            System.out.println("---------");
        }
    }

    public static Contact addContact(){
        String name = "";
        String surname = "";
        String mail = "";
        String phone = "";
        String description = "";

        boolean bolName = false;
        boolean bolSur = false;
        boolean bolMail = false;
        boolean bolPho = false;
        boolean bolDesc = false;

        while(!bolName){
            System.out.println("Please, enter the name of the contact: ");
            name = scanner.nextLine();
            bolName = validationVar (name, "name", false);
        }

        while(!bolSur){
            System.out.println("Please, enter the surname of the contact: ");
            surname = scanner.nextLine();
            bolSur = validationVar (surname, "surname", false);
        }

        while(!bolMail){
            System.out.println("Please, enter the e-mail of the contact: ");
            mail = scanner.nextLine();
            if(mail.contains("@")){
                bolMail = validationVar (mail, "e-mail", false);
            }else{
                System.out.println("Please, write a valid  e-mail");
            }
        }

        while(!bolPho){
            System.out.println("Please, enter the phone number of the contact: ");
            phone = scanner.nextLine();
            bolPho = validationVar (phone, "phone-number", true);
        }

        while(!bolDesc){
            System.out.println("Please, enter the description of the contact: ");
            description = scanner.nextLine();
            bolDesc = validationVar (description, "description", false);
        }

        Contact c = new Contact(name, surname, mail, Integer.valueOf(phone), description);

        return c;
    }

    public static void writeContactToFile(Contact c){
        contactList.add(c);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("contacts.obj"))) {
            out.writeObject(contactList);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public static void searchBy (String typeOf, String match){
        BufferedReader bf = null;
        try {
            if(contactList.size()>0) {
                int total = 0;
                System.out.println("---------");
                for (Contact c : contactList) {
                    boolean write = false;

                    if (typeOf.equalsIgnoreCase("n")) {
                        if (c.getName().contains(match) || c.getSurname().contains(match)) {
                            write = true;
                            total++;
                        }
                    } else if (typeOf.equalsIgnoreCase("p")) {
                        if (c.getPhone() == Integer.valueOf(match)) {
                            write = true;
                            total++;
                        }
                    }

                    if(write){
                        System.out.println("Full name: "+c.getName()+" "+c.getSurname());
                        System.out.println("E-mail: "+c.getMail());
                        System.out.println("Phone: "+c.getPhone());
                        System.out.println("Description: "+c.getDescription());
                        System.out.println("---------");

                        total++;
                    }
                }
                if(total==0){
                    System.out.println("No matches found");
                }
            }else{
                System.out.println("There's no contacts on the list to search from");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}