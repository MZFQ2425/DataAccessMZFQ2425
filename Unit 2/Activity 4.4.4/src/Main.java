import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Main {

    @Test
    public void readBMP() throws IOException {
        FileInputStream fIn = null;
        String pathFile = "files\\bmp.bmp";

        if(!new File(pathFile).exists()){
            fail("File not found at "+pathFile);
        }

        try {
            fIn = new FileInputStream(pathFile);

            byte[] bytes = new byte[54];
            fIn.read(bytes);

            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            int size = buffer.getInt(2);
            int width = buffer.getInt(18);
            int height = buffer.getInt(22);
            int bitsPerPixel = buffer.getInt(28);

            //check for exact values extracted from 4.4.3
            assertEquals("Size should be 38478", 38478, size); // 38478
            assertEquals("Width should be 199", 199, width); // 199
            assertEquals("Height should be 187", 187, height); //187
            assertEquals("Bits per pixel should be 8", 8, bitsPerPixel);

        } catch (IOException e) {
            fail("IOException - Error reading file: "+e.toString());
        } finally {
            if (fIn != null) {
                try {
                    fIn.close();
                } catch (IOException e) {
                    fail("Failed to close file input stream");
                }
            }
        }
    }
}
