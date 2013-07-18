////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

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
