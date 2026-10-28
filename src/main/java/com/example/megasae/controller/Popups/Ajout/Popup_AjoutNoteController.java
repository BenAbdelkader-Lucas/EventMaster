package com.example.megasae.controller.Popups.Ajout;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Note;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.logging.Logger; // Ajout du logger pour la cohérence de la SAÉ

public class Popup_AjoutNoteController {

    // Initialisation du Logger
    private static final Logger logger = Logger.getLogger(Popup_AjoutNoteController.class.getName());

    private Evenement evenementActif;
    private Note noteEnCoursDeModification = null;

    @FXML private Label lblTitreNote;
    @FXML private TextField txtNomNote;
    @FXML private TextArea txtDescriptionNote;
    @FXML private Button btnAnnuler;
    @FXML private Button btnConfirmer;

    @FXML
    public void initialize() {
        evenementActif = PageController.getEvenementActif();
    }


    // Méthode appelée par le contrôleur principal pour passer en mode "Modification"
    public void chargerDonneesPourModification(Note note) {
        this.noteEnCoursDeModification = note;

        // Reprise exacte de tes identifiants FXML
        lblTitreNote.setText("Modification de : Note");
        txtNomNote.setText(note.getLibelle());
        txtDescriptionNote.setText(note.getDescription());

        logger.fine("Données chargées dans la modale pour la note : " + note.getLibelle());
    }

    @FXML
    private void handleAnnulerNote(ActionEvent event) {
        logger.fine("Fermeture de la popup note (Action annulée).");
        fermerFenetre();
    }

    @FXML
    private void handleConfirmerNote(ActionEvent event) {
        try {
            String libelle = txtNomNote.getText();
            String description = txtDescriptionNote.getText();

            if (noteEnCoursDeModification != null) {
                noteEnCoursDeModification.setLibelle(libelle);
                noteEnCoursDeModification.setDescription(description);
                logger.info("Note modifiée avec succès : " + libelle);
            } else {
                Note nouvelleNote = new Note(libelle, description);
                evenementActif.addNote(nouvelleNote);
                logger.info("Nouvelle note créée avec succès : " + libelle);
            }

            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());

            fermerFenetre();
        } catch (Exception e) {
            PageController.getInstance().afficherAlerte("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

}