package com.example.megasae.model.Miscellaneous;

import com.example.megasae.model.Evenement.Evenement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Tag tag;
    private Evenement evenement;

    @BeforeEach
    void setUp() {
        evenement = new Evenement("Festival", LocalDate.of(2222, 12, 22), LocalDate.of(1212, 12, 12));
        tag = new Tag("VIP", "MEMBRE", "#FF0000", "description", 50, evenement);
    }

    @Test
    void testConstructeurEtGetters() {
        assertEquals("VIP", tag.getLibelle());
        assertEquals("MEMBRE", tag.getTypeCible());
        assertEquals("#FF0000", tag.getCouleur());
        assertEquals("description", tag.getDescription());
        assertEquals(50, tag.getCapacite());
    }

    @Test
    void testSetters() {
        tag.setLibelle("Standard");
        tag.setCapacite(100);
        tag.setTypeCible("EVENEMENT");
        tag.setCouleur("#00FF00");
        tag.setDescription("Tag standard");

        assertEquals("Standard", tag.getLibelle());
        assertEquals(100, tag.getCapacite());
        assertEquals("EVENEMENT", tag.getTypeCible());
        assertEquals("#00FF00", tag.getCouleur());
        assertEquals("Tag standard", tag.getDescription());
    }

    @Test
    void testSetEvenement() {
        Evenement nouvelEvenement = new Evenement("Concert", LocalDate.of(2222, 12, 22), LocalDate.of(1212, 12, 12));
        tag.setEvenement(nouvelEvenement);
        assertEquals(nouvelEvenement, tag.getEvenement());
    }

    @Test
    void testSetCouleurVide(){
        assertThrows(IllegalArgumentException.class, () -> {
            tag.setCouleur("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            tag.setCouleur(null);
        });
    }

    @Test
    void testSetLibelleVide(){
        assertThrows(IllegalArgumentException.class, () -> {
            tag.setLibelle("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            tag.setLibelle(null);
        });
    }

}