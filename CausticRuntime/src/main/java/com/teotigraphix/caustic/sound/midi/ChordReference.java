
package com.teotigraphix.caustic.sound.midi;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum class providing access to ready made chord degrees based on scale
 * references.
 * 
 * @author gmuller
 */
public enum ChordReference {
    /**
     * Scale Degrees {1, 3, 5} Midi Note Numbers {0, 4, 7}
     */
    MAJOR("M"),

    /**
     * Scale Degrees {1, b3, 5} Midi Note Numbers {0, 3, 7}
     */
    MINOR("m"),

    /**
     * Scale Degrees {1, b3, b5} Midi Note Numbers {0, 3, 6}
     */
    DIMINISHED("dim"),

    /**
     * Scale Degrees {1, b3, b5, b7} Midi Note Numbers {0, 3, 6, 9}
     */
    DIMISHED_SEVENTH("dim7"),

    /**
     * Scale Degrees {1, b3, b5, 7} Midi Note Numbers {0, 3, 6, 10}
     */
    HALF_DIMINISHED("m7b5"),

    /**
     * Scale Degrees {1, 3, #5} Midi Note Numbers {0, 4, 8}
     */
    AUGMENTED("aug"),

    /**
     * Scale Degrees {1, 5} Midi Note Numbers {0, 7}
     */
    FIFTH("5"),

    /**
     * Scale Degrees {1, 3, 5, b7} Midi Note Numbers {0, 4, 7, 10}
     */
    SEVENTH("7"),

    /**
     * Scale Degrees {1, b3, 5, b7} Midi Note Numbers {0, 3, 7, 10}
     */
    MINOR_SEVENTH("m7"),

    /**
     * Scale Degrees {1, 3, 5, 7} Midi Note Numbers {0, 4, 7, 11}
     */
    MAJOR_SEVENTH("maj7"),

    /**
     * Scale Degrees {1, b3, 5, 7} Midi Note Numbers {0, 3, 7, 11}
     */
    MINOR_MAJOR_SEVENTH("m/maj7"),

    /**
     * Scale Degrees {1, 4, 5} Midi Note Numbers {0, 5, 7}
     */
    SUSPENDED_FOURTH("sus4"),

    /**
     * Scale Degrees {1, 2, 5} Midi Note Numbers {0, 2, 7}
     */
    SUSPENDED_SECOND("sus2"),

    /**
     * Scale Degrees {1, 4, 5, b7} Midi Note Numbers {0, 5, 7, 10}
     */
    SEVENTH_SUSPENDED_FOURTH("7sus4"),

    /**
     * Scale Degrees {1, 2, 5, b7} Midi Note Numbers {0, 2, 7, 10}
     */
    SEVENTH_SUSPENDED_SECOND("7sus2"),

    /**
     * Scale Degrees {1, 2, 3, 5} Midi Note Numbers {0, 2, 4, 7}
     */
    ADD_TWO("add2"),

    /**
     * Scale Degrees {0, 3, 5, 9} Midi Note Numbers {0, 4, 7, 14}
     */
    ADD_NINE("add9"),

    /**
     * Scale Degrees {1, 3, 4, 5} Midi Note Numbers {0, 4, 5, 7}
     */
    ADD_FOURTH("add4"),

    /**
     * Scale Degrees {1, 3, 5, 6} Midi Note Numbers {0, 4, 7, 9}
     */
    SIXTH("6"),

    /**
     * Scale Degrees {1, b3, 5, 6} Midi Note Numbers {0, 3, 7, 9}
     */
    MINOR_SIXTH("m6"),

    /**
     * Scale Degrees {1, 3, 5, 6, 9} Midi Note Numbers {0, 4, 7, 9, 14}
     */
    SIX_NINE("6/9"),

    /**
     * Scale Degrees {1, 3, 5, b7, 9} Midi Note Numbers {0, 4, 7, 10, 14}
     */
    NINTH("9"),

    /**
     * Scale Degrees {1, b3, 5, b7, 9} Midi Note Numbers {0, 3, 7, 10, 14}
     */
    MINOR_NINTH("m9"),

    /**
     * Scale Degrees {1, 3, 5, 7, 9} Midi Note Numbers {0, 4, 7, 11, 14}
     */
    MAJOR_NINTH("maj9"),

    /**
     * Scale Degrees {1, 3, 5, b7, 9, 11} Midi Note Numbers {0, 4, 7, 10, 14,
     * 17}
     */
    ELEVENTH("11"),

