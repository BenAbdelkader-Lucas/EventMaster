package com.example.megasae.model.Evenement;

import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Miscellaneous.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour Evenement.
 */
class EvenementTest {

    private Evenement evenement;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    /**
     * Cette méthode s'exécute avant CHAQUE test.
     * Elle permet de remettre l'environnement "à zéro".
     */
    @BeforeEach
    void setUp() {
        dateDebut = LocalDate.of(2026,6,15);
        dateFin = LocalDate.of(2026,6,19);
        evenement = new Evenement("Evenement test", dateDebut, dateFin);
    }

    // Tests des Constructeurs

    @Test
    void testConstructeurStandard() {
        // Vérifie que l'ID est bien généré automatiquement
        assertNotNull(evenement.getId(), "L'ID ne doit pas être nul après la création");
        assertEquals("Evenement test", evenement.getNom(), "Le nom doit correspondre à celui passé en paramètre");
        assertEquals(dateDebut, evenement.getDateDebut());
        assertEquals(dateFin, evenement.getDateFin());
    }

    @Test
    void testConstructeurAvecTemplate() {
        TemplateEvenement template = new TemplateEvenement();
        template.setNom("Conférence Tech");
        template.setDescription("Une super conférence");

        Evenement eventDepuisTemplate = new Evenement(template);

        assertNotNull(eventDepuisTemplate.getId());
        assertEquals("Conférence Tech", eventDepuisTemplate.getNom());
        assertEquals("Une super conférence", eventDepuisTemplate.getDescription());
        assertEquals(template, eventDepuisTemplate.getTemplateEvenement());
    }

    @Test
    void testConstructeurAvecTemplateNull() {
        // Vérifie que le constructeur lève bien l'exception IllegalArgumentException si on lui passe un template null
        assertThrows(IllegalArgumentException.class, () -> {
            new Evenement(null);
        });
    }

    @Test
    void testSetNomNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            evenement.setNom(null);
        });
    }

    @Test
    void testDateDebutNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            evenement.setDateDebut(null);
        });
    }

    @Test
    void testDateFinNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            evenement.setDateFin(null);
        });
    }

    @Test
    void testSetStatut() {
        evenement.setStatut(StatutEvenement.BROUILLON);
        evenement.setStatut(StatutEvenement.PUBLIE);

        assertEquals(StatutEvenement.PUBLIE, evenement.getStatut(), "Le statut actuel doit être PUBLIE");
        assertEquals(StatutEvenement.BROUILLON, evenement.getStatutPrecedent(), "Le statut précédent doit être sauvegardé (BROUILLON)");
    }

    // Tests de la Gestion des Listes

    @Test
    void testAddMembre() {
        Membre nouveauMembre = new Membre();

        evenement.addMembre(nouveauMembre);

        assertTrue(evenement.getMembres().contains(nouveauMembre), "Le membre doit être dans la liste");
        assertEquals(1, evenement.getMembres().size(), "La liste doit contenir exactement 1 membre");
    }

    @Test
    void testAddMembreNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            evenement.addMembre(null);
        });
    }

    @Test
    void testAddTagAvecDoublon() {
        Tag tagJava = new Tag();

        evenement.addTag(tagJava);
        evenement.addTag(tagJava);

        assertEquals(1, evenement.getTags().size(), "Le tag ne doit pas être ajouté en double");
    }

    @Test
    void testSuprimerLieu() {
        // Arrange
        Lieu lieu = new Lieu();
        evenement.addLieu(lieu);
        assertEquals(1, evenement.getLieux().size());

        evenement.suprimerLieu(lieu);

        assertEquals(0, evenement.getLieux().size(), "Le lieu doit avoir été supprimé de la liste");
    }

    // Tests Capacité et Budget

    @Test
    void testSetBudgetNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            evenement.setBudget(-500.0);
        });
    }

    @Test
    void testAddMembreCapaciteMaxAtteinte() {
        evenement.setCapaciteMax(Integer.valueOf(1));
        Membre membre1 = new Membre();
        Membre membre2 = new Membre();

        assertThrows(IllegalArgumentException.class, () -> {
            evenement.addMembre(membre1);
            evenement.addMembre(membre2);
        });
    }
}