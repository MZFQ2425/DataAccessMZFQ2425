import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/VTInstitute";
            String user = "postgres";
            String password = "admin";
            con = DriverManager.getConnection(url, user,  password);

            pstmt = con.prepareStatement("CREATE TABLE public.\"Courses\"" +
                    "(\"Code\" serial NOT NULL," +
                    "\"Name\" character varying(90) NOT NULL," +
                    "PRIMARY KEY (\"Code\"));");

            pstmt.executeUpdate();

            System.out.println("Table created successfully!");

            //inserts
            pstmt = con.prepareStatement("INSERT INTO public.\"Courses\" (\"Name\") VALUES (?)");
            pstmt.setString( 1, "MULTIPLATFORM APP DEVELOPMENT" );
            int i = pstmt.executeUpdate();
            if(i>0){
                System.out.println("Data inserted successfully, rows affected: "+i);
            }else{
                System.out.println("Couldn't insert data");
                return;
            }

            pstmt = con.prepareStatement("INSERT INTO public.\"Courses\" (\"Name\") VALUES (?)");
            pstmt.setString( 1, "WEB DEVELOPMENT" );
            i = pstmt.executeUpdate();
            if(i>0){
                System.out.println("Data inserted successfully, rows affected: "+i);
            }else{
                System.out.println("Couldn't insert data");
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