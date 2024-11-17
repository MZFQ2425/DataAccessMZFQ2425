import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        FileInputStream fIn = null;
        FileOutputStream fOut = null;
        byte[] data = new byte[54];

        try {
            fIn = new FileInputStream("D:\\testin.txt");
            fOut = new FileOutputStream("D:\\testout.txt");
            int bytesRead = 0;
            while( (bytesRead = fIn.read( data )) != -1 ) {
                fOut.write( data, 0, bytesRead);
            }
        }
        catch( IOException e ) {
            System.out.println( e.getMessage() );
        }
        finally {
            try {
                fIn.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fOut.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}