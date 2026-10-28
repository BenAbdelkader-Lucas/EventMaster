package com.example.megasae.model.Evenement;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe modèle représentant un lieu physique pouvant accueillir un ou plusieurs événements.
 * Un lieu est en relation d'agrégation avec l'événement : il peut exister indépendamment.
 * Implémente Serializable pour permettre la sauvegarde binaire de la SAE.
 */
public class Lieu implements Serializable {

    // ── VARIABLES ET ATTRIBUTS ──────────────────────────────────────────────

    /**
     * Identifiant unique pour la sérialisation binaire de la classe
     */
    private static final long serialVersionUID = 1L;

    /**
     * Journal de log pour suivre les actions sur les lieux
     */
    private static final Logger LOGGER = Logger.getLogger(Lieu.class.getName());

    /**
     * Identifiant unique du lieu généré automatiquement
     */
    private UUID id;

    /**
     * Nom du lieu (ex : "Salle des fêtes de Lyon")
     */
    private String nom;

    /**
     * Adresse postale complète du lieu
     */
    private String adresse;

    /**
     * Capacité maximale d'accueil en nombre de personnes
     */
    private int capaciteMax;

    /**
     * L'événement auquel est rattaché ce lieu
     */
    private Evenement evenement;


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur par défaut.
     * Initialise automatiquement un identifiant unique pour éviter les valeurs nulles.
     */
    public Lieu() {
        this.id = UUID.randomUUID();
    }

    /**
     * Constructeur complet pour instancier un lieu avec toutes ses informations de base.
     */
    public Lieu(Evenement evenement, String nom, String adresse, int capaciteMax) {
        this.id = UUID.randomUUID();
        this.evenement = evenement;
        setNom(nom);
        setAdresse(adresse);
        setCapaciteMax(capaciteMax);
        LOGGER.info("Nouveau lieu instancié : " + this.nom);
    }


    // ── GETTERS ET SETTERS COMMENTÉS ─────────────────────────────────────────

    /**
     * Récupère l'identifiant unique du lieu
     */
    public UUID getId() {
        return id;
    }

    /**
     * Modifie l'identifiant unique du lieu
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Récupère le nom du lieu
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom du lieu (déclenche une erreur s'il est null ou vide)
     */
    public void setNom(String nom) {
        if (nom == null || nom.isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative d''ajout d''un nom null éu lieu {0} ({1})", new Object[]{this.nom, this.id});
            throw new IllegalArgumentException("le nom d'un lieu ne peut etre null ou vide");
        }
        this.nom = nom;
    }

    /**
     * Récupère l'adresse complète du lieu
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Modifie l'adresse complète du lieu
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * @return la capacité maximale d'accueil
     */
    public int getCapaciteMax() {
        return capaciteMax;
    }

    /**
     * @param capaciteMax la capacité maximale à attribuer
     */
    public void setCapaciteMax(int capaciteMax) throws IllegalArgumentException {
        if (capaciteMax <= 0) {
            LOGGER.log(Level.WARNING, "Capacité d''accueil négative ({0}) fournie pour le lieu {1}", new Object[]{capaciteMax, this.nom});
            throw new IllegalArgumentException("La capacité maximum doit être supérieur ou égale à 0");
        }
        this.capaciteMax = capaciteMax;
    }

    /**
     * Récupère l'événement associé à ce lieu
     */
    public Evenement getEvenement() {
        return evenement;
    }

    /**
     * Associe un événement à ce lieu
     */
    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }


    // ── MÉTHODE TOSTRING STANDARD ────────────────────────────────────────────

    @Override
    public String toString() {
        return "Lieu [Nom = " + nom + ", Adresse = " + adresse + ", Capacité max = " + capaciteMax + " personnes]";
    }

}