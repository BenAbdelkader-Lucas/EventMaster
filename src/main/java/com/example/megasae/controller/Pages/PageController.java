package com.example.megasae.controller.Pages;

import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.GestionnaireDesEvenements;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javafx.scene.control.ContentDisplay;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gère le cycle de vie de la fenêtre principale, la navigation globale par sous-vues FXML, ainsi que l'état de l'application (événement et utilisateur actifs).
 *
 * @version 1.0
 */
public class PageController {

    // COMPOSANTS FXML ET ATTRIBUTS

    @FXML
    public Label labelVueGenerale;
    @FXML
    public Button btnTableauDeBord;
    @FXML
    public Button btnListeEvenements;
    @FXML
    public VBox sousMenuEvenement;
    @FXML
    public Button btnInformationsEvenement;
    @FXML
    public Button btnParticipantsEvenement;
    @FXML
    public Button btnTachesEvenement;
    @FXML
    public Button btnFinancesEvenement;

    @FXML
    public AnchorPane topSpace1;
    @FXML
    public AnchorPane topSpace2;
    @FXML
    public Label lblNomEvenementActif;
    @FXML
    public Button btnEditerEvenementActif;
    @FXML
    public Button btnExporterEvenementActif;

    /**
     * Zone d'ancrage centrale où sont injectées alternativement les sous-pages FXML.
     */
    @FXML
    private StackPane mainBoundary;

    /**
     * Référence statique de l'événement en cours de traitement dans l'application.
     */
    private static Evenement evenementActif;

    /**
     * Référence statique du membre / utilisateur actuellement connecté à la session.
     */
    private static Membre membreActif;

    /**
     * Instance unique du contrôleur (Pattern d'accès global pour les sous-pages).
     */
    private static PageController instance;

    /**
     * Stockage du bouton de navigation actuellement sélectionné pour figer son style.
     */
    private Button boutonActuel;

    /**
     * Logger de l'application configuré sur le package du modèle Événement pour centraliser les traces.
     */
    private static final Logger LOGGER = Logger.getLogger(Evenement.class.getPackageName());

    /**
     * Liste principale contenant l'ensemble des événements managés.
     */
    private GestionnaireDesEvenements gestionnaire = new GestionnaireDesEvenements();

    /**
     * Style par défaut appliqué aux boutons de la sidebar lorsqu'ils ne sont pas sélectionnés.
     */
    private final String STYLE_INACTIF = "-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 16; -fx-background-radius: 25; -fx-padding: 10 15; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";

    /**
     * tyle appliqué au bouton correspondant à la page actuellement affichée à l'écran.
     */
    private final String STYLE_ACTIF = "-fx-background-color: #3b82f6; -fx-text-fill: black; -fx-font-size: 16; -fx-background-radius: 25; -fx-padding: 10 15; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";

    /**
     * Style appliqué temporairement au survol de la souris sur un bouton inactif.
     */
    private final String STYLE_HOVER = "-fx-background-color: rgba(59, 130, 246, 0.3); -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 25; -fx-padding: 10 15; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";


    // INITIALISATION

