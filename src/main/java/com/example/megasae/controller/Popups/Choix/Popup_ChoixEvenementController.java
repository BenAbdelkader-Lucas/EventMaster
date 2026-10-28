package com.example.megasae.controller.Popups.Choix;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Popup_EvenementController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Contrôleur de la popup de choix de création d'événement.
 * Permet à l'utilisateur de choisir entre un événement vierge, l'utilisation d'un template, ou l'annulation.
 */
public class Popup_ChoixEvenementController {

    @FXML private Button btnNouveau;
    @FXML private Button btnTemplate;
    @FXML private Button btnAnnuler;

    /**
     * Action déclenchée par le bouton "Nouveau".
     * Ouvre la vue de création d'un nouvel événement vierge et ferme la popup actuelle.
     */
    @FXML
    void handleNouveauAction(ActionEvent event) {
        PageController.getInstance().setEvenementActif(null);
        Popup_EvenementController.setTemplateSelectionne(null);
        PageController.getInstance().afficherVue("Popup_Evenement.fxml", "Créer un Événement");
        fermerFenetre();
    }

    /**
     * Action déclenchée par le bouton "Template".
     * Ouvre la liste des templates disponibles et ferme la popup actuelle.
     */
    @FXML
    void handleTemplateAction(ActionEvent event) {
        PageController.getInstance().afficherVue("Popup_ChoixTemplates.fxml", "Sélectionner un modèle");
        fermerFenetre();
    }

    /**
     * Action déclenchée par le bouton "Annuler".
     * Ferme la fenêtre sans effectuer d'action.
     */
    @FXML
    void handleAnnulerAction(ActionEvent event) {
        fermerFenetre();
    }

    /**
     * Méthode utilitaire privée pour récupérer la fenêtre actuelle et la fermer.
     */
    private void fermerFenetre() {
        // On récupère la scène puis la fenêtre à partir d'un des boutons
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

}