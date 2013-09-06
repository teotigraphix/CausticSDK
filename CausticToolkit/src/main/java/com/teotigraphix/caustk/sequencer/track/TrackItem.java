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

package com.teotigraphix.caustk.sequencer.track;

import java.util.UUID;

import javax.sound.midi.Track;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.pattern.PatternUtils;
import com.teotigraphix.caustk.service.ISerialize;

/**
 * A {@link TrackItem} is a value object that is contained in a {@link Track}.
 * <p>
 * A new track item is created for every span of a {@link ChannelPhrase}
 * inserted into the Track with {@link Track#addPhrase(int, ChannelPhrase)}.
 */
public class TrackItem implements ISerialize {

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    private int numMeasures;

    //----------------------------------
    //  trackIndex
    //----------------------------------

    private int trackIndex;

    /**
     * The track/tone index.
     */
    public int getTrackIndex() {
        return trackIndex;
    }

    public void setTrackIndex(int value) {
        trackIndex = value;
    }

    //----------------------------------
    //  phraseId
    //----------------------------------

    private UUID phraseId;

    /**
     * Returns the {@link LibraryPhrase} id that was copied when the channel had
     * its phrase assigned.
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
     * 0-3, (A-D).
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
     * located 0-15, (1-16).
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

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public TrackItem() {
    }

    /**
     * Returns whether the measure passed is located on the start or within this
     * item.
     * <p>
     * Will return false if the measure is equal to the end measure of this
     * item. This means the measure starts when this items stops, no overlap.
     * 
     * @param measure The measure to check containment from start to end of this
     *            item.
     */
    public boolean contains(int measure) {
        int start = getStartMeasure();
        int end = getEndMeasure();
        if (measure >= start && measure < end)
            return true;
        return false;
    }

    //--------------------------------------------------------------------------
    // ISerialize API
    //--------------------------------------------------------------------------

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
    }

    @Override
    public String toString() {
        return "Track" + trackIndex + "[" + startMeasure + "|"
                + PatternUtils.toString(bankIndex, patternIndex) + "|" + endMeasure + "]";
    }

}
