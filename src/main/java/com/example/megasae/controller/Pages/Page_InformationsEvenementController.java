package com.example.megasae.controller.Pages;

import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Evenement.Creneau;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Tache;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe gère le tableau de bord principal d'un événement sélectionné.
 * Elle affiche ses caractéristiques, calcule ses statistiques, gère la liste des créneaux, et met à jour les données en temps réel (auto-sauvegarde).
 *
 * @version 1.1
 */
public class Page_InformationsEvenementController {

    // COMPOSANTS FXML ET ATTRIBUTS
    @FXML
    private TextField txtNomEvenement;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Button btnStatut;
    @FXML
    private HBox hboxTagsContainer;
    @FXML
    private DatePicker pickerDateDebut;
    @FXML
    private DatePicker pickerDateFin;
    @FXML
    private VBox vboxListeCreneaux;
    @FXML
    private TextField txtCapacite;
    @FXML
    private TextField txtBudget;
    @FXML
    private TextField txtCoutMoyen;
    @FXML
    private TextField txtTarifMoyen;
    @FXML
    private TextField txtNbParticipants;
    @FXML
    private ProgressBar progressTaches;
    @FXML
    private Label lblProgressText; // Ajout du label pour l'affichage textuel de l'avancement

    /**
     * L'événement actif actuellement manipulé par l'utilisateur.
     */
    private Evenement evenementActif;


    // INITIALISATION

    @FXML
    public void initialize() {
        evenementActif = PageController.getInstance().getEvenementActif();
        if (evenementActif != null) {
            chargerDonneesEvenement();
            configurerAutoSauvegarde();
        }
    }


    // MÉTHODES AFFICHAGE

    private void chargerDonneesEvenement() {
        if (evenementActif.getNom() != null) {
            txtNomEvenement.setText(evenementActif.getNom());
        }
        if (evenementActif.getDescription() != null) {
            txtDescription.setText(evenementActif.getDescription());
        }
        if (evenementActif.getDateDebut() != null) {
            pickerDateDebut.setValue(evenementActif.getDateDebut());
        }
        if (evenementActif.getDateFin() != null) {
            pickerDateFin.setValue(evenementActif.getDateFin());
        }
        btnStatut.setText(evenementActif.getStatut().getStatut());
        btnStatut.setStyle(evenementActif.getStatut().getStyleCss() + "; -fx-cursor: hand");

        rafraichirListeCreneaux();
        calculerEtAfficherStatistiques();
    }

    /**
     * Calcule et met à jour les données de l'événement
     */
    private void calculerEtAfficherStatistiques() {

        // Nombre de participants
        int nbParticipants = (evenementActif.getMembres().size());
        txtNbParticipants.setText(String.valueOf(nbParticipants));

        // Budget global
        double budget = evenementActif.getBudget();
        txtBudget.setText(String.format("%.2f €", budget));

        // Capacité d'accueil
        txtCapacite.setText(String.valueOf(evenementActif.getCapaciteMax()));

        // Coûts et tarifs moyens
        if (nbParticipants > 0) {
            double coutMoyen = budget / nbParticipants;
            txtCoutMoyen.setText(String.format("%.2f €/pers.", coutMoyen));
            double tarifMoyen = 0.0;
            for (int t = 0; t < nbParticipants; t++) {
                List<Role> roles = evenementActif.getMembres().get(t).getRoles();
                for (int r = 0; r < roles.size(); r++) {
                    tarifMoyen += roles.get(r).getTarif();
                }
            }
            txtTarifMoyen.setText(String.format("%.2f €/pers.", tarifMoyen));
        } else {
            txtCoutMoyen.setText("0.00 €/pers.");
            txtTarifMoyen.setText("0.00 €/pers.");
        }

        // Avancement des tâches
        if (evenementActif.getTaches() != null && !evenementActif.getTaches().isEmpty()) {
            int totalTaches = evenementActif.getTaches().size();
            int cochees = 0;
            for (int t = 0; t < totalTaches; t++) {
                Tache tache = evenementActif.getTaches().get(t);
                if (tache.isEstCochee()) {
                    cochees++;
                }
            }
            double progression = (double) cochees / totalTaches;
            progressTaches.setProgress(progression);

            int pourcentage = (int) (progression * 100);
            lblProgressText.setText(String.format("%d / %d terminées (%d%%)", cochees, totalTaches, pourcentage));
        } else {
            progressTaches.setProgress(0.0);
            lblProgressText.setText("Aucune tâche (0%)");
        }
    }

