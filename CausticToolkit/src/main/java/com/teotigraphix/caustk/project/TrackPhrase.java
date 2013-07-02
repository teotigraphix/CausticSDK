
package com.teotigraphix.caustk.project;

/*

What is a track pattern;
- a placeholder for pattern slots within a Track
- has assigned bank/pattern index


*/

public class TrackPhrase {

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

    public int getLength() {
        // this allows for virtual lengths in the track
        // this means that a phrase may be 8 measures but was added as 4 measures
        // which the start and end would show with this calc
        return getEndMeasure() - getStartMeasure();
    }

    //--------------------------------------------------------------------------
    // 
    //  Constructor
    // 
    //--------------------------------------------------------------------------

    public TrackPhrase() {
    }

}
