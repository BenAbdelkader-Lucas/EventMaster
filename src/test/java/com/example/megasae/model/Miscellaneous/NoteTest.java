package com.example.megasae.model.Miscellaneous;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void testConstructeur(){
        Note note = new Note("libelle", "description");

        assertEquals("libelle", note.getLibelle());
        assertEquals("description", note.getDescription());
    }


    @Test
    void testSetLibelleVide() {
        Note note = new Note("libelle", "description");

        assertThrows(IllegalArgumentException.class, () -> {
            note.setLibelle("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            note.setLibelle(null);
        });
    }
}