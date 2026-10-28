package com.example.megasae.model.Miscellaneous;

import com.example.megasae.model.Evenement.Evenement;

import java.io.Serializable;
import java.util.UUID;

/**
 * Représente un tag/étiquette pouvant être associé à un ou plusieurs événements.
 * Les tags permettent de catégoriser et de filtrer les événements.
 * Cette classe est sérialisable pour permettre sa sauvegarde sur le disque.
 */
public class Tag implements Serializable {

    // ── Constante de Sérialisation (Obligatoire pour la stabilité du fichier .ser) ──
    private static final long serialVersionUID = 1L;

    // ── Variables d'état ────────────────────────────────────────────────────

    /** Identifiant unique du tag. */
    private UUID id;

    /** Libellé court et descriptif du tag. */
    private String libelle;

    /** Type d'entité cible auquel s'applique ce tag (ex : "EVENEMENT", "MEMBRE"). */
    private String typeCible;

    /** Couleur associée au tag pour son affichage (ex : code hexadécimal "#FF5733"). */
    private String couleur;

    /** Description détaillée du tag et de son utilisation. */
    private String description;

    /** Capacité maximale associée à ce tag. */
    private int capacite;

    /** Événement auquel appartient le tag. */
    private Evenement evenement;

    // ── Constructeurs ──────────────────────────────────────────────────────────

    /** Constructeur par défaut. */
    public Tag() {
        this.id = UUID.randomUUID(); // Sécurité pour s'assurer qu'un tag a toujours un identifiant unique
    }

    /**
     * Constructeur complet.
     */
    public Tag(String libelle, String typeCible, String couleur, String description, int capacite, Evenement evenement) {
        this.id = UUID.randomUUID();
        setLibelle(libelle);
        setCapacite(capacite);
        setTypeCible(typeCible);
        setCouleur(couleur);
        setDescription(description);
        setEvenement(evenement);
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    /** @return l'UUID du tag */
    public UUID getId() {
        return id;
    }

    /** @param id l'UUID à attribuer */
    public void setId(UUID id) {
        this.id = id;
    }

    /** @return le libellé du tag */
    public String getLibelle() {
        return libelle;
    }

    /** @param libelle le libellé à attribuer */
    public void setLibelle(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'un tag ne peut être null ou vide");
        }
        this.libelle = libelle;
    }

    /** @return le type de cible du tag */
    public String getTypeCible() {
        return typeCible;
    }

    /** @param typeCible le type de cible à attribuer */
    public void setTypeCible(String typeCible) {
        this.typeCible = typeCible;
    }

    /** @return la couleur du tag (ex : code hexadécimal) */
    public String getCouleur() {
        return couleur;
    }

    /** @param couleur la couleur à attribuer */
    public void setCouleur(String couleur) {
        if (couleur == null || couleur.trim().isEmpty()) {
            throw new IllegalArgumentException("Un tag doit forcément avoir une couleur");
        }
        this.couleur = couleur;
    }

    /** @return la description du tag */
    public String getDescription() {
        return description;
    }

    /** @param description la description à attribuer */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return la capacité associée au tag */
    public int getCapacite() {
        return capacite;
    }

    /** @param capacite la capacité à attribuer */
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    /** @return l'événement auquel appartient le tag */
    public Evenement getEvenement() {
        return evenement;
    }

    /** @param evenement l'événement auquel appartient le tag */
    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    // ── Méthodes d'affichage d'état ──────────────────────────────────────────

    @Override
    public String toString() {
        return "Tag{" +
                "libelle='" + libelle + '\'' +
                ", typeCible='" + typeCible + '\'' +
                ", couleur='" + couleur + '\'' +
                ", description='" + description + '\'' +
                ", capacite=" + capacite +
                '}';
    }

}