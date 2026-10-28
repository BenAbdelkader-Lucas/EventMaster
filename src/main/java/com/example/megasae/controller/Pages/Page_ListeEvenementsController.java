package com.example.megasae.controller.Pages;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Finance.BilanFinancier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe sert à afficher tous les événements sous forme de grille de cartes.
 * Elle crée les cartes une par une en code, gère le clic pour ouvrir un événement et contient les boutons pour éditer, cloner ou supprimer.
 *
 * @version 1.0
 */
public class Page_ListeEvenementsController {

    // COMPOSANTS FXML ET ATTRIBUTS

    @FXML
    private GridPane eventsGrid;

    @FXML
    private TextField txtRecherche;

    @FXML
    private Button btnFilter;

    private ObservableList<Evenement> listeObservableEvenements;
    private FilteredList<Evenement> listeFiltree;


    // INITIALISATION

    /**
     * Méthode lancée automatiquement par JavaFX quand la page s'affiche.
     * Elle lance le dessin de la grille d'évènements.
     */
    @FXML
    public void initialize() {
        List<Evenement> listeBrute = PageController.getInstance().getGestionnaire().getEvenements();

        if (listeBrute != null) {
            listeObservableEvenements = FXCollections.observableArrayList(listeBrute);
        } else {
            listeObservableEvenements = FXCollections.observableArrayList();
        }

        listeFiltree = new FilteredList<>(listeObservableEvenements, b -> true);
        rafraichirGrilleEvenements();
    }


    // MÉTHODES AFFICHAGE

