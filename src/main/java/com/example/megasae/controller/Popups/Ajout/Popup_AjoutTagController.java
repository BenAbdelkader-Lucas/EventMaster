package com.example.megasae.controller.Popups.Ajout;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Popup_AjoutTagController {

    private Evenement evenementActif;
    private Tag oldTag;

    @FXML private TextField txtNomTag;
    @FXML private TextField txtDescriptionTag;
    @FXML private Spinner<Integer> spinCapaciteTag;
    @FXML private ColorPicker colorPickerTag;
    @FXML private Button btnAnnuler;
    @FXML private Button btnConfirmer;

    @FXML
    public void initialize() {
        evenementActif = PageController.getEvenementActif();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
        spinCapaciteTag.setValueFactory(valueFactory);
        spinCapaciteTag.setEditable(true);

        colorPickerTag.setValue(Color.web("#2D8CD5"));
    }

    @FXML
    public void initialisationValeur(Tag tag) {
        oldTag = tag;
        txtNomTag.setText(tag.getLibelle());
        colorPickerTag.setValue(Color.web(tag.getCouleur()));
        spinCapaciteTag.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, tag.getCapacite()));
        txtDescriptionTag.setText(tag.getDescription());
    }

    /**
     * Clic sur le bouton CONFIRMER
     */
    @FXML
    public void handleConfirmerTag(ActionEvent event) {
        try {
            evenementActif.getTags().remove(oldTag);
            Tag tag = new Tag(txtNomTag.getText(), "", PageController.toHexString(colorPickerTag.getValue()), txtDescriptionTag.getText(), spinCapaciteTag.getValue(), evenementActif);
            evenementActif.addTag(tag);
            fermerPopup();
        } catch (Exception e) {
            PageController.getInstance().afficherAlerte(e.getMessage());
        }
    }

    /**
     * Clic sur le bouton ANNULER
     */
    @FXML
    public void handleAnnulerTag(ActionEvent event) {
        fermerPopup();
    }

    /**
     * Utilitaire pour fermer la fenêtre actuelle
     */
    private void fermerPopup() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    /**
     * Effet de survol sur le bouton annuler (entrer)
     * @param event
     */
    @FXML
    void handleHoverAnnulerEnt(MouseEvent event) {
        btnAnnuler.setStyle("-fx-background-color: #A93244; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-cursor: hand;");
    }

    /**
     * Effet de survol sur le bouton annuler (quitter)
     * @param event
     */
    @FXML
    void handleHoverAnnulerSort(MouseEvent event) {
        btnAnnuler.setStyle("-fx-background-color: #D3455B; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;");
    }

    /**
     * Effet de survol sur le bouton confirmer (entrer)
     * @param event
     */
    @FXML
    void handleHoverConfirmerEnt(MouseEvent event) {
        btnConfirmer.setStyle("-fx-background-color: #1F6FAA; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-cursor: hand;");
    }

    /**
     * Effet de survol sur le bouton confirmer (quitter)
     * @param event
     */
    @FXML
    void handleHoverConfirmerSort(MouseEvent event) {
        btnConfirmer.setStyle("-fx-background-color: #2D8CD5; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;");
    }

}