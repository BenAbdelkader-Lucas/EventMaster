package com.example.megasae.controller.Pages;

import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Finance.BilanFinancier;
import com.example.megasae.model.Finance.DepenseRecette;
import com.example.megasae.model.Miscellaneous.Tag;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class Page_FinancesEvenementController {

    // COMPOSANTS FXML ET ATTRIBUTS

    @FXML private Button btnDepense;
    @FXML private TextField txtRecherche;

    @FXML private TableView<DepenseRecette> tableAllOp;
    @FXML private TableView<DepenseRecette> tableOpRecente;

    @FXML private TableColumn<DepenseRecette, Void> colAnnuler;
    @FXML private TableColumn<DepenseRecette, Void> colAnnulerAll;
    @FXML private TableColumn<DepenseRecette, Double> colMontantAll;
    @FXML private TableColumn<DepenseRecette, Double> colMontant;
    @FXML private TableColumn<DepenseRecette, String> colNom;
    @FXML private TableColumn<DepenseRecette, String> colNomAll;
    @FXML private TableColumn<DepenseRecette, Double> colTotal;
    @FXML private TableColumn<DepenseRecette, Double> colTotalAll;
    @FXML private TableColumn<DepenseRecette, Integer> colQuantite;
    @FXML private TableColumn<DepenseRecette, Integer> colQuantiteAll;
    @FXML private TableColumn<DepenseRecette, List<Tag>> colTagAll;

    @FXML private Label textSolde;
    @FXML private Label textTotalDepenses;
    @FXML private Label textTotalRecettes;

    private Evenement evenementActif;
    private BilanFinancier bilanFinancier;

    // Listes pour gérer la recherche
    private ObservableList<DepenseRecette> listeObservableOperations;
    private FilteredList<DepenseRecette> listeFiltree;


    // INITIALISATION

    public void initialize() {

        colNomAll.setStyle("-fx-alignment: CENTER;");
        colNom.setStyle("-fx-alignment: CENTER;");
        colTotalAll.setStyle("-fx-alignment: CENTER;");
        colTotal.setStyle("-fx-alignment: CENTER;");
        colQuantite.setStyle("-fx-alignment: CENTER;");
        colQuantiteAll.setStyle("-fx-alignment: CENTER;");
        colTagAll.setStyle("-fx-alignment: CENTER;");

        btnDepense.setCursor(javafx.scene.Cursor.HAND);

        evenementActif = PageController.getEvenementActif();
        bilanFinancier = new BilanFinancier(evenementActif);

        faireSynthese();

        // Initialisation de la liste observable
        List<DepenseRecette> opsBrutes = evenementActif.getDepensesRecette();
        if (opsBrutes != null) {
            listeObservableOperations = FXCollections.observableArrayList(opsBrutes);
        } else {
            listeObservableOperations = FXCollections.observableArrayList();
        }

        // ====================== TABLE RECENTE =============================

        if (tableOpRecente != null) {
            colNom.setCellValueFactory(new PropertyValueFactory<>("Libelle"));
            colMontant.setCellValueFactory(new PropertyValueFactory<>("Montant"));
            colMontant.setCellFactory(column -> creerCelluleMontant());
            colAnnuler.setCellFactory(colum -> creerCelluleSuppression());
            colQuantite.setCellValueFactory(new PropertyValueFactory<>("Quantite"));
            colTotal.setCellValueFactory(column -> new SimpleObjectProperty<>(column.getValue().getCoutTotal()));

            // Liée à la liste globale (non filtrée par la recherche)
            tableOpRecente.setItems(listeObservableOperations);
        }

        // ====================== TABLE TOTALE ==============================

        if (tableAllOp != null) {
            colNomAll.setCellValueFactory(new PropertyValueFactory<>("Libelle"));
            colQuantiteAll.setCellValueFactory(new PropertyValueFactory<>("Quantite"));
            colMontantAll.setCellValueFactory(new PropertyValueFactory<>("Montant"));
            colMontantAll.setCellFactory(column -> creerCelluleMontant());
            colAnnulerAll.setCellFactory(param -> creerCelluleSuppression());
            colTotalAll.setCellValueFactory(column -> new SimpleObjectProperty<>(column.getValue().getCoutTotal()));

            colTagAll.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTag()));
            colTagAll.setCellFactory(column -> new TableCell<DepenseRecette, List<Tag>>() {
                @Override
                protected void updateItem(List<Tag> tags, boolean empty) {
                    super.updateItem(tags, empty);

                    if (empty || tags == null || tags.isEmpty()) {
                        setGraphic(null);
                    } else {
                        HBox conteneurTags = new HBox(5);
                        conteneurTags.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                        for (int t=0; t<tags.size(); t++) {
                            Label labelTag = new Label(tags.get(t).getLibelle());
                            labelTag.setStyle("-fx-background-color: " + tags.get(t).getCouleur() + ";" + "-fx-background-radius: 6px;" + "-fx-border-radius: 6px;" + "-fx-text-fill: ladder(" + tags.get(t).getCouleur() + ", white 49%, black 50%);" + "-fx-font-weight: bold;");
                            labelTag.setPadding(new javafx.geometry.Insets(3, 7, 3, 7));
                            conteneurTags.getChildren().add(labelTag);
                        }
                        setGraphic(conteneurTags);
                    }
                }
            });

            // Configuration du filtrage et du tri pour le tableau principal
            listeFiltree = new FilteredList<>(listeObservableOperations, b -> true);
            SortedList<DepenseRecette> listeTriee = new SortedList<>(listeFiltree);
            listeTriee.comparatorProperty().bind(tableAllOp.comparatorProperty());

            tableAllOp.setItems(listeTriee);
        }
    }

    /**
     * Action déclenchée par la recherche (quand on relâche une touche du clavier).
     */
    @FXML
    void handleRecherche(KeyEvent event) {
        String motCle = txtRecherche.getText().toLowerCase();

        listeFiltree.setPredicate(operation -> {
            if (motCle.isEmpty()) return true;

            // Recherche par le nom de l'opération
            boolean matchNom = operation.getLibelle() != null && operation.getLibelle().toLowerCase().contains(motCle);

            // Recherche également à travers les labels des Tags attachés
            boolean matchTag = false;
            if (operation.getTag() != null) {
                for (Tag t : operation.getTag()) {
                    if (t.getLibelle() != null && t.getLibelle().toLowerCase().contains(motCle)) {
                        matchTag = true;
                        break;
                    }
                }
            }

            return matchNom || matchTag;
        });
    }

    /**
     * Créer la cellule montant du tableau
     */
    private TableCell<DepenseRecette, Double> creerCelluleMontant() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);

                DepenseRecette depenseRecette = getTableRow().getItem();

                if (empty || montant == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else if (depenseRecette.isDepense()) {
                    setText("-" + montant + "€");
                    setStyle("-fx-text-fill: #e63946; -fx-font-weight: bold;-fx-alignment: CENTER;");
                } else {
                    setText("+" + montant + "€");
                    setStyle("-fx-text-fill: #2ec4b6; -fx-font-weight: bold;-fx-alignment: CENTER;");
                }
            }
        };
    }

    /**
     * Créer la cellule suppression du tableau
     */
    private <T> TableCell<DepenseRecette, T> creerCelluleSuppression() {
        return new TableCell<>() {
            private final Button btnDelete = new Button("X");

            {
                btnDelete.setStyle("-fx-text-fill: red; -fx-background-color: #f8d7da; -fx-border-color: #f5c6cb; -fx-border-radius: 3; -fx-background-radius: 3; -fx-cursor: hand;");
                btnDelete.setOnAction(event -> {
                    DepenseRecette depenseRecette = getTableView().getItems().get(getIndex());
                    ButtonType choix = PageController.getInstance().afficherSuppression(Alert.AlertType.CONFIRMATION, "Confirmation de suppression", "Voulez-vous vraiment supprimer cet élément ?");
                    if (choix == ButtonType.YES) {
                        supprimerDepense(depenseRecette);
                        PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
                    }
                });
            }

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        };
    }

    /**
     * Fonction de suppression de la dépense/recette
     */
    private void supprimerDepense(DepenseRecette depenseRecette) {
        evenementActif.getDepensesRecette().remove(depenseRecette);
        listeObservableOperations.remove(depenseRecette); // Met à jour automatiquement les vues
        faireSynthese();
    }

    /**
     * Fonction d'ajout de dépense/recette
     */
    @FXML
    private void actionBtnAjoutDepenseRecette(ActionEvent event) throws IOException {
        PageController.getInstance().afficherVue("Popup_AjoutDepenseRecette.fxml", "Ajouter Recettes/Dépenses");

        // On rafraîchit la liste avec les nouvelles données ajoutées depuis la popup
        if (evenementActif.getDepensesRecette() != null) {
            listeObservableOperations.setAll(evenementActif.getDepensesRecette());
        }

        faireSynthese();
        PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
    }

    // ==================== Synthèse =======================

    @FXML
    public void faireSynthese() {
        textTotalRecettes.setText(String.format("%.2f€", bilanFinancier.calculerTotalRecettes()));
        textTotalDepenses.setText(String.format("%.2f€", bilanFinancier.calculerTotalDepenses()));
        textSolde.setText(String.format("%.2f€", bilanFinancier.calculerSoldeNet()));
    }
    @FXML
    public void actionBtnExporter(ActionEvent event) {
        //TODO bouton exporter a faire pour la finance
    }

}