    /**
     * Parcourt la liste des événements et fabrique une "carte" (VBox) pour chacun d'eux, puis les place dans le GridPane sur 2 colonnes.
     */
    public void rafraichirGrilleEvenements() {

        // On vide la grille
        eventsGrid.getChildren().clear();

        // Si la liste filtrée est vide ou n'existe pas, on arrête
        if (listeFiltree == null || listeFiltree.isEmpty()) {
            return;
        }

        // Variables pour savoir où placer la carte dans la grille
        int colonne = 0;
        int ligne = 0;

        // CORRECTION : On boucle maintenant sur la liste filtrée !
        for (int i = 0; i < listeFiltree.size(); i++) {
            Evenement evenement = listeFiltree.get(i);

            // On convertit le "0x" de JavaFX en "#" pour le CSS
            String couleurBordure = "#cccccc";
            if (evenement.getCouleur() != null) {
                if (!evenement.getCouleur().isEmpty()) {
                    if (evenement.getCouleur().startsWith("0x")) {
                        couleurBordure = "#" + evenement.getCouleur().substring(2);
                    } else {
                        couleurBordure = evenement.getCouleur();
                    }
                }
            }

            // Conteneur de la carte
            VBox card = new VBox(12);
            card.setStyle("-fx-transition: 2s ease-out;-fx-background-color: white; -fx-border-color: " + couleurBordure + "; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 18; -fx-cursor: hand;");

            card.setOnMouseEntered(event -> {
                card.setTranslateY(-7);
                card.setStyle(card.getStyle() + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);");
            });

            card.setOnMouseExited(event -> {
                card.setTranslateY(0);
                card.setStyle(card.getStyle().replace("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);", ""));
            });

            // Prendre toute la place en largeur dans sa case de la grille
            card.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(card, Priority.ALWAYS);

            // Clic sur la carte -> On active cet événement et on charge la page de détails
            card.setOnMouseClicked((MouseEvent event) -> {
                PageController.getInstance().setEvenementActif(evenement);
                PageController.getInstance().chargerVue("Page_InformationsEvenement.fxml", true, PageController.getInstance().btnInformationsEvenement);
            });

            // Header
            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setMaxWidth(Double.MAX_VALUE);

            // Icone
            Label lblIcon = new Label("🎭");
            lblIcon.setStyle("-fx-font-size: 26; -fx-background-color: #e0e0e0; -fx-background-radius: 8; -fx-padding: 6 8;");

            // Espacement
            Region headerSpacer = new Region();
            HBox.setHgrow(headerSpacer, Priority.ALWAYS);

            // Statut (brouillon, validé...)
            Label badgeStatut = new Label(evenement.getStatut().getStatut());
            badgeStatut.setStyle(evenement.getStatut().getStyleCss());
            header.getChildren().addAll(lblIcon, headerSpacer, badgeStatut);

            // Titre
            Label lblNom = new Label(evenement.getNom());
            lblNom.setStyle("-fx-font-size: 17; -fx-font-weight: bold;");
            lblNom.setMaxWidth(Double.MAX_VALUE);

            // Dates
            String dateString = "Non définie";
            if (evenement.getDateDebut() != null) {
                dateString = evenement.getDateDebut().toString();
            }
            Label lblDate = new Label("Date : " + dateString);
            lblDate.setStyle("-fx-font-size: 13;");
            lblDate.setMaxWidth(Double.MAX_VALUE);

            // Boucle pour les lieux
            String lieuxString = "";
            if (evenement.getCreneaux() == null || evenement.getCreneaux().isEmpty()) {
                lieuxString = "Non défini";
            } else {
                for (int l = 0; l < evenement.getCreneaux().size() && l <= 2; l++) {
                    lieuxString += evenement.getCreneaux().get(l).getAdresse() + ", ";
                }
                if (evenement.getMembres().size() > 3) {
                    lieuxString += "...";
                } else {
                    lieuxString = lieuxString.substring(0, lieuxString.length() - 2);
                }
            }
            Label lblLieu = new Label("Lieu(x) : " + lieuxString);
            lblLieu.setStyle("-fx-font-size: 13;");
            lblLieu.setMaxWidth(Double.MAX_VALUE);

            // Couts
            BilanFinancier bilanFinancier = new BilanFinancier(evenement);
            Label lblDepenses = new Label("Dépenses : " + bilanFinancier.calculerTotalDepenses() + " €");
            lblDepenses.setStyle("-fx-font-size: 13;");
            lblDepenses.setMaxWidth(Double.MAX_VALUE);
            Label lblRevenus = new Label("Revenus : " + bilanFinancier.calculerTotalRecettes() + " €");
            lblRevenus.setStyle("-fx-font-size: 13;");
            lblRevenus.setMaxWidth(Double.MAX_VALUE);

            // Boucle pour les participants
            String membresString = "";
            if (evenement.getMembres() == null || evenement.getMembres().isEmpty()) {
                membresString = "Aucun";
            } else {
                for (int j = 0; j < evenement.getMembres().size() && j <= 3; j++) {
                    membresString += evenement.getMembres().get(j).getNom() + ", ";
                }
                if (evenement.getMembres().size() > 3) {
                    membresString += "...";
                } else {
                    membresString = membresString.substring(0, membresString.length() - 2);
                }
            }
            Label lblParticipants = new Label("Participant(s) : " + membresString);
            lblParticipants.setStyle("-fx-font-size: 13;");
            lblParticipants.setMaxWidth(Double.MAX_VALUE);

            // Ligne de séparation
            Separator sep = new Separator();
            sep.setMaxWidth(Double.MAX_VALUE);

            // Boutons d'action
            HBox actionsBox = new HBox(10);
            actionsBox.setAlignment(Pos.CENTER_LEFT);
            actionsBox.setMaxWidth(Double.MAX_VALUE);

            Button btnEditer = new Button("Éditer");
            btnEditer.setStyle("-fx-background-color: #319ebc;-fx-text-fill: white;   -fx-border-color: #DBEAFE;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);");
            btnEditer.setOnMouseClicked(e -> e.consume());
            btnEditer.setOnAction(e -> {
                PageController.getInstance().setEvenementActif(evenement);
                PageController.getInstance().afficherVue("Popup_Evenement.fxml", "Modifier");
                rafraichirGrilleEvenements();
            });

            btnEditer.setOnMouseEntered(event -> {
                btnEditer.setStyle(("-fx-background-color: white;-fx-border-color: #319ebc;-fx-text-fill: #319ebc;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);"));
            });

            btnEditer.setOnMouseExited(event -> {
                btnEditer.setStyle("-fx-background-color: #319ebc;-fx-text-fill: white;   -fx-border-color: #DBEAFE;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);");
            });

            Button btnDupliquer = new Button("Dupliquer");
            btnDupliquer.setStyle("-fx-background-color: #319ebc;-fx-text-fill: white;   -fx-border-color: #DBEAFE;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);");
            btnDupliquer.setOnMouseClicked(e -> e.consume());
            btnDupliquer.setOnAction(e -> {
                try {
                    Evenement copie = new Evenement(evenement.getNom() + " (Copie)", evenement.getDateDebut(), evenement.getDateFin());
                    copie.setBudget(evenement.getBudget());
                    copie.setStatut(evenement.getStatut());
                    copie.setMembres(new ArrayList<>(evenement.getMembres()));

                    // CORRECTION : On met à jour le gestionnaire ET la liste observable
                    PageController.getInstance().getGestionnaire().getEvenements().add(copie);
                    listeObservableEvenements.add(copie);

                    PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
                    rafraichirGrilleEvenements();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            btnDupliquer.setOnMouseEntered(event -> {
                btnDupliquer.setStyle(("-fx-background-color: white;-fx-border-color: #319ebc;-fx-text-fill: #319ebc;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);"));
            });

            btnDupliquer.setOnMouseExited(event -> {
                btnDupliquer.setStyle("-fx-background-color: #319ebc;-fx-text-fill: white;   -fx-border-color: #DBEAFE;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);");
            });


            Button btnTemplate = new Button("Template");
            btnTemplate.setStyle("-fx-background-color: #319ebc;-fx-text-fill: white;   -fx-border-color: #DBEAFE;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);");
            btnTemplate.setOnMouseClicked(e -> e.consume());
            btnTemplate.setOnAction(e -> {
                try {
                    PageController.getInstance().getGestionnaire().addTemplateEvenement(evenement.getTemplateEvenement());
                    PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
                } catch (IllegalArgumentException ex) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText("Template déjà existant");
                    alert.setContentText("Ce template est déjà présent dans votre liste.");
                    alert.showAndWait();
                }
            });
            btnTemplate.setOnMouseEntered(event -> {
                btnTemplate.setStyle(("-fx-background-color: white;-fx-border-color: #319ebc;-fx-text-fill: #319ebc;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);"));
            });

            btnTemplate.setOnMouseExited(event -> {
                btnTemplate.setStyle("-fx-background-color: #319ebc;-fx-text-fill: white;   -fx-border-color: #DBEAFE;-fx-border-width: 2px;-fx-border-radius: 12px;  -fx-background-radius: 12px; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.05), 10, 0, 0, 4);");
            });

            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setStyle("-fx-background-color: #DC143C; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 8 18; -fx-cursor: hand; -fx-min-width: 110;");
            btnSupprimer.setOnMouseClicked(e -> e.consume());
            btnSupprimer.setOnAction(e -> {
                ButtonType choix = PageController.getInstance().afficherSuppression(Alert.AlertType.CONFIRMATION, "Confirmation de suppression", "Voulez-vous vraiment supprimer cet événement ?");
                if (choix == ButtonType.YES) {
                    // CORRECTION : On retire des deux listes
                    PageController.getInstance().getGestionnaire().getEvenements().remove(evenement);
                    listeObservableEvenements.remove(evenement);

                    PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
                    rafraichirGrilleEvenements();
                }
            });

            Region actionsSpacer = new Region();
            HBox.setHgrow(actionsSpacer, Priority.ALWAYS);

            actionsBox.getChildren().addAll(btnEditer, btnDupliquer, btnTemplate, actionsSpacer, btnSupprimer);
            card.getChildren().addAll(header, lblNom, lblDate, lblLieu, lblDepenses, lblRevenus, lblParticipants, sep, actionsBox);
            eventsGrid.add(card, colonne, ligne);

            colonne++;
            if (colonne > 1) {
                colonne = 0;
                ligne++;
            }
        }
    }


    // METHODES FILTRES ET DES RECHERCHES

    /**
     * Ouvre la fenêtre popup pour choisir les filtres.
     *
     * @param event L'événement du clic.
     */
    @FXML
    void handleFilterAction(ActionEvent event) {
        //TODO PageController.getInstance().afficherVue("Popup_Filtres.fxml", "Filtrer EVENEMENT");
    }

    /**
     * Action déclenchée par la recherche.
     * Trie les évènements en fonction de ce que l'utilisateur tape dans la barre de recherche ()
     *
     * @param event L'événement du clavier.
     */
    @FXML
    void handleRecherche(KeyEvent event) {
        String motCle = txtRecherche.getText().toLowerCase();

        listeFiltree.setPredicate(evenement -> {
            if (motCle.isEmpty()) return true;

            boolean matchNom = evenement.getNom() != null && evenement.getNom().toLowerCase().contains(motCle);
            boolean matchDescription = evenement.getDescription() != null && evenement.getDescription().toLowerCase().contains(motCle);
            boolean matchStatut = evenement.getStatut() != null && evenement.getStatut().getStatut().toLowerCase().contains(motCle);

            return matchNom || matchDescription || matchStatut;
        });

        rafraichirGrilleEvenements();
    }


    /**
     * Gère le survol du bouton de filtre.
     */
    public void handleFilterBtnHovered(MouseEvent mouseEvent) {
        //TODO filtre
    }

    /**
     * Gère la sortie de souris du bouton de filtre.
     */
    public void handleFilterBtnExited(MouseEvent mouseEvent) {
        //TODO filtre
    }

}