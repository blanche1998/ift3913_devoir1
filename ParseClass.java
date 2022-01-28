import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public  class ParseClass{

    public static boolean containsComment(String ligne) {
        boolean test = false;
        for(int i = 0; i < ligne.length();i++) {
            if(ligne.charAt(i)=='/') {
                if(test) {
                    return true;
                } else {
                    test = true; 
                }
            } else if(test) {
                test = false;
            }
        }
        return false;
    }

    public static boolean debut(String ligne) {
        boolean test = false;
        for(int i = 0; i < ligne.length(); i++) {
            if(ligne.charAt(i)=='/') {
                test = true;
            } else if(test && ligne.charAt(i)=='*') {
                return true;
            } else if(test) {
                test = false;
            }
        }
        return false;
    }

    public static boolean fin(String ligne) {
        boolean test = false;
        for(int i = 0; i < ligne.length(); i++) {
            if(ligne.charAt(i)=='*') {
                test = true;
            } else if(test && ligne.charAt(i)=='/') {
                return true;
            } else if(test) {
                test = false;
            }
        }
        return false;
    }

    public static boolean vide(String ligne) {
        for(int i = 0; i < ligne.length(); i++) {
            if(ligne.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static Object[] read(String acces) {
        Object[] mesures = new Object[3];
        boolean debut = false; //servira à voir quand on a un commentaire ouvert qui pourrait s'étendre sur plus d'une ligne
        int compteCommentaires = 0;
        int compteLignes = 0;
        //cette méthode pour lire un fichier est inspirée de celle trouvée sur : https://www.w3schools.com/java/java_files_read.asp
        //Puisque le code pour lire un fichier ne relève pas des connaissances du cours, on prends sur nous d'utiliser ce code
        try {
            File myObj = new File(acces);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //on ne prend pas en compte les lignes vides
                System.out.println("Nombre de caractères: "+data.length());
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
            System.out.println("Nombre de lignes : "+compteLignes);
            System.out.println("Nombre de commentaires : "+compteCommentaires);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        double densite = ((double) compteCommentaires)/((double) compteLignes);
        mesures[0] = compteLignes;
        mesures[1] = compteCommentaires;
        mesures[2] = densite;
        return mesures;
    }

}