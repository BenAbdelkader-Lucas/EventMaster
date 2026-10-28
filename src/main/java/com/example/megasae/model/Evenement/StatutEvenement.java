package com.example.megasae.model.Evenement;

/**
 * Énumération des statuts possibles d'un événement.
 * Décrit le cycle de vie complet d'un événement, de sa création à son archivage.
 */
public enum StatutEvenement {

    /** L'événement est en cours de rédaction et non visible des participants. */
    BROUILLON("-fx-background-color: #FF4D00; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 3 10; -fx-font-size: 11;"),

    /** L'événement a été publié et est visible par tous les participants. */
    PUBLIE("-fx-background-color: #1ABC9C; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 3 10; -fx-font-size: 11;"),

    /** L'événement est actuellement en cours de déroulement. */
    ENCOURS("-fx-background-color: #2980B9; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 3 10; -fx-font-size: 11;"),

    /** L'événement est terminé et officiellement clôturé. */
    CLOTURE("-fx-background-color: #7F8C8D; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 3 10; -fx-font-size: 11;"),

    /** L'événement a été annulé avant sa tenue. */
    ANNULE("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 3 10; -fx-font-size: 11;"),

    /** L'événement a été archivé après sa clôture ou son annulation. */
    ARCHIVE("-fx-background-color: #34495E; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 3 10; -fx-font-size: 11;");

    // Attribut privé pour stocker la chaîne CSS de chaque statut
    private final String styleCss;

    // Constructeur de l'énumération
    StatutEvenement(String styleCss) {
        this.styleCss = styleCss;
    }

    // Getter pour récupérer le style CSS
    public String getStyleCss() {
        return this.styleCss;
    }

    // Ton getter existant pour le texte affiché
    public String getStatut() {
        return switch (this) {
            case BROUILLON -> "Brouillon";
            case PUBLIE -> "Publié";
            case ENCOURS -> "En cours";
            case CLOTURE -> "Cloturé";
            case ANNULE -> "Annulé";
            case ARCHIVE -> "Archivé";
        };
    }
}