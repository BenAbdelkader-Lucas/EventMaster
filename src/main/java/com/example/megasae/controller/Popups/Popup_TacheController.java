package com.example.megasae.controller.Popups;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Tache;
import com.example.megasae.model.Miscellaneous.NiveauImportance;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Popup_TacheController {

    private Evenement evenementParent;
    private Tache tacheEnCoursDeModification = null;

    @FXML
    private TextField txtNomTache;
    @FXML
    private ComboBox<NiveauImportance> comboImportanceTache;
    @FXML
    private DatePicker txtDateTache;
    @FXML
    private ColorPicker comboCouleurTache;
    @FXML
    private Button btnAnnulerTache;
    @FXML
    private Button btnConfirmerTache;
    @FXML
    private Button btnAjouterTag;
    @FXML
    private FlowPane listeTag;

    @FXML
    public void initialize() {
        // Chargement des valeurs de l'importance dans le menu déroulant
        comboImportanceTache.setItems(FXCollections.observableArrayList(NiveauImportance.values()));
        comboImportanceTache.getSelectionModel().select(NiveauImportance.MOYENNE); // Valeur par défaut
    }

    public void setEvenementParent(Evenement evenement) {
        this.evenementParent = evenement;
    }

    public void chargerDonneesPourModification(Tache tache) {
        this.tacheEnCoursDeModification = tache;

        txtNomTache.setText(tache.getLibelle());

        if (tache.getImportance() != null) {
            comboImportanceTache.setValue(tache.getImportance());
        }

        if (tache.getCouleur() != null && !tache.getCouleur().isEmpty()) {
            try {
                comboCouleurTache.setValue(Color.web(tache.getCouleur()));
            } catch (Exception e) {
                comboCouleurTache.setValue(Color.WHITE);
            }
        }

        if (tache.getDate() != null) {
            txtDateTache.setValue(tache.getDate());
        }
    }

    @FXML
    private void handleAnnulerTache(ActionEvent event) {
        fermerFenetre();
    }

    @FXML
    private void handleConfirmerTache(ActionEvent event) {
        if (evenementParent == null || txtNomTache.getText().trim().isEmpty()) {
            fermerFenetre();
            return;
        }

        String libelle = txtNomTache.getText().trim();

        Color selectedColor = comboCouleurTache.getValue();
        String couleur = PageController.toHexString(comboCouleurTache.getValue());

        NiveauImportance importance = comboImportanceTache.getValue();
        if (importance == null) {
            importance = NiveauImportance.MOYENNE;
        }

        LocalDate dateEcheance = txtDateTache.getValue();

        if (tacheEnCoursDeModification != null) {
            // Mode modification
            tacheEnCoursDeModification.setLibelle(libelle);
            tacheEnCoursDeModification.setCouleur(couleur);
            tacheEnCoursDeModification.setImportance(importance);
            tacheEnCoursDeModification.setDate(dateEcheance);
        } else {
            // Mode ajout
            Tache nouvelleTache = new Tache(libelle, couleur, importance, dateEcheance, evenementParent);
            nouvelleTache.setEstCochee(false);
            evenementParent.addTache(nouvelleTache);
        }

        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtNomTache.getScene().getWindow();
        stage.close();
    }

    @FXML
        //TODO réutilise la fonction de PageController ?
    void handleOpenPopupAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Popup_AttributionTag.fxml"));
            Scene scene = new Scene(loader.load());

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.setTitle("Ajouter tag");
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Impossible de charger la popup d'ajout", e);
        }
    }

}