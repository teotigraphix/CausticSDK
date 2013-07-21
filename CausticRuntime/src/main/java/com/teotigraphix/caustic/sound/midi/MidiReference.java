
package com.teotigraphix.caustic.sound.midi;

import java.util.HashMap;
import java.util.Map;


/**
 * Collection of methods designed to make translating between the language of
 * music to midi values. Contains methods that make note numbers, not names, and
 * frequencies available, as well as simple methods to create scales and chords
 * based on a note.
 * 
 * @author gmuller
 */
public class MidiReference {

    private static MidiReference midiReference;

    private static Map<String, Integer> noteNameMap = new HashMap<String, Integer>();

    private static String[] noteNumberArray = new String[132];

    private static float[] noteFreqArray = new float[132];

    private int[] range = {
            -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    };

    int[] octave = {
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
    };

    private int a = 440; // a is 440 hz...

    private MidiReference() {
        mapNoteNames();
        mapNoteNumbers();
        mapNoteFreq();
    }

    private void mapNoteNames() {
        for (int i = 1; i < 12; i++) {
            for (NoteReference note : NoteReference.values()) {
                int noteNumber = note.getBaseNumber() + (octave[i] * 12);
                noteNameMap.put(note.getBaseName() + range[i], noteNumber);
            }
        }
    }

    /*
    * method to create a map of Note numbers to Note Names Numbers
    * to get a note number out of the map use something like:
    * String[] noteNumberArray = midiNote.getNoteNumberArray();
    * println(noteNumberArray[60]);
    * println(noteNumberArray[2]);
    * println(noteNumberArray[127]);
    *
    * Intentionally using a simple array here for performance reasons
    */
    private void mapNoteNumbers() {
        String noteName;
        int octaveValue;
        for (int i = 1; i < 11; i++) {
            octaveValue = octave[i] * 12;
            for (NoteReference note : NoteReference.values()) {
                //if the note name contains a "flat" then it has the identical note number
                //as the previous flat, decrement the total note number count and insert into
                //the array a string made up of both the previous notes name and the current one
                if (note.getBaseName().contains("b")) {
                    String previous = noteNumberArray[note.getBaseNumber() + octaveValue];
                    noteName = previous + "/" + note.getBaseName() + range[i];
                } else {
                    noteName = note.getBaseName() + range[i];
                }
                noteNumberArray[note.getBaseNumber() + octaveValue] = noteName;
            }
        }
    }

    private void mapNoteFreq() {
        for (Integer i = 0; i < noteNumberArray.length; ++i) {
            noteFreqArray[i] = (float)(a * Math.pow(2, (i - 69d) / 12));
        }
    }

    /**
     * Get note name from note number.
     * 
     * @param noteNumber
     * @return String note name with no formatting (B Flat at octave 6 will
     *         appear as "Bb6/A#6") ;
     */
    public String getNoteName(int noteNumber) {
        return noteNumberArray[noteNumber];
    }

    /**
     * Get note name from midi note number. If transform note is true, return
     * only one note name (i.e. B flat at octave 6 returns "Bb6")
     * 
     * @param noteNumber
     * @param transformNote
     * @return String note name
     */
    public String getNoteName(int noteNumber, boolean transformNote) {
        if (transformNote) {
            return transformNote(getNoteName(noteNumber));
        } else {
            return getNoteName(noteNumber);
        }
    }

    /**
     * Get note name from midi note number. If transform note is true, return
     * only one note name (i.e. B flat at octave 6 returns "Bb" when both
     * transform note and stripOctave are true)
     * 
     * @param noteNumber
     * @param transformNote
     * @param stripOctave
     * @return String note name
     */
    public String getNoteName(int noteNumber, boolean transformNote, boolean stripOctave) {
        String noteName = getNoteName(noteNumber);
        if (transformNote) {
            noteName = transformNote(noteName);
        }

        if (stripOctave) {
            noteName = stripOctave(noteName);
        }

        return noteName;
    }

    /**
     * Get midi note number from note name
     * 
     * @param noteName
     * @return int midi note number
     */

    public int getNoteNumber(String noteName) {
        return noteNameMap.get(noteName);
    }

    /**
     * Get note frequency from midi note number
     * 
     * @param noteNumber
     * @return float frequency
     */
    public float getNoteFrequency(int noteNumber) {
        return noteFreqArray[noteNumber];
    }

    /**
     * Get note frequency from note name.
     * 
     * @param noteName
     * @return float frequency
     */
    public float getNoteFrequency(String noteName) {
        return getNoteFrequency(getNoteNumber(noteName));
    }

    /**
     * Get approximate midi note number from frequency
     * 
     * @param frequency
     * @return int midi note number
     */
    public int getNoteFromFrequency(float frequency) {
        float noteNumber;
        noteNumber = (float)(69 + 12 * (Math.log(frequency / 440) / Math.log(2)));
        return Math.round(noteNumber);
    }

    /**
     * Get note number from samples
     * 
     * @param sampleRate (ex: 44100)
     * @param samples (256)
     * @return Integer midi note number
     */
    public Integer getNoteFromSamples(Integer sampleRate, float samples) {
        return getNoteFromFrequency(sampleRate / samples);
    }

    private String transformNote(String longNote) {
        String shortNote;
        String temp[] = null;
        temp = longNote.split("/");
        if ((temp.length > 1) && (temp[1].contains("Bb") || temp[1].contains("Eb"))) {
            shortNote = temp[1];
        } else {
            shortNote = temp[0];
        }

        return shortNote;
    }

