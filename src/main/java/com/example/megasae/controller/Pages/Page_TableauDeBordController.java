package com.example.megasae.controller.Pages;

import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Finance.BilanFinancier;
import com.example.megasae.model.Miscellaneous.Tache;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Responsable de l'agrégation des indicateurs clés de performance, du calcul des métriques financières, du suivi de la progression globale, des tâches, ainsi que des cartes d'aperçu des derniers événements enregistrés.
 *
 * @version 1.0
 */
public class Page_TableauDeBordController {

    // COMPOSANTS FXML ET ATTRIBUTS

    @FXML
    private Label statNbEvenements;
    @FXML
    private Label statSoldes;
    @FXML
    private Label statTaches;
    @FXML
    private Label statTemplates;
    @FXML
    private Label statTotalParticipants;
    @FXML
    private FlowPane progressionRow;


    // INITIALISATION

    /**
     * Point d'entrée de l'initialisation JavaFX.
     * Délègue le calcul des métriques au conteneur de thread d'application afin de s'assurer du rendu complet préalable des conteneurs parents.
     */
    @FXML
    public void initialize() {
        // Exécution différée pour éviter les problèmes lors de la récupération des données (bug affichage évènements)
        Platform.runLater(() -> {
            calculerStatistiquesEtProgressions();
        });
    }


    // METHODES

    /**
     * Analyse l'ensemble des données issues du modèle via le contrôleur principal, calcule les indicateurs consolidés et injecte dynamiquement des composants d'interface dans la vue.
     */
    private void calculerStatistiquesEtProgressions() {
        // Vidage pour éviter la duplication
        progressionRow.getChildren().clear();

        // Récupération des données
        List<Evenement> listeEvenements = PageController.getInstance().getGestionnaire().getEvenements();

        // Gestion si aucun événement enregistré
        if (listeEvenements == null || listeEvenements.isEmpty()) {
            statNbEvenements.setText("0");
            statSoldes.setText("0 €");
            statTaches.setText("0%");
            if (statTotalParticipants != null) statTotalParticipants.setText("0");
            if (statTemplates != null) {
                statTemplates.setText(String.valueOf(PageController.getInstance().getGestionnaire().getTemplateEvenements().size()));
            }
            return;
        }

        int totalEvenements = listeEvenements.size();
        double sommeBenefices = PageController.getInstance().getGestionnaire().calculerBeneficesTotal();
        double totalTachesTermineesGlobales = PageController.getInstance().getGestionnaire().calculerProgressionMoyenneTaches();
        int participantsUniques = PageController.getInstance().getGestionnaire().calculerNombreMembres();

        // Affichage des 3 derniers événements ajoutés
        int i = Math.max(0, listeEvenements.size() - 3); // Protège des erreurs si moins de 3 évènements

        // Affichage inversé (le plus récent en premier)
        for (int j = listeEvenements.size() - 1; j >= i; j--) {
            Evenement evenement = listeEvenements.get(j);

            // Etat d'avancement des tâches
            int nbTachesEvenement;
            if (evenement.getTaches() != null) {
                nbTachesEvenement = evenement.getTaches().size();
            } else {
                nbTachesEvenement = 0;
            }
            int nbTachesTermineesEvenement = 0;
            if (evenement.getTaches() != null) {
                for (int t = 0; t < evenement.getTaches().size(); t++) {
                    Tache tache = evenement.getTaches().get(t);
                    if (tache.isEstCochee()) {
                        nbTachesTermineesEvenement++;
                    }
                }
            }

            // On convertit le "0x" de JavaFX en "#" pour le CSS
            String couleurBordure = "#cccccc"; // Gris par défaut
            if (evenement.getCouleur() != null) {
                if (!evenement.getCouleur().isEmpty()) {
                    if (evenement.getCouleur().startsWith("0x")) {
                        couleurBordure = "#" + evenement.getCouleur().substring(2);
                    } else {
                        couleurBordure = evenement.getCouleur();
                    }
                }
            }

            // Conteneur
            VBox card = new VBox(16);
            card.setMaxWidth(350);
            card.setPrefWidth(350);
            card.setMinHeight(300);
            // Couleur sur la bordure
            card.setStyle("-fx-background-color: white; -fx-border-color: " + couleurBordure + "; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 20; -fx-cursor: hand;");

            // Sélectionne l'événement cliqué et bascule vers sa fiche d'informations
            card.setOnMouseClicked((MouseEvent event) -> {
                PageController.getInstance().setEvenementActif(evenement);
                PageController.getInstance().chargerVue("Page_InformationsEvenement.fxml", true, PageController.getInstance().btnInformationsEvenement);
            });

            card.setOnMouseEntered(event -> {
                card.setTranslateY(-7);
                card.setStyle(card.getStyle() + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);");
            });

            card.setOnMouseExited(event -> {
                card.setTranslateY(0);
                card.setStyle(card.getStyle().replace("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);", ""));
            });

            // En-tête de la carte
            HBox header = new HBox(6);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setMaxWidth(Double.MAX_VALUE);

            Label icon = new Label("🎭");
            icon.setStyle("-fx-font-size: 24; -fx-background-color: #e0e0e0; -fx-background-radius: 6; -fx-padding: 5;");

            // Espacement pour pousser le badge à droite
            Region spaceR = new Region();
            HBox.setHgrow(spaceR, Priority.ALWAYS);

            // Statut
            String statutText = evenement.getStatut().getStatut();
            Label badge = new Label(statutText);
            badge.setStyle(evenement.getStatut().getStyleCss());

            header.getChildren().addAll(icon, spaceR, badge);

            // Corps de la carte
            Label name = new Label(evenement.getNom());
            name.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
            name.setMaxWidth(Double.MAX_VALUE);

            String dateString;
            if (evenement.getDateDebut() != null) {
                dateString = evenement.getDateDebut().toString();
            } else {
                dateString = "Non définie";
            }
            Label date = new Label("📅 Date : " + dateString);
            date.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

            Label participants = new Label("👥 Participant(s) : " + evenement.getMembres().size());
            participants.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

            BilanFinancier bilanFinancier = new BilanFinancier(evenement);
            Label balance = new Label("💰 Bénéfices : " + bilanFinancier.calculerSoldeNet() + " €");
            balance.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

            Label lblTaches = new Label("📋 Progression :");
            lblTaches.setStyle("-fx-font-size: 14; -fx-text-fill: #555555;");

            // Progression
            HBox progressBox = new HBox(10);
            progressBox.setAlignment(Pos.CENTER_LEFT);
            progressBox.setMaxWidth(Double.MAX_VALUE);

            ProgressBar progressBar = new ProgressBar();
            progressBar.setStyle("-fx-accent: #1E90FF;");
            progressBar.setPrefHeight(16);
            progressBar.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(progressBar, Priority.ALWAYS);

            // Alignement du ratio de la barre
            double progressionRatio;
            if (nbTachesEvenement > 0) {
                progressionRatio = (double) nbTachesTermineesEvenement / nbTachesEvenement;
            } else {
                progressionRatio = 0.0;
            }
            progressBar.setProgress(progressionRatio);

            Label lblRatioText = new Label(nbTachesTermineesEvenement + "/" + nbTachesEvenement);
            lblRatioText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

            progressBox.getChildren().addAll(progressBar, lblRatioText);

            // Derniers sous-composants dans le conteneur de la carte
            card.getChildren().addAll(header, name, date, participants, balance, lblTaches, progressBox);
            progressionRow.getChildren().add(card);
        }

        statNbEvenements.setText(String.valueOf(totalEvenements));
        statSoldes.setText(sommeBenefices + " €");

        // Calcul du pourcentage global d'avancement (sans division par zéro)
        if (totalTachesTermineesGlobales > 0) {
            statTaches.setText((int) totalTachesTermineesGlobales + "%");
        } else {
            statTaches.setText("0%");
        }

        if (statTotalParticipants != null) {
            statTotalParticipants.setText(String.valueOf(participantsUniques));
        }
        if (statTemplates != null) {
            statTemplates.setText(String.valueOf(PageController.getInstance().getGestionnaire().getTemplateEvenements().size()));
        }

    }


