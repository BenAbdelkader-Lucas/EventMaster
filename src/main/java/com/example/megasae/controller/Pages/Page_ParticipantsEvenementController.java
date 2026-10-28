package com.example.megasae.controller.Pages;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Ajout.Popup_AjoutMembreController;
import com.example.megasae.controller.Popups.Attribution.Popup_AttributionMembreController;
import com.example.megasae.controller.Popups.Attribution.Popup_AttributionTagController;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Equipe.StatutPresence;

import com.example.megasae.model.Miscellaneous.Tag;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.naming.directory.ModificationItem;
import java.util.List;
import java.util.Optional;

public class Page_ParticipantsEvenementController {

    // COMPOSANTS FXML ET ATTRIBUTS

    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private TextField txtRecherche;
    @FXML
    private Button btnFiltrer;
    @FXML
    private Button btnImporterMembre;
    @FXML
    private Button btnNouveauMembre;

    @FXML
    private TableView<Membre> tableMembres;
    @FXML
    private TableColumn<Membre, String> colMembre;
    @FXML
    private TableColumn<Membre, String> colEmail;
    @FXML
    private TableColumn<Membre, List<Role>> colRole;
    @FXML
    private TableColumn<Membre, List<Tag>> colTags;
    @FXML
    private TableColumn<Membre, Void> colPresence;
    @FXML
    private TableColumn<Membre, Void> colActions;

    private Evenement evenementActif;
    private ObservableList<Membre> listeObservableMembres;
    private FilteredList<Membre> listeFiltree;


    // INITIALISATION

    /**
     * Méthode lancée automatiquement par JavaFX quand la page s'affiche.
     * Elle lance le dessin de la grille des participants.
     */
    @FXML
    public void initialize() {
        evenementActif = PageController.getInstance().getEvenementActif();
        chargerDonnees();
        configurerColonnes();
    }

    /**
     * Charge ou recharge les membres de l'événement dans le TableView
     */
    public void chargerDonnees() {
        List<Membre> membres = evenementActif.getMembres();

        if (membres != null) {
            listeObservableMembres = FXCollections.observableArrayList(membres);
        } else {
            listeObservableMembres = FXCollections.observableArrayList();
        }

        // Recherche instantanée
        listeFiltree = new FilteredList<>(listeObservableMembres, b -> true);
        SortedList<Membre> listeTriee = new SortedList<>(listeFiltree);
        listeTriee.comparatorProperty().bind(tableMembres.comparatorProperty());

        tableMembres.setItems(listeTriee);
    }

