package com.example.megasae.controller.Popups;

import com.example.megasae.controller.Pages.PageController;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Evenement.TemplateEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;

public class Popup_EvenementController {

    // COMPOSANTS FXML

    @FXML private Label lblTitreFormulaire;
    @FXML private VBox boxChampsTemplate;

    // Les champs textes pour la création/modification de modèles (Templates)
    @FXML private TextField txtNomTemplate;
    @FXML private TextArea txtDescTemplate;

    // Les champs textes et sélecteurs pour un événement classique
    @FXML private TextField txtNomEvenement;
    @FXML private DatePicker pickerDateDebut;
    @FXML private DatePicker pickerDateFin;
    @FXML private TextField txtCapacite;
    @FXML private TextField txtBudget;
    @FXML private ColorPicker pickerCouleur;
    @FXML private TextArea txtDescription;

    // Les boutons d'action situés en bas de la fenêtre popup
    @FXML private Button btnAnnuler;
    @FXML private Button btnEnregistrer;

    // VARIABLES D'ÉTAT PRIVÉES ET PARTAGÉES
    private String mode = "CREER_VIERGE"; // Mode par défaut de l'application
    private static TemplateEvenement templateSelectionne;
    private Evenement evenementEnCours;
    private TemplateEvenement templateEnCours;

    /**
     * Méthode statique permettant de passer un modèle avant d'ouvrir la popup.
     */
    public static void setTemplateSelectionne(TemplateEvenement template) {
        templateSelectionne = template;
    }

