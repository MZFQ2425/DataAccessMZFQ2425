import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PrintWriter printWriter = null;
        try {
            String pathFile = "files\\example.txt";
            Scanner scanner = new Scanner(System.in);
            boolean bolAppend = false;
            boolean allOk = false;

            while(!allOk) {
                System.out.println("Hello, would you like to overwrite on the notepad? (y/n)");
                String overwrite = scanner.nextLine();

                if (overwrite.toLowerCase().equals("y") || overwrite.toLowerCase().equals("yes")) {
                    bolAppend = false;
                    allOk = true;
                }else if(overwrite.toLowerCase().equals("n") || overwrite.toLowerCase().equals("no")){
                    bolAppend = true;
                    allOk = true;
                }else{
                    System.out.println("Please, write a correct answer");
                }
            }

            //check if file we're going to write into exists
            File dir = new File("files");
            if (!dir.exists()) {
                boolean dirCreated = dir.mkdirs();
            }

            File txt = new File(pathFile);
            if(!txt.exists()){
                txt.createNewFile();
            }

            printWriter = new PrintWriter(new FileWriter(pathFile, bolAppend));

            int lastNumber = 0;

            if(bolAppend){
                // recover the lines entered previously
                BufferedReader bf = new BufferedReader(new FileReader(pathFile));

                String line;
                // read every line
                while ((line = bf.readLine()) != null) {
                    lastNumber = Integer.valueOf(line.substring(0,1));
                }
            }

            boolean stop = false;
            while(!stop){
                lastNumber++;
                System.out.println("Please, enter the text to add to the file: ");
                String newLine = scanner.nextLine();
                printWriter.println(lastNumber+" - "+newLine);
                System.out.println("Would you like to keep writing? (y/n)");
                String answer = scanner.nextLine();
                boolean isCorrect = false;

                while(!isCorrect && !stop) {
                    if (answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes")) {
                        stop = false;
                        isCorrect = true;
                    } else if (answer.toLowerCase().equals("n") || answer.toLowerCase().equals("no")) {
                        stop = true;
                        isCorrect = true;
                    } else {
                        System.out.println("Please, write a correct answer");
                        stop = false;
                        isCorrect = false;
                    }
                }
            }

            System.out.println("Bye!");
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