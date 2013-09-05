
package com.teotigraphix.caustk.sequencer.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;

public class QueueData implements ISerialize {

    private transient ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    private Map<Integer, QueueDataChannel> map = new TreeMap<Integer, QueueDataChannel>();

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
            channel.setParent(this);
            channel.wakeup(controller);
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

    //--------------------------------------------------------------------------
    // ISerialize API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void sleep() {
        //if (state == QueueDataState.Queued)
        //    state = QueueDataState.Idle;
        for (QueueDataChannel channel : map.values()) {
            channel.sleep();
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (QueueDataChannel channel : map.values()) {
            channel.setParent(this);
            channel.wakeup(controller);
        }
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

}
