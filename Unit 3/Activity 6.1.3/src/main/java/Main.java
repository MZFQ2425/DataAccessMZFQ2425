import java.sql.*;

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

            //add column
            pstmt = con.prepareStatement("ALTER TABLE IF EXISTS public.\"Subjects\" ADD COLUMN \"Course\" integer;");
            pstmt.executeUpdate();
            System.out.println("Created the column Course on the table Subjects");

            //recover a code to specify a default on the new column
            pstmt = con.prepareStatement("SELECT \"Code\" FROM public.\"Courses\" LIMIT 1;");
            ResultSet rs = pstmt.executeQuery();
            int code = 0;

            if(rs.next()){
                code = rs.getInt("Code");
            }

            //Update the "Course" column with the retrieved "Code" value
            pstmt = con.prepareStatement("UPDATE public.\"Subjects\" SET \"Course\" = ?;");
            pstmt.setInt(1, code);
            int i = pstmt.executeUpdate();
            if(i>0){
                System.out.println("Updated default values of column Course from table Subjects");
            }else{
                System.out.println("Error while updating table subjects");
            }

            //Add FK
            pstmt = con.prepareStatement("ALTER TABLE public.\"Subjects\" ADD CONSTRAINT fk_course FOREIGN KEY (\"Course\") REFERENCES public.\"Courses\" (\"Code\");");
            pstmt.executeUpdate();
            System.out.println("Foreign key created successfully");

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