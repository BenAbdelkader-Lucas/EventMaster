package com.example.megasae.controller.Popups.Ajout;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Attribution.Popup_AttributionMembreController;
import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Evenement.Creneau;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.GestionnaireDesEvenements;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Time;

public class Popup_AjoutCreneauController {

    private Evenement evenementActif;
    private Creneau creneau;
    private GestionnaireDesEvenements gestionnaireDesEvenements;

    @FXML
    private Button btnAnnuler;

    @FXML
    private DatePicker dpDateDebut;
    @FXML
    private DatePicker dpDateFin;

    @FXML
    private FlowPane listeMembre;

    @FXML
    private TextField txtAdresse;
    @FXML
    private TextField txtHeureDebut;
    @FXML
    private TextField txtHeureFin;
    @FXML
    private TextField txtNom;
    @FXML
    private TextArea txtNote;


    @FXML
    public void initialize() {
        evenementActif = PageController.getEvenementActif();
        gestionnaireDesEvenements = PageController.getInstance().getGestionnaire();
        
        if (this.creneau == null) {
            this.creneau = new Creneau();
        }
        rafraichirMembresAffiches();
    }

    /**
     * Permet "d'injecter" un créneau existant si on clique sur "Modifier" depuis le tableau principal
     */
    public void setCreneau(Creneau creneauExistant) {
        if (creneauExistant != null) {
            this.creneau = creneauExistant;

            // Pré-remplissage
            txtNom.setText(creneau.getTitreActivite());
            txtAdresse.setText(creneau.getAdresse());
            dpDateDebut.setValue(creneau.getDateDebut());
            dpDateFin.setValue(creneau.getDateFin());
            txtNote.setText(creneau.getNote());

            if (creneau.getHeureDebut() != null) {
                txtHeureDebut.setText(creneau.getHeureDebut().toString().substring(0, 5));
            }
            if (creneau.getHeureFin() != null) {
                txtHeureFin.setText(creneau.getHeureFin().toString().substring(0, 5));
            }

            rafraichirMembresAffiches();
        }
    }

    private void rafraichirMembresAffiches() {
        listeMembre.getChildren().clear();
        if (creneau != null && creneau.getMembres() != null) {
            for (Membre membre : creneau.getMembres()) {
                Button btnMembre = new Button(membre.getNom() + " " + membre.getPrenom());
                btnMembre.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-background-radius: 6px; -fx-border-radius: 6px; -fx-font-weight: bold;");
                btnMembre.setPadding(new Insets(5, 8, 5, 8));

                // Retirer le membre du créneau si on clique sur son badge directement depuis cette vue
                btnMembre.setOnAction(e -> {
                    creneau.getMembres().remove(membre);
                    rafraichirMembresAffiches();
                });

                listeMembre.getChildren().add(btnMembre);
            }
        }
    }

    @FXML
    void handleAjouterMembreClic(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Attribution/Popup_AttributionMembre.fxml"));
            Scene scene = new Scene(loader.load());

            Popup_AttributionMembreController dynamicController = loader.getController();
            dynamicController.setCreneau(this.creneau);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.setTitle("Attribuer des Membres au Créneau");
            popupStage.setScene(scene);

            popupStage.showAndWait();

            rafraichirMembresAffiches();
        } catch (Exception e) {
            e.printStackTrace();
            PageController.getInstance().afficherAlerte(e.getMessage());
        }
    }

    @FXML
    void handleAnnuler(ActionEvent event) {
        fermezFenetre();
    }

    @FXML
    void handleConfirmer(ActionEvent event) {
        try {
            String hDebutSaisie = txtHeureDebut.getText().trim();
            String hFinSaisie = txtHeureFin.getText().trim();

            if (hDebutSaisie.isEmpty() || hFinSaisie.isEmpty()) {
                throw new IllegalArgumentException("Les heures de début et de fin sont obligatoires.");
            }

            if (hDebutSaisie.length() == 5) hDebutSaisie += ":00";
            if (hFinSaisie.length() == 5) hFinSaisie += ":00";

            Time heureDebutConvertie;
            Time heureFinConvertie;
            try {
                heureDebutConvertie = Time.valueOf(hDebutSaisie);
                heureFinConvertie = Time.valueOf(hFinSaisie);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Le format de l'heure est incorrect. Utilisez HH:MM (ex: 14:30).");
            }


            // Attribution définitive des valeurs au modèle existant
            this.creneau.setTitreActivite(txtNom.getText());

            try {
                this.creneau.setDateDebut(dpDateDebut.getValue());
                this.creneau.setDateFin(dpDateFin.getValue());
                this.creneau.setHeureDebut(heureDebutConvertie);
                this.creneau.setHeureFin(heureFinConvertie);
            } catch (IllegalArgumentException e) {
                PageController.getInstance().afficherAlerte(e.getMessage());
                return;
            }
            this.creneau.setAdresse(txtAdresse.getText());
            this.creneau.setNote(txtNote.getText());
            this.creneau.setEvenement(evenementActif);


            // Pour éviter les doublons dans l'événement si c'est une modification
            if (evenementActif != null && !evenementActif.getCreneaux().contains(this.creneau)) {
                evenementActif.addCreneau(this.creneau);
            }

            gestionnaireDesEvenements.sauvegarderDonnees(gestionnaireDesEvenements);

            fermezFenetre();

        } catch (Exception e) {
            PageController.getInstance().afficherAlerte(e.getMessage());
        }
    }

    @FXML
    public void fermezFenetre() {
        Stage fenetre = (Stage) btnAnnuler.getScene().getWindow();
        fenetre.close();
    }

}