
package com.teotigraphix.caustk.core.midi;

/**
 * Enum class providing access to scales based on chromatic degree reference.
 */

public enum ScaleReference {

    CHROMATIC("Chromatic", new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
    }),

    MAJOR("Major", new int[] {
            0, 2, 4, 5, 7, 9, 11
    }),

    MINOR("Minor", new int[] {
            0, 2, 3, 5, 7, 8, 10
    }),

    DORIAN("Dorian", new int[] {
            0, 2, 3, 5, 7, 9, 10
    }),

    MIXOLYDIAN("Mixolydian", new int[] {
            0, 2, 4, 5, 7, 9, 10
    }),

    LYDIAN("Lydian", new int[] {
            0, 2, 4, 6, 7, 9, 11
    }),

    PHRYGIAN("Phrygian", new int[] {
            0, 1, 3, 5, 7, 8, 10
    }),

    LOCRIAN("Locrian", new int[] {
            0, 1, 3, 5, 6, 8, 10
    }),

    DIMINISHED("Diminished", new int[] {
            0, 1, 3, 4, 6, 7, 9
    }),

    WHOLE_HALF("Whole-half", new int[] {
            0, 2, 3, 5, 6, 8, 9
    }),

    WHOLE_TONE("Whole Tone", new int[] {
            0, 2, 4, 6, 8, 10
    }),

    MINOR_BLUES("Minor Blues", new int[] {
            0, 3, 5, 6, 7, 10
    }),

    MINOR_PENTATONIC("Minor Pentatonic", new int[] {
            0, 3, 5, 7, 10
    }),

    MAJOR_PENTATONIC("Major Pentatonic", new int[] {
            0, 2, 4, 7, 9
    }),

    HARMONIC_MINOR("Harmonic Minor", new int[] {
            0, 2, 3, 5, 7, 8, 11
    }),

    MELODIC_MINOR("Melodic Minor", new int[] {
            0, 2, 3, 5, 7, 9, 11
    }),

    SUPER_LOCRIAN("Super Locrian", new int[] {
            0, 1, 3, 4, 6, 8, 10
    }),

    BHAIRAV("Bhairav", new int[] {
            0, 1, 4, 5, 7, 8, 11
    }),

    HUNGARIAN_MINOR("Hungarian Minor", new int[] {
            0, 2, 3, 6, 7, 8, 11
    }),

    MINOR_GYPSY("Minor Gypsy", new int[] {
            0, 1, 4, 5, 7, 8, 10
    }),

    HIROJOSHI("Hirojoshi", new int[] {}),

    IN_SEN("In-Sen", new int[] {
            0, 4, 6, 7, 11
    }),

    IWATO("Iwato", new int[] {
            0, 1, 5, 6, 10
    }),

    KUMOI("Kumoi", new int[] {
            0, 2, 3, 7, 9
    }),

    PELOG("Pelog", new int[] {
            0, 1, 3, 7, 8
    }),

    SPANISH("Spanish", new int[] {
            0, 1, 4, 5, 7, 9, 10
    });

    private String name;

    private int[] intervals;

    public String getName() {
        return name;
    }

    /**
     * Gets the integer array containing the scale degrees of the scale. For
     * instance, at any octave let the base note of the scale equal 0, the
     * degrees for a major scale would then be {0, 2, 4, 5, 7, 9, 11}
     * 
     * @return int array containing the degrees between 0 - 12 representing the
     *         scale
     */
    public int[] getIntervals() {
        return intervals;
    }

    ScaleReference(String name, int[] intervals) {
        this.name = name;
        this.intervals = intervals;

    }

    public static ScaleReference fromName(String scaleName) {
        for (ScaleReference scale : values()) {
            if (scale.getName().equals(scaleName))
                return scale;
        }
        return null;
    }
}
