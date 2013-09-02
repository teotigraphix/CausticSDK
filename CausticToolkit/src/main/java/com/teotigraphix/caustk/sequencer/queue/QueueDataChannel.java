
package com.teotigraphix.caustk.sequencer.queue;

import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.pattern.PatternUtils;
import com.teotigraphix.caustk.sequencer.track.TrackChannel;
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
    // libraryId
    //----------------------------------

    private UUID ownerLibraryId;

    public UUID getOwnerLibraryId() {
        return ownerLibraryId;
    }

    //----------------------------------
    // phraseId
    //----------------------------------

    private UUID ownerPhraseId;

    public UUID getOwnerPhraseId() {
        return ownerPhraseId;
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

    public void assignPhrase(LibraryPhrase phrase) {
        int index = getToneIndex();
        TrackChannel track = controller.getTrackSequencer().getTrack(index);
        TrackPhrase trackPhrase = track.getPhrase(getBankIndex(), getPatternIndex());

        trackPhrase.setNoteData(phrase.getNoteData());
        trackPhrase.setLength(phrase.getLength());

        //        if (phrase == null) {
        //            clearPhrase();
        //            return;
        //        }
        //        // save original the libraryId and phraseId if ever this
        //        // channel wants to try and reset its phrase data
        //        // but we copy the whole thing so there is no dependencies
        //        // between this project and library since they could get disconnected
        //        ownerLibraryId = phrase.getLibrary().getId();
        //        ownerPhraseId = phrase.getId();
        //        LibraryPhrase newPhrase = controller.getSerializeService()
        //                .copy(phrase, LibraryPhrase.class);
        //
        //        // update the decoration to hold our position
        //        // the original ID from the library can always get the original bank/pattern locations
        //        newPhrase.setId(UUID.randomUUID());
        //        newPhrase.setBankIndex(getBankIndex());
        //        newPhrase.setPatternIndex(getPatternIndex());
        //
        //        //        channelPhrase = new ChannelPhrase(newPhrase);
        //        //
        //        assignNoteData(phrase);
    }

    private void assignNoteData(LibraryPhrase channelPhrase) {
        if (getTone() == null)
            return;

        String data = channelPhrase.getNoteData();
        int oldBank = getTone().getPatternSequencer().getSelectedBank();
        int oldPattern = getTone().getPatternSequencer().getSelectedPattern();
        getTone().getPatternSequencer().setSelectedBankPattern(getBankIndex(), getPatternIndex());
        getTone().getPatternSequencer().setLength(channelPhrase.getLength());
        getTone().getPatternSequencer().assignNoteData(data);
        getTone().getPatternSequencer().setSelectedBankPattern(oldBank, oldPattern);
    }

    private void clearPhrase() {
        ownerLibraryId = null;
        ownerPhraseId = null;
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
