import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Connection con = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/VTInstitute";
            String user = "postgres";
            String password = "admin";
            con = DriverManager.getConnection(url, user,  password);

            /*4.1.1*/
            String SQLsentence = "SELECT * FROM public.\"Subjects\" ORDER BY \"Code\"";
            rs = statement.executeQuery(SQLsentence);
            System.out.println("Code" + "\t" + "Name" + "\t" + "Year");
            System.out.println("-----------------------------------------");
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t " +
                        rs.getString(2)+ "\t " +
                        rs.getString(3));
            }

            /*4.1.2*/
            SQLsentence = "INSERT INTO public.\"Subjects\" (\"Name\", \"Year\") VALUES ('MARKUP LANGUAGES',1);";
            int res = statement.executeUpdate(SQLsentence);
            if(res>0){
                System.out.println("Data inserted successfully, rows affected: "+res);
            }else{
                System.out.println("Couldn't insert data");
            }
            System.out.println("-----------------------------------------");

            /*4.1.3*/
            SQLsentence = "ALTER TABLE IF EXISTS public.\"Subjects\" ADD COLUMN \"Hours\" integer NOT NULL DEFAULT 100;";
            statement.executeUpdate(SQLsentence);
            System.out.println("Table altered successfully!");
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
}