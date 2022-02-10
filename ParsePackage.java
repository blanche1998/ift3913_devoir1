import java.io.File;

public abstract class ParsePackage {
    
    /**
     * 
     * @param path Le chemin d'accès du paquet
     * @return Le premier tableau contient les informations des classes et le second, les informations
     * concernant les packages Java
     */
    public static String[][][] parse(String path,String name){
        double loc = 0;
        double cloc = 0;
        double wcp = 0;

        String[][] packageInfo = new String[0][5];
        String[][] classInfo = new String[0][5];
        File racine = new File(path);
        String[] contenu = racine.list();

        int nbreClasse = 0;

        /*
        Il faut parcourir tout le contenu du dossier. Lorsque l'élément est une classe, on update les
        métriques du paquet. Si c'est un paquet, il faut alors aller parcourir ce paquet.
        */
        for (String s : contenu) {

            //si on a une classe java
            if (s.length() > 5 && s.substring(s.length() - 5).equals(".java")) {

                //on update avec les infos de la classe
                double[] infos = ParseClass.read(path + "/" + s);
                loc += infos[0];
                cloc += infos[1];
                wcp += infos[3];

                String[] infosToAdd = new String[7];
                infosToAdd[0] = path + "/" + s;
                infosToAdd[1] = ParseClass.extraireNom(s);
                infosToAdd[2] = "" + infos[0];
                infosToAdd[3] = "" + infos[1];
                infosToAdd[4] = "" + infos[2];
                infosToAdd[5] = "" + infos[3];
                infosToAdd[6] = "" + infos[4];
                classInfo = ParsePackage.addClass(infosToAdd, classInfo);

                nbreClasse += 1;
            }

            else {
                File dossier = new File(path + "/" + s);

                if (dossier.isDirectory()) {
                    String[][][] infosPaquet = ParsePackage.parse(path + "/" + s, name+"."+s);
                    String[][] classes = infosPaquet[1];
                    String[][] paquets = infosPaquet[0];

                    //le dernier paquet ajouté sera celui qu'on est en train de parcourir et ses
                    //métriques seront donc celles qu'on doit ajouter aux métriques de notre paquet
                    if (paquets.length != 0) {
                        String[] dernier = paquets[paquets.length - 1];
                        loc += Double.parseDouble(dernier[2]);
                        cloc += Double.parseDouble(dernier[3]);
                        wcp += Double.parseDouble(dernier[5]);
                    }

                    //les classes qu'il y avait à l'intérieur doivent être ajoutées à la liste
                    for (int k = 0; k < classes.length; k++) {
                        classInfo = ParsePackage.addClass(classes[k], classInfo);
                    }

                    //les paquets qu'il y avait à l'intérieur doivent être ajoutés à la liste
                    for (int l = 0; l < paquets.length; l++) {
                        packageInfo = ParsePackage.addPackage(paquets[l], packageInfo);
                    }
                }
            }
        }

        //Il faut ajouter à la liste de paquet celui qu'on vient de parcourir
        String[] newPackage = new String[7];
        newPackage[0] = path;
        newPackage[1] = name;
        newPackage[2] = ""+loc;
        newPackage[3] = ""+cloc;
        double dc = cloc/loc;
        newPackage[4] = ""+dc;
        newPackage[5] = ""+wcp;
        double bc = dc/wcp;
        newPackage[6] = ""+bc;

        //si loc=0 et cloc=0, alors le dossier ne contenait aucun code java, donc on ne l'ajoute pas
        if(loc!=0 && nbreClasse > 0) {
            packageInfo = ParsePackage.addPackage(newPackage, packageInfo);
        }

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
        String[][] tab = new String[tableau.length+1][7];

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
        String[][] tab = new String[tableau.length+1][7];
        //on recopie le tableau
        for(int j = 0; j < tableau.length; j++) {
            tab[j] = tableau[j];
        }
        tab[tableau.length] = paquet;
        return tab;
    }

}
