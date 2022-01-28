import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.nio.file.Path;
import java.nio.file.Paths;

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
                //pour l'instant, cette partie liste les fichiers et les dossiers à l'intérieur du chemin d'accès
                File racine = new File(args[0]);
                String[] contenu = racine.list();

                for (int i = 0; i < contenu.length; i++) {
                    System.out.println(contenu[i]);
                }
            }
        }
    }
}