    /**
     * Scale Degrees {1, b3, 5, b7, 9, 11} Midi Note Numbers {0, 3, 7, 10, 14,
     * 17}
     */
    MINOR_ELEVENTH("m11"),

    /**
     * Scale Degrees {1, 3, 5, 7, 9, 11} Midi Note Numbers {0, 4, 7, 11, 14, 17}
     */
    MAJOR_ELEVENTH("maj11"),

    /**
     * Scale Degrees {1, 3, 5, b7, 9, 11, 13} Midi Note Numbers {0, 4, 7, 10,
     * 14, 17, 21}
     */
    THIRTEENTH("13"),

    /**
     * Scale Degrees {1, b3, 5, b7, 9, 11, 13} Midi Note Numbers {0, 3, 7, 10,
     * 14, 17, 21}
     */
    MINOR_THIRTEENTH("m13"),

    /**
     * Scale Degrees {1, 3, 5, 7, 9, 11, 13} Midi Note Numbers {0, 4, 7, 11, 14,
     * 17, 21}
     */
    MAJOR_THIRTEENTH("maj13"),

    /**
     * Scale Degrees {1, 3, 5, b7, #9} Midi Note Numbers {0, 4, 7, 10, 15}
     */
    SEVENTH_SHARP_NINE("7#9"),

    /**
     * Scale Degrees {1, 3, 5, b7, b9} Midi Note Numbers {0, 4, 7, 10, 13}
     */
    SEVENTH_FLAT_NINE("7b9"),

    /**
     * Scale Degrees {1, 3, #5, b7} Midi Note Numbers {0, 4, 8, 10}
     */
    SEVENTH_SHARP_FIFTH("7#5"),

    /**
     * Scale Degrees {1, 3, b5, b7} Midi Note Numbers {0, 4, 6, 10}
     */
    SEVENTH_FLAT_FIFTH("7b5");

    private static final Map<String, ChordReference> lookup = new HashMap<String, ChordReference>();

    static {
        for (ChordReference s : EnumSet.allOf(ChordReference.class))
            lookup.put(s.getCommonName(), s);
    }

    private String commonName;

    private static final int[] MAJOR_CHORD = {
            0, 4, 7
    }, MINOR_CHORD = {
            0, 3, 7
    }, DIMINISHED_CHORD = {
            0, 3, 6
    }, DIMINISHED_SEVENTH_CHORD = {
            0, 3, 6, 9
    }, HALF_DIMINISHED_SEVENTH_CHORD = {
            0, 3, 6, 10
    }, AUGMENTED_CHORD = {
            0, 4, 8
    }, FIFTH_CHORD = {
            0, 5
    }, SEVENTH_CHORD = {
            0, 4, 7, 10
    }, MINOR_SEVENTH_CHORD = {
            0, 3, 7, 10
    }, MAJOR_SEVENTH_CHORD = {
            0, 4, 7, 11
    }, MINOR_MAJOR_SEVENTH_CHORD = {
            0, 3, 7, 11
    }, SUSPENDED_FOURTH_CHORD = {
            0, 5, 7
    }, SUSPENDED_SECOND_CHORD = {
            0, 2, 7
    }, SEVENTH_SUSPENDED_FOURTH_CHORD = {
            0, 5, 7, 10
    }, SEVENTH_SUSPENDED_SECOND_CHORD = {
            0, 2, 7, 10
    }, ADD_TWO_CHORD = {
            0, 2, 4, 7
    }, ADD_NINE_CHORD = {
            0, 4, 7, 14
    }, ADD_FOURTH_CHORD = {
            0, 4, 5, 7
    }, SIXTH_CHORD = {
            0, 4, 7, 9
    }, MINOR_SIXTH_CHORD = {
            0, 3, 7, 9
    }, SIX_NINE_CHORD = {
            0, 4, 7, 9, 14
    }, NINTH_CHORD = {
            0, 4, 7, 10, 14
    }, MINOR_NINTH_CHORD = {
            0, 3, 7, 10, 14
    }, MAJOR_NINTH_CHORD = {
            0, 4, 7, 11, 14
    }, ELEVENTH_CHORD = {
            0, 4, 7, 10, 14, 17
    }, MINOR_ELEVENTH_CHORD = {
            0, 3, 7, 10, 14, 17
    }, MAJOR_ELEVENTH_CHORD = {
            0, 4, 7, 11, 14, 17
    }, THIRTEENTH_CHORD = {
            0, 4, 7, 10, 14, 17, 21
    }, MINOR_THIRTEENTH_CHORD = {
            0, 3, 7, 10, 14, 17, 21
    }, MAJOR_THIRTEENTH_CHORD = {
            0, 4, 7, 11, 14, 17, 21
    }, SEVENTH_SHARP_NINE_CHORD = {
            0, 4, 7, 10, 15
    }, SEVENTH_FLAT_NINE_CHORD = {
            0, 4, 7, 10, 13
    }, SEVENTH_SHARP_FIFTH_CHORD = {
            0, 4, 8, 10
    }, SEVENTH_FLAT_FIFTH_CHORD = {
            0, 4, 6, 10
    };

