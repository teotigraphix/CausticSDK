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

package com.teotigraphix.caustk.sequencer.queue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.sequencer.track.Phrase;
import com.teotigraphix.caustk.sequencer.track.Track;

public class QueueData implements Serializable {

    private static final long serialVersionUID = 8360462328524344204L;

    private Map<Integer, QueueDataChannel> map = new TreeMap<Integer, QueueDataChannel>();

    //----------------------------------
    // queueSong
    //----------------------------------

    private QueueSong queueSong;

    public QueueSong getQueueSong() {
        return queueSong;
    }

    public void setQueueSong(QueueSong value) {
        queueSong = value;
    }

    public IRack getRack() {
        return queueSong.getRack();
    }

    //----------------------------------
    // name
    //----------------------------------

    private String name;

    /**
     * Returns the human readable name of the data the was explicitly set.
     * <p>
     * Can be <code>null</code> if {@link #setName(String)} has not be called.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the human readable name of the data used as a label in the UI.
     * 
     * @param name The String name.
     */
    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------
    // bankIndex
    //----------------------------------

    private final int bankIndex;

    public final int getBankIndex() {
        return bankIndex;
    }

    //----------------------------------
    // patternIndex
    //----------------------------------

    private final int patternIndex;

    public final int getPatternIndex() {
        return patternIndex;
    }

    //----------------------------------
    // state
    //----------------------------------

    private QueueDataState state = QueueDataState.Idle;

    public final QueueDataState getState() {
        return state;
    }

    public void setState(QueueDataState value) {
        state = value;
    }

    //----------------------------------
    // viewChannel
    //----------------------------------

    private int viewChannelIndex = -1;

    /**
     * Returns the {@link QueueDataChannel} index that is considered the top
     * view.
     */
    public int getViewChannelIndex() {
        return viewChannelIndex;
    }

    public void setViewChannelIndex(int value) {
        viewChannelIndex = value;
    }

    public QueueDataChannel getViewChannel() {
        return getChannel(viewChannelIndex);
    }

    //----------------------------------
    // channel
    //----------------------------------

    /**
     * Returns whether the pad contains a {@link QueueDataChannel} at the tone
     * index.
     * 
     * @param toneIndex The toneIndex to test.
     */
    public boolean hasChannel(int toneIndex) {
        return map.containsKey(toneIndex);
    }

    public boolean hasChannels() {
        return map.size() > 0;
    }

    /**
     * Returns a {@link QueueDataChannel} at the tone index, this method will
     * create the channel if it does not exist.
     * 
     * @param toneIndex The toneIndex to retrieve.
     */
    public QueueDataChannel getChannel(int toneIndex) {
        QueueDataChannel channel = map.get(toneIndex);
        if (channel == null) {
            channel = new QueueDataChannel(toneIndex);
            channel.setQueueData(this);
            map.put(toneIndex, channel);
        }
        return channel;
    }

    public Collection<QueueDataChannel> getChannels() {
        return new ArrayList<QueueDataChannel>(map.values());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public QueueData(int bankIndex, int patternIndex) {
        this.bankIndex = bankIndex;
        this.patternIndex = patternIndex;
    }

    /**
     * Returns the {@link Phrase} for the {@link #getViewChannelIndex()},
     * <code>null</code> if the view channel has not been assigned (-1).
     * <p>
     * Uses the {@link #getBankIndex()} and {@link #getPatternIndex()} to find
     * the corresponding {@link Phrase} in the owner {@link Track}.
     */
    public Phrase getPhrase() {
        if (viewChannelIndex == -1)
            return null;
        return getPhrase(viewChannelIndex);
    }

    /**
     * Returns the {@link Phrase} for the toneIndex channel.
     * <p>
     * Uses the {@link #getBankIndex()} and {@link #getPatternIndex()} to find
     * the corresponding {@link Phrase} in the owner {@link Track}.
     * 
     * @param toneIndex The index to retrieve this data's {@link Phrase}.
     */
    public Phrase getPhrase(int toneIndex) {
        Track track = getQueueSong().getRack().getTrackSequencer().getTrack(toneIndex);
        return track.getPhrase(bankIndex, patternIndex);
    }

    @Override
    public String toString() {
        return "Data{" + state + "}[" + bankIndex + "," + patternIndex + "]";
    }

    public enum QueueDataState {
        Idle,

        Play,

        PlayUnqueued,

        Queue,

        UnQueued;
    }

    private transient QueueData theInvalidatedData;

    public QueueData getTheInvalidatedData() {
        return theInvalidatedData;
    }

    public void setTheInvalidatedData(QueueData theInvalidatedData) {
        this.theInvalidatedData = theInvalidatedData;
    }

    private transient QueueData dataThatInvalidated;

    public QueueData getDataThatInvalidated() {
        return dataThatInvalidated;
    }

    public void setDataThatInvalidated(QueueData data) {
        dataThatInvalidated = data;
    }

}
