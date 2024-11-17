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
            String SQLsentence = "INSERT INTO public.\"Subjects\" (\"Name\", \"Year\") VALUES ('MARKUP LANGUAGES',1);";
            int res = statement.executeUpdate(SQLsentence);
            if(res>0){
                System.out.println("Data inserted successfully, rows affected: "+res);
            }else{
                System.out.println("Couldn't insert data");
            }
            con.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}