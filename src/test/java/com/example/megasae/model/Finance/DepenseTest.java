package com.example.megasae.model.Finance;

import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour DepenseRecette.
 * Utilise uniquement de vrais objets sans outils de simulation.
 */
class DepenseRecetteTest {

    private Evenement vraiEvenement;

    @BeforeEach
    void setUp() {
        vraiEvenement = new Evenement("Mon Super Événement", LocalDate.of(2026, 6, 12), LocalDate.of(2026, 6, 15));
    }


    @Test
    void testConstructeurComplet_AssigneTousLesChamps() {
        ArrayList<Tag> listeTags = new ArrayList<>();

        DepenseRecette mouvement = new DepenseRecette("Achat de Boissons", 2.5, 100, listeTags, true, vraiEvenement);

        assertNotNull(mouvement.getId(), "L'identifiant UUID doit être généré automatiquement.");
        assertEquals("Achat de Boissons", mouvement.getLibelle());
        assertEquals(2.5, mouvement.getMontant());
        assertEquals(100, mouvement.getQuantite());
        assertTrue(mouvement.isDepense(), "Ce mouvement doit être enregistré comme une dépense.");
        assertEquals(vraiEvenement, mouvement.getEvenement());
    }


    @Test
    void testGetCoutTotal() {
        DepenseRecette mouvement = new DepenseRecette();
        mouvement.setMontant(15.0);
        mouvement.setQuantite(4);

        // Cout attendu : 15.0 * 4 = 60.0
        double total = mouvement.getCoutTotal();
        assertEquals(60.0, total, "Le coût total doit être égal au montant unitaire multiplié par la quantité.");
    }

    // ── Tests des Règles de validation (Erreurs / Exceptions) ───────────────

    @Test
    void testSetLibelleVide_Vide_LanceException() {
        DepenseRecette mouvement = new DepenseRecette();

        // On vérifie qu'une IllegalArgumentException est levée si le libellé est vide
        assertThrows(IllegalArgumentException.class, () -> {
            mouvement.setLibelle("");
        });
    }

    @Test
    void testSetMontantNégatif() {
        DepenseRecette mouvement = new DepenseRecette();

        // Vérification pour un montant de 0
        assertThrows(IllegalArgumentException.class, () -> {
            mouvement.setMontant(0);
        }, "Un montant égal à 0 doit déclencher une erreur.");

        // Vérification pour un montant négatif
        assertThrows(IllegalArgumentException.class, () -> {
            mouvement.setMontant(-12.5);
        }, "Un montant négatif doit déclencher une erreur.");
    }

    @Test
    void testSetQuantiteNegatif() {
        DepenseRecette mouvement = new DepenseRecette();

        // On vérifie qu'une quantité négative est interdite
        assertThrows(IllegalArgumentException.class, () -> {
            mouvement.setQuantite(-1);
        }, "Une quantité négative doit déclencher une erreur.");
    }

    @Test
    void testSetEvenementNull() {
        DepenseRecette mouvement = new DepenseRecette();

        // On vérifie qu'on ne peut pas passer un événement null
        assertThrows(IllegalArgumentException.class, () -> {
            mouvement.setEvenement(null);
        }, "Associer un événement null doit déclencher une erreur.");
    }


    @Test
    void testToStringPourDepense() {
        DepenseRecette mouvement = new DepenseRecette();
        mouvement.setLibelle("Location Salle");
        mouvement.setMontant(400.0);
        mouvement.setQuantite(1);
        mouvement.setDepense(true); // Dépense

        String texte = mouvement.toString();

        assertTrue(texte.contains("Location Salle"));
        assertTrue(texte.contains("400.0"));
    }

    @Test
    void testToStringPourRecette() {
        DepenseRecette mouvement = new DepenseRecette();
        mouvement.setLibelle("Vente de tickets");
        mouvement.setMontant(12.0);
        mouvement.setQuantite(10);
        mouvement.setDepense(false); // Recette

        String texte = mouvement.toString();

        // Coût total attendu affiché : 12.0 * 10 = 120.0
        assertTrue(texte.contains("Vente de tickets"));
        assertTrue(texte.contains("12"));
    }
}