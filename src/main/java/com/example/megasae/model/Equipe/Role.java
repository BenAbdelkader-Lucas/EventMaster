package com.example.megasae.model.Equipe;

import com.example.megasae.model.Evenement.Creneau;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Classe modèle représentant un rôle attribuable à des membres dans le cadre d'un événement.
 * Un rôle définit une fonction précise (ex : Organisateur, Intervenant, Participant),
 * avec une capacité maximale et un tarif optionnel.
 * Implémente Serializable pour permettre la sauvegarde binaire de la SAE.
 */
public class Role implements Serializable {

    // ── VARIABLES ET ATTRIBUTS ──────────────────────────────────────────────

    /** Identifiant unique pour la sérialisation binaire de la classe */
    private static final long serialVersionUID = 1L;

    /** Journal de log pour suivre les actions sur les rôles */
    private static final Logger logger = Logger.getLogger(Role.class.getName());

    /** Identifiant unique du rôle généré automatiquement */
    private UUID id;

    /** Nom du rôle (ex : "Organisateur", "Intervenant", "Participant") */
    private String nom;

    /** Description détaillée des responsabilités liées à ce rôle */
    private String description;

    /** Nombre maximal de membres pouvant occuper ce rôle sur un événement donné */
    private int capaciteMaximale;

    /** Tarif appliqué aux membres ayant ce rôle (peut être null si gratuit) */
    private double tarif;

    /** Couleur au format texte (ex: code hexadécimal) associée au rôle pour l'affichage visuel */
    private String couleur;

    /** L'événement auquel ce rôle est rattaché */
    private Evenement evenement;

    /** Liste des étiquettes (tags) de catégorisation affectées à ce rôle */
    private List<Tag> tags = new ArrayList<>();

    /** Liste des membres actuellement assignés à ce rôle */
    private List<Membre> membres = new ArrayList<>();


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur par défaut.
     * Initialise un identifiant unique de manière automatique.
     */
    public Role() {
        this.id = UUID.randomUUID();
    }

    /**
     * Constructeur complet pour instancier un rôle avec toutes ses informations de base.
     */
    public Role(String nom, String description, int capaciteMaximale, Double tarif, String couleur) {
        this.id = UUID.randomUUID();
        setNom(nom);
        setDescription(description);
        setCapaciteMaximale(capaciteMaximale);
        setTarif(tarif);
        setCouleur(couleur);
    }


    // ── GETTERS ET SETTERS CLASSIQUES ─────────────────────────────────────────

    /** Récupère l'identifiant unique du rôle */
    public UUID getId() {
        return this.id;
    }
    /** Modifie l'identifiant unique du rôle */
    public void setId(UUID id) {
        this.id = id;
    }

    /** Récupère le nom du rôle */
    public String getNom() {
        return this.nom;
    }
    /** Modifie le nom du rôle */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /** Récupère la description des responsabilités */
    public String getDescription() {
        return this.description;
    }
    /** Modifie la description des responsabilités */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Récupère la capacité maximale d'accueil de ce rôle */
    public int getCapaciteMaximale() {
        return this.capaciteMaximale;
    }
    /** Modifie la capacité maximale d'accueil de ce rôle */
    public void setCapaciteMaximale(int capaciteMaximale) {
        this.capaciteMaximale = capaciteMaximale;
    }

    /** Récupère le tarif en euros (ou null si gratuit) */
    public double getTarif() {
        return this.tarif;
    }
    /** Modifie le tarif en euros (ou null si gratuit) */
    public void setTarif(Double tarif) { this.tarif = tarif; }

    /** Récupère la couleur associée au rôle */
    public String getCouleur() {
        return this.couleur;
    }
    /** Modifie la couleur associée au rôle */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    /** Récupère l'événement auquel ce rôle appartient */
    public Evenement getEvenement() {
        return this.evenement;
    }
    /** Modifie l'événement auquel ce rôle appartient */
    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    /** Récupère la liste des tags associés à ce rôle */
    public List<Tag> getTags() {
        return this.tags;
    }
    /** Remplace la liste complète des tags */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /** Récupère la liste des membres possédant ce rôle */
    public List<Membre> getMembres() {
        return this.membres;
    }
    /** Remplace la liste complète des membres possédant ce rôle */
    public void setMembres(List<Membre> membres) {
        this.membres = membres;
    }


    // ── MÉTHODES MÉTIER DE CALCUL ET VÉRIFICATION ───────────────────────────

    /**
     * Vérifie si le quota maximal de membres autorisés pour ce rôle a été atteint.
     * @return true si le rôle est complet, false sinon.
     */
    public boolean isCapaciteAtteinte() {
        return this.membres.size() >= this.capaciteMaximale;
    }

    /**
     * Retourne le nombre exact de membres actuellement associés à ce rôle précis.
     * @return Le compte de membres présents dans la liste.
     */
    public int getNombreMembres() {
        return this.membres.size();
    }

    public void addMembre(Membre membre) {
        this.membres.add(membre);
    }

    public void removeMembre(Membre membre) {
        this.membres.remove(membre);
    }

    // ── MÉTHODE TOSTRING STANDARD ────────────────────────────────────────────

    @Override
    public String toString() {
        return "Rôle [Nom = " + nom + ", Capacité max = " + capaciteMaximale + ", Tarif = " + tarif + " €]";
    }

}