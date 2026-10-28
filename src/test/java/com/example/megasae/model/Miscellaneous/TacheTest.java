package com.example.megasae.model.Miscellaneous;

import com.example.megasae.model.Evenement.Evenement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TacheTest {

    private Evenement evenement;
    private Tache tache;

    @BeforeEach
    void setUp() {
        evenement = new Evenement("Concert", LocalDate.of(2026, 06, 15), LocalDate.of(2026, 9, 29));
        tache = new Tache("Réserver traiteur", "Rouge", NiveauImportance.HAUTE, LocalDate.now(), evenement);
    }

    @Test
    void testConstructeur() {
        assertEquals("Réserver traiteur", tache.getLibelle());
        assertEquals("Rouge", tache.getCouleur());
        assertEquals(evenement, tache.getEvenement());
        assertNotNull(tache.getId(), "L'ID ne devrait pas être nul");
    }

    @Test
    void testSetEstCochee() {
        tache.setEstCochee(true);
        assertTrue(tache.isEstCochee(), "La tâche devrait être cochée");

        tache.setEstCochee(false);
        assertFalse(tache.isEstCochee(), "La tâche ne devrait pas être cochée");
    }

    @Test
    void testSetCouleurException() {
        assertThrows(IllegalArgumentException.class, () -> {
            tache.setCouleur(null);
        });
    }

    @Test
    void testSetDate() {
        LocalDate dateFuture = LocalDate.now().plusDays(5);
        tache.setDate(dateFuture);
        assertEquals(dateFuture, tache.getDate());
    }

    @Test
    void testSetEvenement() {
        Evenement nouvelEvenement = new Evenement("Conférence", LocalDate.of(2026, 06, 15), LocalDate.of(2026, 9, 29));
        tache.setEvenement(nouvelEvenement);
        assertEquals(nouvelEvenement, tache.getEvenement());
    }

    @Test
    void testSetLibelleVide(){
        assertThrows(IllegalArgumentException.class, () -> {
            tache.setLibelle(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            tache.setLibelle("");
        });
    }
}