    /**
     * Initialise les composants de l'interface graphique immédiatement après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        instance = this;
        // Récupérer les données sauvegardées
        this.gestionnaire = gestionnaire.chargerDonnees();
        // Affichage de la page par défaut au démarrage
        chargerVue("Page_TableauDeBord.fxml", false, btnTableauDeBord);
    }

    /**
     * Instancie dynamiquement un fichier FXML et l'intègre au centre du layout principal.
     * Met également à jour l'état visuel des menus de navigation.
     *
     * @param nomFichier   Nom complet du fichier FXML cible (ex: "Page_FinancesEvenement.fxml").
     * @param menu         Indicateur de visibilité des menus contextuels liés à un événement.
     * @param boutonClique Instance du bouton cliqué pour appliquer la mise en surbrillance.
     */
    public void chargerVue(String nomFichier, Boolean menu, Button boutonClique) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Pages/" + nomFichier));
            Parent nouvelleVue = loader.load();
            // Nettoyage de la zone centrale et injection de la nouvelle page
            mainBoundary.getChildren().clear();
            mainBoundary.getChildren().add(nouvelleVue);
            // Actualisation des menus
            setSousMenuVisible(menu);
            rafraichirMenuGauche(boutonClique);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'arborescence ou du chargement de la vue : " + nomFichier, e);
        }
    }

    /**
     * Crée et affiche une fenêtre de dialogue modale (Popup bloquante) à partir d'un FXML.
     *
     * @param nomFichier FXML de la popup stocké dans le sous-dossier Popups.
     * @param nomFenetre Texte de titre à assigner au bandeau supérieur du Stage.
     */
    public void afficherVue(String nomFichier, String nomFenetre) {
        List<String> chemin = new ArrayList<>(Arrays.asList("Ajout/", "Attribution/", "Choix/", ""));

        javafx.scene.Parent root = null;

        for (String dossier : chemin) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/" + dossier + nomFichier));
                root = loader.load();
                break;
            } catch (Exception e) {
            }
        }
        Stage popupStage = new Stage();
        popupStage.setResizable(false);
        popupStage.setTitle(nomFenetre);
        popupStage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        popupStage.showAndWait();
    }

    /**
     * Petite méthode bien pratique pour afficher des fenêtres d'infos, d'erreurs ou de warnings.
     *
     * @param message Le texte explicatif à afficher.
     */
    public void afficherAlerte(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }

    public ButtonType afficherSuppression(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        return alert.getResult();
    }

    /**
     * Conversion de la couleur en format Hexadécimal pour le CSS
     *
     * @param value
     * @return
     */
    public static String toHexString(Color value) {
        return String.format("#%02X%02X%02X", (int) (value.getRed() * 255), (int) (value.getGreen() * 255), (int) (value.getBlue() * 255));
    }

    // METHODES MENU

    /**
     * Gère la cohérence esthétique de la navigation.
     * Réinitialise le style de l'ensemble des boutons et applique le marquage actif uniquement sur l'élément sélectionné.
     *
     * @param boutonActif Le bouton qui doit prendre le focus visuel.
     */
    private void rafraichirMenuGauche(Button boutonActif) {
        this.boutonActuel = boutonActif;

        // Reset global des styles en ligne
        btnTableauDeBord.setStyle(STYLE_INACTIF);
        btnListeEvenements.setStyle(STYLE_INACTIF);
        btnInformationsEvenement.setStyle(STYLE_INACTIF);
        btnParticipantsEvenement.setStyle(STYLE_INACTIF);
        btnTachesEvenement.setStyle(STYLE_INACTIF);
        btnFinancesEvenement.setStyle(STYLE_INACTIF);

        this.boutonActuel.setStyle(STYLE_ACTIF);
    }


    /**
     * Alterne l'affichage des bandeaux et volets selon que l'on se situe dans la
     * gestion globale de l'application ou au sein des détails d'un événement spécifique.
     *
     * @param visible True pour activer le contexte d'événement, False pour le mode général.
     */
    private void setSousMenuVisible(boolean visible) {
        sousMenuEvenement.setVisible(visible);
        sousMenuEvenement.setManaged(visible);

        topSpace1.setVisible(!visible);
        topSpace1.setManaged(!visible);

        topSpace2.setVisible(visible);
        topSpace2.setManaged(visible);
    }

    /**
     * Intercepte l'entrée du curseur de la souris sur un bouton de la sidebar.
     * Déclenche l'effet de surbrillance à condition que ce ne soit pas la page active.
     *
     * @param mouseEvent Objet d'événement capturé par le framework JavaFX.
     */
    @FXML
    public void handleSidebarBtnHovered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Button) {
            Button btnSurvole = (Button) mouseEvent.getSource();
            if (btnSurvole != boutonActuel) {
                btnSurvole.setStyle(STYLE_HOVER);
            }
        }
    }

    /**
     * Intercepte la sortie du curseur de la souris d'un bouton de la sidebar.
     * Restitue le style neutre d'origine à condition que ce ne soit pas la page active.
     *
     * @param mouseEvent Objet d'événement capturé par le framework JavaFX.
     */
    @FXML
    public void handleSidebarBtnExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Button) {
            Button btnQuitte = (Button) mouseEvent.getSource();
            if (btnQuitte != boutonActuel) {
                btnQuitte.setStyle(STYLE_INACTIF);
            }
        }
    }


    // ACTIONS

    /**
     * Route l'affichage vers l'écran d'accueil général (Tableau de bord).
     */
    @FXML
    public void handleTableauDeBordAction(ActionEvent actionEvent) {
        LOGGER.log(Level.INFO, "Routage : Redirection Tableau de Bord.");
        chargerVue("Page_TableauDeBord.fxml", false, btnTableauDeBord);
    }

    /**
     * Route l'affichage vers l'écran récapitulant tous les événements existants.
     */
    @FXML
    public void handleListeEvenementsAction(ActionEvent actionEvent) {
        LOGGER.log(Level.INFO, "Routage : Redirection Liste des Événements.");
        chargerVue("Page_ListeEvenements.fxml", false, btnListeEvenements);
    }

    /**
     * Route l'affichage vers l'écran des informations de l'événement actif.
     */
    @FXML
    public void handleEvenementAction(ActionEvent actionEvent) {
        LOGGER.log(Level.INFO, "Routage : Redirection Fiche Information Événement.");
        chargerVue("Page_InformationsEvenement.fxml", true, btnInformationsEvenement);
    }

    /**
     * Route l'affichage vers le module de gestion de la liste des participants.
     */
    @FXML
    public void handleParticipantsAction(ActionEvent actionEvent) {
        LOGGER.log(Level.INFO, "Routage : Redirection Liste Participants Événement.");
        chargerVue("Page_ParticipantsEvenement.fxml", true, btnParticipantsEvenement);
    }

    /**
     * Route l'affichage vers le tableau d'affectation et de suivi des tâches.
     */
    @FXML
    public void handleTachesAction(ActionEvent actionEvent) {
        LOGGER.log(Level.INFO, "Routage : Redirection Planification des Tâches.");
        chargerVue("Page_TachesEvenement.fxml", true, btnTachesEvenement);
    }

    /**
     * Route l'affichage vers la balance et le livre de comptes financier de l'événement.
     */
    @FXML
    public void handleFinancesAction(ActionEvent actionEvent) {
        LOGGER.log(Level.INFO, "Routage : Redirection finances.");
        chargerVue("Page_FinancesEvenement.fxml", true, btnFinancesEvenement);
    }


    // METHODES MENU (HAUT)

    /**
     * Ouvre l'interface ou le formulaire permettant d'ajouter un nouvel événement au système.
     */
    @FXML
    public void handleAjouterEvenementAction(ActionEvent actionEvent) {
        afficherVue("Popup_ChoixEvenements.fxml", "Création d'un évènement");
    }

    /**
     * Déclenche la compilation des données et l'exportation (ex: format PDF) de l'événement en cours.
     */
    @FXML
    public void handleExporterEvenementActifAction(ActionEvent actionEvent) {
        // TODO
    }

    /**
     * Active le mode modification ou ouvre l'écran d'édition des paramètres de l'événement actif.
     */
    @FXML
    public void handleEditerEvenementActifAction(ActionEvent actionEvent) {
        afficherVue("Popup_Evenement.fxml", "Modification d'un évènement");
    }


    // GETTERS / SETTERS

    /**
     * @return L'instance unique globale du contrôleur de page principal.
     */
    public static PageController getInstance() {
        return instance;
    }

    /**
     * @return Le composant bouton associé au Tableau de bord.
     */
    public Button getBtnTableauDeBord() {
        return btnTableauDeBord;
    }

    /**
     * @return Le composant bouton associé à la Liste des événements.
     */
    public Button getBtnListeEvenements() {
        return btnListeEvenements;
    }

    /**
     * @return Le composant bouton associé aux Informations de l'événement.
     */
    public Button getBtnInformationsEvenement() {
        return btnInformationsEvenement;
    }

    /**
     * @return Le composant bouton associé aux Participants de l'événement.
     */
    public Button getBtnParticipantsEvenement() {
        return btnParticipantsEvenement;
    }

    /**
     * @return Le composant bouton associé aux Tâches de l'événement.
     */
    public Button getBtnTachesEvenement() {
        return btnTachesEvenement;
    }

    /**
     * @return Le composant bouton associé aux Finances de l'événement.
     */
    public Button getBtnFinancesEvenement() {
        return btnFinancesEvenement;
    }

    /**
     * @return Le gestionnaire des évènements contenant toute les données de l'application.
     */
    public GestionnaireDesEvenements getGestionnaire() {
        return gestionnaire;
    }

    /**
     * @param gestionnaire Le gestionnaire des évènements contenant toute les données de l'application.
     */
    public void setGestionnaire(GestionnaireDesEvenements gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    /**
     * @return L'événement actuellement sélectionné au sein de l'application.
     */
    public static Evenement getEvenementActif() {
        return evenementActif;
    }

    /**
     * @param evenement L'instance d'événement à définir comme active globalement.
     */
    public void setEvenementActif(Evenement evenement) {
        evenementActif = evenement;
        if (evenement != null) {
            lblNomEvenementActif.setText(evenement.getNom());
            // TODO lblNomEvenementActif.setStyle("-fx-background-color: " + evenementActif.getCouleur() + ";");
        } else {
            lblNomEvenementActif.setText("");
        }
    }

    /**
     * @return Le membre actuellement connecté sur cette session de l'application.
     */
    public static Membre getMembreActif() {
        return membreActif;
    }

    /**
     * @param membreActif L'instance de membre à connecter sur la session.
     */
    public static void setMembreActif(Membre membreActif) {
        PageController.membreActif = membreActif;
    }

}