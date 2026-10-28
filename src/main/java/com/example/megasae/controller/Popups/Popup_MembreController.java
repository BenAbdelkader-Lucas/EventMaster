package com.example.megasae.controller.Popups;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Equipe.StatutPresence;
import com.example.megasae.model.Evenement.Evenement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Popup_MembreController {

    @FXML
    private Label lblTitrePopup;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private FlowPane containerRoles;
    @FXML private TextField txtMail;
    @FXML private TextField txtTelephone;
    @FXML private TextArea txtNote;

    private Evenement evenementActif;
    private Membre membreEnCours;
    private boolean isModeModification = false;

    @FXML
    public void initialize() {
        this.evenementActif = PageController.getInstance().getEvenementActif();

        if (this.evenementActif == null) {
            System.err.println("Erreur : Aucun événement actif trouvé.");
            return;
        }

        // On regarde si un membre a été passé pour modification
        Membre membreAModifier = PageController.getInstance().getMembreActif();

        if (membreAModifier != null) {
            // Modification
            this.isModeModification = true;
            this.membreEnCours = membreAModifier;

            // Pré-remplissage des champs
            txtNom.setText(membreEnCours.getNom());
            txtPrenom.setText(membreEnCours.getPrenom() != null ? membreEnCours.getPrenom() : "");
            txtMail.setText(membreEnCours.getMail());
            txtTelephone.setText(membreEnCours.getTelephone());
            txtNote.setText(membreEnCours.getNoteInterne() != null ? membreEnCours.getNoteInterne() : "");

            if (lblTitrePopup != null) {
                lblTitrePopup.setText("Modifier le membre : " + membreEnCours.getNom());
            }
        } else {
            // Création
            this.isModeModification = false;
            this.membreEnCours = new Membre();
            this.membreEnCours.setEvenement(evenementActif);
        }

        rafraichirBadgesRoles();
    }

    @FXML
    void handleConfirmer(ActionEvent event) {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String mail = txtMail.getText().trim();
        String telephone = txtTelephone.getText().trim();
        String note = txtNote.getText().trim();

        if (nom.isEmpty() || mail.isEmpty() || telephone.isEmpty()) {
            PageController.getInstance().afficherAlerte("Le Nom, l'Email et le Téléphone sont obligatoires.");
            return;
        }

        try {
            membreEnCours.setNom(nom);
            membreEnCours.setPrenom(prenom.isEmpty() ? null : prenom);
            membreEnCours.setMail(mail);
            membreEnCours.setTelephone(telephone);
            membreEnCours.setNoteInterne(note.isEmpty() ? null : note);

            if (!isModeModification) {
                // Si c'est un nouveau membre, on l'ajoute à la liste de l'événement
                membreEnCours.setPresence(StatutPresence.INCONNU);
                evenementActif.getMembres().add(membreEnCours);
                System.out.println("Nouveau membre ajouté !");
            } else {
                System.out.println("Membre mis à jour !");
            }

            PageController.getInstance().setMembreActif(null);
            fermerFenetre();

        } catch (IllegalArgumentException e) {
            PageController.getInstance().afficherAlerte(e.getMessage());
        }
    }

    @FXML
    void handleAnnuler(ActionEvent event) {
        // Pense à nettoyer ici aussi en cas d'annulation
        PageController.getInstance().setMembreActif(null);
        fermerFenetre();
    }

    @FXML
    void handleOuvrirPopupTags(ActionEvent event) {
        // On partage le membre en cours à la popup de tags
        PageController.getInstance().setMembreActif(membreEnCours);
        PageController.getInstance().afficherVue("Popup_AjoutTag.fxml", "Attribution de TAGS");
        rafraichirBadgesRoles();
    }

    private void rafraichirBadgesRoles() {
        containerRoles.getChildren().clear();
        if (membreEnCours.getRoles() == null || membreEnCours.getRoles().isEmpty()) {
            Label lblVide = new Label("Aucun rôle");
            lblVide.setStyle("-fx-text-fill: #94A3B8; -fx-font-style: italic; -fx-font-size: 13;");
            containerRoles.getChildren().add(lblVide);
            return;
        }
        for (Role role : membreEnCours.getRoles()) {
            Label badge = new Label(role.getNom());
            badge.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #0369A1; "
                    + "-fx-padding: 4 10 4 10; -fx-background-radius: 12; -fx-font-size: 12; -fx-font-weight: bold;");
            containerRoles.getChildren().add(badge);
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        if (stage != null) stage.close();
    }

}