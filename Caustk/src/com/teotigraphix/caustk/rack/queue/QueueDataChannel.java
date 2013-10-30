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

package com.teotigraphix.caustk.rack.queue;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.track.Phrase;
import com.teotigraphix.caustk.utils.PatternUtils;

/*

14 machines

14 * 4 * 16 = 896 possible patterns

A channel is basically a concrete pattern_sequencer location within a machine.

64 possible pattern locations in a machine, we have 64 pads
- each QueueData bank & localIndex DIRECTLY corresponds to the machines pattern bank

- A channel is assigned a phraseId

Channel;
 - tone index
 - sequencer bank index
 - sequencer pattern index

*/

public class QueueDataChannel {

    @Tag(0)
    private QueueData queueData;

    @Tag(1)
    private int toneIndex;

    @Tag(2)
    private boolean enabled;

    @Tag(3)
    private boolean loopEnabled = true;

    @Tag(4)
    private UUID phraseId;

    //----------------------------------
    // parent
    //----------------------------------

    /**
     * Returns the owner.
     */
    public QueueData getQueueData() {
        return queueData;
    }

    void setQueueData(QueueData value) {
        queueData = value;
    }

    //----------------------------------
    // toneIndex
    //----------------------------------

    public final Tone getTone() {
        return getQueueData().getRack().getSoundSource().getTone(toneIndex);
    }

    /**
     * Returns the index of the channel within it's {@link QueueData} stack.
     * <p>
     * This index represents the tone index assigned to the channel. Which is a
     * machine in the rack.
     */
    public int getToneIndex() {
        return toneIndex;
    }

    //----------------------------------
    // bankIndex
    //----------------------------------

    /**
     * Returns the pattern sequencer bank index this channel is assigned to.
     */
    public final int getBankIndex() {
        return queueData.getBankIndex();
    }

    //----------------------------------
    // patternIndex
    //----------------------------------

    /**
     * Returns the pattern sequencer pattern index this channel is assigned to.
     */
    public final int getPatternIndex() {
        return queueData.getPatternIndex();
    }

    //----------------------------------
    // enabled
    //----------------------------------

    /**
     * Returns the id of the {@link LibraryPhrase} that initialized this
     * channel.
     */
    public UUID getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(UUID value) {
        phraseId = value;
    }

    //----------------------------------
    // enabled
    //----------------------------------

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    //----------------------------------
    // currentBeat 
    //----------------------------------

    public int getCurrentBeat() {
        // XXX        return getQueueData().getRack().getTrackSequencer().getTrack(toneIndex).getPhrase()
        //                .getCurrentBeat();
        return -1;
    }

    //----------------------------------
    // loopEnabled
    //----------------------------------

    public boolean isLoopEnabled() {
        return loopEnabled;
    }

    public void setLoopEnabled(boolean value) {
        loopEnabled = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    QueueDataChannel() {
    }

    /**
     * @param toneIndex The tone index.
     */
    public QueueDataChannel(int toneIndex) {
        this.toneIndex = toneIndex;
    }

    public void assignPhrase(Phrase phrase) {
        setPhraseId(phrase.getPhraseId());
    }

    public void unassignPhrase() {
        setPhraseId(null);
    }

    @SuppressWarnings("unused")
    private void clearPhrase() {
        //        if (channelPhrase != null) {
        //            // remove note data from machines pattern sequencer
        //            getTone().getPatternSequencer().clearIndex(getBankIndex(), getPatternIndex());
        //        }
        //        channelPhrase = null;
    }

    public final String getPatternName() {
        return PatternUtils.toString(getBankIndex(), getPatternIndex());
    }

    @Override
    public String toString() {
        return "Channel[" + getToneIndex() + ":" + getPatternName() + "]";
    }

}
