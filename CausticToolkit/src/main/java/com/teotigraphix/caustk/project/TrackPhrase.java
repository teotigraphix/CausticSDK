
package com.teotigraphix.caustk.project;

import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;

/*

What is a track pattern;
- a placeholder for pattern slots within a Track
- has assigned bank/pattern index


*/

public class TrackPhrase {

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
    //  patternIndex
    //----------------------------------

    private int startMeasure;

    public final int getStartMeasure() {
        return startMeasure;
    }

    public final void setStartMeasure(int startMeasure) {
        this.startMeasure = startMeasure;
    }

    //----------------------------------
    //  patternIndex
    //----------------------------------

    private int endMeasure;

    public final int getEndMeasure() {
        return endMeasure;
    }

    public final void setEndMeasure(int endMeasure) {
        this.endMeasure = endMeasure;
    }

    private int explicitLength;

    public int getExplicitLength() {
        return explicitLength;
    }

    public void setExplicitLength(int explicitLength) {
        this.explicitLength = explicitLength;
    }

    public int getLength() {
        // this allows for virtual lengths in the track
        // this means that a phrase may be 8 measures but was added as 4 measures
        // which the start and end would show with this calc
        return getEndMeasure() - getStartMeasure();
    }

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
