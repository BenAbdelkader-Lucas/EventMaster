package com.example.megasae.model.Evenement;

import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Miscellaneous.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe modèle représentant un modèle (template) d'événement permettant de pré-configurer
 * les paramètres courants d'un type d'événement récurrent.
 * Un template définit les rôles, les tags et les informations génériques
 * appliqués lors de la création d'un événement.
 * Implémente Serializable pour permettre la sauvegarde binaire de la SAE.
 */
public class TemplateEvenement implements Serializable {

    // ── VARIABLES ET ATTRIBUTS ──────────────────────────────────────────────

    /**
     * Identifiant unique pour la sérialisation binaire de la classe
     */
    private static final long serialVersionUID = 1L;

    /**
     * Journal de log pour suivre les actions sur les templates
     */
    private static final Logger LOGGER = Logger.getLogger(TemplateEvenement.class.getName());

    /**
     * Identifiant unique du template généré automatiquement
     */
    private UUID id;

    /**
     * Nom du template (ex : "Conférence annuelle", "Soirée associative")
     */
    private String nomTemplate;

    /**
     * Description du template et de son cas d'utilisation pratique
     */
    private String descriptionTemplate;

    /**
     * Nom par défaut proposé pour les événements créés à partir de ce template
     */
    private String nom;

    /**
     * Description par défaut proposée pour les événements créés à partir de ce template
     */
    private String description;

    /**
     * Couleur par défaut des événements créés à partir de ce template
     */
    private String couleur;

    /**
     * Liste des rôles prédéfinis inclus dans ce template
     */
    private List<Role> roles = new ArrayList<>();

    /**
     * Liste des étiquettes (tags) prédéfinies incluses dans ce template
     */
    private List<Tag> tags = new ArrayList<>();


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur par défaut.
     * Initialise automatiquement un identifiant unique de sécurité.
     */
    public TemplateEvenement() {
        this.id = UUID.randomUUID();
    }

    /**
     * Constructeur complet pour instancier un template avec ses données par défaut.
     */
    public TemplateEvenement(String nomTemplate, String descriptionTemplate, String nom, String description, String couleur, List<Role> roles, List<Tag> tags) {
        this.id = UUID.randomUUID();
        setNomTemplate(nomTemplate);
        setDescriptionTemplate(descriptionTemplate);
        setNom(nom);
        setDescription(description);
        setCouleur(couleur);
        setRoles(roles);
        setTags(tags);
        LOGGER.info("Nouveau template d'événement créé : " + this.nomTemplate + " (ID: " + this.id + ")");
    }


    // ── GETTERS ET SETTERS COMMENTÉS ─────────────────────────────────────────

    /**
     * Récupère l'identifiant unique du template
     */
    public UUID getId() {
        return id;
    }

    /**
     * Modifie l'identifiant unique du template
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return le nom du template
     */
    public String getNomTemplate() {
        return nomTemplate;
    }

    /**
     * @param nomTemplate le nom à attribuer
     */
    public void setNomTemplate(String nomTemplate) {
        if (nomTemplate == null || nomTemplate.isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative d''ajout d''un nom de template null au template {0} ({1})", new Object[]{this.nom, this.id});
            throw new IllegalArgumentException("le nom de la template ne doit pas être null");
        } else this.nomTemplate = nomTemplate;
    }

    /**
     * Récupère la description d'utilisation du template
     */
    public String getDescriptionTemplate() {
        return descriptionTemplate;
    }

    /**
     * Modifie la description d'utilisation du template
     */
    public void setDescriptionTemplate(String descriptionTemplate) {
        this.descriptionTemplate = descriptionTemplate;
    }

    /**
     * Récupère le nom par défaut destiné aux futurs événements
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom par défaut destiné aux futurs événements (ne doit pas être vide)
     */
    public void setNom(String nom) {
        if (nom == null || nom.isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative d''ajout d''un nom null au template {0} ({1})", new Object[]{this.nom, this.id});
            throw new IllegalArgumentException("le nom d'un template ne peut etre null ou vide");
        }
        this.nom = nom;
    }

    /**
     * Récupère la description par défaut des futurs événements
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifie la description par défaut des futurs événements
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Récupère la couleur graphique par défaut des futurs événements
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * Modifie la couleur graphique par défaut des futurs événements
     */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    /**
     * Récupère la liste des rôles pré-enregistrés dans ce template
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Modifie la liste des rôles pré-enregistrés dans ce template
     */
    public void setRoles(List<Role> roles) {
        this.roles = (roles != null) ? roles : new ArrayList<>();
    }

    /**
     * Récupère la liste des tags pré-enregistrés dans ce template
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Modifie la liste des tags pré-enregistrés dans ce template
     */
    public void setTags(List<Tag> tags) {
        this.tags = (tags != null) ? tags : new ArrayList<>();
    }


    // ── MÉTHODE TOSTRING STANDARD ────────────────────────────────────────────

    @Override
    public String toString() {
        return "Template d'événement [Modèle = " + nomTemplate + ", Nom proposé = " + nom + "]";
    }

}