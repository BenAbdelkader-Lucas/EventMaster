package com.example.megasae.controller.Popups.Ajout;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Attribution.Popup_AttributionTagController;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Finance.DepenseRecette;
import com.example.megasae.model.Miscellaneous.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Popup_AjoutDepenseRecetteController {

    private Evenement evenementActif;
    private DepenseRecette depenseRecette = new DepenseRecette();

    @FXML
    private Button btnAnnuler;
    @FXML
    private FlowPane listeTag;
    @FXML
    private RadioButton radioDepense;
    @FXML
    private Spinner<Double> spinPrix;
    @FXML
    private Spinner<Integer> spinQuantite;
    @FXML
    private ToggleButton btnQuantite;
    @FXML
    private TextField txtNom;

    public void initialize() {
        evenementActif = PageController.getEvenementActif();

        spinPrix.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Integer.MAX_VALUE, 15));
        spinQuantite.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1));

        spinQuantite.setEditable(false);
        spinQuantite.setDisable(true);

        btnQuantite.setOnAction(event -> {
            if (btnQuantite.isSelected()) {
                spinQuantite.setDisable(false);
                spinQuantite.setEditable(true);
                spinQuantite.setStyle("");
            } else {
                spinQuantite.setDisable(true);
                spinQuantite.setEditable(false);
                spinQuantite.getValueFactory().setValue(1);
                spinQuantite.setStyle("");
            }
        });

        rafraichirTag();
    }

    @FXML
    void handleAnnulerAction(ActionEvent event) {
        Stage fenetre = (Stage) btnAnnuler.getScene().getWindow();
        fenetre.close();
    }

    @FXML
    void handleConfirmerAction(ActionEvent event) {
        try {
            depenseRecette.setEvenement(evenementActif);
            depenseRecette.setLibelle(txtNom.getText());
            depenseRecette.setMontant(spinPrix.getValue());
            depenseRecette.setQuantite(spinQuantite.getValue());
            depenseRecette.setDepense(radioDepense.isSelected());

            evenementActif.addDepense(depenseRecette);

            Stage fenetre = (Stage) btnAnnuler.getScene().getWindow();
            fenetre.close();
        } catch (Exception e) {
            PageController.getInstance().afficherAlerte(e.getMessage());
        }
    }

    @FXML
    public void rafraichirTag() {
        listeTag.getChildren().clear();
        for (Tag tag : depenseRecette.getTag()) {
            Label labelTag = new Label();
            labelTag.setText(tag.getLibelle());
            labelTag.setStyle("-fx-background-color: " + tag.getCouleur() +
                    "; -fx-background-radius: 6px; -fx-border-radius: 6px; -fx-text-fill: ladder(" +
                    tag.getCouleur() + ", white 49%, black 50%); -fx-font-weight: bold;");
            labelTag.setPadding(new Insets(5, 5, 5, 5));

            listeTag.getChildren().add(labelTag);
        }
    }

    @FXML
    void handleOpenPopupAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Attribution/Popup_AttributionTag.fxml"));
            Scene scene = new Scene(loader.load());

            Popup_AttributionTagController popup_attributionTagController = loader.getController();
            popup_attributionTagController.setDepensesRecette(depenseRecette);

            Stage popupStage = new Stage();

            popupStage.initModality(Modality.APPLICATION_MODAL);

            popupStage.setResizable(false);
            popupStage.setTitle("Ajouter tag");
            popupStage.setScene(scene);

            popupStage.showAndWait();
            rafraichirTag();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Impossible de charger la popup d'ajout", e);
        }
    }
}
