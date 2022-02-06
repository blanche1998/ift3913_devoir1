import java.io.*;

public class MesureMetrics {

   public static void writeCSV(String[][] content, String type) {
        try (PrintWriter writer = new PrintWriter(type + ".csv")) {
            StringBuilder sb = new StringBuilder();
            sb.append("chemin,");
            sb.append(type);
            sb.append(",classe_LOC,classes_CLOC,classe_DC\n");

            for (String[] strings : content) {
                for (String string : strings) {
                    sb.append(string);
                    sb.append(",");
                }

                sb.replace(sb.length()-1, sb.length(), "\n");
            }

            writer.write(sb.toString());
        }

        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * 
     * @param args le chemin d'accès spécifié par l'usager qui mène à la classe ou au paquet
     */
    public static void main(String[] args) {
        //System.out.println("done!");

        if (args.length != 1) {
            System.out.println("Veuillez entrer le chemin d'accès d'un dossier qui contient du code java.");
        }

        else {
            int debut = args[0].length()-5;
            String end = args[0].substring(debut);

            //si end=.java, alors on a une classe java
            if (end.equals(".java")) {

                //à changer pour que ces infos soient redirigées vers le fichier csv classe
                //le fichier package sera alors vide
                double[] infos = ParseClass.read(args[0]);

                //writeCSV(infos, "classes");
            }

            else {
                File racine = new File(args[0]);

                //on a un paquet, il faut donc le parse
                if (racine.isDirectory()) {
                    String[][][] infos = ParsePackage.parse(args[0], args[0]);
                    String[][] packages = infos[0];
                    String[][] classes = infos[1];
                    writeCSV(classes, "classes");
                    writeCSV(packages, "paquets");
                }

                else {
                    System.out.println("Veuillez entrer le chemin d'accès d'un dossier qui contient du code java.");
                }
            }
        }
    }
}