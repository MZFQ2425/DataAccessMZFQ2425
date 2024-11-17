import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String day = "";
        String month = "";
        String year = "";

        MyDate date = new MyDate();

        while(day.isEmpty()){
            System.out.print("Please, enter the day of the date: ");
            day = scan.nextLine();
            try {
                date.setDay(Integer.valueOf(day));
            } catch(Exception e){
                day = "";
            }
        }

        while(month.isEmpty()){
            System.out.print("Please, enter the month of the date: ");
            month = scan.nextLine();
            try {
                date.setMonth(Integer.valueOf(month));
            } catch(Exception e){
                month = "";
            }
        }

        while(year.isEmpty()){
            System.out.print("Please, enter the year of the date: ");
            year = scan.nextLine();
            date.setYear(Integer.valueOf(year));
        }

        System.out.print("Here's your full date: "+ day + "/" + month + "/" + year);
    }
}