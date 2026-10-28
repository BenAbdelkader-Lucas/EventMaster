package com.example.megasae.model.Finance;

import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Evenement.Evenement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests unitaires pour BilanFinancier.
 * Utilise uniquement des vrais objets du modèle.
 */
class BilanFinancierTest {

    private Evenement evenement;
    private BilanFinancier bilanFinancier;

    @BeforeEach
    void setUp() {
        // Initialisation de vrais objets avant chaque test
        evenement = new Evenement("Festival de Musique", LocalDate.of(2026, 6, 12), LocalDate.of(2026, 6, 14));
        bilanFinancier = new BilanFinancier(evenement);
        evenement.setBilanFinancier(bilanFinancier);
    }

    // ── Tests des dépenses et recettes matérielles ──────────────────────────

    @Test
    void testCalculDepense() {
        DepenseRecette depenseMateriel = new DepenseRecette();
        depenseMateriel.setDepense(true);
        depenseMateriel.setMontant(150.0);
        depenseMateriel.setQuantite(1);

        DepenseRecette depense = new DepenseRecette();
        depense.setDepense(true);
        depense.setQuantite(3);
        depense.setMontant(50.0);

        DepenseRecette recetteBillet = new DepenseRecette();
        recetteBillet.setMontant(500.0);
        recetteBillet.setQuantite(2);

        evenement.addDepense(depenseMateriel);
        evenement.addDepense(depense);
        evenement.addDepense(recetteBillet);

        // Vérification : seule la dépense (150.0) doit être comptabilisée
        double totalDepenses = bilanFinancier.calculerDepense();
        assertEquals(300.0, totalDepenses, "Le calcul doit ignorer les recettes.");
    }

    @Test
    void testCalculeRecettes() {
        DepenseRecette depenseMateriel = new DepenseRecette();
        depenseMateriel.setDepense(true);
        depenseMateriel.setMontant(150.0);
        depenseMateriel.setQuantite(1);

        DepenseRecette depense = new DepenseRecette();
        depense.setDepense(true);
        depense.setQuantite(3);
        depense.setMontant(50.0);

        DepenseRecette recetteBillet = new DepenseRecette();
        recetteBillet.setMontant(500.0);
        recetteBillet.setQuantite(2);

        evenement.addDepense(depenseMateriel);
        evenement.addDepense(depense);
        evenement.addDepense(recetteBillet);

        // Vérification : seule la dépense (150.0) doit être comptabilisée
        double totalRecette = bilanFinancier.calculerTotalRecettes();
        assertEquals(1000.0, totalRecette, "Le calcul doit ignorer les dépenses.");
    }

    // ── Tests des coûts liés aux rôles ──────────────────────────────────────

    @Test
    void testCalculerDepenseRoles() {
        Role technicien = new Role();
        technicien.setTarif(50.0);
        Membre membre1 = new Membre();
        Membre membre2 = new Membre();

        technicien.addMembre(membre1);
        technicien.addMembre(membre2);

        Role securite = new Role();
        securite.setTarif(40.0);

        securite.addMembre(membre1);
        securite.addMembre(membre2);

        evenement.addRole(technicien);
        evenement.addRole(securite);

        bilanFinancier.setEvenement(evenement);

        double coutRoles = bilanFinancier.calculerDepenseRoles();
        assertEquals(180, coutRoles, "Le coût total des rôles doit être la somme de (tarif * nombre de membres).");
    }

    @Test
    void testCalculeTarifMoyen() {
        Role rolePayant1 = new Role();
        rolePayant1.setTarif(60.0);

        Role rolePayant2 = new Role();
        rolePayant2.setTarif(40.0);

        Role roleBenevole = new Role();
        roleBenevole.setTarif(null); // Pas de tarif (bénévole)

        evenement.addRole(rolePayant1);
        evenement.addRole(rolePayant2);
        evenement.addRole(roleBenevole);

        // Moyenne attendue : (60 + 40) / 2 rôles payants = 50.0
        double tarifMoyen = bilanFinancier.calculeTarifMoyen();
        assertEquals(50.0, tarifMoyen, "Le tarif moyen doit ignorer les rôles qui ont un tarif null.");
    }




    @Test
    void testCalculerSoldeNet() {
        DepenseRecette recette = new DepenseRecette();
        recette.setQuantite(5);
        recette.setMontant(200.0);
        evenement.addDepense(recette);

        DepenseRecette depense = new DepenseRecette();
        depense.setDepense(true);
        depense.setQuantite(2);
        depense.setMontant(100.0);
        evenement.addDepense(depense);

        Role r = new Role();
        r.setTarif(100.0);
        Membre m = new Membre();
        r.addMembre(m);
        evenement.addRole(r);
        bilanFinancier.setEvenement(evenement);

        // Total recettes = 1000€
        // Total dépenses = 200€ (matériel) + 100€ (rôle) = 300€
        // Solde net attendu = 1000 - 300 = 700€
        double soldeNet = bilanFinancier.calculerSoldeNet();
        assertEquals(700.0, soldeNet, "Le solde net doit être égal aux Recettes moins le total des Dépenses.");
    }


    @Test
    void testCalculerSoldeNetNegatif() {
        DepenseRecette recette = new DepenseRecette();
        recette.setQuantite(1);
        recette.setMontant(200.0);
        evenement.addDepense(recette);

        DepenseRecette depense = new DepenseRecette();
        depense.setDepense(true);
        depense.setQuantite(5);
        depense.setMontant(200.0);
        evenement.addDepense(depense);

        Role r = new Role();
        r.setTarif(100.0);
        Membre m = new Membre();
        r.addMembre(m);
        evenement.addRole(r);
        bilanFinancier.setEvenement(evenement);


        double soldeNet = bilanFinancier.calculerSoldeNet();
        assertEquals(-900.0, soldeNet);
    }


    @Test
    void testExporterBilan() {
        // L'événement s'appelle "Festival de Musique" (avec des espaces)
        File fichier = bilanFinancier.exporterBilan("CSV");

        assertNotNull(fichier, "Le fichier retourné ne doit pas être null.");

        // Vérification du remplacement des espaces par des underscores
        assertEquals("BilanFinancier_Festival_de_Musique.csv", fichier.getName(),
                "Les espaces doivent être remplacés par des tirets du bas.");

        // Sécurité : On nettoie le disque si le fichier s'est réellement créé
        if (fichier.exists()) {
            fichier.delete();
        }
    }
}