    public void rafraichirListeCreneaux() {
        vboxListeCreneaux.getChildren().clear();

        if (evenementActif.getCreneaux() == null || evenementActif.getCreneaux().isEmpty()) {
            return;
        }

        for (int c = 0; c < evenementActif.getCreneaux().size(); c++) {
            Creneau creneau = evenementActif.getCreneaux().get(c);

            HBox ligne = new HBox(15);
            ligne.setAlignment(Pos.CENTER_LEFT);
            ligne.setStyle("-fx-padding: 8; -fx-border-color: #eeeeee; -fx-border-width: 0 0 1 0;");
            ligne.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(ligne, javafx.scene.layout.Priority.ALWAYS);

            String lettrePastille = "";
            boolean aUnMembre = false;

            if (creneau.getMembres() != null && creneau.getMembres().size() > 0) {
                String nomMembre = creneau.getMembres().get(0).getNom();
                if (nomMembre != null && nomMembre.length() > 0) {
                    lettrePastille = nomMembre.substring(0, 1).toUpperCase();
                    aUnMembre = true;
                }
            }

            String texteInfos = creneau.getTitreActivite();

            if (creneau.getDateDebut() != null && creneau.getHeureDebut() != null && creneau.getDateFin() != null && creneau.getHeureFin() != null) {
                texteInfos = texteInfos + " - débute le " + creneau.getDateDebut() + " à " + creneau.getHeureDebut() + " jusqu'au " + creneau.getDateFin() + " à " + creneau.getHeureFin();
            } else {
                texteInfos = texteInfos + " - " + creneau.getDateDebut();
            }

            if (creneau.getAdresse() != null && !creneau.getAdresse().trim().isEmpty()) {
                texteInfos = texteInfos + " - " + creneau.getAdresse();
            }

            Label lblInfos = new Label(texteInfos);
            lblInfos.setAlignment(Pos.CENTER_LEFT);
            lblInfos.setWrapText(true);
            HBox.setHgrow(lblInfos, javafx.scene.layout.Priority.ALWAYS);

            javafx.scene.layout.Pane ressort = new javafx.scene.layout.Pane();
            HBox.setHgrow(ressort, javafx.scene.layout.Priority.ALWAYS);

            Button btnSupprimer = new Button("X");
            btnSupprimer.setStyle("-fx-background-color: #FADBD8; -fx-text-fill: #e74c3c; -fx-cursor: hand;");
            btnSupprimer.setMinWidth(30);

            btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    ButtonType choix = PageController.getInstance().afficherSuppression(Alert.AlertType.CONFIRMATION, "Confirmation de suppression", "Voulez-vous vraiment supprimer cet élément ?");
                    if (choix == ButtonType.YES) {
                        evenementActif.getCreneaux().remove(creneau);
                        PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
                        rafraichirListeCreneaux();
                    }
                }
            });

            if (aUnMembre) {
                Label lblType = new Label(lettrePastille);
                lblType.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 2 8; -fx-font-weight: bold;");
                lblType.setMinWidth(25);
                lblType.setAlignment(Pos.CENTER);
                ligne.getChildren().add(lblType);
            }

            ligne.getChildren().addAll(lblInfos, ressort, btnSupprimer);
            vboxListeCreneaux.getChildren().add(ligne);
        }
    }


    // ACTIONS UTILISATEUR

    private void configurerAutoSauvegarde() {

        txtNomEvenement.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                evenementActif.setNom(newValue);
                PageController.getInstance().lblNomEvenementActif.setText(newValue);
            }
        });

        txtDescription.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                evenementActif.setDescription(newValue);
            }
        });

        pickerDateDebut.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate ancienneDate, LocalDate nouvelleDate) {
                if (nouvelleDate != null) {
                    evenementActif.setDateDebut(nouvelleDate);
                }
            }
        });

        pickerDateFin.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate ancienneDate, LocalDate nouvelleDate) {
                if (nouvelleDate != null) {
                    evenementActif.setDateFin(nouvelleDate);
                }
            }
        });
    }

    @FXML
    public void handleOuvrirPopupStatut(ActionEvent event) {
        PageController.getInstance().afficherVue("Popup_Statut.fxml", "Attribution du statut");
        if (evenementActif.getStatut() != null) {
            btnStatut.setText(evenementActif.getStatut().getStatut());
            btnStatut.setStyle(evenementActif.getStatut().getStyleCss() + "; -fx-cursor: hand");
        }
    }

    @FXML
    public void handleOuvrirPopupTag(ActionEvent event) {
        PageController.getInstance().afficherVue("Popup_AttributionTag.fxml", "Ajout de TAG");
    }

    @FXML
    public void handleOuvrirPopupCreneau(ActionEvent event) {
        PageController.getInstance().afficherVue("Popup_AjoutCreneau.fxml", "Ajout de Créneau");
        rafraichirListeCreneaux();
    }

}