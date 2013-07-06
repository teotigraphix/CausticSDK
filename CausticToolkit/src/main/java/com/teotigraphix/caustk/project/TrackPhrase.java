
package com.teotigraphix.caustk.project;

import java.util.UUID;

/*

What is a track pattern;
- a placeholder for pattern slots within a Track
- has assigned bank/pattern index


*/

public class TrackPhrase {
    //----------------------------------
    //  numMeasures
    //----------------------------------

    private int numMeasures;

    public final int getNumMeasures() {
        return numMeasures;
    }

    public final void setNumMeasures(int value) {
        numMeasures = value;
    }

    //----------------------------------
    //  id
    //----------------------------------

    private UUID id;

    public final UUID getId() {
        return id;
    }

    public final void setId(UUID value) {
        id = value;
    }

    //----------------------------------
    //  bankIndex
    //----------------------------------

    private int bankIndex;

    public final int getBankIndex() {
        return bankIndex;
    }

    public final void setBankIndex(int bankIndex) {
        this.bankIndex = bankIndex;
    }

    //----------------------------------
    //  patternIndex
    //----------------------------------

    private int patternIndex;

    public final int getPatternIndex() {
        return patternIndex;
    }

    public final void setPatternIndex(int patternIndex) {
        this.patternIndex = patternIndex;
    }

    //----------------------------------
    //  noteData
    //----------------------------------

    private String noteData;

    public final String getNoteData() {
        return noteData;
    }

    public final void setNoteData(String value) {
        noteData = value;
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public TrackPhrase() {
    }

}
