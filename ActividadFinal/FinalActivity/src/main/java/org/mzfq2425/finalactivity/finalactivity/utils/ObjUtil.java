package org.mzfq2425.finalactivity.finalactivity.utils;

import org.mzfq2425.finalactivity.finalactivity.model.Sellers;

import java.io.*;

public class ObjUtil {
    static String sellersPath = "sellers.txt";

    public static Sellers readObj(){
        Sellers userRemembered = null;
        File sellersObj = null;

        try {
            sellersObj = new File(sellersPath);
            if(!sellersObj.exists()){
                sellersObj.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Object sellers could not be created");
        }

        if (sellersObj.exists() && sellersObj.length() != 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(sellersPath))) {
                userRemembered = (Sellers) in.readObject();
            } catch (FileNotFoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return userRemembered;
    }


    public static boolean writeSellersToObj(Sellers c){
        try {
            File sellersObj = new File(sellersPath);
            if(!sellersObj.exists()){
                sellersObj.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Object sellers could not be created");
            return false;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(sellersPath))) {
            out.writeObject(c);
            return true;
        } catch (IOException e) {
            System.err.println("Error while writing on file: " + e.getMessage());
        }
        return false;
    }

    public static void cleanRememberUser() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(sellersPath);
            fileWriter.write("");
        } catch (IOException e) {
            System.err.println("Error while cleaning users file: " + e.getMessage());
        }finally{
            if(fileWriter != null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    System.err.println("Error while closing file writer: " + e.getMessage());
                }
            }
        }
    }
}
