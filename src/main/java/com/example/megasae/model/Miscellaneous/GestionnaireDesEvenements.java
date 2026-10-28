package com.example.megasae.model.Miscellaneous;

import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Evenement.TemplateEvenement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Gestionnaire principal du système de gestion d'événements.
 * Il sert de point d'entrée unique pour créer, supprimer, dupliquer et analyser
 * globalement l'ensemble des données de l'application.
 * Implémente Serializable pour permettre de sauvegarder toute l'application d'un coup.
 */
public class GestionnaireDesEvenements implements Serializable {

    // ── VARIABLES ET ATTRIBUTS ──────────────────────────────────────────────

    /** Identifiant unique pour la sérialisation binaire de la classe */
    private static final long serialVersionUID = 1L;

    /** Journal de log pour suivre les actions globales de l'application */
    private static final Logger LOGGER = Logger.getLogger(GestionnaireDesEvenements.class.getName());

    /** Liste principale contenant tous les événements enregistrés dans l'application */
    private List<Evenement> evenements = new ArrayList<>();

    /** Liste principale contenant tous les événements enregistrés dans l'application */
    private List<TemplateEvenement> templateEvenements = new ArrayList<>();

    /** Le nom du fichier masqué où seront stockées toutes les données de la SAE */
    private static final String FICHIER_SAUVEGARDE = "sauvegarde.dat";

    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur par défaut.
     * Initialise un gestionnaire vide de tout événement.
     */
    public GestionnaireDesEvenements() {
        LOGGER.info("Création et initialisation du gestionnaire des événements.");
    }


    // ── GETTERS ET SETTERS COMMENTÉS ─────────────────────────────────────────

    /** Récupère la liste de tous les événements de l'application */
    public List<Evenement> getEvenements() {
        return evenements;
    }
    /** Remplace la liste complète des événements de l'application */
    public void setEvenements(List<Evenement> evenements) {
        if (evenements != null) {
            this.evenements = evenements;
        }
    }

    /** Récupère la liste de tous les templates d'événements de l'application */
    public List<TemplateEvenement> getTemplateEvenements() {
        return templateEvenements;
    }
    /** Remplace la liste complète des templates d'événements de l'application */
    public void setTemplateEvenements(List<TemplateEvenement> templates) {
        if (templates != null) {
            this.templateEvenements = templates;
        }
    }


    // ── MÉTHODES DE GESTION DE LISTE ─────────────────────────────────────────

    /**
     * Ajoute un nouvel événement au système.
     * Vérifie que l'événement n'est pas null et n'existe pas déjà pour éviter les doublons.
     * @param evenement L'événement à enregistrer.
     */
    public void addEvenement(Evenement evenement) {
        if (evenement == null) {
            throw new IllegalArgumentException("Un événement ne peut pas être null");
        }
        if (evenements.contains(evenement)) {
            throw new IllegalArgumentException("Cet événement est déjà présent dans le gestionnaire");
        }
        evenements.add(evenement);
        sauvegarderDonnees(this);
        LOGGER.info("Événement " + evenement.getNom() + " ajouté avec succès au gestionnaire.");
    }
    /**
     * Supprime définitivement un événement du système.
     * @param evenement L'événement à retirer.
     */
    public void supprimerEvenement(Evenement evenement) {
        if (evenement != null) {
            evenements.remove(evenement);
            LOGGER.info("Événement " + evenement.getNom() + " supprimé du gestionnaire.");
        }
    }

    /**
     * Ajoute un nouvel événement au système.
     * Vérifie que l'événement n'est pas null et n'existe pas déjà pour éviter les doublons.
     * @param template L'événement à enregistrer.
     */
    public void addTemplateEvenement(TemplateEvenement template) {
        if (template == null) {
            throw new IllegalArgumentException("Un template ne peut pas être null");
        }
        if (templateEvenements.contains(template)) {
            throw new IllegalArgumentException("Ce template est déjà présent dans le gestionnaire");
        }
        templateEvenements.add(template);
        LOGGER.info("Template " + template.getNom() + " ajouté avec succès au gestionnaire.");
    }
    /**
     * Supprime définitivement un événement du système.
     * @param template L'événement à retirer.
     */
    public void supprimerTemplateEvenement(TemplateEvenement template) {
        if (template != null) {
            templateEvenements.remove(template);
            LOGGER.info("Template " + template.getNom() + " supprimé du gestionnaire.");
        }
    }


    // ── MÉTHODES MÉTIERS AVANCÉES ────────────────────────────────────────────

    /**
     * Crée une copie complète (deep copy) d'un événement existant.
     * Le nouvel événement reçoit un nouvel identifiant UUID et est rattaché au gestionnaire.
     * @param evenement L'événement d'origine à dupliquer.
     * @return La nouvelle instance d'événement dupliquée.
     */
    public Evenement dupliquer(Evenement evenement) {
        if (evenement == null) {
            throw new IllegalArgumentException("Impossible de dupliquer un événement null");
        }

        // Création de la copie avec les informations de base (génère un nouvel UUID)
        Evenement evenementDuplique = new Evenement(evenement.getNom() + " (Copie)", evenement.getDateDebut(), evenement.getDateFin());

        // Recopie de l'ensemble des données
        evenementDuplique.setHeure(evenement.getHeure());
        evenementDuplique.setDescription(evenement.getDescription());
        evenementDuplique.setStatut(evenement.getStatut());
        evenementDuplique.setStatutPrecedent(evenement.getStatutPrecedent());
        evenementDuplique.setCouleur(evenement.getCouleur());
        evenementDuplique.setProgressionChecklist(evenement.getProgressionChecklist());
        evenementDuplique.setNotes(new ArrayList<>(evenement.getNotes()));
        evenementDuplique.setBudget(evenement.getBudget());
        evenementDuplique.setCapaciteMax(evenement.getCapaciteMax());
        evenementDuplique.setGestionnaireDesEvenements(evenement.getGestionnaireDesEvenements());
        evenementDuplique.setBilanFinancier(evenement.getBilanFinancier());
        evenementDuplique.setLieux(new ArrayList<>(evenement.getLieux()));
        evenementDuplique.setMembres(new ArrayList<>(evenement.getMembres()));
        evenementDuplique.setTags(new ArrayList<>(evenement.getTags()));
        evenementDuplique.setRoles(new ArrayList<>(evenement.getRoles()));
        evenementDuplique.setCreneaux(new ArrayList<>(evenement.getCreneaux()));
        evenementDuplique.setTaches(new ArrayList<>(evenement.getTaches()));
        evenementDuplique.setDepenses(new ArrayList<>(evenement.getDepensesRecette()));
        evenementDuplique.setTemplateEvenement(evenement.getTemplateEvenement());

        // Ajout automatique au catalogue global
        evenements.add(evenementDuplique);
        LOGGER.log(Level.WARNING, "Duplication de l''événement {0}", new Object[]{evenement.getNom()});
        return evenementDuplique;
    }

