package com.example.megasae.model.Miscellaneous;

/**
 * Énumération représentant le niveau d'importance d'une tâche.
 * Permet de prioriser les tâches au sein de la checklist d'un événement.
 */
public enum NiveauImportance {

    /** Importance faible — tâche non urgente. */
    BASSE,

    /** Importance modérée. */
    MOYENNE,

    /** Importance élevée — tâche prioritaire. */
    HAUTE,

    /** La tâche est clôturée. */
    CLOTURE,

    /** La tâche est annulée. */
    ANNULE,

    /** La tâche est archivée. */
    ARCHIVE;

    public String getStatut() {
        return switch (this) {
            case BASSE -> "Basse";
            case MOYENNE -> "Moyenne";
            case HAUTE -> "Haute";
            case CLOTURE -> "Cloture";
            case ANNULE -> "Annulé";
            case ARCHIVE -> "Archivé";
        };
    }
}