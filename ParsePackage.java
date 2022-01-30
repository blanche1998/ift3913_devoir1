import java.io.File;

public class ParsePackage {
    
    /**
     * 
     * @param path
     * @return Un tableau qui contient deux éléments de type tableau
     * Le premier tableau contient les informations des classes et le seconde, les informations
     * concernant les packages Java
     */
    public static String[][] parse(String path){
        String[] packageInfo = new String[0];
        String[] classInfo = new String[0];
        File racine = new File(path);
        String[] contenu = racine.list();
        for(int i = 0; i < contenu.length; i++) {
            //on a alors une classe java
            if(contenu[i].length() > 5 && contenu[i].substring(contenu[i].length()-5).equals(".java")) {
                //Object[] mesureClasse = ParseClass.read(path+"/contenu["+i+"]");
                classInfo = ParsePackage.addClass(path+"/"+contenu[i],classInfo);
            } else {
                File dossier = new File(path+"/"+contenu[i]);
                if(dossier.isDirectory()) {
                    //on l'ajoute à la liste de package qu'il y a 
                    packageInfo = ParsePackage.addPackage(path+"/"+contenu[i],packageInfo);
                    System.out.println("Dossier : "+path+"/"+contenu[i]);
                    String[][] infosPaquet = ParsePackage.parse(path+"/"+contenu[i]);
                    String[] classes = infosPaquet[1];
                    String[] paquets = infosPaquet[0];
                    //les classes qu'il y avait à l'intérieur doivent être ajoutés à la liste 
                    for(int k = 0; k < classes.length; k++) {
                        classInfo = ParsePackage.addClass(classes[k], classInfo);
                    }
                    //les paquets qu'il y avait à l'intérieur doivent être ajoutés à la liste 
                    for(int l = 0; l < paquets.length; l++) {
                        packageInfo = ParsePackage.addPackage(paquets[l], packageInfo);
                    }
                }
            }
        }
        String[][] result = new String[2][];
        result[0] = packageInfo;
        result[1] = classInfo;
        return result;
    }

    //ajouter une classe au tableau qui contient les classes
    public static String[] addClass(String classe,String[] tableau) {
        String[] tab = new String[tableau.length+1];
        //on recopie le tableau
        for(int j = 0; j < tableau.length; j++) {
            tab[j] = tableau[j];
        }
        tab[tableau.length] = classe;
        return tab;
    }

    public static String[] addPackage(String paquet, String[] tableau) {
        String[] tab = new String[tableau.length+1];
        //on recopie le tableau
        for(int j = 0; j < tableau.length; j++) {
            tab[j] = tableau[j];
        }
        tab[tableau.length] = paquet;
        return tab;
    }

}
