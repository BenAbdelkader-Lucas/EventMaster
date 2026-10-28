package com.example.megasae.model.Evenement;

import com.example.megasae.model.Equipe.Membre;
import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Finance.BilanFinancier;
import com.example.megasae.model.Finance.DepenseRecette;
import com.example.megasae.model.Miscellaneous.GestionnaireDesEvenements;
import com.example.megasae.model.Miscellaneous.Note;
import com.example.megasae.model.Miscellaneous.Tache;
import com.example.megasae.model.Miscellaneous.Tag;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entité centrale du modèle représentant un événement.
 * Un événement regroupe les membres, créneaux, tâches, dépenses, lieux, rôles et tags,
 * et possède un bilan financier associé.
 * Implémente Serializable pour permettre la sauvegarde binaire globale.
 */
public class Evenement implements Serializable {

    // ── VARIABLES ET ATTRIBUTS ──────────────────────────────────────────────

    /**
     * Récupération du logger pour la classe Evenement.
     */
    private static final Logger LOGGER = Logger.getLogger(Evenement.class.getPackageName());


    /**
     * Identifiant unique de l'événement généré automatiquement
     * */
    private UUID id;

    /**
     * Stocke le chemin relatif du logo
     */
    private String imagePath;

    /**
     * Nom de l'événement
     * */
    private String nom;

    /**
     * Date de début de l'événement
     */
    private LocalDate dateDebut;

    /**
     * Date de fin de l'événement
     */
    private LocalDate dateFin;

    /**
     * Heure de début de l'événement
     */
    private Time heure;

    /**
     * Description détaillée de l'événement
     */
    private String description;

    /**
     * Statut actuel de l'événement (BROUILLON, PUBLIE, etc.)
     */
    private StatutEvenement statut;

    /**
     * Historique du statut précédent lors des changements de phase
     */
    private StatutEvenement statutPrecedent;

    /**
     * Couleur thématique de l'événement pour l'affichage graphique
     */
    private String couleur;

    /**
     * Progression globale de la checklist des tâches (en %)
     */
    private double progressionChecklist;

    /**
     * Liste des notes ajoutées par l'organisateur
     */
    private List<Note> notes = new ArrayList<>();

    /**
     * Budget prévisionnel alloué en euros
     */
    private double budget;

    /**
     * Nombre maximal de participants autorisés
     */
    private Integer capaciteMax;

    /**
     * Le gestionnaire global auquel cet événement est rattaché
     */
    private GestionnaireDesEvenements gestionnaireDesEvenements;

    /**
     * Bilan financier calculé pour l'événement (Relation de Composition)
     */
    private BilanFinancier bilanFinancier;

    /**
     * Liste des lieux réservés (Relation d'Agrégation)
     */
    private List<Lieu> lieux = new ArrayList<>();

    /**
     * Liste des membres inscrits à cet événement
     */
    private List<Membre> membres = new ArrayList<>();

    /**
     * Liste des étiquettes (tags) appliquées à l'événement
     */
    private List<Tag> tags = new ArrayList<>();

    /**
     * Liste des rôles configurés pour cet événement
     */
    private List<Role> roles = new ArrayList<>();

    /**
     * Liste des créneaux horaires programmés
     */
    private List<Creneau> creneaux = new ArrayList<>();

    /**
     * Liste des tâches de préparation à accomplir
     */
    private List<Tache> taches = new ArrayList<>();

    /**
     * Liste des mouvements financiers (dépenses et recettes)
     */
    private List<DepenseRecette> depensesRecettes = new ArrayList<>();

    /**
     * Modèle prédéfini (template) utilisé pour générer cet événement
     */
    private TemplateEvenement templateEvenement;


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur de base.
     * Initialise un identifiant unique, met le statut en BROUILLON et configure les infos clés.
     */
    public Evenement(String nom, LocalDate dateDebut, LocalDate dateFin) {
        this.id = UUID.randomUUID();
        setTemplateEvenement(new TemplateEvenement(nom, "", nom, "", "", new ArrayList<Role>(), new ArrayList<Tag>()));
        setStatut(StatutEvenement.BROUILLON);
        setNom(nom);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
        LOGGER.info("Création d'un événement : " + this.nom + " (ID: " + this.id + ")");
    }

    /**
     * Constructeur basé sur un modèle (template) pour pré-remplir les données de l'événement.
     */
    public Evenement(TemplateEvenement templateEvenement) {
        if (templateEvenement == null) {
            throw new IllegalArgumentException("un template ne peut etre null");
        } else {
            this.id = UUID.randomUUID();
            this.templateEvenement = templateEvenement;
            this.nom = templateEvenement.getNom();
            this.description = templateEvenement.getDescription();
            this.couleur = templateEvenement.getCouleur();
            this.roles = templateEvenement.getRoles();
            this.tags = templateEvenement.getTags();
            LOGGER.log(Level.INFO, "Création d''un événement grace à une template : {0} (ID: {1})", new Object[]{this.nom, this.id});
        }

    }


