
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

    public static final int[] DRUM_MATRIX = {
            0, 1, 2, 3, -1, -1, -1, -1, 4, 5, 6, 7, -1, -1, -1, -1, 8, 9, 10, 11, -1, -1, -1, -1,
            12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };

    public static ScaleInterval[] intervals;

    public static final int FOURTH_UP = 0;

    public static final int FOURTH_RIGHT = 1;

    public static final int THIRD_UP = 2;

    public static final int THIRD_RIGHT = 3;

    public static final int SEQUENT_UP = 4;

    public static final int SEQUENT_RIGHT = 5;

    public static final String[] LAYOUT_NAMES = {
            "4th ^", "4th >", "3rd ^", "3rd >", "Seqent^", "Seqent>"
    };

    public static final int ORIENT_UP = 0;

    public static final int ORIENT_RIGHT = 1;

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private int startNote;

    private int endNote;

    private int numColumns;

    private int numRows;

    private int selectedScale;

    private int scaleOffset;

    private int scaleLayout;

    private int orientation;

    private boolean chromaticOn;

    private int octave;

    private int shift;

    private int drumOctave;

    public String getName(int scale) {
        return scale < Scales.intervals.length ? Scales.intervals[scale].name : "";
    }

    public int getSelectedScale() {
        return selectedScale;
    }

    public int getScaleSize() {
        return scales.length;
    }

    public void setSelectdScale(int scale) {
        selectedScale = Math.max(0, Math.min(scale, this.scales.length - 1));
    }

    public void setSelectdScale(ScaleSelection scale) {
        for (int i = 0; i < intervals.length; i++) {
            ScaleInterval interval = intervals[i];
            if (interval.name.equals(scale.getLabel())) {
                setSelectdScale(i);
                break;
            }
        }
    }

    public void nextScale() {
        setSelectdScale(this.selectedScale + 1);
    }

    public void prevScale() {
        setSelectdScale(this.selectedScale - 1);
    }

    public int getScaleOffset() {
        return scaleOffset;
    }

    /**
     * Sets the root key, 0 == C.
     * 
     * @param scaleOffset
     */
    public void setScaleOffset(int scaleOffset) {
        this.scaleOffset = scaleOffset;
    }

    /**
     * Sets the root key, 0 == C.
     * 
     * @param noteReference
     */
    public void setScaleOffset(NoteReference noteReference) {
        for (int i = 0; i < BASES.length; i++) {
            if (BASES[i].equals(noteReference.getBaseName())) {
                this.scaleOffset = i;
            }
        }
    }

    public int getScaleLayout() {
        return scaleLayout;
    }

    public void setScaleLayout(int scaleLayout) {
        this.scaleLayout = Math.max(Scales.FOURTH_UP, Math.min(scaleLayout, Scales.SEQUENT_RIGHT));
        this.orientation = this.scaleLayout % 2 == 0 ? Scales.ORIENT_UP : Scales.ORIENT_RIGHT;
        switch (this.scaleLayout) {
            case 0:
            case 1:
                setPlayShift(3);
                break;
            case 2:
            case 3:
                setPlayShift(2);
                break;
            case 4:
            case 5:
                setPlayShift(8);
                break;
        }
    }

    public void setPlayShift(int shift) {
        this.shift = shift;
        this.generateMatrices();
    }

    public void setChromatic(boolean enable) {
        this.chromaticOn = enable;
    }

    public void toggleChromatic() {
        this.chromaticOn = !this.chromaticOn;
    }

    public boolean isChromatic() {
        return chromaticOn;
    }

    public void setOctave(int octave) {
        this.octave = Math.max(-3, Math.min(octave, 3));
    }

    public int getOctave() {
        return octave;
    }

    public void incOctave() {
        setOctave(octave + 1);
    }

    public void decOctave() {
        setOctave(octave - 1);
    }

    public void setDrumOctave(int drumOctave) {
        this.drumOctave = Math.max(-3, Math.min(drumOctave, 5));
    }

    public int getDrumOctave() {
        return drumOctave;
    }

    public void incDrumOctave() {
        setDrumOctave(this.drumOctave + 1);
    }

    public void decDrumOctave() {
        setDrumOctave(this.drumOctave - 1);
    }

    public Color getColor(int[] noteMap, int note) {
        int midiNote = noteMap[note];
        if (midiNote == -1)
            return Model.PUSH_COLOR_BLACK;
        int n = (midiNote - Scales.OFFSETS[this.scaleOffset]) % 12;
        if (n == 0)
            return Model.PUSH_COLOR2_OCEAN_HI;
        if (this.isChromatic()) {
            int[] notes = Scales.intervals[this.selectedScale].notes;
            for (int i = 0; i < notes.length; i++) {
                if (notes[i] == n)
                    return Model.PUSH_COLOR2_WHITE;
            }
            return Model.PUSH_COLOR_BLACK;
        }
        return Model.PUSH_COLOR2_WHITE;
    }

    public Color getSequencerColor(int[] noteMap, int note) {
        int midiNote = noteMap[note];
        if (midiNote == -1)
            return Model.PUSH_COLOR_BLACK;
        int n = (midiNote - Scales.OFFSETS[this.scaleOffset]) % 12;
        if (n == 0)
            return Model.PUSH_COLOR2_OCEAN_HI;
        if (this.isChromatic()) {
            int[] notes = Scales.intervals[this.selectedScale].notes;
            for (int i = 0; i < notes.length; i++) {
                if (notes[i] == n)
                    return Model.PUSH_COLOR2_WHITE;
            }
            return Model.PUSH_COLOR_BLACK;
        }
        return Model.PUSH_COLOR2_WHITE;
    }

    public int[] getActiveMatrix() {
        return this.isChromatic() ? this.scales[this.selectedScale].chromatic
                : this.scales[this.selectedScale].matrix;
    };

    public int[] getNoteMatrix() {
        int[] matrix = this.getActiveMatrix();
        int[] noteMap = this.getEmptyMatrix();
        for (int note = this.startNote; note < this.endNote; note++) {
            int n = matrix[note - this.startNote] + Scales.OFFSETS[this.scaleOffset]
                    + this.startNote + this.octave * 12;
            noteMap[note] = n < 0 || n > 127 ? -1 : n;
        }
        return noteMap;
    }

    public int[] getSequencerMatrix(int length, int offset) {
        int[] matrix = this.getActiveMatrix();
        int[] noteMap = initArray(-1, length);
        for (int note = 0; note < length; note++) {
            int n = matrix[note] + Scales.OFFSETS[this.scaleOffset] + offset;
            noteMap[note] = n < 0 || n > 127 ? -1 : n;
        }
        return noteMap;
    }

    public int[] getEmptyMatrix() {
        return initArray(-1, 128);
    }

    public int[] getDrumMatrix() {
        int[] matrix = Scales.DRUM_MATRIX;
        int[] noteMap = this.getEmptyMatrix();
        for (int note = this.startNote; note < this.endNote; note++) {
            int n = matrix[note - this.startNote] == -1 ? -1 : matrix[note - this.startNote]
                    + this.startNote + this.drumOctave * 16;
            noteMap[note] = n < 0 || n > 127 ? -1 : n;
        }
        return noteMap;
    }

    public String getRangeText() {
        int[] matrix = this.getActiveMatrix();
        int offset = Scales.OFFSETS[this.scaleOffset];
        return formatNote(offset + matrix[0]) + " to "
                + formatNote(offset + matrix[matrix.length - 1]);
    }

    public String getSequencerRangeText(int from, int to) {
        return this.formatNoteAndOctave(from, -2) + " to " + this.formatDrumNote(to/*, -2*/);
    }

    public String getDrumRangeText() {
        int s = this.startNote + this.drumOctave * 16;
        return this.formatDrumNote(s) + " to " + this.formatDrumNote(s + 15);
    }

    public String formatDrumNote(int note) {
        return this.formatNoteAndOctave(note, -2);
    }

    public String formatNoteAndOctave(int note, int octaveOffset) {
        return Scales.NOTE_NAMES[Math.abs(note % 12)] + (int)(Math.floor(note / 12) + octaveOffset);
    }

    public String formatNote(int note) {
        return Scales.NOTE_NAMES[note % 12] + (int)((2 + Math.floor(note / 12) + this.octave));
    }

    private static int[] initArray(int value, int length) {
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = value;
        }
        return result;
    }

    public void setGrid(int numColumns, int numRows) {
        this.numColumns = numColumns;
        this.numRows = numRows;
        this.generateMatrices();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

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

    public Scales(int startNote, int endNote, int numColumns, int numRows) {
        this.startNote = startNote;
        this.endNote = endNote; // last note + 1
        this.numColumns = numColumns;
        this.numRows = numRows;

        this.selectedScale = 0; // Major
        this.scaleOffset = 0; // C
        this.scaleLayout = Scales.FOURTH_UP;
        this.orientation = Scales.ORIENT_UP;
        this.chromaticOn = false;
        this.octave = 0;
        this.shift = 3;
        this.drumOctave = 0;

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

        this.generateMatrices();
    }

    private ScaleInfo[] scales;

    private ScaleInfo createScale(ScaleInterval scale) {
        int compacity = numRows * numColumns;

        int len = scale.notes.length;
        int[] matrix = new int[compacity];
        int[] chromatic = new int[compacity];

        int index = 0;
        boolean isUp = this.orientation == Scales.ORIENT_UP;
        for (int row = 0; row < this.numRows; row++) {
            for (int column = 0; column < this.numColumns; column++) {
                int y = isUp ? row : column;
                int x = isUp ? column : row;
                int offset = y * this.shift + x;
                matrix[index] = (int)((Math.floor(offset / len)) * 12 + scale.notes[offset % len]);
                chromatic[index] = (y
                        * (this.shift == this.numRows ? this.numRows
                                : scale.notes[this.shift % len]) + x);

                index++;
            }
        }
        return new ScaleInfo(scale.name, matrix, chromatic);
    };

    public void generateMatrices() {
        this.scales = new ScaleInfo[Scales.intervals.length];
        for (int i = 0; i < Scales.intervals.length; i++)
            this.scales[i] = this.createScale(Scales.intervals[i]);
    }

    class ScaleInfo {

        @SuppressWarnings("unused")
        private String name;

        private int[] matrix;

        private int[] chromatic;

        public ScaleInfo(String name, int[] matrix, int[] chromatic) {
            this.name = name;
            this.matrix = matrix;
            this.chromatic = chromatic;
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

}
