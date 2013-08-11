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

package com.teotigraphix.caustk.sequencer;

import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.service.ISerialize;

/**
 * A {@link TrackItem} is a value object that is contained in a {@link Track}.
 * <p>
 * A new track item is created for every span of a {@link ChannelPhrase}
 * inserted into the Track with {@link Track#addPhrase(int, ChannelPhrase)}.
 */
public class TrackItem implements ISerialize {

    private int numMeasures;

    //----------------------------------
    //  phraseId
    //----------------------------------

    private UUID phraseId;

    /**
     * Returns the {@link ChannelPhrase#getId()} of the underlying
     * {@link LibraryPhrase} that was copied when the channel had its phrase
     * assigned.
     */
    public final UUID getPhraseId() {
        return phraseId;
    }

    public final void setPhraseId(UUID value) {
        phraseId = value;
    }

    //----------------------------------
    //  bankIndex
    //----------------------------------

    private int bankIndex;

    /**
     * The bank index in the pattern sequencer where the phrase data is located
     * (A-D).
     */
    public final int getBankIndex() {
        return bankIndex;
    }

    public final void setBankIndex(int value) {
        bankIndex = value;
    }

    //----------------------------------
    //  patternIndex
    //----------------------------------

    private int patternIndex;

    /**
     * The pattern index in the pattern sequencer where the phrase data is
     * located (1-16).
     */
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

    /**
     * The measure at which this item starts in the song sequencer.
     */
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

    /**
     * The measure at which this item ends in the song sequencer.
     */
    public final int getEndMeasure() {
        return endMeasure;
    }

    public final void setEndMeasure(int value) {
        endMeasure = value;
    }

    public int getNumMeasures() {
        return numMeasures;
    }

    public void setNumMeasures(int value) {
        numMeasures = value;
    }

    /**
     * Returns the length of the item span, end - start measure.
     */
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

    /**
     * The number of loops that the original phrase num measures is multiplied
     * by.
     * 
     * @return
     */
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
        if (measure >= start && measure < end)
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

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
    }

}
