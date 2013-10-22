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

package com.teotigraphix.caustk.rack.track;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.utils.PatternUtils;

/**
 * A {@link TrackItem} is a value object that is contained in a {@link Track}.
 * <p>
 * A new track item is created for every span of a {@link Phrase} inserted into
 * the Track with {@link Track#addPhrase(int, Phrase)}.
 */
public class TrackItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private int trackIndex;

    @Tag(1)
    private int bankIndex;

    @Tag(2)
    private int patternIndex;

    @Tag(3)
    private int startMeasure;

    @Tag(4)
    private int endMeasure;

    @Tag(5)
    private int numMeasures;

    @Tag(6)
    private int numLoops;

    @Tag(7)
    private UUID phraseId;

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    //  trackIndex
    //----------------------------------

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
    //  bankIndex
    //----------------------------------

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

    /**
     * The measure at which this item ends in the song sequencer.
     */
    public final int getEndMeasure() {
        return endMeasure;
    }

    public final void setEndMeasure(int value) {
        endMeasure = value;
    }

    //----------------------------------
    //  numMeasures
    //----------------------------------

    public int getNumMeasures() {
        return numMeasures;
    }

    public void setNumMeasures(int value) {
        numMeasures = value;
    }

    //----------------------------------
    //  length
    //----------------------------------

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

    //----------------------------------
    //  phraseId
    //----------------------------------

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

    @Override
    public String toString() {
        return "Track" + trackIndex + "[" + startMeasure + "|"
                + PatternUtils.toString(bankIndex, patternIndex) + "|" + endMeasure + "]";
    }

}
