package com.example.megasae.model.Evenement;

import com.example.megasae.model.Equipe.Role;
import com.example.megasae.model.Miscellaneous.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour TemplateEvenement.
 */
class TemplateEvenementTest {

    private TemplateEvenement template;

    @BeforeEach
    void setUp() {
        template = new TemplateEvenement();
    }

    @Test
    void testConstructeur() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role());

        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag());


        TemplateEvenement temp = new TemplateEvenement(
                "Conférence", "Template description", "Nouvelle Conférence",
                "Description...", "Bleu", roles, tags
        );

        // Assert
        assertEquals("Conférence", temp.getNomTemplate());
        assertEquals("Nouvelle Conférence", temp.getNom());
        assertEquals(roles, temp.getRoles());
        assertEquals(tags, temp.getTags());
    }

    @Test
    void testSettersEtGetters() {
        template.setNomTemplate("Formation");
        template.setCouleur("Rouge");

        assertEquals("Formation", template.getNomTemplate());
        assertEquals("Rouge", template.getCouleur());
    }

    @Test
    void testGestionListes() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role());

        template.setRoles(roles);

        assertEquals(1, template.getRoles().size());
        assertTrue(template.getRoles().contains(roles.get(0)));
    }

    @Test
    void testSetNomVide(){
        assertThrows(IllegalArgumentException.class, () -> {
            template.setNom(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            template.setNom("");
        });
    }

    @Test
    void testSetNomTemplateVide(){
        assertThrows(IllegalArgumentException.class, () -> {
            template.setNomTemplate(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            template.setNomTemplate("");
        });
    }
}