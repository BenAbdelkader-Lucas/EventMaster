package com.example.megasae.controller.Popups;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Evenement.StatutEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Popup_StatutController {

    @FXML private Button btnStatutEnCours;
    @FXML private Button btnStatutBrouillon;
    @FXML private Button btnStatutArchive;
    @FXML private Button btnStatutPublie;
    @FXML private Button btnStatutCloture;
    @FXML private Button btnStatutAnnule;

    /**
     * Méthode appelée lors du clic sur l'un des boutons de la popup.
     */
    @FXML
    void handleSelectStatutAction(ActionEvent event) {
        Evenement evenementActif = PageController.getEvenementActif();
        PageController mainController = PageController.getInstance();

        // Sécurité si aucun événement n'est sélectionné
        if (evenementActif == null) {
            fermerFenetre(event);
            return;
        }

        Button boutonClique = (Button) event.getSource();
        StatutEvenement nouveauStatut = StatutEvenement.BROUILLON; // Statut par défaut

        // Association entre le bouton cliqué et l'élément de ton Enum
        if (boutonClique == btnStatutEnCours) {
            nouveauStatut = StatutEvenement.ENCOURS;
        } else if (boutonClique == btnStatutBrouillon) {
            nouveauStatut = StatutEvenement.BROUILLON;
        } else if (boutonClique == btnStatutArchive) {
            nouveauStatut = StatutEvenement.ARCHIVE;
        } else if (boutonClique == btnStatutPublie) {
            nouveauStatut = StatutEvenement.PUBLIE;
        } else if (boutonClique == btnStatutCloture) {
            nouveauStatut = StatutEvenement.CLOTURE;
        } else if (boutonClique == btnStatutAnnule) {
            nouveauStatut = StatutEvenement.ANNULE;
        }

        // Mise à jour de l'événement actif avec le bon type d'Enum
        evenementActif.setStatut(nouveauStatut);
        // Sauvegarde immédiate dans le fichier .dat/.ser
        mainController.getGestionnaire().sauvegarderDonnees(mainController.getGestionnaire());

        // Fermeture automatique
        fermerFenetre(event);
    }

    private void fermerFenetre(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

}