    private String stripOctave(String shortNote) {
        if (shortNote.contains("-")) {
            shortNote = shortNote.substring(0, shortNote.length() - 2);
        } else {
            shortNote = shortNote.substring(0, shortNote.length() - 1);
        }

        return shortNote;
    }

    /**
     * Create a scale type using the int note number and int[] scale degrees
     * provided
     * 
     * @param scaleDegrees
     * @param noteNumber
     * @return int[] scale
     */
    public static int[] createScale(int[] scaleDegrees, int noteNumber) {
        int[] newScale = new int[scaleDegrees.length];
        if (noteNumber > 11) {
            noteNumber = noteNumber % 12;
        }
        for (int i = 0; i < scaleDegrees.length; i++) {
            int scaleDegree = scaleDegrees[i] + noteNumber;
            if (scaleDegree < 12) {
                newScale[i] = scaleDegree;
            } else {
                newScale[i] = scaleDegree - 12;
            }
        }
        return newScale;
    }

    /**
     * Create a scale from a NoteReference and int[] scale degrees
     * 
     * @see MidiReference#createScale(int[], int)
     * @param scaleDegrees
     * @param baseNote
     * @return int[] scale
     */
    public int[] createScale(int[] scaleDegrees, NoteReference baseNote) {
        return createScale(scaleDegrees, baseNote.getBaseNumber());
    }

    /**
     * Create a scale from a NoteReference and a ScaleReference
     * 
     * @see MidiReference#createScale(int[], int)
     * @param baseScale
     * @param baseNote
     * @return int[] scale
     */
    public static int[] createScale(ScaleReference baseScale, NoteReference baseNote) {
        return createScale(baseScale.getDegrees(), baseNote.getBaseNumber());
    }

    /**
     * NOT YET IMPLEMENTED
     * 
     * @param scaleIn
     * @param scaleRef
     * @return int[] scale
     */
    public int[] getRelatedMode(int[] scaleIn, ScaleReference scaleRef) {
        //TODO: Implement this method.
        return null;
    }

    /**
     * Check if a note is in a scale
     * 
     * @param scale
     * @param noteNumber
     * @return boolean note is in scale
     */
    public boolean isScale(int[] scale, int noteNumber) {
        for (int i = 0; i < scale.length; i++) {
            if (noteNumber % 12 == (scale[i]))
                return true;
        }
        return false;
    }

    /**
     * Create a int[] chord using a NoteReference and a ChordReference using
     * octave 0
     * 
     * @see MidiReference#createChord(int, int[])
     * @param noteRef
     * @param chordRef
     * @return int[] chord
     */
    public int[] createChord(NoteReference noteRef, ChordReference chordRef) {
        return createChord(noteRef.getBaseNumber(), chordRef.getDegrees(), 0);
    }

    /**
     * Create a int[] chord using an NoteReference and ChordReference
     * 
     * @see MidiReference#createChord(int, int[], int)
     * @param noteRef
     * @param chordRef
     * @param octave
     * @return int[] chord
     */
    public int[] createChord(NoteReference noteRef, ChordReference chordRef, int octave) {
        return createChord(noteRef.getBaseNumber(), chordRef.getDegrees(), octave);
    }

    /**
     * Create a int[] chord using an int note number and a ChordReference using
     * octave 0
     * 
     * @see MidiReference#createChord(int, int[])
     * @param noteNumber
     * @param chordRef
     * @return int[] chord
     */
    public int[] createChord(int noteNumber, ChordReference chordRef) {
        return createChord(noteNumber, chordRef.getDegrees(), 0);
    }

    /**
     * Create a int[] chord using an int note number and ChordReference
     * 
     * @see MidiReference#createChord(int, int[], int)
     * @param note
     * @param chordRef
     * @param octave
     * @return int[] chord
     */
    public int[] createChord(int note, ChordReference chordRef, int octave) {
        return createChord(note, chordRef.getDegrees(), octave);
    }

    /**
     * Create a int[] chord using an NoteReference and an int[] chord using
     * octave 0
     * 
     * @see MidiReference#createChord(int, int[])
     * @param noteRef
     * @param chordDegrees
     * @return int[] chord
     */
    public int[] createChord(NoteReference noteRef, int[] chordDegrees) {
        return createChord(noteRef.getBaseNumber(), chordDegrees, 0);
    }

    /**
     * Create a int[] chord using an int note number and int[] chord using
     * octave 0
     * 
     * @see MidiReference#createChord(int, int[], int)
     * @param note
     * @param chordDegrees
     * @return int[] chord
     */
    public int[] createChord(int note, int[] chordDegrees) {
        return createChord(note, chordDegrees, 0);
    }

    /**
     * Create a int[] chord using an int note number and int[] chord
     * 
     * @see MidiReference#createChord(int, int[], int)
     * @param note
     * @param chordDegrees
     * @param octave
     * @return int[] chord
     */
    public int[] createChord(int note, int[] chordDegrees, int octave) {
        int[] notesOut = new int[chordDegrees.length];
        for (int i = 0; i < notesOut.length; i++) {
            notesOut[i] = (chordDegrees[i] + note) + (12 * octave);
        }
        return notesOut;
    }

    /**
     * Create a midiReference class. Calls is protected to the extent that it
     * will only allow one instantiation.
     * 
     * @return MidiReference
     */
    public static MidiReference getInstance() {
        if (midiReference == null)
            midiReference = new MidiReference();
        return midiReference;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
