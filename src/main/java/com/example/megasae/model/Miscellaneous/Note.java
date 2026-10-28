package com.example.megasae.model.Miscellaneous;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Classe modèle représentant une note textuelle ajoutée par l'organisateur sur un événement.
 * Une note est composée d'un libellé (titre) et d'une description (contenu).
 * Implémente Serializable pour permettre la sauvegarde binaire de la SAE.
 */
public class Note implements Serializable {

    // ── Variables ──────────────────────────────────────────────────────

    /** Identifiant unique pour la sérialisation binaire de la classe */
    private static final long serialVersionUID = 1L;

    /** Journal de log pour suivre les actions sur les notes */
    private static final Logger LOGGER = Logger.getLogger(Note.class.getName());

    /** Titre ou libellé de la note */
    private String libelle;

    /** Contenu textuel détaillé de la note */
    private String description;

    /**
     * Indique si la tâche a été réalisée (true) ou si elle est encore à faire (false)
     */
    private boolean estCochee;


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur complet pour créer une note avec son titre et son contenu.
     * @param libelle     Le titre de la note (ne doit pas être vide).
     * @param description Le contenu de la note.
     */
    public Note(String libelle, String description) {
        setLibelle(libelle);
        setDescription(description);
        LOGGER.info("Création d'une note : " + this.libelle);
    }


    // ── GETTERS ET SETTERS COMMENTÉS ─────────────────────────────────────────

    /** Récupère le titre ou libellé de la note */
    public String getLibelle() {
        return libelle;
    }
    /** Modifie le titre de la note (déclenche une erreur s'il est null ou vide) */
    public void setLibelle(String libelle) {
        if (libelle == null || libelle.isEmpty()) {
            throw new IllegalArgumentException("Il faut obligatoirement mettre un nom pour une note");
        }
        this.libelle = libelle;
    }

    /** Récupère le contenu détaillé de la note */
    public String getDescription() {
        return description;
    }
    /** Modifie le contenu détaillé de la note */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Vérifie si la note est cochée (réalisée) */
    public boolean isEstCochee() {
        return estCochee;
    }
    /** @param estCochee */
    public void setEstCochee(boolean estCochee) {
        this.estCochee = estCochee;
    }

    // ── TOSTRING DE SÉCURITÉ ─────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Note [Titre = " + libelle + "]";
    }

}