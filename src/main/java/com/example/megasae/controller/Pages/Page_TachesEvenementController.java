package com.example.megasae.controller.Pages;

import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Note;
import com.example.megasae.model.Miscellaneous.Tache;
import com.example.megasae.model.Miscellaneous.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Page_TachesEvenementController {

    private static final Logger logger = Logger.getLogger(Page_TachesEvenementController.class.getName());

    @FXML
    private Button btnFiltrer;
    @FXML
    private Button btnNouvelleNote;
    @FXML
    private Button btnNouvelleTache;
    @FXML
    private VBox containerArchive;
    @FXML
    private VBox containerEnCours;
    @FXML
    private Label lblProgressText;
    @FXML
    private VBox mainContent;
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private ProgressBar progressTaches;
    @FXML
    private TextField txtRechercheTache;

    private Evenement evenementActif;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    public void initialize() {
        evenementActif = PageController.getEvenementActif();
        rafraichirListes();
    }

    /**
     * Recalcule la progression globale et redessine les éléments en fonction du filtre de recherche.
     */
    public void rafraichirListes() {
        containerEnCours.getChildren().clear();
        containerArchive.getChildren().clear();

        if (evenementActif == null) {
            return;
        }

        if (evenementActif.getNotes() == null) {
            evenementActif.setNotes(new ArrayList<>());
        }

        String filtre = (txtRechercheTache != null) ? txtRechercheTache.getText().toLowerCase().trim() : "";

        int totalTaches = evenementActif.getTaches().size();
        int tachesCompletes = 0;

        // Affichage des Tâches
        for (int t = 0; t < totalTaches; t++) {
            Tache tache = evenementActif.getTaches().get(t);
            if (tache.isEstCochee()) {
                tachesCompletes++;
            }

            // Recherche par nom de tâche
            boolean matchRecherche = filtre.isEmpty() ||
                    (tache.getLibelle() != null && tache.getLibelle().toLowerCase().contains(filtre));

            // Rehercher une tâche par le nom de ses TAGS
            if (!matchRecherche && tache.getTags() != null) {
                for (Tag tag : tache.getTags()) {
                    if (tag.getLibelle() != null && tag.getLibelle().toLowerCase().contains(filtre)) {
                        matchRecherche = true;
                        break;
                    }
                }
            }

            if (matchRecherche) {
                HBox ligneTache = creerLigneTache(tache);
                if (tache.isEstCochee()) {
                    containerArchive.getChildren().add(ligneTache);
                } else {
                    containerEnCours.getChildren().add(ligneTache);
                }
            }
        }

        // Affichage des Notes
        int totalNotes = evenementActif.getNotes().size();

        for (int n = 0; n < totalNotes; n++) {
            Note note = evenementActif.getNotes().get(n);
            boolean matchRecherche = filtre.isEmpty() || (note.getLibelle() != null && note.getLibelle().toLowerCase().contains(filtre)) || (note.getDescription() != null && note.getDescription().toLowerCase().contains(filtre));

            if (matchRecherche) {
                HBox ligneNote = creerLigneNote(note);
                if (note.isEstCochee()) {
                    containerArchive.getChildren().add(ligneNote);
                } else {
                    containerEnCours.getChildren().add(ligneNote);
                }
            }
        }

        // Calcul et mise à jour de la jauge d'avancement
        if (totalTaches > 0) {
            double progression = (double) tachesCompletes / totalTaches;
            progressTaches.setProgress(progression);
            lblProgressText.setText((int) (progression * 100) + "% Fait");
            evenementActif.setProgressionChecklist(progression * 100.0);
        } else {
            progressTaches.setProgress(0.0);
            lblProgressText.setText("0% Fait");
            evenementActif.setProgressionChecklist(0.0);
        }

        lblProgressText.setStyle("-fx-text-fill: white");

        logger.fine(String.format("Interface rafraîchie : %d/%d tâches complétées.", tachesCompletes, totalTaches));
    }

    private HBox creerLigneTache(Tache tache) {
        HBox ligne = new HBox();
        ligne.setAlignment(Pos.CENTER_LEFT);
        ligne.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12 15;");
        HBox.setHgrow(ligne, Priority.ALWAYS);

        if (tache.isEstCochee()) {
            ligne.setStyle(ligne.getStyle() + " -fx-opacity: 0.65;");
        }

        CheckBox cb = new CheckBox();
        cb.setSelected(tache.isEstCochee());
        cb.setStyle("-fx-scale-x: 1.2; -fx-scale-y: 1.2; -fx-cursor: hand;");
        HBox.setMargin(cb, new Insets(0, 15, 0, 5));

        cb.setOnAction(e -> {
            tache.basculerEtatCocher();
            logger.info(String.format("[UC20] Tâche '%s' marquée comme %s.",
                    tache.getLibelle(), tache.isEstCochee() ? "COMPLÉTÉE" : "À FAIRE"));
            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
            rafraichirListes();
        });

        // Conteneur vertical des textes de la tâche
        VBox zoneTexte = new VBox(4);
        Label lblNom = new Label(tache.getLibelle());
        lblNom.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1E293B;");
        if (tache.isEstCochee()) {
            lblNom.setStyle(lblNom.getStyle() + " -fx-strikethrough: true; -fx-text-fill: #64748B;");
        }

        // Taches
        HBox boxTags = new HBox(4);
        boxTags.setAlignment(Pos.CENTER_LEFT);
        if (tache.getTags() != null) {
            for (Tag tag : tache.getTags()) {
                Label lblTag = new Label(tag.getLibelle());
                lblTag.setStyle("-fx-background-color: " + tag.getCouleur() + ";" + " -fx-text-fill: ladder(" + tag.getCouleur() + ", white 49%, black 50%);" + " -fx-padding: 2 6; -fx-background-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
                boxTags.getChildren().add(lblTag);
            }
        }

        String strDate = "Pas d'échéance";
        if (tache.getDate() != null) {
            strDate = tache.getDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        Label lblDate = new Label("📅 " + strDate);
        lblDate.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 11px;");

        // Ajout du nom, des badges de tags et de la date
        zoneTexte.getChildren().addAll(lblNom, boxTags, lblDate);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String badgeColor = (tache.getCouleur() != null) ? tache.getCouleur() : "#0b0947";
        Label lblImportance = new Label(tache.getImportance() != null ? tache.getImportance().toString() : "MOYENNE");
        lblImportance.setStyle("-fx-background-color: " + badgeColor + "; -fx-text-fill: ladder(" + badgeColor + ", white 49%, black 50%); -fx-padding: 4 12; -fx-background-radius: 20; -fx-font-size: 11px; -fx-font-weight: bold;");
        HBox.setMargin(lblImportance, new Insets(0, 15, 0, 0));

        Button btnSuppr = new Button("✕");
        btnSuppr.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 14px;");

        btnSuppr.setOnAction(e -> {
            logger.info(String.format("Suppression de la tâche : '%s'.", tache.getLibelle()));
            evenementActif.suprimerTache(tache);
            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
            rafraichirListes();
        });

        ligne.getChildren().addAll(cb, zoneTexte, spacer, lblImportance, btnSuppr);
        return ligne;
    }

    private HBox creerLigneNote(Note note) {
        HBox ligne = new HBox();
        ligne.setAlignment(Pos.CENTER_LEFT);
        ligne.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12 15;");
        HBox.setHgrow(ligne, Priority.ALWAYS);

        if (note.isEstCochee()) {
            ligne.setStyle(ligne.getStyle() + " -fx-opacity: 0.65;");
        }

        CheckBox cb = new CheckBox();
        cb.setSelected(note.isEstCochee());
        cb.setStyle("-fx-scale-x: 1.2; -fx-scale-y: 1.2; -fx-cursor: hand;");
        HBox.setMargin(cb, new Insets(0, 15, 0, 5));

        cb.setOnAction(e -> {
            note.setEstCochee(!note.isEstCochee());
            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
            rafraichirListes();
        });

        VBox zoneTexte = new VBox(4);
        Label lblNom = new Label(note.getLibelle());
        lblNom.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1E293B;");

        if (note.isEstCochee()) {
            lblNom.setStyle(lblNom.getStyle() + " -fx-strikethrough: true; -fx-text-fill: #64748B;");
        }

        Label lblDesc = new Label(note.getDescription());
        lblDesc.setStyle("-fx-text-fill: #64748B; -fx-font-size: 12px;");
        zoneTexte.getChildren().addAll(lblNom, lblDesc);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnSuppr = new Button("✕");
        btnSuppr.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 14px;");
        btnSuppr.setOnAction(e -> {
            evenementActif.getNotes().remove(note);
            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
            rafraichirListes();
        });

        //TODO
//        zoneTexte.setCursor(javafx.scene.Cursor.HAND);
//        zoneTexte.setOnMouseClicked(e -> {
//            PageController.getInstance().afficherVue("Popup_AjoutNote.fxml", "Modifier la Note");
//            rafraichirListes();
//        });

        ligne.getChildren().addAll(cb, zoneTexte, spacer, btnSuppr);
        return ligne;
    }

    @FXML
    private void handleFiltrerAction() {
        rafraichirListes();
    }

    @FXML
    void handleRecherche(KeyEvent event) {
        rafraichirListes();
    }

    @FXML
    private void handleNouvelleTacheAction(ActionEvent event) {
        PageController.getInstance().afficherVue("Popup_AjoutTache.fxml", "Créer une Tâche");
        rafraichirListes();
    }

    @FXML
    private void handleNouvelleNoteAction(ActionEvent event) {
        PageController.getInstance().afficherVue("Popup_AjoutNote.fxml", "Créer une Note");
        rafraichirListes();
    }

}