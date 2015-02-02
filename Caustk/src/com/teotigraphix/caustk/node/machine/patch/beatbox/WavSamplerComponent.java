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

package com.teotigraphix.caustk.node.machine.patch.beatbox;

import java.io.File;
import java.util.TreeMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.BeatboxMessage;
import com.teotigraphix.caustk.node.machine.BeatBoxMachine;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;

/**
 * The {@link BeatBoxMachine} sampler component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see BeatBoxMachine#getSampler()
 */
public class WavSamplerComponent extends MachineChannel {

    private static final int NUM_CHANNELS = 8;

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private TreeMap<Integer, WavSamplerChannel> channels = new TreeMap<Integer, WavSamplerChannel>();

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public WavSamplerComponent() {
    }

    public WavSamplerComponent(MachineNode machineNode) {
        super(machineNode);
        for (int i = 0; i < NUM_CHANNELS; i++) {
            WavSamplerChannel channel = new WavSamplerChannel(machineNode, i);
            channels.put(i, channel);
        }
    }

    //--------------------------------------------------------------------------
    // API :: Methods
    //--------------------------------------------------------------------------

    /**
     * @param channel (0..7)
     * @see com.teotigraphix.caustk.core.osc.BeatboxMessage#QUERY_CHANNEL_SAMPLE_NAME
     */
    public String querySampleName(int channel) {
        return BeatboxMessage.QUERY_CHANNEL_SAMPLE_NAME.queryString(getRack(), getMachineIndex(),
                channel);
    }

    /**
     * Returns the sampler channel for the index.
     * 
     * @param index The sample index (0..7).
     */
    public WavSamplerChannel getChannel(int index) {
        return channels.get(index);
    }

    /**
     * Loads a wav sample into the sampler's channel.
     * <p>
     * {@link WavSamplerChannel#restore()} is called after the sample is loaded
     * from disk, which validates all values on the channel instance.
     * 
     * @param index The sample index (0..7).
     * @param wavFile The wave file, absolute location.
     * @return The loaded {@link WavSamplerChannel}.
     */
    public WavSamplerChannel loadChannel(int index, File wavFile) {
        BeatboxMessage.CHANNEL_LOAD.send(getRack(), getMachineIndex(), index,
                wavFile.getAbsolutePath());
        WavSamplerChannel channel = getChannel(index);
        channel.restore();
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
        // XXX Same problem here as the PCMSynth, it relies on wavs, so there would
        // need to be paths to wav files that could be loaded
    }

    @Override
    protected void restoreComponents() {
        for (WavSamplerChannel channel : channels.values()) {
            channel.restore();
        }
    }
}
