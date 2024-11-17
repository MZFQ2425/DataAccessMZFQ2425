import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static String empno = "";
    static String eName = "";
    static String job = "";
    static String dept = "";

    public static void main(String[] args) {
        boolean stop = false;

        while(!stop) {

            empno = askForInfo("Enter the number of the new employee: ", 10, true).toUpperCase();
            eName = askForInfo("Enter the name of the new employee: ", 10, false).toUpperCase();
            job = askForInfo("Enter the job of the new employee: ", 9, false).toUpperCase();
            dept = askForInfo("Enter the name of the department of the new employee: ", 14,false ).toUpperCase();

            int deptno = recoverDeptNo(dept);

            if(deptno == 0){
                String ifContinue = "";
                while (!ifContinue.equalsIgnoreCase("y") && !ifContinue.equalsIgnoreCase("n")) {
                    System.out.println("The department '"+dept+"' does not exist, would you like to create it? (y/n)");
                    ifContinue = scanner.nextLine();
                }
                boolean isCreated = false;
                if (ifContinue.equalsIgnoreCase("y")) {
                    int lastDeptNo = getLastDeptNo();
                    //create table, we need a location for the department
                    String location = askForInfo("Enter the location of the department '"+dept+"': ", 13, false).toUpperCase();
                    isCreated = createDept(lastDeptNo, dept, location);

                    if(isCreated){
                        createEmployee(empno, eName, job, lastDeptNo);
                    }
                }
            }else{
                //we can
                createEmployee(empno, eName, job, deptno);
            }

            String shouldExit = "";
            while (!shouldExit.equalsIgnoreCase("y") && !shouldExit.equalsIgnoreCase("n")) {
                System.out.println("Would you like to stop? (y/n)");
                shouldExit = scanner.nextLine();
            }
            if (shouldExit.equalsIgnoreCase("y")) {
                System.out.println("Goodbye!");
                stop = true;
            }else{
                empno = "";
                eName = "";
                job = "";
                dept = "";
            }
        }
    }

    public static int recoverDeptNo(String deptName){
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Employees";
            String user = "postgres";
            String password = "admin";
            con = DriverManager.getConnection(url, user,  password);

            pstmt = con.prepareStatement("SELECT * FROM public.dept WHERE dname=?;");
            pstmt.setString(1, deptName);

            rs = pstmt.executeQuery();
            int deptno = 0;

            if(rs.next()){
                deptno = rs.getInt("deptno");
            }
            return deptno;
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

            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Couldn't close resultset");
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static int getLastDeptNo(){
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Employees";
            String user = "postgres";
            String password = "admin";
            con = DriverManager.getConnection(url, user,  password);

            pstmt = con.prepareStatement("SELECT deptno FROM public.dept ORDER BY deptno DESC LIMIT 1");

            rs = pstmt.executeQuery();
            int deptno = 10;

            if(rs.next()){
                deptno = rs.getInt("deptno");
            }
            return deptno;
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

            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Couldn't close resultset");
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static boolean createDept(int deptNo, String deptName, String location){
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Employees";
            String user = "postgres";
            String password = "admin";
            con = DriverManager.getConnection(url, user,  password);
            con.setAutoCommit(false);

            pstmt = con.prepareStatement("INSERT INTO public.dept(deptno, dname, loc) VALUES (?, ?, ?);");
            pstmt.setInt(1, deptNo);
            pstmt.setString(2, deptName);
            pstmt.setString(3, location);

            int i = pstmt.executeUpdate();

            if(i>0){
                System.out.println("Department created successfully!");
                con.commit();
                return true;
            }else{
                System.out.println("There was an error while creating the department, try again later.");
                con.rollback();
                return false;
            }
        } catch (ClassNotFoundException e) {
            if(con!=null){
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println("Cannot load PostgreSQL.");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            if(con!=null){
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
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

            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Couldn't close resultset");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void createEmployee(String empno, String ename, String jobm, int deptNo){
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/Employees";
            String user = "postgres";
            String password = "admin";
            con = DriverManager.getConnection(url, user,  password);
            con.setAutoCommit(false);

            pstmt = con.prepareStatement("INSERT INTO public.employee(empno, ename, job, deptno) VALUES (?, ?, ?, ?);");
            pstmt.setInt(1, Integer.parseInt(empno));
            pstmt.setString(2, ename);
            pstmt.setString(3, jobm);
            pstmt.setInt(4, deptNo);

            int i = pstmt.executeUpdate();
            if(i>0){
                System.out.println("New employee successfully!");
            }else{
                System.out.println("The employee could not be inserted correctly, please, try again later.");
            }
            con.commit();
        } catch (ClassNotFoundException e) {
            if(con!=null){
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println("Cannot load PostgreSQL.");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            if(con!=null){
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
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

            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Couldn't close resultset");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String askForInfo(String prompt, int extension, boolean isNumber){
        String search = "";
        boolean isOk = false;

        while(!isOk){
            System.out.println(prompt);
            search = scanner.nextLine().toUpperCase();

            if((search.trim().isEmpty() || search.trim().length()<=0) || search.length()>extension){
                System.out.println("Please write a valid answer, remember this data can't be longer than "+extension+
                        " characters, yours is "+search.length()+" characters");
            }else if(isNumber){
                try{
                    int i = Integer.parseInt(search);
                    break;
                }catch (NumberFormatException e){
                    System.out.println("This data is not a valid number, please, try again");
                }
            }else{
                break;
            }
        }

        return search;
    }
}