import java.io.*;
import java.util.Properties;

public class MeasureMetrics {

   public static void writeCSV(String[][] content, String type, String outputName) {
        try (PrintWriter writer = new PrintWriter("PARTIE4/" + outputName + ".csv")) {
            StringBuilder sb = new StringBuilder();
            sb.append("chemin,");
            sb.append(type);

            if(type.equals("class")){
                sb.append(",classe_LOC,classe_CLOC,classe_DC,WMC,classe_BC");
                sb.append("");
            }
            if(type.equals("paquet")){
                sb.append(",paquet_LOC,paquet_CLOC,paquet_DC,WCP,paquet_BC");
            }

            sb.append("\n");

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
    public static void main(String[] args) throws IOException {

        //https://www.baeldung.com/java-properties
        //String configPath = "/comment.properties";
        //Properties commentProps = new Properties();
        //commentProps.load(new FileInputStream(configPath));

        //InputStream in = this.getClass().getResourceAsStream("comment.properties");
        //Properties commentProps = new Properties();
        //commentProps.load(in);
        //in.close();

        InputStream fin=null;
        fin=ClassLoader.getSystemResourceAsStream("comment.properties"); //this is a static function
        Properties commentProps = new Properties();
        commentProps.load(fin);

        String[] infosCommentaires = new String[4];
        infosCommentaires[0] = commentProps.getProperty("comment_type");
        infosCommentaires[1] = commentProps.getProperty("comment_debut");
        infosCommentaires[2] = commentProps.getProperty("comment_fin");
        infosCommentaires[3] = commentProps.getProperty("extension");

        String[][] infosComplexite = new String[2][];
        String class_delimiters = commentProps.getProperty("class_delimiters");
        class_delimiters = class_delimiters.substring(1,class_delimiters.length()-1);
        String[] class_delimiters_array = class_delimiters.split(",");
        for(int i = 0; i < class_delimiters_array.length; i++) {
            String delimiter = class_delimiters_array[i];
            delimiter = delimiter.substring(1,delimiter.length()-1);//enlever les guillemets
            class_delimiters_array[i] = delimiter;
        }

        String flow_elements = commentProps.getProperty("flow_elements");
        flow_elements = flow_elements.substring(1,flow_elements.length()-1);
        String[] flow_elements_array = flow_elements.split(",");
        for(int i = 0; i < flow_elements_array.length; i++) {
            String element = flow_elements_array[i];
            element = element.substring(1,element.length()-1);//enlever les guillemets
            flow_elements_array[i] = element;
        }

        infosComplexite[0] = class_delimiters_array;
        infosComplexite[1] = flow_elements_array;

        int debut = args[0].length()-5;
        String end = args[0].substring(debut);

        //si end=.java, alors on a une classe java
        if (end.equals("."+infosCommentaires[3])) {
            double[] infos = ParseClass.read(args[0],infosCommentaires,infosComplexite);
            String nom = ParseClass.extraireNom(args[0],infosCommentaires[3]);
            //si on a juste une classe, le tableau des paquets est vide
            String[][] packages = new String[0][5];
            String[][] classes = new String[1][7];
            classes[0][0] = args[0]; //chemin d'accès
            classes[0][1] = nom;
            classes[0][2] = ""+infos[0]; //loc
            classes[0][3] = ""+infos[1]; //cloc
            classes[0][4] = ""+infos[2]; //densité
            classes[0][5] = ""+infos[3]; //densité
            classes[0][6] = ""+infos[4]; //densité

            writeCSV(classes, "class", "classes");
            writeCSV(packages, "paquet", "paquets");
        }

        else {
            File racine = new File(args[0]);

            //on a un paquet, il faut donc le parse
            if (racine.isDirectory()) {
                String name = ParseClass.extraireNom(args[0],infosCommentaires[3]);
                String[][][] infos = ParsePackage.parse(args[0], name,infosCommentaires,infosComplexite);
                String[][] packages = infos[0];
                String[][] classes = infos[1];
                writeCSV(classes, "class", "classes");
                writeCSV(packages, "paquet", "paquets");
            }


        }

    }
}