import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    static ArrayList<String> words = new ArrayList<>();
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        boolean isFile1Ok = false;
        boolean isFile2OK = false;
        String file1 = "";
        String file2 = "";

        while(!isFile1Ok) {
            System.out.println("Please, enter the name of the first file: ");
            file1 = scan.nextLine();
            if(!file1.isEmpty()){
                try{
                    File ej1 = new File("files\\"+file1.replace(".txt","")+".txt");
                    if(ej1.exists()){
                        isFile1Ok = true;
                        break;
                    }else{
                        System.out.println("That file does not exist");
                        isFile1Ok = false;
                    }
                }catch(Exception e){
                    isFile1Ok = false;
                    System.out.println("That file does not exist");
                }
            }else{
                isFile1Ok = false;
            }
        }

        while(!isFile2OK) {
            System.out.println("Please, enter the name of the second file: ");
            file2 = scan.nextLine();
            if(!file2.isEmpty()){
                try{
                    File ej1 = new File("files\\"+file2.replace(".txt","")+".txt");
                    if(ej1.exists()){
                        isFile2OK = true;
                        break;
                    }else{
                        System.out.println("That file does not exist");
                        isFile2OK = false;
                    }
                }catch(Exception e){
                    isFile2OK = false;
                    System.out.println("That file does not exist");
                }
            }else{
                isFile2OK = false;
            }
        }
        String pathFile1 = "files\\"+file1.replace(".txt","")+".txt";
        String pathFile2 = "files\\"+file2.replace(".txt","")+".txt";
        String output = "files\\sorted.txt";

        readFile(pathFile1);
        readFile(pathFile2);

        Collections.sort(words);
        PrintWriter printWriter = null;
        try {
             printWriter = new PrintWriter(new FileWriter(output, false));
            for(String w : words){
                printWriter.println(w);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if ( printWriter != null ) {
                printWriter.close();
            }
        }

        System.out.println("Sorted!");
    }

    public static void readFile(String path){
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(path));

            String line = "";
            // read every line
            while ((line = bf.readLine()) != null) {
                words.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            try{
                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}