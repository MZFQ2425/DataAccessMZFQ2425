import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PrintWriter printWriter = null;
        try {
            String pathFile = "files\\example.txt";
            boolean keepReading = true;

            System.out.println("Showing the contents on file 'example.txt' from 23 lines on");

            BufferedReader bf = new BufferedReader(new FileReader(pathFile));
            BufferedReader consoleUser = new BufferedReader(new InputStreamReader(System.in));

            while(keepReading) {
                String line = "";
                int i= 0;

                while (i < 23 && (line = bf.readLine()) != null) {
                    System.out.println(line);
                    i++;
                }

                if (line == null) {
                    System.out.println("Reached the end of the file.");
                    System.out.println("Bye!");
                    keepReading = false;
                } else {
                    System.out.println("Would you like to continue? If so, please, press [ENTER], otherwise write exit.");

                    String input = null;

                    boolean canContinue = false;

                    while(!canContinue) {
                        input =consoleUser.readLine();
                        if (input.equalsIgnoreCase("exit")) {
                            keepReading = false;
                            canContinue = true;
                            System.out.println("Bye!");
                        } else if (input.isEmpty()) {
                            keepReading = true;
                            canContinue = true;
                        } else {
                            System.out.println("Please either press [ENTER] or write 'exit'.");
                            canContinue = false;
                        }
                    }
                }
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        finally {
            if ( printWriter != null ) {
                printWriter.close();
            }
        }

    }
}