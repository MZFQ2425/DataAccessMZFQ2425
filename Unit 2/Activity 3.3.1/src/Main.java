import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        String name =  "";
        while (name.isEmpty()){
            System.out.print("Please, enter your name: ");
            name = myScanner.nextLine();
        }

        String surname = "";
        while (surname.isEmpty()){
            System.out.print("Please, enter your surname: ");
            surname = myScanner.nextLine();
        }
        System.out.println("Hello, " + name+ " " + surname + "!");
        myScanner.close();
    }
}