    // METHODES

    /**
     * Intercepte le survol d'une carte.
     * Injecte un effet CSS d'ombrage.
     *
     * @param mouseEvent Événement d'entrée souris fourni par l'implémentation JavaFX.
     */
    @FXML
    void handleStatCardHovered(MouseEvent mouseEvent) {
        VBox card = (VBox) mouseEvent.getSource();
        card.setStyle(card.getStyle() + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);");
    }

    /**
     * Intercepte la sortie du curseur d'une carte.
     * Supprime l'effet d'ombrage.
     *
     * @param mouseEvent Événement de sortie souris fourni par l'implémentation JavaFX.
     */
    @FXML
    void handleStatCardExited(MouseEvent mouseEvent) {
        VBox card = (VBox) mouseEvent.getSource();
        card.setStyle(card.getStyle().replace("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);", ""));
    }

    /**
     * Déclenche une redirection vers la page de liste des évènements
     *
     * @param mouseEvent Événement de clic de souris fourni par l'implémentation JavaFX.
     */
    @FXML
    void handleStatEvenementsClicked(MouseEvent mouseEvent) {
        PageController.getInstance().chargerVue("Page_ListeEvenements.fxml", false, PageController.getInstance().btnListeEvenements);
    }

    /**
     * Déclenche une popup de la liste des templates
     *
     * @param mouseEvent Événement de clic de souris fourni par l'implémentation JavaFX.
     */
    public void handleTemplateEvenementsClicked(MouseEvent mouseEvent) {
        PageController.getInstance().afficherVue("Popup_ChoixTemplates.fxml", "Liste des templates");
    }

}