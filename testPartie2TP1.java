import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class testPartie2TP1 {
    
    
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Veuillez entrer le chemin d'accès d'un dossier qui contient du code java.");
        } else {
            int debut = args[0].length()-5;
            String end = args[0].substring(debut);
            //si end=.java, alors on a une classe java
            if(end.equals(".java")) {
                Object[] infos = ParseClass.read(args[0]);
                for(int j = 0; j < infos.length; j++) {
                    System.out.println(infos[j]);
                }
            } else {
                System.out.println("Le chemin ne donnait pas accès à une classe java.");
            }
        }
    }
}