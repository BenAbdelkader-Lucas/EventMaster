package com.example.megasae.model.Miscellaneous;

import com.example.megasae.model.Evenement.Evenement;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe modèle représentant une tâche de préparation à accomplir pour un événement.
 * Chaque tâche fait partie d'une checklist, possède un état (cochée ou non), une date d'échéance,
 * un niveau d'importance et une couleur d'affichage.
 * Implémente Serializable pour permettre la sauvegarde binaire de la SAE.
 */
public class Tache implements Serializable {

    // ── Variables ──────────────────────────────────────────────────────

    /**
     * récuperation du logger
     */
    private static final Logger LOGGER = Logger.getLogger(Note.class.getPackageName());

    /**
     * Identifiant unique pour la sérialisation binaire de la classe
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique de la tâche généré automatiquement
     */
    private UUID id;

    /**
     * Description courte ou nom de la tâche (ex : "Réserver le traiteur")
     */
    private String libelle;

    /**
     * Indique si la tâche a été réalisée (true) ou si elle est encore à faire (false)
     */
    private boolean estCochee;

    /**
     * Couleur associée à la tâche pour son affichage visuel dans l'interface
     */
    private String couleur;

    /**
     * Niveau d'importance de la tâche permettant de la prioriser (URGENT, MOYEN, etc.)
     */
    private NiveauImportance importance;

    /**
     * Date limite ou d'échéance à laquelle la tâche doit être finalisée
     */
    private LocalDate date;

    /**
     * L'événement auquel est rattachée cette tâche
     */
    private Evenement evenement;

    private ArrayList<Tag> tags = new ArrayList<>();


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────


    /**
     * Constructeur complet pour instancier une tâche avec ses paramètres de base.
     */
    public Tache(String libelle, String couleur, NiveauImportance importance, LocalDate date, Evenement evenement) {
        this.id = UUID.randomUUID();
        setLibelle(libelle);
        setCouleur(couleur);
        setImportance(importance);
        setDate(date);
        setEvenement(evenement);
        setCouleur(couleur);
        this.estCochee = false;
        LOGGER.log(Level.INFO, "Création d''une tache : {0} ", new Object[]{this.libelle});

    }

    /**
     * Construteur par défaut
     */
    public Tache() {
        this.id = UUID.randomUUID();
        LOGGER.log(Level.INFO, "Création d''une tache : {0} ", new Object[]{this.libelle});
        this.estCochee = false; // Par défaut, une nouvelle tâche n'est pas cochée
    }


    // ── GETTERS ET SETTERS COMMENTÉS ─────────────────────────────────────────

    /**
     * Récupère l'identifiant unique de la tâche
     */
    public UUID getId() {
        return id;
    }

    /**
     * Récupère le libellé de la tâche
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Modifie le libellé de la tâche (déclenche une erreur s'il est vide ou null)
     */
    public void setLibelle(String libelle) {
        if (libelle == null || libelle.isEmpty()) {
            throw new IllegalArgumentException("Le libellé d'une tâche ne doit pas être null ou vide");
        }
        this.libelle = libelle;
    }

    /**
     *
     * @param estCochee
     */
    public void setEstCochee(boolean estCochee) {
        this.estCochee = estCochee;
    }

    /**
     * Vérifie si la tâche est cochée (réalisée)
     */
    public boolean isEstCochee() {
        return estCochee;
    }

    /**
     * Inverse l'état actuel de la tâche (passe de cochée à décochée, et inversement)
     */
    public void basculerEtatCocher() {
        this.estCochee = !this.estCochee;
    }

    /**
     * Récupère la couleur de la tâche
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * Modifie la couleur de la tâche (ne doit pas être nulle)
     */
    public void setCouleur(String couleur) {
        if (couleur == null) {
            throw new IllegalArgumentException("Une tâche doit obligatoirement avoir une couleur définie");
        }
        this.couleur = couleur;
    }

    /**
     * Récupère le niveau d'importance de la tâche
     */
    public NiveauImportance getImportance() {
        return importance;
    }

    /**
     * Modifie le niveau d'importance de la tâche
     */
    public void setImportance(NiveauImportance importance) {
        if (importance == null) throw new IllegalArgumentException("Une tache doit avoir un niveau d'importance");
        this.importance = importance;
    }

    /**
     * Récupère la date d'échéance de la tâche
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Modifie la date d'échéance de la tâche
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Récupère l'événement associé à cette tâche
     */
    public Evenement getEvenement() {
        return evenement;
    }

    /**
     * Associe un événement à cette tâche
     */
    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }


    // ── TOSTRING STANDARD ────────────────────────────────────────────────────

    @Override
    public String toString() {
        String etat = estCochee ? "[Fait]" : "[À faire]";
        return "Tâche " + etat + " : " + libelle + " (Échéance : " + date + ")";
    }

}