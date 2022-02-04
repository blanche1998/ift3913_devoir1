import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public  class ParseClass{

    /**
     *
     * @param ligne à analyse
     * @return true si la ligne contient un commentaire qui commence par "//"; false sinon
     */
    public static boolean containsComment(String ligne) {
        boolean first = false;
        for(int i = 0; i < ligne.length();i++) {
            if(ligne.charAt(i)=='/') {
                //le paramètre précédent était aussi un '/' --> on a un commentaire
                //on peut cesser de chercher
                if(first) {
                    return true;
                } else {
                    first = true;
                }
            } else if(first) { //la caractère précédent était un '/' mais il n'y en a pas
                //un deuxième qui suit
                first = false;
            }
        }
        return false;
    }

    /**
     *
     * @param ligne à analyser
     * @return true si on a un début de commentaire -- donc un '/*' ('/**' sera découvert également)
     */
    public static boolean debut(String ligne) {
        boolean slash = false;
        for(int i = 0; i < ligne.length(); i++) {
            if(ligne.charAt(i)=='/') {
                slash = true;
            } else if(slash && ligne.charAt(i)=='*') {
                return true;
            } else if(slash) {
                slash = false;
            }
        }
        return false;
    }

    /**
     *
     * @param ligne à analyser
     * @return true si on a une fin de commentaire -- donc un '* /'
     */
    public static boolean fin(String ligne) {
        boolean asterisk = false;
        for(int i = 0; i < ligne.length(); i++) {
            if(ligne.charAt(i)=='*') {
                asterisk = true;
            } else if(asterisk && ligne.charAt(i)=='/') {
                return true;
            } else if(asterisk) {
                asterisk = false;
            }
        }
        return false;
    }

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
        return true;//retournera true si la ligne est de longueur 1
    }

    /**
     *
     * @param ligne la ligne qu'on veut parcourir
     * @param pattern le pattern de string qu'on cherche
     * @return true si la ligne contient pattern, false sinon
     */
    public static boolean contains(String ligne, String pattern) {
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
                            return true; //on a trouvé le motif
                        } else {
                            test = pattern.charAt(indexChar);
                        }
                    } else {
                        indexChar = 0;
                        test = pattern.charAt(indexChar);
                    }
                }
            } else {
                if(ligne.charAt(i)=='"' || ligne.charAt(i)=='\'') {
                    isInQuote = false; //on finit le commentaire
                }
            }
        }
        return false;
    }

    public static double[] read(String acces) {
        double[] mesures = new double[3];
        boolean debut = false; //servira à voir quand on a un commentaire ouvert qui pourrait s'étendre sur plus d'une ligne
        double compteCommentaires = 0;
        double compteLignes = 0;
        //cette méthode pour lire un fichier est inspirée de celle trouvée sur : https://www.w3schools.com/java/java_files_read.asp
        //Puisque le code pour lire un fichier ne relève pas des connaissances du cours, on prends sur nous d'utiliser ce code
        try {
            File myObj = new File(acces);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //on ne prend pas en compte les lignes vides
                if(data.length()!=0 && !ParseClass.vide(data)) {
                    compteLignes += 1;
                    if(ParseClass.containsComment(data)) {
                        compteCommentaires += 1;
                    }
                    if(debut || ParseClass.debut(data)) { //on est à l'intérieur d'un commentaire ou au début
                        compteCommentaires += 1;
                        if(ParseClass.fin(data)) {
                            debut = false;
                        } else { //on n'est pas encore sur la dernière ligne du commentaire
                            debut = true;
                        }
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        double densite = compteCommentaires/compteLignes;
        mesures[0] = compteLignes;
        mesures[1] = compteCommentaires;
        mesures[2] = densite;
        return mesures;
    }

}