    // ── GETTERS ET SETTERS COMMENTÉS ─────────────────────────────────────────

    /**
     * Récupère l'identifiant unique de l'événement
     */
    public UUID getId() {
        return id;
    }

    /**
     * Récupère le nom de l'événement
     */
    public String getNom() {
        return nom;
    }
    /**
     * Modifie le nom de l'événement (génère une erreur s'il est vide)
     */
    public void setNom(String nom) {
        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("le nom d'un événement ne peut être null");
        } else this.nom = nom;
    }

    /**
     * Récupère le chemin du logo de l'événement
     */
    public String getImagePath() { return imagePath; }
    /**
     * Modifie le chemin du logo de l'événement
     */
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }


    /**
     * Récupère la date de début
     */
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    /**
     * Modifie la date de début de l'événement
     */
    public void setDateDebut(LocalDate dateDebut) {
        if (dateDebut == null) {
            throw new IllegalArgumentException("la date de début d'un événement ne peut être null");
        } else if (dateFin != null &&  dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("la date de fin doit être après la date de début");
        }
        else this.dateDebut = dateDebut;
    }


    /**
     * Récupère la date de fin
     */
    public LocalDate getDateFin() {
        return dateFin;
    }
    /**
     * Modifie la date de fin de l'événement
     */
    public void setDateFin(LocalDate dateFin) {
        if (dateFin == null) {
            throw new IllegalArgumentException("la date de fin d'un événement ne peut être null");
        } else if(dateDebut != null && dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("la date de fin doit être après la date de début");
        } else this.dateFin = dateFin;
    }

    /**
     * @return l'heure de début
     */
    public Time getHeure() {
        return heure;
    }
    /**
     * Modifie l'heure de début
     */
    public void setHeure(Time heure) {
        this.heure = heure;
    }

    /**
     * Récupère la description textuelle
     */
    public String getDescription() {
        return description;
    }
    /**
     * Modifie la description textuelle
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Récupère le statut actuel
     */
    public StatutEvenement getStatut() {
        return statut;
    }
    /**
     * Modifie le statut actuel et bascule l'ancien statut dans l'historique
     */
    public void setStatut(StatutEvenement statut) {
        LOGGER.info("Changement de statut pour l'événement " + this.nom + " : " + statut);
        this.statutPrecedent = this.statut;
        this.statut = statut;
    }

    /**
     * Récupère le précédent statut enregistré
     */
    public StatutEvenement getStatutPrecedent() {
        return statutPrecedent;
    }
    /**
     * Modifie manuellement le statut précédent
     */
    public void setStatutPrecedent(StatutEvenement statutPrecedent) {
        this.statutPrecedent = statutPrecedent;
    }

    /**
     * Récupère la couleur graphique
     */
    public String getCouleur() {
        return couleur;
    }
    /**
     * Modifie la couleur graphique
     */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    /**
     * Récupère le pourcentage d'avancement de la checklist
     */
    public double getProgressionChecklist() {
        return progressionChecklist;
    }
    /**
     * Modifie le pourcentage d'avancement de la checklist
     */
    public void setProgressionChecklist(double progressionChecklist) {
        this.progressionChecklist = progressionChecklist;
    }

    /**
     * Récupère le budget financier alloué
     */
    public double getBudget() {
        return budget;
    }
    /**
     * Modifie le budget financier (interdit les valeurs négatives)
     */
    public void setBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("lun budget ne peut être négatif");
        } else this.budget = budget;
    }

    /**
     * Récupère la capacité maximale de personnes
     */
    public Integer getCapaciteMax() {
        return capaciteMax;
    }
    /**
     * Modifie la capacité maximale de personnes
     */
    public void setCapaciteMax(Integer capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    /**
     * Récupère le bilan financier complet
     */
    public BilanFinancier getBilanFinancier() {
        return bilanFinancier;
    }
    /**
     * Associe un bilan financier à l'événement
     */
    public void setBilanFinancier(BilanFinancier bilanFinancier) {
        this.bilanFinancier = bilanFinancier;
    }

    /**
     * Récupère la liste de tous les créneaux
     */
    public List<Creneau> getCreneaux() {
        return creneaux;
    }
    /**
     * Remplace la liste complète des créneaux
     */
    public void setCreneaux(List<Creneau> creneaux) {
        this.creneaux = creneaux;
    }

    /**
     * Récupère la liste de toutes les tâches
     */
    public List<Tache> getTaches() {
        return taches;
    }
    /**
     * Remplace la liste complète des tâches
     */
    public void setTaches(List<Tache> taches) {
        this.taches = taches;
    }

    /**
     * Récupère la liste de toutes les notes
     */
    public List<Note> getNotes() {
        return notes;
    }
    /**
     * Remplace la liste complète des notes
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    /**
     * Récupère la liste des dépenses et des recettes
     */
    public List<DepenseRecette> getDepensesRecette() {
        return depensesRecettes;
    }
    /**
     * Remplace la liste des dépenses et recettes
     */
    public void setDepenses(List<DepenseRecette> depensesRecettes) {
        this.depensesRecettes = depensesRecettes;
    }

    /**
     * Récupère la liste des lieux
     */
    public List<Lieu> getLieux() {
        return lieux;
    }
    /**
     * Remplace la liste complète des lieux
     */
    public void setLieux(List<Lieu> lieux) {
        this.lieux = lieux;
    }

    /**
     * Récupère la liste de tous les membres inscrits
     */
    public List<Membre> getMembres() {
        return membres;
    }
    /**
     * Remplace la liste des membres inscrits
     */
    public void setMembres(List<Membre> membres) {
        this.membres = membres;
    }

    /**
     * Récupère la liste des étiquettes (tags) de l'événement
     */
    public List<Tag> getTags() {
        return tags;
    }
    /**
     * Remplace la liste des tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Récupère la liste des rôles configurés
     */
    public List<Role> getRoles() {
        return roles;
    }
    /**
     * Remplace la liste complète des rôles
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Récupère la classe qui gère l'ensemble des événements
     */
    public GestionnaireDesEvenements getGestionnaireDesEvenements() {
        return gestionnaireDesEvenements;
    }
    /**
     * Modifie le gestionnaire général de rattachement
     */
    public void setGestionnaireDesEvenements(GestionnaireDesEvenements gestionnaireDesEvenements) {
        this.gestionnaireDesEvenements = gestionnaireDesEvenements;
    }

    /**
     * Récupère le modèle de template utilisé
     */
    public TemplateEvenement getTemplateEvenement() {
        return templateEvenement;
    }
    /**
     * Modifie ou associe un modèle de template
     */
    public void setTemplateEvenement(TemplateEvenement templateEvenement) {
        this.templateEvenement = templateEvenement;
    }

    // ── MÉTHODES DE GESTION DES LISTES (AVEC COMMENTAIRES) ───────────────────

    /**
     * Ajoute un lieu à l'événement s'il n'est pas null.
     *
     * @param lieu Le lieu géographique à rajouter.
     */
    public void addLieu(Lieu lieu) {
        if (lieu != null) {
            this.lieux.add(lieu);
            LOGGER.info("Lieu ajouté : " + lieu);
        } else {
            LOGGER.warning("Tentative d'ajout d'un lieu null à l'événement " + this.nom);
        }
    }

    /**
     * Supprime un lieu associé à l'événement.
     *
     * @param lieu Le lieu à enlever.
     */
    public void suprimerLieu(Lieu lieu) {
        if (lieu != null) {
            this.lieux.remove(lieu);
            LOGGER.info("Lieu supprimé : " + lieu);
        } else {
            LOGGER.warning("Tentative de suppression d'un lieu null à l'événement " + this.nom);
        }
    }

    /**
     * Inscrit un nouveau membre à l'événement.
     * Vérifie au préalable que la capacité maximale de participants n'est pas atteinte.
     *
     * @param membre Le membre à inscrire.
     */
    public void addMembre(Membre membre) {
        if (membre != null && (capaciteMax == null || membres.size() < capaciteMax)) {
            this.membres.add(membre);
            LOGGER.log(Level.INFO, "Membre ajouté à l''événement {0} : {1}", new Object[]{this.nom, membre});
        } else if (membre == null) {
            LOGGER.log(Level.WARNING, "Tentative d''ajout d''un membre nul à l''événement {0}", this.nom);
            throw new IllegalArgumentException("un membre ne peut pas etre null");
        } else throw new IllegalArgumentException("la capacité max est atteinte");
    }

    /**
     * Désinscrit un membre existant de l'événement.
     *
     * @param membre Le membre à retirer.
     */
    public void suprimerMembre(Membre membre) {
        if (membre != null) {
            this.membres.remove(membre);
            LOGGER.info("Membre désinscrit : " + membre);
        } else {
            LOGGER.warning("Tentative de suppression d'un membre null à l'événement " + this.nom);
        }
    }

    /**
     * Ajoute une étiquette (tag) de classement à l'événement (évite les doublons).
     *
     * @param tag L'étiquette à appliquer.
     */
    public void addTag(Tag tag) {
        if (tag == null) {
            LOGGER.warning("Tentative d'ajout d'un tag null à l'événement " + this.nom);
            throw new IllegalArgumentException("Un tag ne peut pas être null");
        }
        if (!tags.contains(tag)) {
            this.tags.add(tag);
            LOGGER.info("Tag ajouté : " + tag);
        } else {
            LOGGER.warning("Le tag " + tag + " est déjà appliqué à l'événement " + this.nom);
        }
    }

    /**
     * Enlève une étiquette (tag) de l'événement.
     *
     * @param tag L'étiquette à retirer.
     */
    public void suprimerTag(Tag tag) {
        if (tag != null) {
            this.tags.remove(tag);
            LOGGER.info("Tag retiré de l'événement " + this.nom);
        } else {
            LOGGER.warning("Tentative de suppression d'un tag null à l'événement " + this.nom);
        }
    }

    /**
     * Configure et ajoute un nouveau rôle d'équipe pour cet événement.
     *
     * @param role Le rôle à intégrer.
     */
    public void addRole(Role role) {
        if (role != null) {
            this.roles.add(role);
            LOGGER.log(Level.INFO, "Rôle {1} ajouté à l''événement {0}", new Object[]{this.nom, role});
        } else {
            LOGGER.log(Level.WARNING, "Tentative d''ajout d''un role null dans l''événement {0}", new Object[]{this.nom});
            throw new IllegalArgumentException("un role ne peut pas etre null");
        }
    }

    /**
     * Supprime un rôle configuré de l'événement.
     *
     * @param role Le rôle à effacer.
     */
    public void suprimerRole(Role role) {
        if (role != null) {
            this.roles.remove(role);
            LOGGER.info("Rôle supprimé : " + role);
        }
    }

    /**
     * Ajoute un créneau d'activité au planning de cet événement.
     *
     * @param creneau Le créneau horaire à ajouter.
     */
    public void addCreneau(Creneau creneau) {
        if (creneau == null) {
            LOGGER.warning("Tentative d'ajout d'un créneau null dans l'événement " + this.nom);
            throw new IllegalArgumentException("Un créneau ne peut pas être null");
        }
        this.creneaux.add(creneau);
        LOGGER.info("Créneau ajouté : " + creneau);
    }

    /**
     * Supprime un créneau horaire existant de la planification.
     *
     * @param creneau Le créneau à enlever.
     */
    public void removeCreneau(Creneau creneau) {
        if (creneau != null) {
            this.creneaux.remove(creneau);
            LOGGER.info("Créneau supprimé de l'événement " + this.nom);
        }
    }

    /**
     * Rajoute une tâche à faire dans la checklist de préparation de l'événement.
     *
     * @param tache La tâche à accomplir.
     */
    public void addTache(Tache tache) {
        if (tache == null) {
            LOGGER.warning("Tentative d'ajout d'une tâche nulle dans l'événement " + this.nom);
            throw new IllegalArgumentException("Une tâche ne peut pas être nulle");
        }
        this.taches.add(tache);
        LOGGER.info("Tâche ajoutée à la checklist : " + tache);
    }

    /**
     * Supprime une tâche de la checklist de préparation.
     *
     * @param tache La tâche à retirer.
     */
    public void suprimerTache(Tache tache) {
        if (tache != null) {
            this.taches.remove(tache);
            LOGGER.info("Tâche retirée de la checklist de l'événement " + this.nom);
        }
    }

    /**
     * Enregistre un mouvement comptable (une dépense ou une recette) pour l'événement.
     *
     * @param depenseRecette La ligne comptable à intégrer.
     */
    public void addDepense(DepenseRecette depenseRecette) {
        if (depenseRecette != null) {
            this.depensesRecettes.add(depenseRecette);
        } else {
            LOGGER.log(Level.WARNING, "Tentative d''ajout d''une depense nul dans l''événement {0}", new Object[]{this.nom});
            throw new IllegalArgumentException("une depense ne peut pas etre null");
        }
    }

    /**
     * Supprime un mouvement comptable (dépense ou recette) enregistré.
     *
     * @param depenseRecette Le mouvement à effacer.
     */
    public void suprimerDepense(DepenseRecette depenseRecette) {
        if (depenseRecette != null) {
            this.depensesRecettes.remove(depenseRecette);
            LOGGER.info("Mouvement financier supprimé.");
        }
    }


    // ── TOSTRING STANDARD ────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Evénement [Nom = " + nom + ", Date début = " + dateDebut + ", Budget = " + budget + " €]";
    }

}