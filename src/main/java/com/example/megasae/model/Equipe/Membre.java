package com.example.megasae.model.Equipe;

import com.example.megasae.model.Evenement.Creneau;
import com.example.megasae.model.Evenement.Evenement;
import com.example.megasae.model.Miscellaneous.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Classe modèle représentant un membre inscrit à un événement.
 * Un membre possède des informations de contact, une liste de rôles,
 * de créneaux et un statut de présence géré par l'organisateur.
 * Implémente Serializable pour permettre la sauvegarde binaire de la SAE.
 */
public class Membre implements Serializable {

    // ── VARIABLES ET ATTRIBUTS ──────────────────────────────────────────────

    /**
     * Identifiant unique pour la sérialisation binaire de la classe
     */
    private static final long serialVersionUID = 1L;

    /**
     * Journal de log pour suivre les actions sur les membres
     */
    private static final Logger logger = Logger.getLogger(Membre.class.getName());

    /**
     * Identifiant unique du membre généré automatiquement
     */
    private UUID id;

    /**
     * Nom de famille du membre
     */
    private String nom;

    /**
     * Prénom du membre
     */
    private String prenom;

    /**
     * Adresse e-mail du membre
     */
    private String mail;

    /**
     * Numéro de téléphone du membre
     */
    private String telephone;

    /**
     * Statut de présence du membre (PRESENT ou ABSENT ou INCONNU)
     */
    private StatutPresence presence;

    /**
     * Note confidentielle visible uniquement par l'organisateur
     */
    private String noteInterne;

    /**
     * L'événement auquel ce membre est rattaché
     */
    private Evenement evenement;

    /**
     * Liste des étiquettes (tags) de catégorisation affectées au membre
     */
    private List<Tag> tags = new ArrayList<>();

    /**
     * Liste des plages horaires (créneaux) réservées par le membre
     */
    private List<Creneau> creneaux = new ArrayList<>();

    /**
     * Liste des rôles attribués à ce membre sur l'événement
     */
    private List<Role> roles = new ArrayList<>();


    // ── CONSTRUCTEURS ──────────────────────────────────────────────────────

    /**
     * Constructeur par défaut.
     * Initialise un identifiant unique et configure la présence par défaut sur INCONNU.
     */
    public Membre() {
        this.id = UUID.randomUUID();
        this.presence = StatutPresence.INCONNU;
    }

    /**
     * Constructeur complet pour instancier un membre avec toutes ses informations de base.
     */
    public Membre(Evenement evenement, String nom, String prenom, String mail, String telephone, StatutPresence presence, String noteInterne) {
        this.id = UUID.randomUUID();
        this.evenement = evenement;
        setNom(nom);
        setPrenom(prenom);
        setMail(mail);
        setTelephone(telephone);
        setNoteInterne(noteInterne);
        setPresence(presence);
    }


    // ── GETTERS ET SETTERS CLASSIQUES ─────────────────────────────────────────

