import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String pathFile = "files\\file1.txt";
        //String pathFile = "files\\file2.txt";
        Scanner scanner = new Scanner(System.in);

        File file1 = new File(pathFile);

        if(file1.exists() ){
            ArrayList<String> result = new ArrayList<String>();

            System.out.println("Please, write the word you'd like to search in the files:");
            String word = scanner.nextLine();

            try {
                BufferedReader bf = new BufferedReader(new FileReader(pathFile));
                String line = "";
                int i = 0;
                while ((line = bf.readLine()) != null) {
                    i++;
                    if(line.toLowerCase().contains(word.toLowerCase())){
                        String toAdd = "Line "+i+", "+line;
                        result.add(toAdd);
                    }
                }

                if(result.size() == 0){
                    System.out.println("On the file 'file1.txt' we haven't found any matches");
                }else{
                    System.out.println("On the file 'file1.txt' we've found "+result.size()+" matches:");
                    for(String res : result){
                        System.out.println(res);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("The file to check '"+pathFile+"' does not exists");
        }
    }
}