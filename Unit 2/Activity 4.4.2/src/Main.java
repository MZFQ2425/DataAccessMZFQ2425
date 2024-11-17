import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        FileInputStream fIn = null;

        try {
            fIn = new FileInputStream("files\\bmp.bmp");
            //fIn = new FileInputStream("files\\gif.gif");
            //fIn = new FileInputStream("files\\ico.ico");
            //fIn = new FileInputStream("files\\jpeg.jpeg");
            //fIn = new FileInputStream("files\\png.png");

            byte[] bytes = new byte[8];
            fIn.read(bytes);

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X ", b));
            }

            System.out.println(sb.toString());

            if(sb.toString().startsWith("42 4D")){
                System.out.println("Your file is a BMP");
            }else if(sb.toString().startsWith("47 49 46 38 39 61") ||
                    sb.toString().startsWith("47 49 46 38 37 61")){
                System.out.println("Your file is a GIF");
            }else if(sb.toString().startsWith("00 00 01 00")){
                System.out.println("Your file is an ICO");
            }else if(sb.toString().startsWith("FF D8 FF")){
                System.out.println("Your file is a JPEG");
            }else if(sb.toString().startsWith("89 50 4E 47")){
                System.out.println("Your file is a PNG");
            }else{
                System.out.println("Unknown file");
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
        }
    }
}