    /**
     * Récupère l'identifiant unique du membre
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * Modifie l'identifiant unique du membre
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Récupère le nom de famille du membre
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Modifie le nom de famille avec une vérification de sécurité (sans .trim())
     */
    public void setNom(String nom) {
        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Un membre doit avoir un nom");
        }
        this.nom = nom;
    }

    /**
     * Récupère le prénom du membre
     */
    public String getPrenom() {
        return this.prenom;
    }

    /**
     * Modifie le prénom du membre
     */
    public void setPrenom(String prenom) {
        if (prenom == null || prenom.isEmpty()) {
            throw new IllegalArgumentException("Un membre doit avoir un prénom");
        }
        this.prenom = prenom;
    }

    /**
     * Récupère l'adresse e-mail du membre
     */
    public String getMail() {
        return this.mail;
    }

    /**
     * Modifie l'adresse e-mail avec une vérification de sécurité (sans .trim())
     */
    public void setMail(String mail) {
        if (mail == null || mail.isEmpty()) {
            throw new IllegalArgumentException("Un membre doit avoir un mail");
        }
        this.mail = mail;
    }

    /**
     * Récupère le numéro de téléphone
     */
    public String getTelephone() {
        return this.telephone;
    }

    /**
     * Modifie le numéro de téléphone avec une vérification de sécurité (sans .trim())
     */
    public void setTelephone(String telephone) {
        if (telephone == null || telephone.isEmpty()) {
            throw new IllegalArgumentException("Un membre doit avoir un téléphone");
        }
        this.telephone = telephone;
    }

    /**
     * Récupère le statut de présence actuel
     */
    public StatutPresence getPresence() {
        return this.presence;
    }

    /**
     * Modifie le statut de présence et ajoute un message de suivi dans la console (Logger)
     */
    public void setPresence(StatutPresence presence) {
        if (presence == null) {
            this.presence = StatutPresence.INCONNU;
        } else {
            this.presence = presence;
        }
        logger.info("Présence modifiée pour le membre : " + this.prenom + " " + this.nom + ". Nouveau statut : " + this.presence);
    }

    /**
     * Récupère la note interne de l'organisateur
     */
    public String getNoteInterne() {
        return this.noteInterne;
    }

    /**
     * Modifie la note interne de l'organisateur
     */
    public void setNoteInterne(String noteInterne) {
        this.noteInterne = noteInterne;
    }

    /**
     * Récupère l'événement de rattachement
     */
    public Evenement getEvenement() {
        return this.evenement;
    }

    /**
     * Modifie l'événement de rattachement
     */
    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    /**
     * Récupère la liste des tags associés
     */
    public List<Tag> getTags() {
        return this.tags;
    }

    /**
     * Remplace la liste complète des tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Récupère la liste des créneaux horaires associés
     */
    public List<Creneau> getCreneaux() {
        return this.creneaux;
    }

    /**
     * Remplace la liste complète des créneaux
     */
    public void setCreneaux(List<Creneau> creneaux) {
        this.creneaux = creneaux;
    }

    /**
     * Récupère la liste des rôles affectés au membre
     */
    public List<Role> getRoles() {
        return this.roles;
    }

    /**
     * Remplace la liste complète des rôles
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    // ── MÉTHODES DE GESTION DES LISTES ───────────────────────────────────────

    /**
     * Ajoute un nouveau tag au membre si celui-ci n'est pas déjà présent dans sa liste.
     */
    public void addTag(Tag tag) {
        if (tag != null && !this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Associe un nouveau créneau horaire au membre si celui-ci n'est pas déjà enregistré.
     */
    public void addCreneau(Creneau creneau) {
        if (creneau != null && !this.creneaux.contains(creneau)) {
            this.creneaux.add(creneau);
        }
    }

    /**
     * Attribue un nouveau rôle au membre si celui-ci n'a pas déjà été ajouté.
     */
    public void addRole(Role role) {
        if (role != null && !this.roles.contains(role)) {
            this.roles.add(role);
        }
    }


    // ── MÉTHODE DE DUPLICATION (COPIE MULTIPLE) ──────────────────────────────

    /**
     * Duplique ce membre un nombre de fois défini si les capacités maximales
     * de l'événement et des rôles ne sont pas dépassées.
     *
     * @param nombreDeCopies Nombre de fois que l'on veut cloner ce membre.
     * @return La liste contenant tous les nouveaux membres copiés.
     */
    public List<Membre> dupliquerMembresCopies(int nombreDeCopies) {
        List<Membre> listeCopies = new ArrayList<>();

        for (int i = 1; i <= nombreDeCopies; i++) {

            // 1. Sécurité : Vérification de la capacité globale de l'événement
            if (this.evenement != null && this.evenement.getMembres().size() + i > this.evenement.getCapaciteMax()) {
                throw new IllegalStateException("Impossible de dupliquer : Capacité maximale de l'événement atteinte.");
            }

            // 2. Sécurité : Vérification de la capacité spécifique de chaque rôle associé
            for (Role roleActuel : this.roles) {
                if (roleActuel.getNombreMembres() + i > roleActuel.getCapaciteMaximale()) {
                    throw new IllegalStateException("Impossible de dupliquer : Capacité maximale atteinte pour le rôle " + roleActuel.getNom());
                }
            }

            String mailCopie = (this.mail == null || this.mail.isEmpty()) ? "copie." + i + "@example.com" : this.mail;
            String telCopie = (this.telephone == null || this.telephone.isEmpty()) ? "0000000000" : this.telephone;
            String nomCopie = (this.nom == null || this.nom.isEmpty()) ? "SansNom" : this.nom;

            Membre copieMembre = new Membre(
                    this.evenement,
                    nomCopie + " (" + i + ")",
                    this.prenom,
                    mailCopie,
                    telCopie,
                    StatutPresence.INCONNU,
                    this.noteInterne
            );

            // Copie indépendante des listes pour éviter que les clones partagent la même adresse mémoire
            copieMembre.setRoles(new ArrayList<>(this.roles));
            copieMembre.setTags(new ArrayList<>(this.tags));

            // Ajout du clone à la liste de retour
            listeCopies.add(copieMembre);
            logger.fine("Création en mémoire de la copie numéro " + i + " pour : " + copieMembre.getNom());
        }

        logger.info("Succès : " + listeCopies.size() + " copie(s) créée(s) pour le membre " + this.prenom + " " + this.nom);
        return listeCopies;
    }


    // ── MÉTHODE TOSTRING STANDARD ────────────────────────────────────────────

    @Override
    public String toString() {
        return "Membre [Nom = " + nom + ", Prénom = " + prenom + ", Mail = " + mail + "]";
    }

}