import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        boolean stop = false;
        String search = "";

        while(!stop) {

            int operation = createMenu();

            switch(operation){
                case 1:
                    search = askForCorrectAnswer("Enter the job title to search for employees:", 9);
                    callProcedure("show_employees_by_job", search.toUpperCase());
                    break;
                case 2:
                    search = askForCorrectAnswer("Enter the department to search for employees:", 14);
                    callProcedure("show_employees_by_dept", search.toUpperCase());
                    break;
                case 3:
                    search = askForCorrectAnswer("Enter the word or letter to search in employees' names:", 10);
                    callProcedure("show_employees_by_pattern", search.toUpperCase());
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    stop = true;
                    break;
            }

            if(!stop) {
                String shouldExit = "";
                while (!shouldExit.equalsIgnoreCase("y") && !shouldExit.equalsIgnoreCase("n")) {
                    System.out.println("Would you like to stop? (y/n)");
                    shouldExit = scanner.nextLine();
                }
                if (shouldExit.equalsIgnoreCase("y")) {
                    System.out.println("Goodbye!");
                    stop = true;
                }
            }
        }
    }

    public static int createMenu(){
        while (true) {
            System.out.println("What would you like to do? Please, write the operation");
            System.out.println("1 - Search employees by specific job");
            System.out.println("2 - Search employees by specific department");
            System.out.println("3 - Search employees by a pattern on their names");
            System.out.println("4 - Exit");

            String answer = scanner.nextLine();

            if(!answer.isEmpty() || answer.trim().length()<=0){
                try {
                    int intAnswer = Integer.valueOf(answer);
                    if(intAnswer<0 || intAnswer>4){
                        System.out.println("Please, write a valid operation");
                    }else{
                        return intAnswer;
                    }
                } catch (NumberFormatException e){
                    System.out.println("Please, write a valid operation");
                }
            }
        }
    }

    public static boolean callProcedure(String procedureName, String match){
        Connection con = null;
        CallableStatement statement =  null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Employees";
            String user = "postgres";
            String password = "admin";
            con = DriverManager.getConnection(url, user,  password);

            System.out.println("----------------------");
            statement = con.prepareCall( "{call "+procedureName+"('"+match+"')}" );
            rs = statement.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt(1) + "\t "
                        + rs.getString(2)+ "\t "
                        + rs.getString(3)+ "\t "
                        + rs.getInt(4));
            }
            System.out.println("----------------------");
            return true;
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

            if(statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println("Error on creation the CallableStatement");
                    throw new RuntimeException(e);
                }
            }

            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Error on creation the rs");
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static String askForCorrectAnswer(String prompt, int extension){
        String search = "";
        boolean isOk = false;

        while(!isOk){
            System.out.println(prompt);
            search = scanner.nextLine().toUpperCase();

            if((!search.trim().isEmpty() || search.trim().length()<0) && search.length()>extension){
                System.out.println("Please write a valid answer");
            }else{
                break;
            }
        }

        return search;
    }
}