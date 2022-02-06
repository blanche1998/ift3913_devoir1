import java.io.*;

public class MesureMetrics {

   public static void writeCSV(String[][] content, String type, String outputName) {
        try (PrintWriter writer = new PrintWriter(outputName + ".csv")) {
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
                double[] infos = ParseClass.read(args[0]);
                String nom = ParseClass.extraireNom(args[0]);
                //si on a juste une classe, le tableau des paquets est vide
                String[][] packages = new String[0][5];
                String[][] classes = new String[1][5];
                classes[0][0] = args[0]; //chemin d'accès
                classes[0][1] = nom;
                classes[0][2] = ""+infos[0];//loc
                classes[0][3] = ""+infos[1];//cloc
                classes[0][4] = ""+infos[2];//densité

                writeCSV(classes, "class", "classes");
                writeCSV(packages, "paquet", "paquets");
            }

            else {
                File racine = new File(args[0]);

                //on a un paquet, il faut donc le parse
                if (racine.isDirectory()) {
                    String[][][] infos = ParsePackage.parse(args[0], args[0]);
                    String[][] packages = infos[0];
                    String[][] classes = infos[1];
                    writeCSV(classes, "class", "classes");
                    writeCSV(packages, "paquet", "paquets");
                }

                else {
                    System.out.println("Veuillez entrer le chemin d'accès d'un dossier qui contient du code java.");
                }
            }
        }
    }
}