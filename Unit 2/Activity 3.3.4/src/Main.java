import java.util.Scanner;

public class Main {

    static MyDate date = new MyDate();

    static String day = "";
    static String month = "";
    static String year = "";
    static boolean isLeapDate = false;
    static int newMaxDay = 31;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        askDay(scan);

        while(month.isEmpty()){
            System.out.print("Please, enter the month of the date: ");
            month = scan.nextLine();
            try {
                if(month.equals("2") && date.getDay()>29){
                    newMaxDay = 29;
                    System.out.print("You cannot enter the date "+day+"/"+month+", enter the day again\n");
                    day = "";
                    askDay(scan);
                }
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

        if(!isLeapDate() && date.getDay()==29 && date.getMonth()==2){
            System.out.print("You cannot enter the day "+day+" for the year "+year+" since it's not a leap year, please introduce again the day\n");
            newMaxDay = 28;
            day = "";
            askDay(scan);
        }

        String completeResult = "not";
        if(isLeapDate){
            completeResult = "";
        }

        System.out.print("Here's your full date: "+ date.getDay() + "/" + date.getMonth() + "/" + date.getYear()+", which it's "+completeResult+ " a leap date");
    }

    public static void askDay(Scanner scan){
        while(day.isEmpty()){
            System.out.print("Please, enter the day of the date: ");
            day = scan.nextLine();
            try {
                date.setDay(Integer.valueOf(day), newMaxDay);
            } catch(Exception e){
                day = "";
            }
        }
    }

    public static boolean isLeapDate(){
        isLeapDate = false;
        if( Integer.valueOf(year) % 4 == 0){
            if(date.getMonth()==2){
                newMaxDay = 28;
            }
            isLeapDate = true;
        }
        return isLeapDate;
    }
}
