package com.example.megasae.controller.Popups.Choix;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Popup_EvenementController;
import com.example.megasae.model.Evenement.TemplateEvenement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * Contrôleur de la popup affichant la liste des templates d'événements disponibles.
 * Permet l'affichage des descriptions au survol, l'édition, la suppression et l'import/création.
 */
public class Popup_ChoixTemplatesController {

    @FXML private TableView<TemplateEvenement> tableTemplates;
    @FXML private TableColumn<TemplateEvenement, String> colNom;
    @FXML private TableColumn<TemplateEvenement, Void> colActions;

    @FXML private Button btnAnnuler;
    @FXML private Button btnImporter;
    @FXML private Button btnCreerTemplate;

    private ObservableList<TemplateEvenement> listeObservableTemplates;

    @FXML
    public void initialize() {
        chargerDonnees();

        // Configuration de la colonne Nom + Gestion du Tooltip
        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        colNom.setCellFactory(column -> new TableCell<TemplateEvenement, String>() {
            private final Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(String obj, boolean vide) {
                super.updateItem(obj, vide);

                if (vide || obj == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(obj);

                    // Récupération de l'objet de la ligne en question
                    TemplateEvenement template = getTableView().getItems().get(getIndex());
                    if (template != null && template.getDescription() != null) {
                        tooltip.setText(template.getDescription());
                        setTooltip(tooltip);
                    } else {
                        setTooltip(null);
                    }
                }
            }
        });

        // Configuration de la colonne Actions (Modifier / Supprimer)
        configurerColonneActions();

        // Détecter le double-clic sur une ligne du tableau
        tableTemplates.setRowFactory(tv -> {
            TableRow<TemplateEvenement> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TemplateEvenement templateChoisi = row.getItem();

                    // Stocke les données en mémoire
                    Popup_EvenementController.setTemplateSelectionne(templateChoisi);
                    PageController.getInstance().setEvenementActif(null);

                    // Récupère et ferme l'ancienne fenêtre immédiatement
                    Stage stage = (Stage) tableTemplates.getScene().getWindow();
                    if (stage != null) {
                        stage.close();
                    }

                    javafx.application.Platform.runLater(() -> {
                        PageController.getInstance().afficherVue("Popup_Evenement.fxml", "Nouvel Événement");
                    });
                }
            });
            return row;
        });
    }

    /**
     * Charge la liste des templates dans la table.
     */
    private void chargerDonnees() {
        List<TemplateEvenement> templates = PageController.getInstance().getGestionnaire().getTemplateEvenements();
        if (templates != null) {
            listeObservableTemplates = FXCollections.observableArrayList(templates);
        } else {
            listeObservableTemplates = FXCollections.observableArrayList();
        }
        tableTemplates.setItems(listeObservableTemplates);
    }

    /**
     * Génère dynamiquement les boutons Modifier et Supprimer pour chaque ligne.
     */
    private void configurerColonneActions() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("✏️");
            private final Button btnSupprimer = new Button("X");
            private final HBox conteneurBoutons = new HBox(10, btnModifier, btnSupprimer);

            {
                conteneurBoutons.setAlignment(Pos.CENTER);
                String styleBase = "-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 13;";
                btnModifier.setStyle(styleBase);
                btnSupprimer.setStyle(styleBase);

                btnModifier.setTooltip(new Tooltip("Modifier ce modèle"));
                btnSupprimer.setTooltip(new Tooltip("Supprimer ce modèle"));

                // Action Modifier
                btnModifier.setOnAction(event -> {
                    TemplateEvenement template = getTableView().getItems().get(getIndex());
                    modifierTemplate(template);
                });

                // Action Supprimer
                btnSupprimer.setOnAction(event -> {
                    TemplateEvenement template = getTableView().getItems().get(getIndex());
                    supprimerTemplate(template);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    setGraphic(conteneurBoutons);
                }
            }
        });
    }

    /**
     *
     * @param template l'objet de la ligne cliquée
     */
    private void modifierTemplate(TemplateEvenement template) {
        PageController.getInstance().setEvenementActif(null);
        Popup_EvenementController.setTemplateSelectionne(template);
        PageController.getInstance().afficherVue("Popup_Evenement.fxml", "Modifier le Template");
        chargerDonnees();
    }

    private void supprimerTemplate(TemplateEvenement template) {
        ButtonType choix = PageController.getInstance().afficherSuppression(
                Alert.AlertType.CONFIRMATION,
                "Supprimer le modèle",
                "Voulez-vous vraiment supprimer définitivement le template : " + template.getNom() + " ?"
        );

        if (choix == ButtonType.YES) {
            PageController.getInstance().getGestionnaire().getTemplateEvenements().remove(template);
            listeObservableTemplates.remove(template);
            PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
        }
    }

    @FXML
    void handleImporterAction(ActionEvent event) {
        //TODO System.out.println("Déclenchement de l'import d'un template externe.");
    }

    @FXML
    void handleCreerTemplateAction(ActionEvent event) {
        PageController.getInstance().setEvenementActif(null);

        TemplateEvenement nouveauTemplate = new TemplateEvenement();
        Popup_EvenementController.setTemplateSelectionne(nouveauTemplate);

        PageController.getInstance().afficherVue("Popup_Evenement.fxml", "Créer un nouveau Template");
        chargerDonnees();
    }

    @FXML
    void handleAnnulerAction(ActionEvent event) {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

}