    /**
     * Fait le lien entre les colonnes FXML et les propriétés réelles de l'objet Membre
     */
    private void configurerColonnes() {

        colMembre.setStyle("-fx-alignment: CENTER;");
        colEmail.setStyle("-fx-alignment: CENTER;");
        colRole.setStyle("-fx-alignment: CENTER;");
        colPresence.setStyle("-fx-alignment: CENTER;");
        colActions.setStyle("-fx-alignment: CENTER;");

        // COLONNE MEMBRE : Concaténation du Prénom et du Nom
        colMembre.setCellValueFactory(cellData -> {
            Membre m = cellData.getValue();
            String prenom = m.getPrenom() != null ? m.getPrenom() + " " : "";
            String nom = m.getNom() != null ? m.getNom() : "";
            return new SimpleStringProperty(prenom + nom);
        });

        // COLONNE EMAIL
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getMail() != null ? cellData.getValue().getMail() : "Non renseigné"
        ));

        // COLONNE RÔLE : Liste des rôles
        colRole.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRoles()));
        colRole.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(List<Role> roles, boolean empty) {
                super.updateItem(roles, empty);
                if (empty || roles == null || roles.isEmpty()) {
                    setGraphic(null);
                } else {
                    HBox conteneur = new HBox(5);
                    conteneur.setAlignment(Pos.CENTER);
                    for (Role r : roles) {
                        Label lbl = new Label(r.getNom());
                        String col = (r.getCouleur() != null) ? r.getCouleur() : "#2D8CD5";
                        lbl.setStyle("-fx-background-color: " + col + "; -fx-background-radius: 6px; -fx-padding: 3 7 3 7; -fx-text-fill: ladder(" + col + ", white 49%, black 50%); -fx-font-weight: bold;");
                        conteneur.getChildren().add(lbl);
                    }
                    setGraphic(conteneur);
                }
            }
        });

        // COLONNE PRÉSENCE : Génération d'une CheckBox interactive
        colPresence.setCellFactory(param -> new TableCell<Membre, Void>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setAlignment(Pos.CENTER);
                checkBox.setStyle("-fx-cursor: hand;");
                checkBox.setOnAction(event -> {
                    Membre membre = getTableView().getItems().get(getIndex());
                    if (membre != null) {
                        if (checkBox.isSelected()) {
                            membre.setPresence(StatutPresence.PRESENT);
                        } else {
                            membre.setPresence(StatutPresence.ABSENT);
                        }
                        PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
                    }
                });
            }

            @Override
            protected void updateItem(Void element, boolean celluleVide) {
                super.updateItem(element, celluleVide);

                if (celluleVide || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Membre membreLigne = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(membreLigne.getPresence() == StatutPresence.PRESENT);
                    setGraphic(checkBox);
                }
            }
        });

        // 2. COLONNE TAGS (Style avec badges)
        colTags.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTags()));
        colTags.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(List<Tag> tags, boolean empty) {
                super.updateItem(tags, empty);
                if (empty || tags == null || tags.isEmpty()) {
                    setGraphic(null);
                } else {
                    HBox conteneur = new HBox(5);
                    conteneur.setAlignment(Pos.CENTER);
                    for (Tag t : tags) {
                        Label lbl = new Label(t.getLibelle());
                        String col = (t.getCouleur() != null) ? t.getCouleur() : "#BDC3C7";
                        lbl.setStyle("-fx-background-color: " + col + "; -fx-background-radius: 6px; -fx-padding: 3 7 3 7; -fx-text-fill: ladder(" + col + ", white 49%, black 50%); -fx-font-weight: bold;");
                        conteneur.getChildren().add(lbl);
                    }
                    setGraphic(conteneur);
                }
            }
        });

        // COLONNE ACTIONS : Création des 4 boutons (Exporter, Dupliquer, Modifier, Supprimer)
        colActions.setCellFactory(param -> new TableCell<>() {

            Button btnDupliquer = new Button("", getIcone("MembreDupliquer.png"));
            Button btnModifier = new Button("", getIcone("MembreModifier.png"));
            Button btnSupprimer = new Button("", getIcone("MembreSupprimer.png"));
            private final HBox paneActions = new HBox(8, btnDupliquer, btnModifier, btnSupprimer);

            {
                paneActions.setAlignment(Pos.CENTER);
                String styleBase = "-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14;";
                btnDupliquer.setStyle(styleBase);
                btnModifier.setStyle(styleBase);
                btnSupprimer.setStyle(styleBase);

                // Tooltips pour aider l'utilisateur à l'affichage
                btnDupliquer.setTooltip(new Tooltip("Dupliquer le membre"));
                btnModifier.setTooltip(new Tooltip("Modifier"));
                btnSupprimer.setTooltip(new Tooltip("Supprimer"));

                // ACTION DUPLIQUER
                btnDupliquer.setOnAction(event -> {
                    Membre membreSelect = getTableView().getItems().get(getIndex());
                    dupliquerMembre(membreSelect);
                });

                // ACTION MODIFIER
                btnModifier.setOnAction(event -> {
                    Membre membre = getTableView().getItems().get(getIndex());
                    modifierMembre(membre);
                });

                // ACTION SUPPRIMER
                btnSupprimer.setOnAction(event -> {
                    Membre membre = getTableView().getItems().get(getIndex());
                    supprimerMembre(membre);
                    PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
                });
            }

            @Override
            protected void updateItem(Void element, boolean vide) {
                // Réinitialise la cellule
                super.updateItem(element, vide);
                if (vide || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    setGraphic(paneActions);
                }
            }
        });
        colPresence.setPrefWidth(80);

    }

    /**
     * Récupérer les icones dans le fichier des images et définir sa taille 20x20
     *
     * @param nomFichier
     * @return
     */
    public ImageView getIcone(String nomFichier) {
        ImageView icone = new ImageView(new Image(getClass().getResourceAsStream("/com/example/megasae/images/" + nomFichier)));
        icone.setFitWidth(20);
        icone.setFitHeight(20);
        return icone;
    }


    // ACTIONS

    /**
     * todo
     *
     * @param membre Le membre à exporter.
     */
    private void exporterMembre(Membre membre) {
        //TODO
    }

    /**
     * Gère la duplication d'un membre à l'aide de sa méthode interne de duplication
     */
    private void dupliquerMembre(Membre membre) {
        // Boîte de dialogue pour saisir le nombre de copies souhaitées
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Dupliquer le membre");
        dialog.setHeaderText("Duplication de : " + (membre.getPrenom() != null ? membre.getPrenom() + " " : "") + membre.getNom());
        dialog.setContentText("Nombre de copies à créer :");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(quantiteStr -> {
            try {
                int nbCopies = Integer.parseInt(quantiteStr.trim());

                if (nbCopies <= 0) {
                    PageController.getInstance().afficherAlerte("Veuillez saisir un nombre supérieur à 0.");
                    return;
                }

                // Appel de la méthode de duplication
                List<Membre> copies = membre.dupliquerMembresCopies(nbCopies);

                // Ajout des copies
                for (int c = 0; c < copies.size(); c++) {
                    evenementActif.getMembres().add(copies.get(c));
                    listeObservableMembres.add(copies.get(c));
                }
            } catch (NumberFormatException e) {
                PageController.getInstance().afficherAlerte("Veuillez entrer un nombre valide.");
            } catch (IllegalStateException e) {
                PageController.getInstance().afficherAlerte("Capacité insuffisante");
            }
        });
    }

    /**
     * Ouvre la popup de modification d'un membre et met à jour l'affichage de la liste des membres
     *
     * @param membre
     */
    private void modifierMembre(Membre membre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Ajout/Popup_AjoutMembre.fxml"));
            Scene scene = new Scene(loader.load());

            Popup_AjoutMembreController popup_ajoutMembreController = loader.getController();
            popup_ajoutMembreController.initialisationValeur(membre);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            popupStage.setTitle("Modification m'un membre");
            popupStage.setScene(scene);

            popupStage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
        chargerDonnees();
        tableMembres.refresh();
    }

    /**
     * Demande confirmation de suppression d'un membre
     *
     * @param membre
     */
    private void supprimerMembre(Membre membre) {
        ButtonType choix = PageController.getInstance().afficherSuppression(Alert.AlertType.CONFIRMATION, "Confirmation de suppression", "Voulez-vous vraiment supprimer ce membre ?");
        if (choix == ButtonType.YES) {
            evenementActif.getMembres().remove(membre);
            listeObservableMembres.remove(membre);
            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
        }
    }

    /**
     * todo
     */
    @FXML
    void handleImporterMembre(ActionEvent event) {
        // TODO PageController.getInstance().afficherVue("Popup_ImporterMembre.fxml", "Importer membre");
    }

    /**
     * Ouvre la popup d'ajout d'un membre et met à jour l'affichage de la liste des membres
     */
    @FXML
    void handleNouveauMembre(ActionEvent event) {
        PageController.getInstance().afficherVue("Popup_AjoutMembre.fxml", "Nouveau membre");
        PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
        chargerDonnees();
        tableMembres.refresh();
    }

    /**
     * todo
     *
     * @param event
     */
    @FXML
    void handleFiltrer(ActionEvent event) {
        //TODO PageController.getInstance().afficherVue("Popup_FiltresMembres.fxml", "Filtrer membre");
    }

    /**
     * Trie les membres en fonction de ce que l'utilisateur tape dans la barre de recherche (nom, prenom, mail)
     */
    @FXML
    void handleRecherche(KeyEvent event) {
        String motCle = txtRecherche.getText().toLowerCase();

        listeFiltree.setPredicate(membre -> {
            if (motCle.isEmpty()) return true;

            boolean matchNom = membre.getNom() != null && membre.getNom().toLowerCase().contains(motCle);
            boolean matchPrenom = membre.getPrenom() != null && membre.getPrenom().toLowerCase().contains(motCle);
            boolean matchEmail = membre.getMail() != null && membre.getMail().toLowerCase().contains(motCle);

            return matchNom || matchPrenom || matchEmail;
        });
    }

}