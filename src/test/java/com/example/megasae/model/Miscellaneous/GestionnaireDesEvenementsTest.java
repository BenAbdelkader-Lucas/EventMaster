package com.example.megasae.model.Miscellaneous;

import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Finance.BilanFinancier;
import com.example.megasae.model.Finance.DepenseRecette;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe GestionnaireDesEvenements.
 */
class GestionnaireDesEvenementsTest {

    private GestionnaireDesEvenements gestionnaire;
    private Evenement evenement1;
    private Evenement evenement2;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireDesEvenements();
        evenement1 = new Evenement("Festival Musique", LocalDate.of(2026, 6, 15), LocalDate.of(2026, 6, 16));
        evenement2 = new Evenement("Conférence Tech", LocalDate.of(2026, 6, 16), LocalDate.of(2026, 6, 22));
    }

    // ── Tests des ajouts et suppressions ────────────────────────────────────────

    @Test
    void testAddEvenementAjoutNormal() {
        gestionnaire.addEvenement(evenement1);
        assertEquals(1, gestionnaire.getEvenements().size(), "La liste doit contenir 1 événement.");
        assertTrue(gestionnaire.getEvenements().contains(evenement1));
    }

    @Test
    void testAddEvenementNeDoitPasAjouterNull() {
        assertThrows(IllegalArgumentException.class, () -> {
                    gestionnaire.addEvenement(null);
                });
        assertEquals(0, gestionnaire.getEvenements().size(), "Un événement null ne doit pas être ajouté.");
    }

    @Test
    void testAddEvenementNeDoitPasAjouterDoublon() {

        assertThrows(IllegalArgumentException.class, () -> {
        gestionnaire.addEvenement(evenement1);
        gestionnaire.addEvenement(evenement1);
        });

        assertEquals(1, gestionnaire.getEvenements().size(), "Les doublons ne doivent pas être ajoutés.");
    }

    @Test
    void testSupprimerEvenement() {
        gestionnaire.addEvenement(evenement1);
        gestionnaire.supprimerEvenement(evenement1);
        assertEquals(0, gestionnaire.getEvenements().size(), "La liste doit être vide après la suppression.");
    }

    // ── Tests de la duplication ─────────────────────────────────────────────────

    @Test
    void testDupliquerEvenement() {
        // Selon ta Javadoc : "Crée une copie complète (deep copy)... reçoit un nouvel identifiant"
        gestionnaire.addEvenement(evenement1);

        Evenement copie = gestionnaire.dupliquer(evenement1);

        assertNotNull(copie, "La copie ne doit pas être nulle.");
        assertNotEquals(evenement1.getId(), copie.getId(), "La copie doit avoir un ID différent (nouvelle instance).");
        assertEquals(2, gestionnaire.getEvenements().size(), "Le gestionnaire doit maintenant contenir 2 événements.");
    }

    // ── Tests des calculs statistiques ──────────────────────────────────────────

    @Test
    void testCalculerProgressionMoyenneTachesAvecTaches() {
        gestionnaire.addEvenement(evenement1);

        // Création de fausses tâches (Note : adapte selon ton vrai constructeur Tache)
        Tache tache1 = new Tache();
        tache1.setEstCochee(true); // 100% pour la tâche 1

        Tache tache2 = new Tache();
        tache2.setEstCochee(false); // 0% pour la tâche 2

        evenement1.addTache(tache1);
        evenement1.addTache(tache2);

        // 1 tâche cochée sur 2 au total = 50%
        double moyenne = gestionnaire.calculerProgressionMoyenneTaches();
        assertEquals(50.0, moyenne, "La progression moyenne doit être de 50%.");
    }

    @Test
    void testCalculerProgressionMoyenneTachesSansEvenement() {
        double moyenne = gestionnaire.calculerProgressionMoyenneTaches();
        assertEquals(0.0, moyenne, "La progression doit être de 0 s'il n'y a aucune tâche/événement.");
    }


    @Test
    void testCalculerBeneficesTotal() {

        gestionnaire.addEvenement(evenement1);
        DepenseRecette depense = new DepenseRecette();
        DepenseRecette recette = new DepenseRecette();

        depense.setMontant(10);
        depense.setDepense(true);
        depense.setQuantite(2);

        recette.setMontant(40);
        recette.setQuantite(1);

        evenement1.addDepense(depense);
        evenement1.addDepense(recette);

        BilanFinancier bilanFinancier = new BilanFinancier(evenement1);

        evenement1.setBilanFinancier(bilanFinancier);

        assertEquals(20, gestionnaire.calculerBeneficesTotal());
    }

    @Test
    void testCalculeNombreTotalMembres() {
        gestionnaire.addEvenement(evenement1);
        gestionnaire.addEvenement(evenement2);

        evenement1.addMembre(new Membre());
        evenement2.addMembre(new Membre());
        evenement2.addMembre(new Membre());

        assertEquals(3, gestionnaire.calculerNombreMembres());
    }
}