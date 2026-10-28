package com.example.megasae.model.Equipe;

/**
 * Énumération du statut de présence d'un membre à un événement.
 */
public enum StatutPresence {

    /** Le membre est présent à l'événement. */
    PRESENT,

    /** Le membre est absent de l'événement. */
    ABSENT,

    /** Le statut de présence du membre n'est pas encore renseigné. */
    INCONNU;

    public String getStatut() {
        return switch (this) {
            case PRESENT -> "Présent";
            case ABSENT -> "Absent";
            case INCONNU -> "Inconnu";
        };
    }
}
