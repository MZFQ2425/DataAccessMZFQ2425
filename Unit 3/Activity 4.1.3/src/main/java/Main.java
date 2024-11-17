import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/VTInstitute";
            String user = "postgres";
            String password = "admin";
            Connection con = DriverManager.getConnection(url, user,
                    password);
            Statement statement = con.createStatement();
            String SQLsentence = "ALTER TABLE IF EXISTS public.\"Subjects\" ADD COLUMN \"Hours\" integer NOT NULL DEFAULT 100;";
             statement.executeUpdate(SQLsentence);
            System.out.println("Table altered successfully!");
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't alter the target table");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Couldn't alter the target table");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}