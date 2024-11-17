import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Main {
    public static void main(String[] args) {
        FileInputStream fIn = null;

        try {
            fIn = new FileInputStream("files\\bmp.bmp");

            byte[] bytes = new byte[54];
            fIn.read( bytes );

            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            int size = buffer.getInt(2);
            int width = buffer.getInt(18);
            int height = buffer.getInt(22);
            int bitsPerPixel = buffer.getInt(28);

            System.out.println("Size: "+size);
            System.out.println("Width: "+width);
            System.out.println("Height: "+height);
            System.out.println("Bits per pixel: "+bitsPerPixel);
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
        }
    }
}