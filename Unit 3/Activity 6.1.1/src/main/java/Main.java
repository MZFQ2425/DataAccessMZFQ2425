import java.sql.*;
import java.util.Scanner;

public class Main {
    static String name = "";
    static int year = 0;
    static int hours = 0;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        boolean stop = false;

        while(!stop) {

            validateName();
            validateYear();
            validateHours();

            insertData(name, year, hours);

            String shouldExit = "";
            while(!shouldExit.equalsIgnoreCase("y") && !shouldExit.equalsIgnoreCase("n")){
                System.out.println("Would you like to stop? (y/n)");
                shouldExit =  scanner.nextLine();
            }
            if(shouldExit.equalsIgnoreCase("y")){
                System.out.println("Goodbye");
                stop = true;
            }else{
                name = "";
                year = 0;
                hours = 0;
            }
        }
    }

    public static void validateName(){
        System.out.println("To insert data into the [SUBJECTS] table, please, begin introducing the name of the subject:");
        name = scanner.nextLine();
        boolean valid = false;

        while(!valid){
            if(name.isEmpty()){
                System.out.println("Please, write a valid name");
            }else if(name.length()>50){
                System.out.println("The name can be 50 characters max, yours is "+name.length());
            }else{
                break;
            }
            name = scanner.nextLine();
        }
    }

    public static void validateYear(){
        System.out.println("Please, specify the year in which this subject is taught:");
        String yearS = scanner.nextLine();
        boolean valid = false;

        while(!valid){
            if(yearS.isEmpty()){
                System.out.println("Please, write a valid year");
            }

            try{
                year = Integer.valueOf(yearS);
                if(year<=0 || year >2){
                    System.out.println("The year can either be 1 or 2");
                    year = 0;
                }else{
                    break;
                }
            }catch(NumberFormatException e){
                System.out.println("Please, write a valid year");
                year = 0;
            }
            yearS = scanner.nextLine();
        }
    }

    public static void validateHours(){
        System.out.println("Please, specify the hours taught annually of this new subject:");
        String hourString = scanner.nextLine();
        boolean valid = false;

        while(!valid){
            if(hourString.isEmpty()){
                System.out.println("Please, write a valid hour");
            }

            try{
                hours = Integer.valueOf(hourString);
                break;
            }catch(NumberFormatException e){
                System.out.println("Please, write a valid hour");
                hours = 0;
            }
            hourString = scanner.nextLine();
        }
    }

    public static boolean insertData(String name, int year, int hours){

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/VTInstitute";
            String user = "postgres";
            String password = "admin";
            con = DriverManager.getConnection(url, user,  password);

            pstmt = con.prepareStatement("INSERT INTO public.\"Subjects\" (\"Name\", \"Year\", \"Hours\") VALUES (?, ?, ?)");
            pstmt.setString( 1, name );
            pstmt.setInt( 2, year );
            pstmt.setInt( 3, hours );
            int i = pstmt.executeUpdate();
            if(i>0){
                System.out.println("Data inserted successfully, rows affected: "+i);
                return true;
            }else{
                System.out.println("Couldn't insert data");
                return false;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load PostgreSQL.");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Connection couldn't be established");
            throw new RuntimeException(e);
        }finally{
            if(con!=null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Connection couldn't be closed since it didn't properly connect");
                    throw new RuntimeException(e);
                }
            }

            if(pstmt!=null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Error on creation the statement");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}