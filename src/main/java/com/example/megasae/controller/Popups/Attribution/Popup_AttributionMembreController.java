package com.example.megasae.controller.Popups.Attribution;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Ajout.Popup_AjoutMembreController;
import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Evenement.Creneau;
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

public class Popup_AttributionMembreController {

    private Evenement evenementActif;
    private Creneau creneau;

    @FXML private Button btnOk;

    @FXML private FlowPane listAttribue;
    @FXML private FlowPane listDisponible;

    @FXML private ToggleButton toggleModification;
    @FXML private ToggleButton toggleSupprimer;

    @FXML
    public void initialize() {
        evenementActif = PageController.getEvenementActif();
        rafraichirAffichageMembre();
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
        rafraichirAffichageMembre();
    }

    private void rafraichirAffichageMembre() {
        listAttribue.getChildren().clear();
        listDisponible.getChildren().clear();

        if (evenementActif == null) return;

        for (Membre membre : evenementActif.getMembres()) {
            Button btnMembre = new Button();
            btnMembre.setText(membre.getNom() + " " + membre.getPrenom());
            btnMembre.setStyle("-fx-background-radius: 6px; -fx-border-radius: 6px; -fx-font-weight: bold; -fx-cursor: hand;");

            btnMembre.setUserData(membre);

            btnMembre.setOnAction(e -> {
                if (toggleModification.isSelected()) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Ajout/Popup_AjoutMembre.fxml"));
                        Scene scene = new Scene(loader.load());

                        Popup_AjoutMembreController popup_AjoutMembreController = loader.getController();
                        popup_AjoutMembreController.initialisationValeur(membre);

                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setResizable(false);
                        popupStage.setTitle("Modification de Membre");
                        popupStage.setScene(scene);

                        popupStage.showAndWait();
                        rafraichirAffichageMembre();
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                } else if (toggleSupprimer.isSelected()) {
                    evenementActif.getMembres().remove(membre);
                    rafraichirAffichageMembre();
                } else {
                    if (listDisponible.getChildren().contains(btnMembre)) {
                        listDisponible.getChildren().remove(btnMembre);
                        listAttribue.getChildren().add(btnMembre);
                    } else {
                        listDisponible.getChildren().add(btnMembre);
                        listAttribue.getChildren().remove(btnMembre);
                    }
                }
            });

            boolean estAttribue = false;
            if (creneau != null && creneau.getMembres() != null) {
                for (Membre m : creneau.getMembres()) {
                    if (m.getId().equals(membre.getId())) {
                        estAttribue = true;
                        break;
                    }
                }
            }

            if (estAttribue) {
                listAttribue.getChildren().add(btnMembre);
            } else {
                listDisponible.getChildren().add(btnMembre);
            }
        }
    }

    @FXML
    void handleBtnAjouterTag(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Ajout/Popup_AjoutMembre.fxml"));
            Scene scene = new Scene(loader.load());
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Créer un nouveau membre");
            popupStage.setScene(scene);
            popupStage.showAndWait();

            rafraichirAffichageMembre();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toggleActionModification(ActionEvent event) {

        if (toggleSupprimer.isSelected()) toggleSupprimer.setSelected(false);

        if (toggleModification.isSelected()) {
            // Mode modification
            for (Node btn : listAttribue.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().add("modeModif");
                }
            }
            for (Node btn : listDisponible.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().add("modeModif");
                }
            }
        } else {
            // Mode hors modification
            for (Node btn : listAttribue.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().remove("modeModif");
                }
            }
            for (Node btn : listDisponible.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().remove("modeModif");
                }
            }
        }
    }

    @FXML
    void toggleActionSuppresion(ActionEvent event) {

        if (toggleModification.isSelected()) toggleModification.setSelected(false);

        if (toggleSupprimer.isSelected()) {
            // Mode modification
            for (Node btn : listAttribue.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().add("modeSuppresion");
                }
            }
            for (Node btn : listDisponible.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().add("modeSuppresion");
                }
            }
        } else {
            // Mode hors modification
            for (Node btn : listAttribue.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().remove("modeSuppresion");
                }
            }
            for (Node btn : listDisponible.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().remove("modeSuppresion");
                }
            }
        }
    }

    @FXML
    void handleBtnConfirmer(ActionEvent event) {
        List<Membre> membresSelectionnes = new ArrayList<>();

        for (Node node : listAttribue.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                Membre membre = (Membre) btn.getUserData();
                if (membre != null) {
                    membresSelectionnes.add(membre);
                }
            }
        }

        if (creneau != null) {
            creneau.setMembres(membresSelectionnes);
        }

        Stage fenetre = (Stage) btnOk.getScene().getWindow();
        fenetre.close();
    }

}