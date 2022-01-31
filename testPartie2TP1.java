import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class testPartie2TP1 {
    
    /**
     * 
     * @param args le chemin d'accès spécifié par l'usager qui mène à la classe ou au paquet
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Veuillez entrer le chemin d'accès d'un dossier qui contient du code java.");
        } else {
            int debut = args[0].length()-5;
            String end = args[0].substring(debut);
            //si end=.java, alors on a une classe java
            if(end.equals(".java")) {
                //à changer pour que ces infos soient redirigées vers le fichier csv classe
                //le fichier package sera alors vide
                double[] infos = ParseClass.read(args[0]);
                for(int j = 0; j < infos.length; j++) {
                    System.out.println(infos[j]);
                }
            } else {
                File racine = new File(args[0]);
                //on a paquet, il faut donc le parse
                if(racine.isDirectory()) {
                    String[][][] infos = ParsePackage.parse(args[0],args[0]);
                    String[][] packages = infos[0];
                    String[][] classes = infos[1];
                    System.out.println("Voici les classes :");
                    for(int i = 0; i < classes.length; i++) {
                        System.out.println(classes[i][0]+" "+classes[i][1]+" "+classes[i][2]+" "+classes[i][3]+" "+classes[i][4]);
                    }
                    System.out.println("Voici les paquets :");
                    for(int m = 0; m < packages.length; m++) {
                        System.out.println(packages[m][0]+" "+packages[m][1]+" "+packages[m][2]+" "+packages[m][3]+" "+packages[m][4]);
                    }
                } else {
                    System.out.println("Veuillez entrer le chemin d'accès d'un dossier qui contient du code java.");
                }
            }
        }
    }
}