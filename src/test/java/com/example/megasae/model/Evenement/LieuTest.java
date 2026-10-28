package com.example.megasae.model.Evenement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour Lieu.
 */
class LieuTest {

    private Evenement event;

    @BeforeEach
    void setUp() {
        event = new Evenement("Concert Test", LocalDate.of(2026, 6, 15), LocalDate.of(2026, 6, 18));
    }

    // Tests des Constructeurs

    @Test
    void testConstructeurComplet() {
        Lieu lieu = new Lieu(event, "salle concert", "67 avenue de l'avenue", 5000);

        assertNotNull(lieu.getId(), "L'ID doit être généré automatiquement");
        assertEquals(event, lieu.getEvenement());
        assertEquals("salle concert", lieu.getNom());
        assertEquals("67 avenue de l'avenue", lieu.getAdresse());
        assertEquals(5000, lieu.getCapaciteMax());
    }




    @Test
    void testSetCapaciteMaxNegative() {
        Lieu lieu = new Lieu();

        assertThrows(IllegalArgumentException.class, () -> {
            lieu.setCapaciteMax(-50);
        });
    }

    @Test
    void testSetLibelleVide(){
        Lieu lieu = new Lieu(event, "lieu1", "adresse", 4);
        assertThrows(IllegalArgumentException.class, () -> {
            lieu.setNom(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            lieu.setNom("");
        });
    }
}