    /**
     * Sérialise et sauvegarde l'intégralité des données de l'application dans un fichier binaire.
     * @param gestionnaire Le gestionnaire principal contenant tous les événements à sauvegarder.
     */
    public void sauvegarderDonnees(GestionnaireDesEvenements gestionnaire) {
        if (gestionnaire == null) {
            LOGGER.warning("Tentative de sauvegarde d'un gestionnaire null. Annulation.");
            return;
        }

        // Utilisation du Try-with-resources pour fermer automatiquement les flux même en cas d'erreur
        try (FileOutputStream fichier = new FileOutputStream(FICHIER_SAUVEGARDE);
             ObjectOutputStream fluxSortie = new ObjectOutputStream(fichier)) {

            // Écriture de l'objet global dans le fichier
            fluxSortie.writeObject(gestionnaire);
            LOGGER.info("Sauvegarde binaire réussie dans le fichier : " + FICHIER_SAUVEGARDE);

        } catch (IOException e) {
            LOGGER.severe("Erreur critique lors de la sauvegarde des données : " + e.getMessage());
        }
    }

    /**
     * Charge et désérialise les données de l'application depuis le fichier binaire.
     * @return Le GestionnaireDesEvenements restauré, ou un nouveau gestionnaire vide si aucun fichier n'existe.
     */
    public GestionnaireDesEvenements chargerDonnees() {
        File fichier = new File(FICHIER_SAUVEGARDE);

        // Si le fichier n'existe pas encore (premier lancement de l'application)
        if (!fichier.exists()) {
            LOGGER.info("Aucun fichier de sauvegarde trouvé. Création d'un nouveau gestionnaire vide.");
            return new GestionnaireDesEvenements();
        }

        try (FileInputStream fichierEntree = new FileInputStream(fichier);
             ObjectInputStream fluxEntree = new ObjectInputStream(fichierEntree)) {

            // Lecture de l'objet et conversion (cast) vers le bon type
            GestionnaireDesEvenements gestionnaireCharge = (GestionnaireDesEvenements) fluxEntree.readObject();
            LOGGER.info("Données de la SAE chargées avec succès depuis le fichier.");
            return gestionnaireCharge;

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe("Erreur lors du chargement de la sauvegarde. Création d'un gestionnaire vide. Détails : " + e.getMessage());
            // En cas de fichier corrompu, on renvoie un gestionnaire vide pour éviter le crash de l'application
            return new GestionnaireDesEvenements();
        }
    }

    /**
     * Calcule la somme cumulée des soldes nets de tous les événements.
     * @return Le bénéfice ou la perte globale en euros.
     */
    public double calculerBeneficesTotal() {
        double totalGlobal = 0.0;
        for (int e=0; e<evenements.size(); e++) {
            for (int d=0; d<evenements.get(e).getDepensesRecette().size(); d++) {
                if (evenements.get(e).getDepensesRecette().get(d).isDepense()) {
                    totalGlobal -= evenements.get(e).getDepensesRecette().get(d).getCoutTotal();
                } else {
                    totalGlobal += evenements.get(e).getDepensesRecette().get(d).getCoutTotal();
                }
            }
        }
        return totalGlobal;
    }

    /**
     * Calcule le taux d'avancement moyen des tâches de préparation sur l'ensemble des événements.
     * @return Un pourcentage global moyen compris entre 0.0 et 100.0.
     */
    public double calculerProgressionMoyenneTaches() {
        double tachesCochees = 0.0;
        int totalTaches = 0;

        if (evenements.isEmpty()) {
            return 0.0;
        }
        for (int e=0; e<evenements.size(); e++) {
            for (int t=0; t<evenements.get(e).getTaches().size(); t++) {
                if (evenements.get(e).getTaches().get(t).isEstCochee()) {
                    tachesCochees++;
                }
                totalTaches++;
            }
        }

        // Évite une division par zéro
        if (totalTaches == 0) {
            return 0.0;
        }

        return (tachesCochees / totalTaches) * 100.0;
    }

    /**
     *
     * @return le nombre de membre au total
     */
    public int calculerNombreMembres() {
        // Filtrer les participants (éviter les doublons présents sur plusieurs évènements)
        Set<UUID> participantsUniques = new HashSet<>(); // HashSet pour un nombre de personnes physiques uniques
        for (int e=0; e<evenements.size(); e++) {
            for (int m=0; m<evenements.get(e).getMembres().size(); m++) {
                Membre membre = evenements.get(e).getMembres().get(m);
                participantsUniques.add(membre.getId());
            }
        }
        return participantsUniques.size();
    }

}