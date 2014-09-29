
package com.teotigraphix.caustk.controller.helper;

import com.badlogic.gdx.graphics.Color;
import com.teotigraphix.caustk.controller.daw.Model;
import com.teotigraphix.caustk.core.midi.NoteReference;

public class Scales {

    //--------------------------------------------------------------------------
    // Public :: Constants
    //--------------------------------------------------------------------------

    public static final String[] NOTE_NAMES = {
            "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"
    };

    public static final String[] BASES = {
            "C", "G", "D", "A", "E", "B", "F", "Bb", "Eb", "Ab", "Db", "Gb"
    };

    public static final int[] OFFSETS = {
            0, 7, 2, 9, 4, 11, 5, 10, 3, 8, 1, 6
    };

    /*
    0,   1,  2,  3, -1, -1, -1, -1, 
    4,   5,  6,  7, -1, -1, -1, -1, 
    8,   9, 10, 11, -1, -1, -1, -1, 
    12, 13, 14, 15, -1, -1, -1, -1, 
    -1, -1, -1, -1, -1, -1, -1, -1, 
    -1, -1, -1, -1, -1, -1, -1, -1, 
    -1, -1, -1, -1, -1, -1, -1, -1, 
    -1, -1, -1, -1, -1, -1, -1, -1    
    */

    //    private static final int[] DRUM_MATRIX = {
    //            0, 1, 2, 3, -1, -1, -1, -1, 4, 5, 6, 7, -1, -1, -1, -1, 8, 9, 10, 11, -1, -1, -1, -1,
    //            12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    //            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    //    };

    private static ScaleInterval[] intervals;

    //    private static final int FOURTH_UP = 0;
    //
    //    private static final int FOURTH_RIGHT = 1;
    //
    //    private static final int THIRD_UP = 2;
    //
    //    private static final int THIRD_RIGHT = 3;
    //
    //    private static final int SEQUENT_UP = 4;
    //
    //    private static final int SEQUENT_RIGHT = 5;
    //
    //    private static final String[] LAYOUT_NAMES = {
    //            "4th ^", "4th >", "3rd ^", "3rd >", "Seqent^", "Seqent>"
    //    };

    private static final int ORIENT_UP = 0;

    //    private static final int ORIENT_RIGHT = 1;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private int startNote;

    private int endNote;

    private int numColumns;

    private int numRows;

    private int selectedScale;

    private int rootKey;

    //    private int scaleLayout;

    /**
     * The orientation of the matrix, switches columns/rows.
     */
    private int orientation;

    private boolean chromatic;

    /**
     * Used when get the note matrix with {@link #getNoteMatrix()}.
     */
    private int octave;

    private int shift;

    //    private int drumOctave;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // startNote, endNote
    //----------------------------------

    /**
     * Sets the MIDI note range for the note/sequencer matrix and the column,
     * row layout.
     * <p>
     * Will update the scale intervals accordingly.
     * <p>
     * Calls {@link #generateMatrices()}.
     * 
     * @param startNote
     * @param endNote
     */
    public void update(int startNote, int endNote, int numColumns, int numRows) {
        this.startNote = startNote;
        this.endNote = endNote;
        this.numColumns = numColumns;
        this.numRows = numRows;
        generateMatrices();
    }

    //----------------------------------
    // chromaticOn
    //----------------------------------

    public boolean isChromatic() {
        return chromatic;
    }

    public void setChromatic(boolean chromatic) {
        this.chromatic = chromatic;
    }

    public void toggleChromatic() {
        chromatic = !chromatic;
    }

    //----------------------------------
    // rootKey
    //----------------------------------

    int getRootKey() {
        return rootKey;
    }

    /**
     * Sets the root key, 0 == C.
     * 
     * @param scaleOffset
     */
    void setRootKey(int rootKey) {
        this.rootKey = rootKey;
    }

    /**
     * Sets the root key, 0 == C.
     * 
     * @param rootKey
     */
    public void setRootKey(NoteReference rootKey) {
        for (int i = 0; i < BASES.length; i++) {
            if (BASES[i].equals(rootKey.getBaseName())) {
                this.rootKey = i;
            }
        }
    }

    //--------------------------------------------------------------------------
    // Internal API :: Properties
    //--------------------------------------------------------------------------

