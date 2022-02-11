import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public abstract class ParseClass{

    /**
     * On veut vérifier si la ligne est vide car les lignes vides ne sont pas à prendre en compte
     * @param ligne à analyser
     * @return true si vide, false sinon
     */
    public static boolean vide(String ligne) {
        for(int i = 0; i < ligne.length(); i++) {
            if(ligne.charAt(i) != ' ') {
                return false;
            }
        }
        return true; //retournera true si la ligne est de longueur 1
    }

    /**
     *
     * @param ligne la ligne qu'on veut parcourir
     * @param pattern le pattern de string qu'on cherche
     * @param spaces false: les espaces dans le pattern ne sont pas acceptés, true: ils le sont
     * @return -1 si la ligne ne contient pas le pattern cherché, l'index du début du pattern sinon
     */
    public static int contains(String ligne, String pattern, Boolean spaces) {
        boolean isInQuote = false;
        int length = pattern.length();
        int indexChar = 0;
        char test = pattern.charAt(indexChar); //contient le premier caractère qu'on recherche
        for(int i = 0; i < ligne.length(); i++) {
            if(!isInQuote) {
                if(ligne.charAt(i)=='"' || ligne.charAt(i)=='\'') {
                    isInQuote = true; //on commence un commentaire
                    indexChar = 0;
                    test = pattern.charAt(indexChar);
                } else { //on vérifie si on voit notre pattern
                    if(ligne.charAt(i) == test) {
                        indexChar += 1;
                        if(indexChar == length) {
                            return i-length+1; //on a trouvé le motif
                        } else {
                            test = pattern.charAt(indexChar);
                        }
                    } else {
                        if(!(ligne.charAt(i)==' ' && spaces)) {
                            indexChar = 0;
                            test = pattern.charAt(indexChar);
                        }
                    }
                }
            } else {
                if(ligne.charAt(i)=='"' || ligne.charAt(i)=='\'') {
                    isInQuote = false; //on finit le commentaire
                }
            }
        }
        return -1;
    }

    /**
     * Fonction qui calcule LOC, CLOC et densité en parcourant chacune des lignes de code
     * de la classe
     * @param chemin chemin d'accès de la classe qu'on veut parcourir
     * @return un tableau contenant LOC, CLOC et la densité, WMC et BC
     */
    public static double[] read(String chemin, String[] infos, String[][] infoComplexite) {
        double[] mesures = new double[5];
        String COMMENT_TYPE = infos[0];
        String COMMENT_DEBUT = infos[1];
        String COMMENT_FIN = infos[2];
        String extension = infos[3];
        //variable qui sert à voir quand on a un commentaire ouvert qui pourrait s'étendre sur plus
        // d'une ligne
        boolean debut = false;
        double compteCommentaires = 0;
        double compteLignes = 0;
        double WMC = 0;

        String[] CLASS_DELIMITERS = infoComplexite[0];
        String[] FLOW_ELEMENTS = infoComplexite[1];

        /*
        cette méthode pour lire un fichier est inspirée de celle trouvée sur :
        https://www.w3schools.com/java/java_files_read.asp
        Puisque le code pour lire un fichier ne relève pas des connaissances du cours, on prend sur nous
         d'utiliser ce code
         */
        try {
            File myObj = new File(chemin);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                //on ne prend pas en compte les lignes vides
                if(data.length()!=0 && !ParseClass.vide(data)) {
                    compteLignes += 1;

                    // on compte les méthodes de la classe (on ignore les delimiters des attributs)
                    for (String classDelimiter : CLASS_DELIMITERS) {
                        if (ParseClass.contains(data, classDelimiter,false) != -1) {
                            if (ParseClass.contains(data, "){",true) != -1) {
                                WMC += 1;
                            }
                        }
                    }

                    // compte le nombre d'éléments de flow pour la complexité de McCabe
                    for (String flowElement : FLOW_ELEMENTS) {
                        if (ParseClass.contains(data, flowElement,false) != -1) {
                            WMC += 1;
                        }
                    }

                    //on est à l'intérieur d'un commentaire ou au début
                    if(debut || (ParseClass.contains(data,COMMENT_DEBUT,false)!=-1)) {
                        compteCommentaires += 1;

                        //on n'est pas encore sur la dernière ligne du commentaire
                        debut = ParseClass.contains(data, COMMENT_FIN,false) == -1;
                    }

                    else {
                        if(ParseClass.contains(data,COMMENT_TYPE,false) != -1) {
                            compteCommentaires += 1;
                        }
                    }
                }
            }
            myReader.close();
        }

        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        double densite = compteCommentaires/compteLignes;
        mesures[0] = compteLignes;
        mesures[1] = compteCommentaires;
        mesures[2] = densite;
        mesures[3] = WMC;
        mesures[4] = mesures[2]/mesures[3]; //bc
        return mesures;
    }

    /**
     *
     * @param chemin chemin d'accès de la classe
     * @return le nom de la classe sans le .java
     */
    public static String extraireNom(String chemin, String extension) {
        String nom = "";
        String substring = chemin.substring(0, chemin.length()-extension.length()+1);

        boucle:
        for(int i = substring.length()-1;i >= 0; i--){

            //on va s'arrêter dès qu'on a rejoint le premier / dans le chemin d'accès
            if(substring.charAt(i) == '/') {
                break boucle;
            }

            else {
                nom = substring.charAt(i)+nom;
            }
        }

        return nom;
    }
}
