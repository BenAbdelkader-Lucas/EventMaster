package com.example.megasae.controller.Popups.Attribution;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.controller.Popups.Ajout.Popup_AjoutTagController;
import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Finance.DepenseRecette;
import com.example.megasae.model.Miscellaneous.Tache;
import com.example.megasae.model.Miscellaneous.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Popup_AttributionTagController {

    private Evenement evenementActif = null;
    private DepenseRecette depenseRecette = null;
    private Tache tache = null;
    private Role role = null;
    private Membre membre = null;

    @FXML private Button btnOk;
    @FXML private FlowPane listAttribue;
    @FXML private FlowPane listDisponible;
    @FXML private ToggleButton toggleModification;
    @FXML private ToggleButton toggleSupprimer;

    @FXML
    public void initialize() {
        if (listAttribue != null && listDisponible != null) {
            listAttribue.getChildren().clear();
            listDisponible.getChildren().clear();
        }
        this.evenementActif = PageController.getEvenementActif();
        rafraichirAffichageTags();
    }

    public void setDepensesRecette(DepenseRecette depenseRecette) {
        this.depenseRecette = depenseRecette;
        rafraichirAffichageTags();
    }

    public void setTache(Tache tache) {
        this.tache = tache;
        rafraichirAffichageTags();
    }

    public void setEvenementActif(Evenement evenementActif) {
        this.evenementActif = evenementActif;
        rafraichirAffichageTags();
    }

    public void setRole(Role role) {
        this.role = role;
        rafraichirAffichageTags();
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
        rafraichirAffichageTags();
    }

    /**
     * Reconstruit la liste visuelle des tags disponibles et attribués.
     */
    private void rafraichirAffichageTags() {
        if (listAttribue != null && listDisponible != null) {
            listAttribue.getChildren().clear();
            listDisponible.getChildren().clear();
        } else {
            return;
        }

        if (evenementActif == null) return;

        // On liste tous les tags de l'événement global
        if (evenementActif.getTags() != null) {
            for (Tag tag : evenementActif.getTags()) {
                Button btnTag = new Button();
                btnTag.setText(tag.getLibelle());
                btnTag.setStyle("-fx-background-color: " + tag.getCouleur() + "; -fx-background-radius: 6px; -fx-border-radius: 6px; -fx-text-fill: ladder(" + tag.getCouleur() + ", white 49%, black 50%); -fx-font-weight: bold; -fx-cursor: hand;");

                btnTag.setOnAction(e -> {
                    if (toggleModification.isSelected()) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/megasae/Popups/Ajout/Popup_AjoutTag.fxml"));
                            Scene scene = new Scene(loader.load());

                            String c = tag.getCouleur().trim();
                            if (!c.startsWith("#")) {
                                tag.setCouleur("#" + c);
                            }

                            Popup_AjoutTagController popup_Ajout_TagController = loader.getController();
                            popup_Ajout_TagController.initialisationValeur(tag);

                            Stage popupStage = new Stage();
                            popupStage.initModality(Modality.APPLICATION_MODAL);
                            popupStage.setResizable(false);
                            popupStage.setTitle("Modification de Tag");
                            popupStage.setScene(scene);

                            popupStage.showAndWait();

                            // On rafraîchit proprement la liste après la modification.
                            rafraichirAffichageTags();
                            toggleModification.setSelected(false);
                        } catch (Exception f) {
                            f.printStackTrace();
                        }
                    } else if (toggleSupprimer.isSelected()) {
                        evenementActif.getTags().remove(tag);
                        rafraichirAffichageTags();
                    } else {
                        if (listDisponible.getChildren().contains(btnTag)) {
                            listDisponible.getChildren().remove(btnTag);
                            listAttribue.getChildren().add(btnTag);
                        } else {
                            listDisponible.getChildren().add(btnTag);
                            listAttribue.getChildren().remove(btnTag);
                        }
                    }
                });

                boolean estAttribue = false;
                if (depenseRecette != null && depenseRecette.getTag() != null) {
                    for (Tag t : depenseRecette.getTag()) {
                        if (t.getLibelle().equals(tag.getLibelle())) {
                            estAttribue = true;
                            break;
                        }
                    }
                } else if (tache != null && tache.getTags() != null) {
                    for (Tag t : tache.getTags()) {
                        if (t.getLibelle().equals(tag.getLibelle())) {
                            estAttribue = true;
                            break;
                        }
                    }
                } else if (role != null && role.getTags() != null) {
                    for (Tag t : role.getTags()) {
                        if (t.getLibelle().equals(tag.getLibelle())) {
                            estAttribue = true;
                            break;
                        }
                    }
                } else if (membre != null && membre.getTags() != null) {
                    for (Tag t : membre.getTags()) {
                        if (t.getLibelle().equals(tag.getLibelle())) {
                            estAttribue = true;
                            break;
                        }
                    }
                }

                if (estAttribue) {
                    listAttribue.getChildren().add(btnTag);
                } else {
                    listDisponible.getChildren().add(btnTag);
                }

            }
        }
    }


    @FXML
    void handleBtnAjouterTag(ActionEvent event) {
        PageController.getInstance().afficherVue("Popup_AjoutTag.fxml", "Ajouter un Tag");
        this.evenementActif = PageController.getEvenementActif();
        rafraichirAffichageTags();
    }

    @FXML
    void toggleActionModification(ActionEvent event) {

        if (toggleSupprimer.isSelected()) toggleSupprimer.setSelected(false);

        if (toggleModification.isSelected()) {
            // Mode modification
            for (Node btn : listAttribue.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().add("modeModif");
                }
            }
            for (Node btn : listDisponible.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().add("modeModif");
                }
            }
        } else {
            // Mode hors modification
            for (Node btn : listAttribue.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().remove("modeModif");
                }
            }
            for (Node btn : listDisponible.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().remove("modeModif");
                }
            }
        }
    }

    @FXML
    void toggleActionSuppresion(ActionEvent event) {

        if (toggleModification.isSelected()) toggleModification.setSelected(false);

        if (toggleSupprimer.isSelected()) {
            // Mode modification
            for (Node btn : listAttribue.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().add("modeSuppresion");
                }
            }
            for (Node btn : listDisponible.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().add("modeSuppresion");
                }
            }
        } else {
            // Mode hors modification
            for (Node btn : listAttribue.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().remove("modeSuppresion");
                }
            }
            for (Node btn : listDisponible.getChildren()) {
                if (btn instanceof Button) {
                    btn.getStyleClass().remove("modeSuppresion");
                }
            }
        }
    }

    @FXML
    void handleBtnConfirmer(ActionEvent event) {
        ArrayList<Tag> tags = new ArrayList<>();

        for (int i = 0; i < listAttribue.getChildren().size(); i++) {
            Node node = listAttribue.getChildren().get(i);

            if (node instanceof Button) {
                Button btn = (Button) node;
                String nomDuTagBouton = btn.getText();

                if (evenementActif != null && evenementActif.getTags() != null) {
                    for (Tag tag : evenementActif.getTags()) {
                        if (tag.getLibelle().equals(nomDuTagBouton)) {
                            tags.add(tag);
                            break;
                        }
                    }
                }
            }
        }

        if (depenseRecette != null) {
            depenseRecette.setTagList(tags);
        } else if (tache != null) {
            tache.setTags(tags);
        } else if (role != null) {
            role.setTags(tags);
        } else if (membre != null) {
            membre.setTags(tags);
        } else if (evenementActif != null) {
            evenementActif.setTags(tags);
        }

        PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());

        Stage fenetre = (Stage) btnOk.getScene().getWindow();
        fenetre.close();
    }

}