    ChordReference(String commonName) {
        this.commonName = commonName;
    }

    /**
     * Provides access to the integer array containing the scale degrees for a
     * particular chord. For example a Major chord is made up of the root, the
     * third and the fifth. Taking zero as the root, the third is 5 notes away,
     * and the the fifth is 7 notes away. The integer array for this chord would
     * be {0, 5, 7}.
     * 
     * @see MidiReference#createScale(ScaleReference, NoteReference)
     * @return int array of chord degrees
     */
    public int[] getDegrees() {
        switch (this) {
            case MAJOR:
                return MAJOR_CHORD;
            case MINOR:
                return MINOR_CHORD;
            case DIMINISHED:
                return DIMINISHED_CHORD;
            case DIMISHED_SEVENTH:
                return DIMINISHED_SEVENTH_CHORD;
            case HALF_DIMINISHED:
                return HALF_DIMINISHED_SEVENTH_CHORD;
            case AUGMENTED:
                return AUGMENTED_CHORD;
            case FIFTH:
                return FIFTH_CHORD;
            case SEVENTH:
                return SEVENTH_CHORD;
            case MINOR_SEVENTH:
                return MINOR_SEVENTH_CHORD;
            case MAJOR_SEVENTH:
                return MAJOR_SEVENTH_CHORD;
            case MINOR_MAJOR_SEVENTH:
                return MINOR_MAJOR_SEVENTH_CHORD;
            case SUSPENDED_FOURTH:
                return SUSPENDED_FOURTH_CHORD;
            case SUSPENDED_SECOND:
                return SUSPENDED_SECOND_CHORD;
            case SEVENTH_SUSPENDED_FOURTH:
                return SEVENTH_SUSPENDED_FOURTH_CHORD;
            case SEVENTH_SUSPENDED_SECOND:
                return SEVENTH_SUSPENDED_SECOND_CHORD;
            case ADD_TWO:
                return ADD_TWO_CHORD;
            case ADD_NINE:
                return ADD_NINE_CHORD;
            case ADD_FOURTH:
                return ADD_FOURTH_CHORD;
            case SIXTH:
                return SIXTH_CHORD;
            case MINOR_SIXTH:
                return MINOR_SIXTH_CHORD;
            case SIX_NINE:
                return SIX_NINE_CHORD;
            case NINTH:
                return NINTH_CHORD;
            case MINOR_NINTH:
                return MINOR_NINTH_CHORD;
            case MAJOR_NINTH:
                return MAJOR_NINTH_CHORD;
            case ELEVENTH:
                return ELEVENTH_CHORD;
            case MINOR_ELEVENTH:
                return MINOR_ELEVENTH_CHORD;
            case MAJOR_ELEVENTH:
                return MAJOR_ELEVENTH_CHORD;
            case THIRTEENTH:
                return THIRTEENTH_CHORD;
            case MINOR_THIRTEENTH:
                return MINOR_THIRTEENTH_CHORD;
            case MAJOR_THIRTEENTH:
                return MAJOR_THIRTEENTH_CHORD;
            case SEVENTH_SHARP_NINE:
                return SEVENTH_SHARP_NINE_CHORD;
            case SEVENTH_FLAT_NINE:
                return SEVENTH_FLAT_NINE_CHORD;
            case SEVENTH_SHARP_FIFTH:
                return SEVENTH_SHARP_FIFTH_CHORD;
            case SEVENTH_FLAT_FIFTH:
                return SEVENTH_FLAT_FIFTH_CHORD;
        }
        throw new AssertionError("Unknown Scale: " + this);
    }

    /**
     * Gets the abbreviated name of the chord. For example a Major Thirteenth
     * chord is abbreviated maj13.
     * 
     * @return String abbreviation of the chord.
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * Reverse lookup of the enum ChordReference by the abbreviation. Passing in
     * "maj13" will return the MAJOR_THIRTEENTH ChordReference
     * 
     * @param commonName
     * @return ChordReference matching the abbreviation passed in
     */
    public static ChordReference get(String commonName) {
        return lookup.get(commonName);
    }
}
