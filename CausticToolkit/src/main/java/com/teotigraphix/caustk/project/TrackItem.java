
package com.teotigraphix.caustk.project;

import java.util.UUID;

public class TrackItem {

    private UUID libraryPhraseId;

    public final UUID getLibraryPhraseId() {
        return libraryPhraseId;
    }

    public final void setLibraryPhraseId(UUID value) {
        libraryPhraseId = value;
    }

    private UUID trackPhraseId;

    private int bankIndex;

    private int patternIndex;

    public final UUID getTrackPhraseId() {
        return trackPhraseId;
    }

    public final void setTrackPhraseId(UUID value) {
        trackPhraseId = value;
    }

    public final int getBankIndex() {
        return bankIndex;
    }

    public final void setBankIndex(int value) {
        bankIndex = value;
    }

    public final int getPatternIndex() {
        return patternIndex;
    }

    public final void setPatternIndex(int value) {
        patternIndex = value;
    }

    //----------------------------------
    //  startMeasure
    //----------------------------------

    private int startMeasure;

    public final int getStartMeasure() {
        return startMeasure;
    }

    public final void setStartMeasure(int value) {
        startMeasure = value;
    }

    //----------------------------------
    //  endMeasure
    //----------------------------------

    private int endMeasure;

    public final int getEndMeasure() {
        return endMeasure;
    }

    public final void setEndMeasure(int value) {
        endMeasure = value;
    }

    public int getLength() {
        // this allows for virtual lengths in the track
        // this means that a phrase may be 8 measures but was added as 4 measures
        // which the start and end would show with this calc
        return getEndMeasure() - getStartMeasure();
    }

    //----------------------------------
    //  numLoops
    //----------------------------------

    private int numLoops;

    public int getNumLoops() {
        return numLoops;
    }

    public void setNumLoops(int value) {
        numLoops = value;
    }

    public TrackItem() {
    }

    // 0:4

    public boolean contains(int measure) {
        int start = getStartMeasure();
        int end = getEndMeasure();
        if (measure > start && measure < end)
            return true;
        return false;
    }
    //
    //    public boolean containsInSpan(int measure) {
    //        int start = getStartMeasure();
    //        int end = getEndMeasure();
    //        if (measure > start && measure <= end)
    //            return true;
    //        return false;
    //    }

}