    String getName(int scale) {
        return scale < Scales.intervals.length ? Scales.intervals[scale].name : "";
    }

    int getSelectedScale() {
        return selectedScale;
    }

    int getScaleSize() {
        return scales.length;
    }

    void setSelectdScale(int scaleIndex) {
        selectedScale = Math.max(0, Math.min(scaleIndex, scales.length - 1));
    }

    /**
     * Sets the selected {@link ScaleSelection}.
     * 
     * @param scale The new scale selection.
     */
    public void setSelectdScale(ScaleSelection scale) {
        for (int i = 0; i < intervals.length; i++) {
            ScaleInterval interval = intervals[i];
            if (interval.name.equals(scale.getLabel())) {
                setSelectdScale(i);
                break;
            }
        }
    }

    void nextScale() {
        setSelectdScale(selectedScale + 1);
    }

    void prevScale() {
        setSelectdScale(selectedScale - 1);
    }

    //    int getScaleLayout() {
    //        return scaleLayout;
    //    }

    void setOctave(int octave) {
        octave = Math.max(-3, Math.min(octave, 3));
    }

    int getOctave() {
        return octave;
    }

    void incOctave() {
        setOctave(octave + 1);
    }

    void decOctave() {
        setOctave(octave - 1);
    }

    void setDrumOctave(int drumOctave) {
        drumOctave = Math.max(-3, Math.min(drumOctave, 5));
    }

    //    int getDrumOctave() {
    //        return drumOctave;
    //    }
    //
    //    void incDrumOctave() {
    //        setDrumOctave(drumOctave + 1);
    //    }
    //
    //    void decDrumOctave() {
    //        setDrumOctave(drumOctave - 1);
    //    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    // SequencerView uses
    public Color getSequencerColor(int[] noteMap, int note) {
        int midiNote = noteMap[note];
        if (midiNote == -1)
            return Model.BLACK;
        int n = (midiNote - Scales.OFFSETS[rootKey]) % 12;
        if (n == 0)
            return Model.OCEAN;
        if (isChromatic()) {
            int[] notes = Scales.intervals[selectedScale].notes;
            for (int i = 0; i < notes.length; i++) {
                if (notes[i] == n)
                    return Model.WHITE;
            }
            return Model.BLACK;
        }
        return Model.WHITE;
    }

    // SequencerView uses
    /**
     * @param rows The number of row notes eg 8 for Beatbox 47-54.
     * @param midiRoot The midi root value to start at, bottom to top.
     */
    public int[] getSequencerMatrix(int rows, int midiRoot) {
        int[] matrix = getActiveMatrix();
        int[] noteMap = initArray(-1, rows);
        for (int note = 0; note < rows; note++) {
            int n = matrix[note] + Scales.OFFSETS[rootKey] + midiRoot;
            noteMap[note] = n < 0 || n > 127 ? -1 : n;
        }
        return noteMap;
    }

    // SequencerView uses
    public int[] getEmptyMatrix() {
        return initArray(-1, 128);
    }

    public String getSequencerRangeText(int from, int to) {
        return formatNoteAndOctave(from, -2) + " to " + formatDrumNote(to/*, -2*/);
    }

    //--------------------------------------------------------------------------
    // Internal API :: Methods
    //--------------------------------------------------------------------------

    Color getColor(int[] noteMap, int note) {
        int midiNote = noteMap[note];
        if (midiNote == -1)
            return Model.BLACK;
        int n = (midiNote - Scales.OFFSETS[rootKey]) % 12;
        if (n == 0)
            return Model.OCEAN;
        if (isChromatic()) {
            int[] notes = Scales.intervals[selectedScale].notes;
            for (int i = 0; i < notes.length; i++) {
                if (notes[i] == n)
                    return Model.WHITE;
            }
            return Model.BLACK;
        }
        return Model.WHITE;
    }

    int[] getActiveMatrix() {
        return isChromatic() ? scales[selectedScale].chromaticMatrix : scales[selectedScale].matrix;
    };

    int[] getNoteMatrix() {
        int[] matrix = getActiveMatrix();
        int[] noteMap = getEmptyMatrix();
        for (int note = startNote; note < endNote; note++) {
            int n = matrix[note - startNote] + Scales.OFFSETS[rootKey] + startNote + octave * 12;
            noteMap[note] = n < 0 || n > 127 ? -1 : n;
        }
        return noteMap;
    }

