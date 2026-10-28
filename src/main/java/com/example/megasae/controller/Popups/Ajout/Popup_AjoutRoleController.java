package com.example.megasae.controller.Popups.Ajout;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Evenement.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Popup_AjoutRoleController {

    private Evenement evenementActif;
    private Role roleEnCoursEdition = null;
    private boolean modeModification = false;

    @FXML private Label lblTitreRole;
    @FXML private TextField txtNomRole;
    @FXML private TextField txtDescriptionRole;
    @FXML private Spinner<Integer> spinCapaciteRole;
    @FXML private Spinner<Double> spinTarifRole;
    @FXML private ColorPicker colorPickerRole;

    @FXML private Button btnAnnuler;
    @FXML private Button btnConfirmer;

    @FXML
    public void initialize() {
        evenementActif = PageController.getEvenementActif();
        // Configuration du spinner de TARIF UNITAIRE
        SpinnerValueFactory<Double> valueFactoryTarif = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 0.5);
        spinTarifRole.setValueFactory(valueFactoryTarif);

        // Configuration du spinner de CAPACITÉ
        SpinnerValueFactory<Integer> valueFactoryCapacite = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        spinCapaciteRole.setValueFactory(valueFactoryCapacite);
    }

    public void initialisationValeur(Role role) {
        this.roleEnCoursEdition = role;
        this.modeModification = true;

        lblTitreRole.setText("Modification de : " + role.getNom());
        txtNomRole.setText(role.getNom());
        txtDescriptionRole.setText(role.getDescription());
        spinCapaciteRole.getValueFactory().setValue(role.getCapaciteMaximale());

        if (spinTarifRole != null && spinTarifRole.getValueFactory() != null) {
            spinTarifRole.getValueFactory().setValue(role.getTarif());
        }

        if (role.getCouleur() != null) {
            colorPickerRole.setValue(Color.web(role.getCouleur()));
        }
    }

    @FXML
    public void handleConfirmerRole(ActionEvent event) {
        try {
            String nom = txtNomRole.getText();
            String description = txtDescriptionRole.getText();
            double tarif = spinTarifRole.getValue();
            int capacite = spinCapaciteRole.getValue();
            String couleur = PageController.toHexString(colorPickerRole.getValue());

            if (nom == null || nom.trim().isEmpty()) {
                PageController.getInstance().afficherAlerte("Le nom du rôle est obligatoire.");
                return;
            }

            if (modeModification && roleEnCoursEdition != null) {
                roleEnCoursEdition.setNom(nom);
                roleEnCoursEdition.setDescription(description);
                roleEnCoursEdition.setCapaciteMaximale(capacite);
                roleEnCoursEdition.setTarif(tarif);
                roleEnCoursEdition.setCouleur(couleur);
            } else {
                Role nouveauRole = new Role(nom, description, capacite, tarif, couleur);
                nouveauRole.setEvenement(evenementActif);
                if (evenementActif != null) {
                    evenementActif.getRoles().add(nouveauRole);
                }
            }

            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
            fermerPopup();
        } catch (Exception e) {
            PageController.getInstance().afficherAlerte("Erreur : " + e.getMessage());
        }
    }

    @FXML
    public void handleAnnulerRole(ActionEvent event) {
        fermerPopup();
    }

    private void fermerPopup() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    @FXML void handleHoverAnnulerEnt(MouseEvent event) { btnAnnuler.setStyle("-fx-background-color: #A93244; -fx-text-fill: white; -fx-cursor: hand;"); }
    @FXML void handleHoverAnnulerSort(MouseEvent event) { btnAnnuler.setStyle("-fx-background-color: transparent; -fx-border-color: #CBD5E1;"); }
    @FXML void handleHoverConfirmerEnt(MouseEvent event) { btnConfirmer.setStyle("-fx-background-color: #1F6FAA; -fx-text-fill: white; -fx-cursor: hand;"); }
    @FXML void handleHoverConfirmerSort(MouseEvent event) { btnConfirmer.setStyle("-fx-background-color: transparent; -fx-border-color: #CBD5E1;"); }

}