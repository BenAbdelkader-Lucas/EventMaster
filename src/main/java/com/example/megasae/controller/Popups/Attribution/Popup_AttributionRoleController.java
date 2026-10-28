package com.example.megasae.controller.Popups.Attribution;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Ajout.Popup_AjoutRoleController;
import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Evenement.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Popup_AttributionRoleController {

    private Evenement evenementActif;
    private Membre membre;

    @FXML private FlowPane listAttribue;
    @FXML private FlowPane listDisponible;
    @FXML private Button btnOk;
    @FXML private ToggleButton toggleModification;
    @FXML private ToggleButton toggleSupprimer;

    @FXML
    public void initialize() {
        this.evenementActif = PageController.getEvenementActif();
        rafraichirAffichageRoles();
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
        rafraichirAffichageRoles();
    }

    private void rafraichirAffichageRoles() {
        if (listAttribue == null || listDisponible == null || evenementActif == null) return;

        listAttribue.getChildren().clear();
        listDisponible.getChildren().clear();

        for (Role role : evenementActif.getRoles()) {
            Button btnRole = new Button(role.getNom());
            String couleur = (role.getCouleur() != null) ? role.getCouleur() : "#2D8CD5";
            btnRole.setStyle("-fx-background-color: " + couleur + "; -fx-background-radius: 6px; -fx-cursor: hand; -fx-text-fill: white;");

            btnRole.setOnAction(e -> {
                if (toggleModification.isSelected()) {
                    ouvrirPopupModification(role);
                } else if (toggleSupprimer.isSelected()) {
                    evenementActif.getRoles().remove(role);
                    rafraichirAffichageRoles();
                } else {
                    // Logique d'attribution classique
                    if (listDisponible.getChildren().contains(btnRole)) {
                        listDisponible.getChildren().remove(btnRole);
                        listAttribue.getChildren().add(btnRole);
                    } else {
                        listDisponible.getChildren().add(btnRole);
                        listAttribue.getChildren().remove(btnRole);
                    }
                }
            });

            if (membre != null && membre.getRoles().contains(role)) {
                listAttribue.getChildren().add(btnRole);
            } else {
                listDisponible.getChildren().add(btnRole);
            }
        }
    }

    private void ouvrirPopupModification(Role role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Ajout/Popup_AjoutRole.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            Popup_AjoutRoleController controller = loader.getController();
            controller.initialisationValeur(role);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            rafraichirAffichageRoles();
            toggleModification.setSelected(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnAjouterRole(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Ajout/Popup_AjoutRole.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            rafraichirAffichageRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toggleActionModification(ActionEvent event) {
        if (toggleModification.isSelected()) toggleSupprimer.setSelected(false);
    }

    @FXML
    void toggleActionSuppresion(ActionEvent event) {
        if (toggleSupprimer.isSelected()) toggleModification.setSelected(false);
    }

    @FXML
    void handleBtnConfirmer(ActionEvent event) {
        if (membre != null) {
            List<Role> nouveauxRoles = new ArrayList<>();
            for (Node node : listAttribue.getChildren()) {
                if (node instanceof Button) {
                    String nomRole = ((Button) node).getText();
                    evenementActif.getRoles().stream()
                            .filter(r -> r.getNom().equals(nomRole))
                            .findFirst()
                            .ifPresent(nouveauxRoles::add);
                }
            }
            membre.setRoles(nouveauxRoles);
            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
        }
        ((Stage) btnOk.getScene().getWindow()).close();
    }

}