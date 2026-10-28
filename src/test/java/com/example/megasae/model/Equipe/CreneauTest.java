package com.example.megasae.model.Equipe;

import com.example.megasae.model.Evenement.Creneau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CreneauTest {
    private Creneau creneauTest;

    @BeforeEach
    void setUp() {
        creneauTest = new Creneau();
        creneauTest.setTitreActivite("Conférence Ouverture");

        // Dates de base cohérentes
        LocalDate aujourdhui = LocalDate.of(2026, 12, 5);
        LocalDate demain = LocalDate.of(2026, 12, 6); // + 24 heures

        creneauTest.setDateDebut(aujourdhui);
        creneauTest.setDateFin(demain);
    }

    @Test
    @DisplayName("Un créneau doit lever une exception si un titre obligatoire est vide")
    void testValidationTitreObligatoire() {
        assertThrows(IllegalArgumentException.class, () -> creneauTest.setTitreActivite(""),
                "Un titre vide doit lever une exception.");
    }

    @Test
    @DisplayName("La date de fin ne peut pas être antérieure à la date de début")
    void testCoherenceDatesChronologiques() {
        LocalDate hier = LocalDate.of(2026, 12, 1); // -24 heures

        assertThrows(IllegalArgumentException.class, () -> {
            creneauTest.setDateFin(hier);
        }, "Mettre une date de fin avant la date de début doit lever une exception.");
    }

    @Test
    @DisplayName("L'association d'un membre valide doit être bidirectionnelle")
    void testAssocierMembre() {
        Membre membre = new Membre();

        creneauTest.associerMembre(membre);

        assertTrue(creneauTest.getMembres().contains(membre), "Le membre doit être présent dans le créneau.");
        assertTrue(membre.getCreneaux().contains(creneauTest), "Le créneau doit être présent chez le membre (réciprocité).");
    }

    @Test
    @DisplayName("Associer un membre 'null' ne doit rien faire et ne pas planter (géré par le log Warning)")
    void testAssocierMembreNull() {
        assertDoesNotThrow(() -> creneauTest.associerMembre(null), "L'association d'un membre null ne doit pas jeter d'exception.");
        assertEquals(0, creneauTest.getMembres().size(), "La liste doit rester vide.");
    }

    @Test
    @DisplayName("Désassocier un membre doit le retirer proprement des deux côtés")
    void testDesassocierMembre() {
        Membre membre = new Membre();
        creneauTest.associerMembre(membre); // On l'associe d'abord

        creneauTest.desassocierMembre(membre);

        assertFalse(creneauTest.getMembres().contains(membre), "Le membre ne doit plus être dans le créneau.");
    }
}