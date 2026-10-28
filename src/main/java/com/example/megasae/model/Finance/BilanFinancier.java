package com.example.megasae.model.Finance;

import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Evenement.Evenement;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Classe représentant le bilan financier d'un événement.
 * Elle est liée par une relation de composition (1-1) avec la classe Evenement.
 * Elle centralise tous les calculs des recettes, des dépenses et du solde net.
 * Implémente Serializable pour permettre la sauvegarde binaire de la SAE.
 */
public class BilanFinancier implements Serializable {

    // ── VARIABLES ET ATTRIBUTS ──────────────────────────────────────────────

    /**
     * Identifiant unique pour la sérialisation binaire de la classe
     */
    private static final long serialVersionUID = 1L;

    /**
     * Journal de log pour suivre les actions et calculs financiers
     */
    private static final Logger LOGGER = Logger.getLogger(BilanFinancier.class.getName());

    /**
     * L'événement associé à ce bilan comptable (Composition)
     */
    private Evenement evenement;


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur principal associant directement le bilan financier à son événement.
     *
     * @param evenement L'événement concerné par ce bilan.
     */
    public BilanFinancier(Evenement evenement) {
        this.setEvenement(evenement);
    }


    // ── GETTERS ET SETTERS COMMENTÉS ─────────────────────────────────────────

    /**
     * Récupère l'événement lié à ce bilan financier
     */
    public Evenement getEvenement() {
        return evenement;
    }

    /**
     * Modifie l'événement lié à ce bilan financier
     */
    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }


    // ── MÉTHODES DE CALCULS FINANCIERS ───────────────────────────────────────

    /**
     * Calcule le total des dépenses issues des lignes de mouvements financiers (DepenseRecette).
     *
     * @return Le montant cumulé des dépenses de l'événement en euros.
     */
    public double calculerDepense() {
        double depensesTotales = 0.0;
        if (evenement != null && evenement.getDepensesRecette() != null) {
            for (DepenseRecette depenseRecette : evenement.getDepensesRecette()) {
                if (depenseRecette.isDepense()) {
                    depensesTotales += depenseRecette.getCoutTotal();
                }
            }
        }
        LOGGER.info("Total des dépenses calculé : " + depensesTotales + " euros");
        return depensesTotales;
    }

    /**
     * Calcule le coût total généré par les rôles d'équipe payants de l'événement.
     *
     * @return Le montant total lié aux rémunérations/coûts des rôles en euros.
     */
    public double calculerDepenseRoles() {
        double recettesTotales = 0.0;
        if (evenement != null && evenement.getRoles() != null) {
            for (Role role : evenement.getRoles()) {
                recettesTotales += (role.getTarif() * role.getNombreMembres());
            }
        }
        LOGGER.info("Total des dépenses pour les rôles calculé : " + recettesTotales + " euros");
        return recettesTotales;
    }

    /**
     * Combine les coûts des rôles d'équipe et les dépenses matérielles.
     *
     * @return Le total absolu des charges financières de l'événement en euros.
     */
    public double calculerTotalDepenses() {
        return calculerDepenseRoles() + calculerDepense();
    }

    /**
     * Calcule le total des recettes issues des mouvements financiers (DepenseRecette).
     *
     * @return Le montant cumulé des gains ou entrées d'argent en euros.
     */
    public double calculerTotalRecettes() {
        double recettesTotales = 0.0;
        if (evenement != null && evenement.getDepensesRecette() != null) {
            for (DepenseRecette depenseRecette : evenement.getDepensesRecette()) {
                if (!depenseRecette.isDepense()) {
                    recettesTotales += depenseRecette.getCoutTotal();
                }
            }
        }
        LOGGER.info("Total des recettes calculé : " + recettesTotales + " euros");
        return recettesTotales;
    }

    /**
     * Calcule le bénéfice ou la perte de l'événement (Recettes moins Dépenses).
     *
     * @return Le solde net final de l'événement en euros.
     */
    public double calculerSoldeNet() {
        if (evenement == null) {
            LOGGER.severe("Erreur critique : Impossible de calculer le solde, aucun événement lié.");
            return 0.0;
        }

        double recettes = calculerTotalRecettes();
        double depenses = calculerTotalDepenses();
        double solde = recettes - depenses;

        LOGGER.info("Solde net calculé pour " + evenement.getNom() + " : " + solde + " euros");
        return solde;
    }

    /**
     * Détermine le prix ou tarif moyen de l'ensemble des rôles enregistrés.
     *
     * @return Le tarif moyen calculé en euros, ou 0.0 si aucun rôle n'existe.
     */
    public double calculeTarifMoyen() {
        if (evenement == null || evenement.getRoles() == null || evenement.getRoles().isEmpty()) {
            LOGGER.warning("Impossible de calculer le tarif moyen : cet événement ne possède aucun rôle.");
            return 0.0;
        }

        double sommeTarifs = 0.0;
        int nbRolesPayants = 0;
        for (Role role : evenement.getRoles()) {
            if (role.getTarif() != 0) {
                sommeTarifs += role.getTarif();
                nbRolesPayants++;
            }
        }

        if (nbRolesPayants == 0) {
            LOGGER.warning("Attention : Aucun rôle payant trouvé, le tarif moyen sera de 0.0");
            return 0.0;
        }

        double tarifMoyen = sommeTarifs / nbRolesPayants;
        LOGGER.info("Tarif moyen calculé = " + tarifMoyen + " euros");
        return tarifMoyen;
    }

    /**
     * Détermine le coût unitaire moyen de toutes les factures ou dépenses réelles enregistrées.
     *
     * @return Le coût moyen pondéré en euros, ou 0.0 s'il n'y a pas de mouvements négatifs.
     */
    public double calculCoutMoyen() {
        if (evenement == null || evenement.getDepensesRecette() == null || evenement.getDepensesRecette().isEmpty()) {
            LOGGER.warning("Impossible de calculer le coût moyen : cet événement ne possède aucune dépense.");
            return 0.0;
        }

        double sommeCoutsTotaux = 0.0;
        int quantiteTotale = 0;

        for (DepenseRecette depenseRecette : evenement.getDepensesRecette()) {
            // Uniquement les lignes négatives (représentant les charges réelles)
            if (depenseRecette.getMontant() < 0) {
                sommeCoutsTotaux += (Math.abs(depenseRecette.getMontant()) * depenseRecette.getQuantite());
                quantiteTotale += depenseRecette.getQuantite();
            }
        }

        if (quantiteTotale == 0) {
            LOGGER.warning("Attention : Aucune dépense réelle (négative) trouvée, coût moyen de 0.0");
            return 0.0;
        }

        double coutMoyen = sommeCoutsTotaux / quantiteTotale;
        LOGGER.info("Coût moyen calculé = " + coutMoyen + " euros (Quantité totale d'articles : " + quantiteTotale + ")");
        return coutMoyen;
    }

}