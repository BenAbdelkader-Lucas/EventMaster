package com.example.megasae.model.Equipe;

import com.example.megasae.model.Evenement.Evenement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MembreTest {
    private Membre membrePrincipal;
    private Evenement evenementTest;
    private Role roleTest;

    @BeforeEach
    void setUp() {
        // Configuration d'un rôle (Capacité max du rôle = 3)
        roleTest = new Role("Bénévole", "Aide", 3, 10.0, "#000");

        // Création du membre de référence
        membrePrincipal = new Membre(evenementTest, "Dupont", "Jean", "jean.dupont@mail.com", "0606060606", StatutPresence.INCONNU, "Note");
        membrePrincipal.addRole(roleTest);

        // On ajoute le membre principal dans le rôle (il prend 1 place sur les 5)
        roleTest.getMembres().add(membrePrincipal);
    }

    @Test
    @DisplayName("Les Setters doivent lever une exception si les données obligatoires sont vides")
    void testValidationsObligatoires() {
        assertThrows(IllegalArgumentException.class, () -> membrePrincipal.setNom(""), "Un nom vide doit être refusé.");
        assertThrows(IllegalArgumentException.class, () -> membrePrincipal.setMail(null), "Un mail null doit être refusé.");
    }

    @Test
    @DisplayName("Le changement de présence doit mettre à jour le statut correctement")
    void testMarquerPresence() {
        membrePrincipal.setPresence(StatutPresence.PRESENT);
        assertEquals(StatutPresence.PRESENT, membrePrincipal.getPresence());
    }

    @Test
    @DisplayName("La duplication doit générer le bon nombre de copies formatées 'Nom (i)'")
    void testDuplicationMembres() {
        // Demande de 2 copies (Il y a assez de place globale [1+2 <= 5] et dans le rôle [1+2 <= 3])
        List<Membre> copies = membrePrincipal.dupliquerMembresCopies(2);

        assertEquals(2, copies.size(), "Il doit y avoir exactement 2 copies générées.");
        assertEquals("Dupont (1)", copies.get(0).getNom(), "Le nom de la première copie doit respecter le format.");
        assertEquals("Dupont (2)", copies.get(1).getNom(), "Le nom de la deuxième copie doit respecter le format.");
        assertEquals("Jean", copies.get(0).getPrenom(), "Le prénom doit être identique au modèle.");
        assertEquals(StatutPresence.INCONNU, copies.get(0).getPresence(), "Les copies repartent en statut INCONNU.");
    }

    @Test
    @DisplayName("La duplication doit échouer si la capacité maximale globale de l'événement est dépassée")
    void testDuplicationMembres_ErreurCapaciteGlobale() {
        // Événement actuel = 1 membre. Capacité max = 5. Reste 4 places.
        // Si on demande 5 copies (1 + 5 = 6 > 5), cela doit planter.
        assertThrows(IllegalStateException.class, () -> {
            membrePrincipal.dupliquerMembresCopies(5);
        }, "Le dépassement de la capacité de l'événement doit lever une IllegalStateException.");
    }

    @Test
    @DisplayName(": La duplication doit échouer si la capacité d'un des rôles est dépassée")
    void testDuplicationMembres_ErreurCapaciteRole_UC11() {
        // Événement a de la place (reste 4 places), mais le rôle Bénévole est limité à 3 (1 actuel + 3 demandées = 4 > 3)
        assertThrows(IllegalStateException.class, () -> {
            membrePrincipal.dupliquerMembresCopies(3);
        }, "Le dépassement de la capacité d'un rôle doit lever une IllegalStateException.");
    }
}