    /**
     * Méthode appelée automatiquement par JavaFX dès que le fichier FXML est chargé.
     */
    @FXML
    public void initialize() {
        try {
            determineModeEtInitialise();
        } catch (Exception e) {
            System.err.println("Erreur critique lors de l'initialisation des champs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Analyse la situation pour savoir ce que l'utilisateur est en train de faire
     * (créer à blanc, créer depuis un modèle, modifier, ou éditer un modèle).
     */
    private void determineModeEtInitialise() {
        // On récupère l'événement actif stocké dans le contrôleur de page principal
        evenementEnCours = PageController.getEvenementActif();

        // L'utilisateur a cliqué sur une action liée à un modèle (Template)
        if (templateSelectionne != null) {

            // Si le modèle n'a pas encore de nom, c'est qu'on veut créer un tout nouveau modèle
            if (templateSelectionne.getNom() == null) {
                mode = "EDITION_TEMPLATE";
                templateEnCours = templateSelectionne;
                lblTitreFormulaire.setText("Créer un nouveau Modèle");

                // On affiche la zone des champs spécifiques au modèle
                boxChampsTemplate.setVisible(true);
                boxChampsTemplate.setManaged(true);
            }
            // Sinon, c'est qu'on utilise un modèle existant pour générer un nouvel événement
            else {
                mode = "CREER_DEPUIS_TEMPLATE";
                templateEnCours = templateSelectionne;
                lblTitreFormulaire.setText("Créer depuis le modèle : " + templateSelectionne.getNom());

                // On cache la zone spécifique aux modèles (champs de métadonnées du template)
                boxChampsTemplate.setVisible(false);
                boxChampsTemplate.setManaged(false);

                // Préremplissage des champs
                txtNomEvenement.setText(templateSelectionne.getNom());
                txtDescription.setText(templateSelectionne.getDescription());
            }
        }
        // Pas de modèle sélectionné, mais un événement est déjà en cours (Mode Modification)
        else if (evenementEnCours != null) {
            mode = "MODIFIER_EVENEMENT";
            lblTitreFormulaire.setText("Modifier l'Événement");

            boxChampsTemplate.setVisible(false);
            boxChampsTemplate.setManaged(false);

            // On remplit toutes les cases avec les données actuelles de l'événement
            txtNomEvenement.setText(evenementEnCours.getNom());
            txtDescription.setText(evenementEnCours.getDescription());
            pickerDateDebut.setValue(evenementEnCours.getDateDebut());
            pickerDateFin.setValue(evenementEnCours.getDateFin());
            txtCapacite.setText(String.valueOf(evenementEnCours.getCapaciteMax()));
            txtBudget.setText(String.valueOf(evenementEnCours.getBudget()));
            if (evenementEnCours.getCouleur() != null) {
                pickerCouleur.setValue(Color.valueOf(evenementEnCours.getCouleur()));
            }
        }
        // Aucun modèle et aucun événement actif, on part sur une création classique à blanc
        else {
            mode = "CREER_VIERGE";
            lblTitreFormulaire.setText("Créer un Événement Vierge");

            boxChampsTemplate.setVisible(false);
            boxChampsTemplate.setManaged(false);
        }
    }

    /**
     * Action principale déclenchée lorsque l'utilisateur clique sur "Enregistrer".
     */
    @FXML
    void handleEnregistrerAction(ActionEvent event) {

        // Si on est en train de créer ou d'éditer un MODÈLE de template
        if (mode.equals("EDITION_TEMPLATE")) {
            String nomModeleSaisi = txtNomTemplate.getText();

            // Si le nom du modèle est vide, on affiche l'alerte et on stoppe le code
            if (nomModeleSaisi.isEmpty()) {
                PageController.getInstance().afficherAlerte("Le nom du modèle est obligatoire.");
                return;
            }
        }
        // Pour tous les autres modes (Création ou Modification d'un événement réel)
        else {
            String nomEvenementSaisi = txtNomEvenement.getText();

            // Le nom de l'événement ne doit pas être vide
            if (nomEvenementSaisi.isEmpty()) {
                PageController.getInstance().afficherAlerte("Le nom de l'événement est obligatoire.");
                return;
            }

            // La date de début de l'événement est strictement obligatoire
            if (pickerDateDebut.getValue() == null) {
                PageController.getInstance().afficherAlerte("La date de début est obligatoire.");
                return;
            }
        }

        int capacite = 0;
        String texteCapacite = txtCapacite.getText();

        // Si l'utilisateur a écrit quelque chose dans la case capacité, on essaie de la convertir en nombre
        if (texteCapacite.isEmpty() == false) {
            try {
                capacite = Integer.parseInt(texteCapacite);
            } catch (NumberFormatException e) {
                PageController.getInstance().afficherAlerte("La capacité doit être un nombre entier valide (exemple : 120).");
                return; // Bloque l'enregistrement en cas d'erreur de frappe
            }
        }

        double budget = 0.0;
        String texteBudget = txtBudget.getText();

        // Si l'utilisateur a rempli la case budget, on la convertit en nombre à virgule
        if (texteBudget != null && !texteBudget.trim().isEmpty()) {
            // Nettoyage : on enlève tous les espaces inutiles
            texteBudget = texteBudget.trim();
            texteBudget = texteBudget.replace(",", ".");
            try {
                budget = Double.parseDouble(texteBudget);
            } catch (NumberFormatException e) {
                PageController.getInstance().afficherAlerte("Le budget doit être un nombre décimal valide (utilisez un point pour les décimales, ex: 1500.50).");
                return; // Bloque l'enregistrement
            }
        }

        // Récupération et conversion simple de l'objet couleur en chaîne Hexadécimale Web (ex: #FF5733)
        Color couleurSelectionnee = pickerCouleur.getValue();


        // CAS DE LA CRÉATION : On crée un tout nouvel objet Événement
        if (mode.equals("CREER_VIERGE") || mode.equals("CREER_DEPUIS_TEMPLATE")) {

            LocalDate dateDebut = pickerDateDebut.getValue();
            LocalDate dateFin = pickerDateFin.getValue();

            // Si l'utilisateur n'a pas sélectionné de date de fin, elle devient identique à celle du début
            if (dateFin == null) {
                dateFin = dateDebut;
            }

            // Instanciation de l'objet via son constructeur principal
            String nomEv = txtNomEvenement.getText();
            try {

                Evenement nouvelEvenement = new Evenement(nomEv, dateDebut, dateFin);



            // Injection des autres variables récoltées
            nouvelEvenement.setDescription(txtDescription.getText());
            nouvelEvenement.setCapaciteMax(capacite);
            nouvelEvenement.setBudget(budget);
            nouvelEvenement.setCouleur(String.valueOf(couleurSelectionnee));

            // Ajout définitif de l'événement créé dans la liste du gestionnaire central
            PageController.getInstance().getGestionnaire().addEvenement(nouvelEvenement);

            PageController.getInstance().setEvenementActif(nouvelEvenement);
            }catch (IllegalArgumentException e) {
                PageController.getInstance().afficherAlerte(e.getMessage());
                return;
            }
            PageController.getInstance().chargerVue("Page_InformationsEvenement.fxml", true, PageController.getInstance().getBtnInformationsEvenement());

        }
        // CAS DE LA MODIFICATION : On met à jour directement l'objet existant
        else if (mode.equals("MODIFIER_EVENEMENT")) {

            evenementEnCours.setNom(txtNomEvenement.getText());
            evenementEnCours.setDescription(txtDescription.getText());
            try {


                evenementEnCours.setDateDebut(pickerDateDebut.getValue());
                LocalDate dateFin = pickerDateFin.getValue();
                if (dateFin != null) {
                    evenementEnCours.setDateFin(dateFin);
                }
            }catch (IllegalArgumentException e) {
                PageController.getInstance().afficherAlerte(e.getMessage());
                return;
            }
            evenementEnCours.setCapaciteMax(capacite);
            evenementEnCours.setBudget(budget);
            evenementEnCours.setCouleur(String.valueOf(couleurSelectionnee));

            PageController.getInstance().setEvenementActif(evenementEnCours);
            PageController.getInstance().chargerVue("Page_InformationsEvenement.fxml", true, PageController.getInstance().getBtnInformationsEvenement());
        }
        // CAS DU TEMPLATE : On met à jour et on enregistre un modèle réutilisable
        else if (mode.equals("EDITION_TEMPLATE")) {

            String nomTemp = txtNomTemplate.getText();
            templateEnCours.setNom(nomTemp);
            templateEnCours.setDescription(txtDescTemplate.getText());

            try {


                // Insertion du modèle configuré dans la liste des templates globaux
                PageController.getInstance().getGestionnaire().getTemplateEvenements().add(templateEnCours);
            }catch (IllegalArgumentException e) {
                PageController.getInstance().afficherAlerte(e.getMessage());
                return;
            }
        }

        // On force l'écriture immédiate et la sauvegarde physique du gestionnaire sur le disque dur (.ser)
        PageController.getInstance().getGestionnaire().sauvegarderDonnees(PageController.getInstance().getGestionnaire());
        templateSelectionne = null;
        fermerFenetre();
    }

    /**
     * Action déclenchée lorsque l'utilisateur décide d'annuler la saisie.
     */
    @FXML
    void handleAnnulerAction(ActionEvent event) {
        templateSelectionne = null;
        fermerFenetre();
    }

    /**
     * Ferme proprement le conteneur Windows/Stage actuel de la popup.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

}