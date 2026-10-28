package com.example.megasae.model.Evenement;

import com.example.megasae.model.Equipe.Membre;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Classe modèle représentant un créneau horaire spécifique pour une activité.
 * Contient un titre, des horaires (début/fin), des dates, un lieu et la liste
 * des membres assignés à cette tâche ou animation.
 * Implémente Serializable pour permettre la sauvegarde binaire de la SAE.
 */
public class Creneau implements Serializable {

    // Déclaration du Logger
    private static final Logger LOGGER = Logger.getLogger(Creneau.class.getPackageName());
    // ── VARIABLES ET ATTRIBUTS ──────────────────────────────────────────────

    /**
     * Identifiant unique pour la sérialisation binaire de la classe
     */
    private static final long serialVersionUID = 1L;

    /**
     * Journal de log pour suivre les actions sur les créneaux
     */
    private static final Logger logger = Logger.getLogger(Creneau.class.getName());

    /**
     * Identifiant unique du créneau généré automatiquement
     */
    private UUID id;

    /**
     * Titre ou nom de l'activité associée à ce créneau (ex : "Keynote d'ouverture")
     */
    private String titreActivite;

    /**
     * Heure de début de l'activité
     */
    private Time heureDebut;

    /**
     * Heure de fin de l'activité
     */
    private Time heureFin;

    /**
     * Adresse spécifique du créneau si elle diffère du lieu principal
     */
    private String adresse;

    /**
     * Date de début du créneau
     */
    private LocalDate dateDebut;

    /**
     * Date de fin du créneau
     */
    private LocalDate dateFin;

    /**
     * Notes complémentaires ou informations pratiques
     */
    private String note;

    /**
     * L'événement auquel ce créneau horaire appartient
     */
    private Evenement evenement;

    /**
     * Le lieu lié au créneau
     */
    private Lieu lieu;

    /**
     * Liste des membres affectés à ce créneau précis
     */
    private List<Membre> membres = new ArrayList<>();


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur par défaut.
     * Initialise automatiquement un identifiant unique.
     */
    public Creneau() {
        this.id = UUID.randomUUID();
    }

    /**
     * Constructeur complet pour instancier un créneau avec toutes ses informations de base.
     */
    public Creneau(String titreActivite, Time heureDebut, Time heureFin, String adresse, LocalDate dateDebut, LocalDate dateFin, String note, Evenement evenement, Lieu lieu, List<Membre> membres) {
        this.id = UUID.randomUUID();
        setTitreActivite(titreActivite);
        setHeureDebut(heureDebut);
        setHeureFin(heureFin);
        setAdresse(adresse);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
        setNote(note);
        setEvenement(evenement);
        setLieu(lieu);
        setMembres(membres);
        LOGGER.log(Level.INFO, "Transition de statut pour l''événement {0} : {1}", new Object[]{this.titreActivite, this.id});
    }

    // ── GETTERS ET SETTERS CLASSIQUES ─────────────────────────────────────────

    /**
     * Récupère l'identifiant unique du créneau
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * Récupère le titre de l'activité
     */
    public String getTitreActivite() {
        return this.titreActivite;
    }

    /**
     * Modifie le titre de l'activité avec une vérification de sécurité (sans .trim())
     */
    public void setTitreActivite(String titreActivite) {
        if (titreActivite == null || titreActivite.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative d'entrer un titre null pour le créneau {0}", new Object[]{this.titreActivite});
            throw new IllegalArgumentException("Un titre ne peut pas être vide");
        }
        this.titreActivite = titreActivite;
    }

    /**
     * Récupère l'heure de début
     */
    public Time getHeureDebut() {
        return this.heureDebut;
    }

    /**
     * Modifie l'heure de début avec une sécurité
     */
    public void setHeureDebut(Time heureDebut) {
        if (heureDebut == null) {
            LOGGER.log(Level.WARNING, "Tentative d'entrer une heure de début null pour le créneau {0}", new Object[]{this.titreActivite});
            throw new IllegalArgumentException("Un créneau doit forcément avoir une heure de début");
        }else if ((dateFin != null && dateDebut != null) &&  dateFin.equals(dateDebut) && (heureFin != null && heureFin.before(heureDebut))) {
            throw new IllegalArgumentException("la date de fin doit être après la date de début");
        }else this.heureDebut = heureDebut;
    }

    /**
     * Récupère l'heure de fin
     */
    public Time getHeureFin() {
        return this.heureFin;
    }

    /**
     * Modifie l'heure de fin avec une sécurité
     */
    public void setHeureFin(Time heureFin) {
        if (heureFin == null) {
            LOGGER.log(Level.WARNING, "Tentative d'entrer une heure de fin null pour le créneau {0}", new Object[]{this.titreActivite});
            throw new IllegalArgumentException("Un créneau doit forcément avoir une heure de fin");
        }
        else if ( (dateFin != null && dateDebut != null) && dateFin.equals(dateDebut) && (heureDebut != null && heureFin.before(heureDebut))) {
            throw new IllegalArgumentException("la date de fin doit être après la date de début");
        }else this.heureFin = heureFin;
    }

