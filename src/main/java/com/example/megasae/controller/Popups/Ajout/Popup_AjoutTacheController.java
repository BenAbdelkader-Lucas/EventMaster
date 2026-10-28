package com.example.megasae.controller.Popups.Ajout;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Attribution.Popup_AttributionTagController;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Tache;
import com.example.megasae.model.Miscellaneous.NiveauImportance;
import com.example.megasae.model.Miscellaneous.Tag;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Popup_AjoutTacheController {

    private Evenement evenementActif;
    private Tache tacheEnCoursDeModification = null;
    private boolean modeModification = false;

    @FXML
    private Label lblTitreFormulaire;
    @FXML
    private Button btnAnnulerTache;
    @FXML
    private Button btnConfirmerTache;
    @FXML
    private ComboBox<NiveauImportance> comboBoxImportance;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField txtNomTache;
    @FXML
    private TextField txtTagsTache;
    @FXML
    private Button btnAjouterTag;

    @FXML
    public void initialize() {
        evenementActif = PageController.getEvenementActif();

        // Remplissage propre de la ComboBox d'importance
        comboBoxImportance.setItems(FXCollections.observableArrayList(NiveauImportance.values()));
        comboBoxImportance.getSelectionModel().select(NiveauImportance.MOYENNE); // Sélection par défaut

        // Si on est en mode création, on crée une instance temporaire pour collecter les futurs tags
        if (tacheEnCoursDeModification == null) {
            tacheEnCoursDeModification = new Tache();
        }
    }

    public void chargerDonneesPourModification(Tache tache) {
        this.tacheEnCoursDeModification = tache;
        this.modeModification = true;

        lblTitreFormulaire.setText("Modifier la Tâche");
        txtNomTache.setText(tache.getLibelle());

        if (tache.getImportance() != null) {
            comboBoxImportance.setValue(tache.getImportance());
        }

        datePicker.setValue(tache.getDate());

        rafraichirAffichageTags();
    }

    @FXML
    void handleOpenPopupTagAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Attribution/Popup_AttributionTag.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

            Popup_AttributionTagController popup_attributionTagController = loader.getController();
            popup_attributionTagController.setTache(tacheEnCoursDeModification);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.setTitle("Ajouter tag à la tâche");
            popupStage.setScene(scene);

            popupStage.showAndWait();

            // Met à jour le champ texte visible après la fermeture de la popup
            rafraichirAffichageTags();

        } catch (Exception e) {
            e.printStackTrace();
            PageController.getInstance().afficherAlerte("Impossible de charger la popup des tags : " + e.getMessage());
        }
    }

    private void rafraichirAffichageTags() {
        if (tacheEnCoursDeModification != null && tacheEnCoursDeModification.getTags() != null) {
            StringBuilder sb = new StringBuilder();
            for (Tag tag : tacheEnCoursDeModification.getTags()) {
                sb.append(tag.getLibelle()).append(", ");
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }
            txtTagsTache.setText(sb.toString());
        }
    }

    @FXML
    private void handleAnnulerTache(ActionEvent event) {
        fermerFenetre();
    }

    @FXML
    private void handleConfirmerTache(ActionEvent event) {
        if (txtNomTache.getText() == null || txtNomTache.getText().trim().isEmpty()) {
            PageController.getInstance().afficherAlerte("Le nom de la tâche ne peut pas être vide !");
            return;
        }

        String libelle = txtNomTache.getText().trim();

        NiveauImportance importance = comboBoxImportance.getValue();
        LocalDate dateEcheance = datePicker.getValue();

        try {
            if (modeModification) {
                // Mode Modification
                tacheEnCoursDeModification.setLibelle(libelle);
                tacheEnCoursDeModification.setImportance(importance);
                tacheEnCoursDeModification.setDate(dateEcheance);
            } else {
                // Mode Ajout
                if (evenementActif == null) {
                    evenementActif = PageController.getEvenementActif();
                }
                Tache nouvelleTache = new Tache(libelle, Color.RED.toString(), importance, dateEcheance, evenementActif);
                nouvelleTache.setTags(tacheEnCoursDeModification.getTags());
                evenementActif.addTache(nouvelleTache);
            }

            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());

            fermerFenetre();
        } catch (Exception e) {
            PageController.getInstance().afficherAlerte("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtNomTache.getScene().getWindow();
        stage.close();
    }

}