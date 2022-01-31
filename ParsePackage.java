import java.io.File;

public class ParsePackage {
    
    /**
     * 
     * @param path Le chemin d'accès du paquet
     * @return Le premier tableau contient les informations des classes et le second, les informations
     * concernant les packages Java
     */
    public static String[][][] parse(String path,String name){
        double loc = 0;
        double cloc = 0;
        double dc = 0;
        String[][] packageInfo = new String[0][5];
        String[][] classInfo = new String[0][5];
        File racine = new File(path);
        String[] contenu = racine.list();
        /*
        Il faut parcourir tout le contenu du dossier. Lorsque l'élément est une classe, on update les
        métriques du paquet. Si c'est un paquet, il faut alors aller parcourir ce paquet.
        */
        for(int i = 0; i < contenu.length; i++) {
            //on a alors une classe java
            if(contenu[i].length() > 5 && contenu[i].substring(contenu[i].length()-5).equals(".java")) {
                //on va chercher les informations de la classe
                double[] infos = ParseClass.read(path+"/"+contenu[i]);
                loc += infos[0];
                cloc += infos[1];
                String[] infosToAdd = new String[5];
                infosToAdd[0] = path+"/"+contenu[i];
                infosToAdd[1] = contenu[i];
                infosToAdd[2] = ""+infos[0];
                infosToAdd[3] = ""+infos[1];
                infosToAdd[4] = ""+infos[2];
                classInfo = ParsePackage.addClass(infosToAdd,classInfo);
            } else {
                File dossier = new File(path+"/"+contenu[i]);
                if(dossier.isDirectory()) {
                    String[][][] infosPaquet = ParsePackage.parse(path+"/"+contenu[i],contenu[i]);
                    String[][] classes = infosPaquet[1];
                    String[][] paquets = infosPaquet[0];
                    //le dernier paquet ajouté sera celui qu'on est en train de parcourir et ses 
                    //métriques seront donc celles qu'on doit ajouter à celle de notre paquet
                    if(paquets.length != 0) {
                        String[] dernier = paquets[paquets.length-1];
                        loc += Double.parseDouble(dernier[2]);
                        cloc += Double.parseDouble(dernier[3]);
                    }
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
        //Il faut ajouter à la liste de paquet celui qu'on vient de parcourir
        String[] newPackage = new String[5];
        newPackage[0] = path;
        newPackage[1] = name;
        newPackage[2] = ""+loc;
        newPackage[3] = ""+cloc;
        dc = cloc/loc;
        newPackage[4] = ""+dc;
        //si loc=0 et cloc=0, alors le dossier ne contenait aucun code java. Ainsi, on ne l'ajoute pas
        if(loc!=0 || cloc!=0) {
            packageInfo = ParsePackage.addPackage(newPackage, packageInfo);
        }
        //packageInfo = ParsePackage.addPackage(newPackage, packageInfo);
        String[][][] result = new String[2][][];
        result[0] = packageInfo;
        result[1] = classInfo;
        return result;
    }

    /**
     * ajouter une classe au tableau qui contient les classes
     * @param infos Les informations concernant la nouvelle classe Java à ajouter
     * @param tableau La liste des classes Java parcourus à présent
     * @return La liste des classes Java updatée
     */
    public static String[][] addClass(String[] infos,String[][] tableau) {
        String[][] tab = new String[tableau.length+1][5];
        //on recopie le tableau
        for(int j = 0; j < tableau.length; j++) {
            tab[j] = tableau[j];
        }
        tab[tableau.length] = infos;
        return tab;
    }

    /**
     * 
     * @param paquet Les informations concernant le nouveau paquet Java à ajouter
     * @param tableau La liste des paquets Java parcourus à présent
     * @return La liste des paquets Java updatée
     */
    public static String[][] addPackage(String[] paquet, String[][] tableau) {
        String[][] tab = new String[tableau.length+1][5];
        //on recopie le tableau
        for(int j = 0; j < tableau.length; j++) {
            tab[j] = tableau[j];
        }
        tab[tableau.length] = paquet;
        return tab;
    }

}
