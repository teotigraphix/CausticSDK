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

package com.teotigraphix.caustk.node.machine.patch.pcmsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.PCMSynthMessage;
import com.teotigraphix.caustk.core.osc.PCMSynthMessage.PlayMode;
import com.teotigraphix.caustk.node.NodeBase;
import com.teotigraphix.caustk.node.NodeBaseEvents.NodeEvent;
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;

import java.io.File;
import java.util.TreeMap;

/**
 * The {@link PCMSynthMachine#getSampler()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PCMSamplerComponent extends MachineComponent {

    private static final int NUM_SAMPLER_CHANNELS = 64;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int activeIndex = 0;

    @Tag(101)
    private TreeMap<Integer, PCMSamplerChannel> channels = new TreeMap<Integer, PCMSamplerChannel>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PCMSynthMessage#QUERY_SAMPLE_INDICIES
     */
    public String querySampleIndicies() {
        return PCMSynthMessage.QUERY_SAMPLE_INDICIES.queryString(getRack(), getMachineIndex());
    }

    //----------------------------------
    // activeIndex
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.PCMSynthMessage#SAMPLE_INDEX
     */
    public int getActiveIndex() {
        return activeIndex;
    }

    public int queryActiveIndex() {
        return (int)PCMSynthMessage.SAMPLE_INDEX.query(getRack(), getMachineIndex());
    }

    /**
     * @param activeIndex (0..63)
     * @see com.teotigraphix.caustk.core.osc.PCMSynthMessage#SAMPLE_INDEX
     */
    public void setActiveIndex(int activeIndex) {
        //        if (activeIndex == this.activeIndex)
        //            return;
        if (activeIndex < 0 || activeIndex >= NUM_SAMPLER_CHANNELS)
            throw newRangeException(PCMSynthMessage.SAMPLE_INDEX, "0..63", activeIndex);

        this.activeIndex = activeIndex;
        PCMSynthMessage.SAMPLE_INDEX.send(getRack(), getMachineIndex(), activeIndex);
    }

    //----------------------------------
    // activeChannel
    //----------------------------------

    /**
     * Returns the active {@link PCMSamplerChannel}.
     * 
     * @see #getActiveIndex()
     */
    public final PCMSamplerChannel getActiveChannel() {
        return getPCMSample(activeIndex);
    }

    /**
     * @param channel (0..63)
     * @see com.teotigraphix.caustk.core.osc.PCMSynthMessage#QUERY_SAMPLE_NAME
     */
    public final String _querySampleName(int channel) {
        return PCMSynthMessage.QUERY_SAMPLE_NAME.queryString(getRack(), getMachineIndex(), channel);
    }

    public final String getSampleName(int channel) {
        PCMSamplerChannel activeChannel = getPCMSample(channel);
        if (activeChannel != null)
            return activeChannel.getName();
        return null;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PCMSamplerComponent() {
    }

    public PCMSamplerComponent(MachineNode machineNode) {
        super(machineNode);
    }

    //--------------------------------------------------------------------------
    // API :: Methods
    //--------------------------------------------------------------------------

    /**
     * Sets the sample channel's start and end points.
     * 
     * @param channel The sample channel index (0..63).
     * @param start The start sample.
     * @param end The end sample/
     */
    public void setChannelSamplePoints(int channel, int start, int end) {
        setActiveIndex(channel);
        getActiveChannel().setStart(start);
        getActiveChannel().setEnd(end);
    }

    /**
     * Sets the sample channel's low, high and root keys.
     * 
     * @param channel The sample channel index (0..63).
     * @param low The sample's low key highKey (24..108).
     * @param high The sample's high key highKey (24..108).
     * @param root The sample's root key highKey (24..108).
     */
    public void setChannelKeys(int channel, int low, int high, int root) {
        setActiveIndex(channel);
        getActiveChannel().setLowKey(low);
        getActiveChannel().setHighKey(high);
        getActiveChannel().setRootKey(root);
    }

    /**
     * Sets the sample channel's level, tune and mode.
     * 
     * @param channel The sample channel index (0..63).
     * @param level The sample's level value (0.0..1.0).
     * @param tune The sample's level value (-50..50).
     * @param mode The sample's mode value
     *            {@link com.teotigraphix.caustk.core.osc.PCMSynthMessage.PlayMode}
     *            .
     */
    public void setChannelProperties(int channel, float level, int tune, PlayMode mode) {
        setActiveIndex(channel);
        getActiveChannel().setLevel(level);
        getActiveChannel().setTune(tune);
        getActiveChannel().setMode(mode);
    }

    /**
     * Loads a <strong>wav</strong> sample in the the sample channel index.
     * 
     * @param index The sample channel index (0..63).
     * @param wavFile The wav file, absolute location.
     * @return The loaded {@link PCMSamplerChannel}
     * @see com.teotigraphix.caustk.node.machine.patch.pcmsynth.PCMSamplerComponent.PCMSamplerRefreshEvent
     */
    public PCMSamplerChannel loadChannel(int index, File wavFile) {
        setActiveIndex(index);
        PCMSamplerChannel result = getActiveChannel();
        loadSample(wavFile);
        result.restore();
        post(new PCMSamplerRefreshEvent(this));
        return result;
    }

    //--------------------------------------------------------------------------
    // Private :: Methods
    //--------------------------------------------------------------------------

    protected void loadSample(File wavFile) {
        PCMSynthMessage.SAMPLE_LOAD.send(getRack(), getMachineIndex(), wavFile.getAbsolutePath());
    }

    protected final PCMSamplerChannel findPCMSample(int index) {
        return channels.get(index);
    }

    protected final PCMSamplerChannel getPCMSample(int index) {
        PCMSamplerChannel channel = channels.get(index);
        if (channel == null) {
            channel = new PCMSamplerChannel(getMachineNode(), index);
            channels.put(index, channel);
        }
        return channel;
    }

    //--------------------------------------------------------------------------
    // Overridden Protected :: Methods
    //--------------------------------------------------------------------------

    @Override
    protected void createComponents() {
    }

    @Override
    protected void destroyComponents() {
    }

    @Override
    protected void updateComponents() {
        // TODO figure out how to implement PCMSamplerComponent#update()
        // XXX Is update() really relevant to a machine that is so dependent
        // on samples like the PCMSynth?
        // I guess it would be the 'path' to wav that would be saved in
        // the channel and then reloaded, but I am not worried about this
        // in the first version
    }

    @Override
    protected void restoreComponents() {
        // the OSC for the sampler is kindof weird in that you have to
        // set the active index first and then issue commands. During the
        // restore, this index gets changed, we need to put it back where it was
        int old = getActiveIndex();
        String indicies = querySampleIndicies();
        if (indicies == null || indicies.equals(""))
            return;

        String[] split = indicies.split(" ");
        for (int i = 0; i < split.length; i++) {
            int index = Integer.parseInt(split[i]);
            // XXX temp set active index, Rej should be fixing this
            setActiveIndex(index);
            PCMSamplerChannel channel = getPCMSample(index);
            channel.restore();
        }
        setActiveIndex(old);
    }

    public static class PCMSamplerRefreshEvent extends NodeEvent {
        public PCMSamplerRefreshEvent(NodeBase target) {
            super(target);
        }
    }
}
