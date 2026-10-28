package com.example.megasae.controller.Popups.Ajout;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Attribution.Popup_AttributionTagController;
// Assurez-vous d'avoir ce contrôleur pour les rôles :
// import com.example.megasae.controller.Popups.Attribution.Popup_AttributionRoleController;
import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Equipe.StatutPresence;
import com.example.megasae.model.Evenement.Evenement;
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

public class Popup_AjoutMembreController {

    private Evenement evenementActif;
    private Membre membreEnCoursEdition = null;
    private boolean modeModification = false;

    @FXML
    private TextField txtNomTag;
    @FXML
    private TextField txtDescriptionTag;
    @FXML
    private TextField txtMailMembre;
    @FXML
    private TextField txtTelMembre;
    @FXML
    private TextArea txtNoteInterneMembre;

    @FXML
    private FlowPane listeTag;
    @FXML
    private FlowPane listeRole;

    @FXML
    private Button btnAnnuler;
    @FXML
    private Button btnConfirmer;
    @FXML
    private Button btnAjouterTag;
    @FXML
    private Button btnAjouterRole;

    @FXML
    public void initialize() {
        evenementActif = PageController.getInstance().getEvenementActif();

        membreEnCoursEdition = PageController.getInstance().getMembreActif();
        // Mode création par défaut : on prépare un membre vierge pour collecter rôles/tags
        if (membreEnCoursEdition == null) {
            membreEnCoursEdition = new Membre();
            membreEnCoursEdition.setEvenement(evenementActif);
            membreEnCoursEdition.setPresence(StatutPresence.INCONNU);
        }

        rafraichirListesVisuelles();
    }

    /**
     * Méthode appelée lors de l'édition d'un membre existant
     */
    @FXML
    public void initialisationValeur(Membre membre) {
        this.membreEnCoursEdition = membre;
        this.modeModification = true;

        txtNomTag.setText(membre.getNom());
        txtDescriptionTag.setText(membre.getPrenom());

        if (txtMailMembre != null) txtMailMembre.setText(membre.getMail());
        if (txtTelMembre != null) txtTelMembre.setText(membre.getTelephone());
        if (txtNoteInterneMembre != null) txtNoteInterneMembre.setText(membre.getNoteInterne());

        rafraichirListesVisuelles();
    }

    @FXML
    void handleOpenPopupTagAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Attribution/Popup_AttributionTag.fxml"));
            Scene scene = new Scene(loader.load());

            Popup_AttributionTagController popupController = loader.getController();
            popupController.setMembre(membreEnCoursEdition);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.setTitle("Attribuer des tags");
            popupStage.setScene(scene);

            popupStage.showAndWait();

            // Mise à jour de l'affichage au retour
            rafraichirListesVisuelles();
        } catch (Exception e) {
            e.printStackTrace();
            PageController.getInstance().afficherAlerte("Impossible de charger l'attribution des tags : " + e.getMessage());
        }
    }

    @FXML
    void handleOpenPopupRoleAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Attribution/Popup_AttributionRole.fxml"));
            Scene scene = new Scene(loader.load());

            Object popupController = loader.getController();
            popupController.getClass().getMethod("setMembre", Membre.class).invoke(popupController, membreEnCoursEdition);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.setTitle("Attribuer des rôles");
            popupStage.setScene(scene);

            popupStage.showAndWait();

            // Mise à jour de l'affichage au retour
            rafraichirListesVisuelles();
        } catch (Exception e) {
            e.printStackTrace();
            PageController.getInstance().afficherAlerte("Impossible de charger l'attribution des rôles : " + e.getMessage());
        }
    }

    // AFFICHAGE GRAPHIQUE DES BADGES

    private void rafraichirListesVisuelles() {
        if (listeTag != null) listeTag.getChildren().clear();
        if (listeRole != null) listeRole.getChildren().clear();

        if (membreEnCoursEdition == null) return;

        if (membreEnCoursEdition.getTags() != null && listeTag != null) {
            for (Tag tag : membreEnCoursEdition.getTags()) {
                Label labelTag = new Label(tag.getLibelle());
                String couleurTag = (tag.getCouleur() != null) ? tag.getCouleur().trim() : "#BDC3C7";
                if (!couleurTag.startsWith("#")) couleurTag = "#" + couleurTag;

                labelTag.setStyle("-fx-background-color: " + couleurTag +
                        "; -fx-background-radius: 6px; -fx-border-radius: 6px;" +
                        " -fx-text-fill: ladder(" + couleurTag + ", white 49%, black 50%);" +
                        " -fx-font-weight: bold;");
                labelTag.setPadding(new Insets(5, 8, 5, 8));

                FlowPane.setMargin(labelTag, new Insets(2, 4, 2, 0));
                listeTag.getChildren().add(labelTag);
            }
        }

        if (membreEnCoursEdition.getRoles() != null && listeRole != null) {
            for (Role role : membreEnCoursEdition.getRoles()) {
                Label labelRole = new Label(role.getNom());
                String couleurRole = (role.getCouleur() != null) ? role.getCouleur().trim() : "#2D8CD5";
                if (!couleurRole.startsWith("#")) couleurRole = "#" + couleurRole;

                labelRole.setStyle("-fx-background-color: " + couleurRole +
                        "; -fx-background-radius: 6px; -fx-border-radius: 6px;" +
                        " -fx-text-fill: ladder(" + couleurRole + ", white 49%, black 50%);" +
                        " -fx-font-weight: bold;");
                labelRole.setPadding(new Insets(5, 8, 5, 8));

                FlowPane.setMargin(labelRole, new Insets(2, 4, 2, 0));
                listeRole.getChildren().add(labelRole);
            }
        }
    }

    // CONFIRMATION ET ANNULATION

    @FXML
    public void handleConfirmerTag(ActionEvent event) {
        try {
            // Validation des champs obligatoires
            String nom = txtNomTag.getText();
            String prenom = txtDescriptionTag.getText();

            if (nom == null || nom.trim().isEmpty() || prenom == null || prenom.trim().isEmpty()) {
                PageController.getInstance().afficherAlerte("Le nom et le prénom sont obligatoires.");
                return;
            }

            String mail = (txtMailMembre != null && !txtMailMembre.getText().isEmpty()) ? txtMailMembre.getText() : "email@example.com";
            String tel = (txtTelMembre != null && !txtTelMembre.getText().isEmpty()) ? txtTelMembre.getText() : "0000000000";
            String note = (txtNoteInterneMembre != null) ? txtNoteInterneMembre.getText() : "";

            // Mise à jour de l'objet global
            membreEnCoursEdition.setNom(nom);
            membreEnCoursEdition.setPrenom(prenom);
            membreEnCoursEdition.setMail(mail);
            membreEnCoursEdition.setTelephone(tel);
            membreEnCoursEdition.setNoteInterne(note);

            if (!modeModification) {
                if (evenementActif == null) {
                    evenementActif = PageController.getInstance().getEvenementActif();
                }
                evenementActif.addMembre(membreEnCoursEdition);
            }

            // Sauvegarde automatique
            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
            fermerPopup();

        } catch (Exception e) {
            PageController.getInstance().afficherAlerte("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    @FXML
    public void handleAnnulerTag(ActionEvent event) {
        fermerPopup();
    }

    private void fermerPopup() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

}