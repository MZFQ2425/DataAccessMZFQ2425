import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String day = "";
        boolean isDayOk = false;
        String month = "";
        boolean isMonthOk = false;
        String year = "";
        boolean isYearOk = false;

        while(day.isEmpty() || !isDayOk){
            System.out.print("Please, enter the day of the date: ");
            day = scan.nextLine();
            isDayOk = isDayCorrect(day);
        }

        while(month.isEmpty() || !isMonthOk){
            System.out.print("Please, enter the month of the date: ");
            month = scan.nextLine();
            isMonthOk = isMonthCorrect(month);
        }

        while(year.isEmpty() || !isYearOk){
            System.out.print("Please, enter the year of the date: ");
            year = scan.nextLine();
            isYearOk = isYearCorrect(year);
        }

        System.out.print("Here's your full date: "+ day + "/" + month + "/" + year);
    }

    public static boolean isDayCorrect(String day){
        boolean isOk = false;
        try{
            int dayNum = Integer.valueOf(day);
            if(dayNum > 0 && dayNum <=31){
                isOk = true;
            }
        }catch(NumberFormatException e){
            System.out.println("Enter a valid day");
        }
        return isOk;
    }

    public static boolean isMonthCorrect(String month){
        boolean isOk = false;
        try{
            int monthNum = Integer.valueOf(month);
            if(monthNum > 0 && monthNum <=12){
                isOk = true;
            }
        }catch(NumberFormatException e){
            System.out.println("Please, enter a valid month");
        }
        return isOk;
    }

    public static boolean isYearCorrect(String year){
        boolean isOk = false;
        try{
            int yearNum = Integer.valueOf(year);
            isOk = true;
        }catch(NumberFormatException e){
            System.out.println("Please, enter a valid year");
        }
        return isOk;
    }
}