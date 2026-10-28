package com.example.megasae.model.Equipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    private Role roleStaff;

    @BeforeEach
    void setUp() {
        // Initialisation d'un rôle selon l'UC13 (nom, description, capacité max, tarif, couleur)
        roleStaff = new Role("Staff Logistique", "Gestion du matériel", 2, 15.0, "#FF5733");
    }

    @Test
    @DisplayName("Le constructeur doit correctement initialiser tous les attributs du rôle")
    void testInitialisationRole() {
        assertNotNull(roleStaff.getId(), "L'UUID doit être généré automatiquement.");
        assertEquals("Staff Logistique", roleStaff.getNom());
        assertEquals(2, roleStaff.getCapaciteMaximale());
    }

    @Test
    @DisplayName("L'indicateur de capacité doit passer à vrai dès que le quota maximal de membres est atteint")
    void testCapaciteMaximaleAtteinte() {
        // Au départ, aucun membre n'est assigné
        assertFalse(roleStaff.isCapaciteAtteinte(), "La capacité ne devrait pas être atteinte au départ.");

        // On simule l'ajout d'un premier membre
        roleStaff.getMembres().add(new Membre());
        assertFalse(roleStaff.isCapaciteAtteinte(), "La capacité n'est pas encore atteinte (1/2).");

        // On simule l'ajout d'un deuxième membre (quota max atteint)
        roleStaff.getMembres().add(new Membre());
        assertTrue(roleStaff.isCapaciteAtteinte(), "La capacité doit être déclarée atteinte (2/2).");
    }
}
