package com.example.megasae.model.Finance;

import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Représente un mouvement financier (dépense ou recette) effectué dans le cadre d'un événement.
 * Cette classe est sérialisable pour permettre la persistance des données sur le disque.
 */
public class DepenseRecette implements Serializable {

    // ── Constante de Sérialisation (Obligatoire pour la stabilité du fichier .ser) ──
    private static final long serialVersionUID = 1L;

    // ── Variables d'état et Logger ──────────────────────────────────────────

    /**
     * Logger de la classe pour le suivi des flux financiers.
     */
    private static final Logger LOGGER = Logger.getLogger(DepenseRecette.class.getPackageName());

    /**
     * Identifiant unique du mouvement financier.
     */
    private UUID id;

    /**
     * Libellé décrivant la nature du mouvement (ex : "Location sono", "Vente billets").
     */
    private String libelle;

    /**
     * Montant unitaire en euros.
     */
    private double montant;

    /**
     * Quantité d'unités (le montant total est calculé par montant × quantite).
     */
    private int quantite;

    /**
     * Liste des tags associés à ce mouvement financier pour sa classification.
     */
    private ArrayList<Tag> tagList = new ArrayList<>();

    /**
     * Indicateur du type de mouvement : vrai (true) s'il s'agit d'une dépense, faux (false) pour une recette.
     */
    private boolean depense;

    /**
     * L'événement auquel est rattaché ce mouvement financier.
     */
    private Evenement evenement;

    // ── Constructeurs ──────────────────────────────────────────────────────────

    /**
     * Constructeur complet pour initialiser un mouvement financier spécifié.
     */
    public DepenseRecette(String libelle, double montant, int quantite, ArrayList<Tag> tagList, boolean depense, Evenement evenement) {
        this.id = UUID.randomUUID();
        setLibelle(libelle);
        setMontant(montant);
        setQuantite(quantite);
        setTagList(tagList);
        setDepense(depense);
        setEvenement(evenement);

        Object[] data = {libelle, (montant * quantite)};
        LOGGER.log(Level.INFO, "Dépense créée : {0} pour un montant total de {1}euros", data);
    }

    /**
     * Constructeur par défaut (utilisé pour les instanciations à blanc).
     */
    public DepenseRecette() {
        this.id = UUID.randomUUID(); // Sécurité pour s'assurer que même un objet vide possède un ID unique
        LOGGER.log(Level.INFO, "création d'un mouvement financier");
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    /**
     * @return L'identifiant unique (UUID) de ce mouvement financier.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return Le libellé ou la désignation textuelle.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit le libellé du mouvement. Le nom ne peut pas être vide.
     */
    public void setLibelle(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Une dépense doit avoir un nom");
        }
        this.libelle = libelle;
    }

    /**
     * @return Le montant unitaire en euros.
     */
    public double getMontant() {
        return montant;
    }

    /**
     * Définit le montant unitaire. Celui-ci doit être strictement positif.
     */
    public void setMontant(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant ne peut être négatif ou nul");
        }
        this.montant = montant;
    }

    /**
     * @return La quantité d'unités associées.
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * Définit la quantité d'unités. Celle-ci doit être supérieure à zéro.
     */
    public void setQuantite(int quantite) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité ne peut être négative ou nulle");
        }
        this.quantite = quantite;
    }

    /**
     * Passerelle de compatibilité pour récupérer les tags associés.
     */
    public ArrayList<Tag> getTag() {
        return tagList;
    }

    /**
     * @return La liste brute des tags associés à ce mouvement financier.
     */
    public ArrayList<Tag> getTagList() {
        return tagList;
    }

    /**
     * Attribue la liste de tags. Initialise une liste vide sécurisée si la valeur passée est nulle.
     */
    public void setTagList(ArrayList<Tag> tagList) {
        // REMPLACEMENT DU "?" : Utilisation d'un bloc if/else classique de débutant
        if (tagList != null) {
            this.tagList = tagList;
        } else {
            this.tagList = new ArrayList<>();
        }
    }

    /**
     * @return L'événement rattaché à ce flux.
     */
    public Evenement getEvenement() {
        return evenement;
    }

    /**
     * Assigne l'événement parent obligatoire lié à ce mouvement.
     */
    public void setEvenement(Evenement evenement) {
        if (evenement == null) {
            throw new IllegalArgumentException("Une dépense doit avoir un évènement");
        }
        this.evenement = evenement;
    }

    /**
     * @return true s'il s'agit d'une charge/dépense, false s'il s'agit d'un produit/recette.
     */
    public boolean isDepense() {
        return depense;
    }

    /**
     * Définit la nature du flux financier (vrai pour dépense, faux pour recette).
     */
    public void setDepense(boolean depense) {
        this.depense = depense;
    }

    /**
     * Calcule automatiquement la valeur financière totale du mouvement.
     */
    public double getCoutTotal() {
        return this.quantite * this.montant;
    }

    // ── Méthodes d'affichage d'état ──────────────────────────────────────────

    @Override
    public String toString() {
        // REMPLACEMENT DU "?" : On extrait le calcul de la taille pour clarifier la construction du texte
        int tailleListeTags = 0;
        if (tagList != null) {
            tailleListeTags = tagList.size();
        }

        String nomEvenementStr = "aucun";
        if (evenement != null) {
            nomEvenementStr = evenement.getNom();
        }

        String typeMouvement = "Recette";
        if (depense) {
            typeMouvement = "Dépense";
        }

        return "DepenseRecette{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", montant=" + montant +
                ", quantite=" + quantite +
                ", type=" + typeMouvement +
                ", nombreDeTags=" + tailleListeTags +
                ", evenement=" + nomEvenementStr +
                '}';
    }

}