
package com.teotigraphix.caustk.sequencer.queue;

import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.pattern.PatternUtils;
import com.teotigraphix.caustk.sequencer.track.TrackPhrase;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;

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

public class QueueDataChannel implements ISerialize {

    private transient ICaustkController controller;

    //----------------------------------
    // parent
    //----------------------------------

    private transient QueueData parent;

    /**
     * Returns the owner.
     */
    public QueueData getParent() {
        return parent;
    }

    void setParent(QueueData value) {
        parent = value;
    }

    //----------------------------------
    // toneIndex
    //----------------------------------

    public final Tone getTone() {
        return controller.getSoundSource().getTone(toneIndex);
    }

    private int toneIndex;

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
        return parent.getBankIndex();
    }

    //----------------------------------
    // patternIndex
    //----------------------------------

    /**
     * Returns the pattern sequencer pattern index this channel is assigned to.
     */
    public final int getPatternIndex() {
        return parent.getPatternIndex();
    }

    //----------------------------------
    // enabled
    //----------------------------------

    private UUID phraseId;

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

    private boolean enabled;

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
        return controller.getTrackSequencer().getTrack(toneIndex).getPhrase().getCurrentBeat();
    }

    //----------------------------------
    // loopEnabled
    //----------------------------------

    private boolean loopEnabled = true;

    public boolean isLoopEnabled() {
        return loopEnabled;
    }

    public void setLoopEnabled(boolean value) {
        loopEnabled = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * @param toneIndex The tone index.
     */
    public QueueDataChannel(int toneIndex) {
        this.toneIndex = toneIndex;
    }

    //--------------------------------------------------------------------------
    // ISerialize API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
    }

    public void assignPhrase(TrackPhrase phrase) {
        setPhraseId(phrase.getPhraseId());
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
