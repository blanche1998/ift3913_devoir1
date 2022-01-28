package src;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Calendrier implements java.io.Serializable {
    private Semaine[] semaines;   
    private RDV[] rdvListe;

    /**
     * Constructeur de la classe Calendrier qui initialise un calendrier
     * de 12 semaines
     */
    public Calendrier() {
        this.semaines = new Semaine[12];
        for(int m = 0; m < this.semaines.length; m++) {
            this.semaines[m] = new Semaine();
        }
        //on instancie un calendrier qui contient 12 semaines
        for(int i = 0; i < this.semaines.length; i++) {
			
            if(i==0) { //le premier
                this.semaines[i].setNext(this.semaines[i+1]);
            }
			
			else if (i==this.semaines.length-1) {
                this.semaines[i].setPrevious(this.semaines[i-1]);
            }
			
			else { //le dernier
                this.semaines[i].setPrevious(this.semaines[i-1]);
                this.semaines[i].setNext(this.semaines[i+1]);
            }
        }
		
        this.setFirstWeek();
    }

    /**
     * On instancie la première semaine actuelle selon la date
     * de la première utilisation
     */
    public void setFirstWeek() {
        Semaine firstWeek = this.semaines[0];
        Jour[] jours = firstWeek.getJours();
        LocalDate date = LocalDate.now();
        DayOfWeek jour = DayOfWeek.from(date);
        int indexJour = jour.getValue(); //retourne un indice entre 1 et 7, avec 1 = lundi
        if(indexJour == 6 || indexJour == 7) { //samedi ou dimanche
            date = date.plusDays(8-indexJour);
            jours[0].setDate(date); //la première semaine va commencer au lundi suivant
            for(int k = 1; k<5; k++) {
                LocalDate newDate = jours[k-1].getDate().plusDays(1);
                jours[k].setDate(newDate);
            }
            this.setWeeks(); //on va chercher les dates pour les autres semaines
        } else {
            for(int j = 1; j < 6; j++) {
                if(j < indexJour) {
                    LocalDate newDate = date.plusDays(j - indexJour);
                    jours[j-1].setDate(newDate);
                    jours[j-1].setPast(true);
                } if (j == indexJour) {
                    jours[j-1].setDate(date);
                } else { //j > indexJour
                    LocalDate newDate = date.plusDays(j-indexJour);
                    jours[j-1].setDate(newDate);
                }
            }
        }
        this.setWeeks();
    }

    /**
     * Fonction qui formate les semaines à venir
     */
    public void setWeeks() {
        for(int i = 1; i < this.semaines.length; i++) {
            Semaine semaineActuelle = this.semaines[i];
            Semaine semainePrecedente = semaineActuelle.getPrevious();
            for(int k = 0; k < 5; k++) {
                LocalDate datePrecedente = semainePrecedente.getJours()[k].getDate();
                LocalDate dateActuelle = datePrecedente.plusDays(7);
                semaineActuelle.getJours()[k].setDate(dateActuelle);
            }
        }
    }

    /**
     * Méthode qui retourne le jour de la semaine (lundi, mardi, etc) d'une
     * date spécifique
     * @param date
     * @return jour de la semaine
     */
    public Jour getJour(String date) {
        Jour jour = new Jour();
        for (Semaine semaine : this.semaines) {
            for (int j = 0; j < 5; j++) {
                String currentDate = semaine.getJours()[j].getDate().toString();
                if (currentDate.equalsIgnoreCase(date)) {
                    return semaine.getJours()[j];
                }
            }
        }
        return jour;
    }

    /**
     * Méthode qui retourne le tableau de semaines
     * @return tableau de semaines
     */
	public Semaine[] getSemaines() {
    	return this.semaines;
	}

    /**
     * Si un utilisateur veut voir une semaine qui n'est pas déjà instanciée,
     * il faut la créer et l'ajouter
     */
	public void ajouterSemaine() {

        Semaine[] newSemaines = new Semaine[this.semaines.length + 1];
        for (int j = 0; j < this.semaines.length; j++) {
            newSemaines[j] = this.semaines[j];
        }
        newSemaines[this.semaines.length] = new Semaine();
        newSemaines[this.semaines.length - 1].setNext(newSemaines[this.semaines.length]);
        newSemaines[this.semaines.length].setPrevious(newSemaines[this.semaines.length - 1]);
        newSemaines[this.semaines.length].setJours();
        this.semaines = newSemaines;

    }

    /**
     * Fonction pour updater le calendrier quand on le réouvre
     * La fonction calcule le nombre de semaines qui se sont écoulées depuis la
     * dernière ouverture et enlève ces semaines du nouveau calendrier
     */
    public void update() {
        LocalDate date = LocalDate.now();
        DayOfWeek jour = DayOfWeek.from(date);
        int indexJour = jour.getValue(); //retourne un indice entre 1 et 7, avec 1 = lundi
        LocalDate lundi = date;
        if (indexJour !=6 && indexJour != 7) {
            lundi = date.plusDays(-(indexJour-1));
        } else {
            lundi = date.plusDays(8-indexJour); //on va chercher le lundi suivant
        }
        LocalDate premier = this.semaines[0].getJours()[0].getDate(); //premier jour du calendrier
        int diff = (int) Math.abs(ChronoUnit.DAYS.between(lundi,premier))/7;//nombre de semaines qui se sont écoulées
        Semaine[] nouveauCalendrier = new Semaine[this.semaines.length -diff];
        int copie = diff;//copie contiendra l'index de la semaine à recopier
        for(int i = 0; i<nouveauCalendrier.length;i++) {
            //on recopie les semaines qui ne sont pas passées
            nouveauCalendrier[i] = this.semaines[copie];
            copie += 1;
        }
        if (indexJour != 6 && indexJour != 7) {
            for(int j = 0; j < indexJour-1; j++) {
                //ces jours sont passés
                nouveauCalendrier[0].getJours()[j].setPast(true);
            }
        }
        this.semaines = nouveauCalendrier;
    }

}