    /**
     * Récupère l'adresse spécifique
     */
    public String getAdresse() {
        return this.adresse;
    }

    /**
     * Modifie l'adresse spécifique
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Récupère la date de début
     */
    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    /**
     * Modifie la date de début avec une sécurité
     */
    public void setDateDebut(LocalDate dateDebut) {
        if (dateDebut == null) {
            LOGGER.log(Level.WARNING, "Tentative d'entrer une date de début null pour le créneau {0}", new Object[]{this.titreActivite});
            throw new IllegalArgumentException("Un créneau doit forcément avoir une date de début");
        } else if (dateFin != null && dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("la date de fin doit être après la date de début");
        } else if ( dateFin != null &&   dateFin.equals(dateDebut) && ((heureFin != null && heureDebut != null) && (heureFin.before(heureDebut)))) {
            throw new IllegalArgumentException("la date de fin doit être après la date de début");
        } else this.dateDebut = dateDebut;
    }

    /**
     * Récupère la date de fin
     */
    public LocalDate getDateFin() {
        return this.dateFin;
    }

    /**
     * Modifie la date de fin en vérifiant qu'elle ne se situe pas avant le début
     */
    public void setDateFin(LocalDate dateFin) {
        if (this.dateDebut != null && dateFin != null && this.dateDebut.isAfter(dateFin)) {
            LOGGER.log(Level.WARNING, "Tentative d'entrer une date de fin null pour le créneau {0}", new Object[]{this.titreActivite});
            throw new IllegalArgumentException("Un créneau doit avoir un horaire de début et de fin fixe");
        }
        else if (dateDebut != null && dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("la date de fin doit être après la date de début");
        } else if (dateDebut != null && dateFin.equals(dateDebut) && ((heureFin != null && heureDebut != null) && (heureFin.before(heureDebut)))) {
            throw new IllegalArgumentException("la date de fin doit être après la date de début");
        } else this.dateFin = dateFin;
    }

    /**
     * Récupère les notes complémentaires
     */
    public String getNote() {
        return this.note;
    }

    /**
     * Modifie les notes complémentaires
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Récupère l'événement associé
     */
    public Evenement getEvenement() {
        return this.evenement;
    }

    /**
     * Lie ce créneau à son événement
     */
    public void setEvenement(Evenement evenement) {
        if (evenement == null) throw new IllegalArgumentException("Un créneau doit avoir un évènement");
        this.evenement = evenement;
    }

    /**
     * Récupère le lieu associé
     */
    public Lieu getLieu() {
        return this.lieu;
    }

    /**
     * Assigne un lieu géographique au créneau
     */
    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    /**
     * Récupère la liste des membres affectés au créneau
     */
    public List<Membre> getMembres() {
        return this.membres;
    }

    /**
     * Remplace la liste complète des membres
     */
    public void setMembres(List<Membre> membres) {
        this.membres = membres;
    }


    // ── MÉTHODES MÉTIER DE GESTION DU PLANNING ───────────────────────────────

    /**
     * Assigne un nouveau membre à ce créneau horaire s'il n'y est pas déjà.
     *
     * @param membre Le membre à ajouter.
     */
    public void associerMembre(Membre membre) {
        if (membre == null) {
            LOGGER.log(Level.WARNING, "Tentative d'association d'un membre null pour le créneau {0}", new Object[]{this.titreActivite});
            return;
        }

        if (!this.membres.contains(membre)) {
            this.membres.add(membre);
            // On s'assure de l'association bilatérale : le membre retient aussi son créneau
            membre.addCreneau(this);
            logger.info("Planning mis à jour : " + membre.getPrenom() + " " + membre.getNom() + " est associé à l'activité : " + this.titreActivite);
        } else {
            logger.fine("Le membre " + membre.getPrenom() + " " + membre.getNom() + " fait déjà partie de l'activité : " + this.titreActivite);
        }
    }

    /**
     * Retire un membre de ce créneau horaire.
     *
     * @param membre Le membre à enlever.
     */
    public void desassocierMembre(Membre membre) {
        if (membre != null && this.membres.contains(membre)) {
            this.membres.remove(membre);
            logger.info("Planning mis à jour : " + membre.getPrenom() + " " + membre.getNom() + " a été retiré de l'activité : " + this.titreActivite);
        }
    }

    /**
     * Modifie le lieu de l'activité et synchronise automatiquement son adresse textuelle.
     *
     * @param nouveauLieu Le nouveau lieu physique de l'activité.
     */
    public void modifierLieu(Lieu nouveauLieu) {
        this.lieu = nouveauLieu;
        if (nouveauLieu != null) {
            this.adresse = nouveauLieu.getAdresse();
        }
    }


    // ── MÉTHODE TOSTRING STANDARD ────────────────────────────────────────────

    @Override
    public String toString() {
        return "Créneau [Activité = " + titreActivite + ", Date début = " + dateDebut + ", Adresse = " + adresse + "]";
    }

}