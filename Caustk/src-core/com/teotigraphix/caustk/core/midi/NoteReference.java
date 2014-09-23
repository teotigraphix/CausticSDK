
package com.teotigraphix.caustk.core.midi;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum containing the basic note information at octave 0.
 * 
 * @author gmuller
 */
public enum NoteReference {
    /**
     * Base Number 0, Note Name "C"
     */
    C(0, "C"),

    /**
     * Base Number 1, Note Name "C#"
     */
    //Csharp(1, "C#"),

    /**
     * Base Number 1, Note Name "Db"
     */
    Dflat(1, "Db"),

    /**
     * Base Number 2, Note Name "D"
     */
    D(2, "D"),

    /**
     * Base Number 3, Note Name "D#"
     */
    //Dsharp(3, "D#"),

    /**
     * Base Number 3, Note Name "Eb"
     */
    Eflat(3, "Eb"),

    /**
     * Base Number 4, Note Name "E"
     */
    E(4, "E"),

    /**
     * Base Number 5, Note Name "F"
     */
    F(5, "F"),

    /**
     * Base Number 6, Note Name "F#"
     */
    //Fsharp(6, "F#"),

    /**
     * Base Number 6, Note Name "Gb"
     */
    Gflat(6, "Gb"),

    /**
     * Base Number 7, Note Name "G"
     */
    G(7, "G"),

    /**
     * Base Number 8, Note Name "G#"
     */
    //Gsharp(8, "G#"),

    /**
     * Base Number 8, Note Name "Ab"
     */
    Aflat(8, "Ab"),

    /**
     * Base Number 9, Note Name "A"
     */
    A(9, "A"),

    /**
     * Base Number 10, Note Name "A#"
     */
    //Asharp(10, "A#"),

    /**
     * Base Number 10, Note Name "Bb"
     */
    Bflat(10, "Bb"),

    /**
     * Base Number 11, Note Name "B"
     */
    B(11, "B");

    private static final Map<String, NoteReference> lookup = new HashMap<String, NoteReference>();

    static {
        for (NoteReference s : EnumSet.allOf(NoteReference.class))
            lookup.put(s.getBaseName(), s);
    }

    private final int baseNumber;

    private final String baseNoteName;

    NoteReference(int baseNumber, String baseNoteName) {
        this.baseNumber = baseNumber;
        this.baseNoteName = baseNoteName;
    }

    /**
     * This method gets the base note number of the note at octave 0. As an
     * example G has a base note number of 7.
     * 
     * @return the integer of the note at octave 0
     */
    public int getBaseNumber() {
        return baseNumber;
    }

    public String getBaseName() {
        return baseNoteName;
    }

    /**
     * Reverse lookup of a NoteReference based on the abbreviation passed in.
     * For example, passing in the String "C#" will return the Csharp
     * NoteReference
     * 
     * @param baseName
     * @return The NoteReference of the abbreviation passed in
     */
    public static NoteReference get(String baseName) {
        return lookup.get(baseName);
    }

    public static NoteReference getNote(int baseNumber) {
        for (NoteReference noteReference : lookup.values()) {
            if (noteReference.getBaseNumber() == baseNumber)
                return noteReference;
        }
        return null;
    }
}