    //    int[] getDrumMatrix() {
    //        int[] matrix = Scales.DRUM_MATRIX;
    //        int[] noteMap = getEmptyMatrix();
    //        for (int note = startNote; note < endNote; note++) {
    //            int n = matrix[note - startNote] == -1 ? -1 : matrix[note - startNote] + startNote
    //                    + drumOctave * 16;
    //            noteMap[note] = n < 0 || n > 127 ? -1 : n;
    //        }
    //        return noteMap;
    //    }

    String getRangeText() {
        int[] matrix = getActiveMatrix();
        int offset = Scales.OFFSETS[rootKey];
        return formatNote(offset + matrix[0]) + " to "
                + formatNote(offset + matrix[matrix.length - 1]);
    }

    //    String getDrumRangeText() {
    //        int s = startNote + drumOctave * 16;
    //        return formatDrumNote(s) + " to " + formatDrumNote(s + 15);
    //    }

    String formatDrumNote(int note) {
        return formatNoteAndOctave(note, -2);
    }

    String formatNoteAndOctave(int note, int octaveOffset) {
        return Scales.NOTE_NAMES[Math.abs(note % 12)] + (int)(Math.floor(note / 12) + octaveOffset);
    }

    String formatNote(int note) {
        return Scales.NOTE_NAMES[note % 12] + (int)((2 + Math.floor(note / 12) + octave));
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Scales(int startNote, int endNote, int numColumns, int numRows) {
        this.startNote = startNote;
        this.endNote = endNote;
        this.numColumns = numColumns;
        this.numRows = numRows;

        selectedScale = 0; // Major
        rootKey = 0; // C
        //        scaleLayout = Scales.FOURTH_UP;
        orientation = Scales.ORIENT_UP;
        chromatic = false;
        octave = 0;
        shift = 3; // TODO figure out how this works when creating scales
        //        drumOctave = 0;

        intervals = new ScaleInterval[] {
                new ScaleInterval("Major", new int[] {
                        0, 2, 4, 5, 7, 9, 11
                }), new ScaleInterval("Minor", new int[] {
                        0, 2, 3, 5, 7, 8, 10
                }), new ScaleInterval("Dorian", new int[] {
                        0, 2, 3, 5, 7, 9, 10
                }), new ScaleInterval("Mixolydian", new int[] {
                        0, 2, 4, 5, 7, 9, 10
                }), new ScaleInterval("Lydian", new int[] {
                        0, 2, 4, 6, 7, 9, 11
                }), new ScaleInterval("Phrygian", new int[] {
                        0, 1, 3, 5, 7, 8, 10
                }), new ScaleInterval("Locrian", new int[] {
                        0, 1, 3, 4, 6, 8, 10
                }), new ScaleInterval("Diminished", new int[] {
                        0, 1, 3, 4, 6, 7, 9
                }), new ScaleInterval("Whole-half", new int[] {
                        0, 2, 3, 5, 6, 8, 9
                }), new ScaleInterval("Whole Tone", new int[] {
                        0, 2, 4, 6, 8, 10
                }), new ScaleInterval("Minor Blues", new int[] {
                        0, 3, 5, 6, 7, 10
                }), new ScaleInterval("Minor Pentatonic", new int[] {
                        0, 3, 5, 7, 10
                }), new ScaleInterval("Major Pentatonic", new int[] {
                        0, 2, 4, 7, 9
                }), new ScaleInterval("Harmonic Minor", new int[] {
                        0, 2, 3, 5, 7, 8, 11
                }), new ScaleInterval("Melodic Minor", new int[] {
                        0, 2, 3, 5, 7, 9, 11
                }), new ScaleInterval("Super Locrian", new int[] {
                        0, 1, 3, 4, 6, 8, 10
                }), new ScaleInterval("Bhairav", new int[] {
                        0, 1, 4, 5, 7, 8, 11
                }), new ScaleInterval("Hungarian Minor", new int[] {
                        0, 2, 3, 6, 7, 8, 11
                }), new ScaleInterval("Minor Gypsy", new int[] {
                        0, 1, 4, 5, 7, 8, 10
                }), new ScaleInterval("Hirojoshi", new int[] {
                        0, 4, 6, 7, 11
                }), new ScaleInterval("In-Sen", new int[] {
                        0, 1, 5, 7, 10
                }), new ScaleInterval("Iwato", new int[] {
                        0, 1, 5, 6, 10
                }), new ScaleInterval("Kumoi", new int[] {
                        0, 2, 3, 7, 9
                }), new ScaleInterval("Pelog", new int[] {
                        0, 1, 3, 7, 8
                }), new ScaleInterval("Spanish", new int[] {
                        0, 1, 4, 5, 7, 9, 10
                })
        };

        generateMatrices();
    }

    public enum ScaleSelection {

        Major("Major"),

        Minor("Minor"),

        Dorian("Dorian"),

        Mixolydian("Mixolydian"),

        Lydian("Lydian"),

        Phrygian("Phrygian"),

        Locrian("Locrian"),

        Diminished("Diminished"),

        Wholehalf("Whole-half"),

        WholeTone("Whole Tone"),

        MinorBlues("Minor Blues"),

        MinorPentatonic("Minor Pentatonic"),

        MajorPentatonic("Major Pentatonic"),

        HarmonicMinor("Harmonic Minor"),

        MelodicMinor("Melodic Minor"),

        SuperLocrian("Super Locrian"),

        Bhairav("Bhairav"),

        HungarianMinor("Hungarian Minor"),

        MinorGypsy("Minor Gypsy"),

        Hirojoshi("Hirojoshi"),

        InSen("In-Sen"),

        Iwato("Iwato"),

        Kumoi("Kumoi"),

        Pelog("Pelog"),

        Spanish("Spanish");

        private String label;

        public String getLabel() {
            return label;
        }

        ScaleSelection(String label) {
            this.label = label;
        }
    }

    //--------------------------------------------------------------------------
    // Private
    //--------------------------------------------------------------------------

    //    void setScaleLayout(int scaleLayout) {
    //        scaleLayout = Math.max(Scales.FOURTH_UP, Math.min(scaleLayout, Scales.SEQUENT_RIGHT));
    //        orientation = scaleLayout % 2 == 0 ? Scales.ORIENT_UP : Scales.ORIENT_RIGHT;
    //        switch (scaleLayout) {
    //            case 0:
    //            case 1:
    //                setPlayShift(3);
    //                break;
    //            case 2:
    //            case 3:
    //                setPlayShift(2);
    //                break;
    //            case 4:
    //            case 5:
    //                setPlayShift(8);
    //                break;
    //        }
    //    }

    //    void setPlayShift(int shift) {
    //        shift = shift;
    //        generateMatrices();
    //    }

    private ScaleInfo[] scales;

    private ScaleInfo createScale(ScaleInterval scale) {
        int compacity = numRows * numColumns;

        int len = scale.notes.length;
        int[] matrix = new int[compacity];
        int[] chromaticMatrix = new int[compacity];

        int index = 0;
        boolean isUp = orientation == Scales.ORIENT_UP;
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                int y = isUp ? row : column;
                int x = isUp ? column : row;
                int offset = y * shift + x;
                matrix[index] = (int)((Math.floor(offset / len)) * 12 + scale.notes[offset % len]);
                chromaticMatrix[index] = (y
                        * (shift == numRows ? numRows : scale.notes[shift % len]) + x);

                index++;
            }
        }
        return new ScaleInfo(scale.name, matrix, chromaticMatrix);
    };

    void generateMatrices() {
        scales = new ScaleInfo[Scales.intervals.length];
        for (int i = 0; i < Scales.intervals.length; i++)
            scales[i] = createScale(Scales.intervals[i]);
    }

    class ScaleInfo {

        @SuppressWarnings("unused")
        private String name;

        private int[] matrix;

        private int[] chromaticMatrix;

        public ScaleInfo(String name, int[] matrix, int[] chromaticMatrix) {
            this.name = name;
            this.matrix = matrix;
            this.chromaticMatrix = chromaticMatrix;
        }
    }

    class ScaleInterval {
        private String name;

        private int[] notes;

        public ScaleInterval(String name, int[] notes) {
            this.name = name;
            this.notes = notes;
        }
    }

    private static int[] initArray(int value, int length) {
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = value;
        }
        return result;
    }
}
