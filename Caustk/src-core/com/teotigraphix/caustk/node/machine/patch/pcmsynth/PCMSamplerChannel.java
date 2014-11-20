////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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
import com.teotigraphix.caustk.node.machine.MachineComponent;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;

/**
 * The {@link PCMSamplerComponent#getActiveChannel()} component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class PCMSamplerChannel extends MachineComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int channelIndex;

    @Tag(101)
    private String name;

    @Tag(102)
    private float level = 1.0f;

    @Tag(103)
    private float pan = 0.5f;

    @Tag(104)
    private int tune;

    @Tag(105)
    private int rootKey;

    @Tag(106)
    private int lowKey;

    @Tag(107)
    private int highKey;

    @Tag(108)
    private PlayMode mode = PlayMode.PLAY_ONCE;

    @Tag(109)
    private int start;

    @Tag(110)
    private int end;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the index of the channel within it's parent
     * {@link PCMSamplerComponent}.
     */
    public final int getChannelIndex() {
        return channelIndex;
    }

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns whether the channel has a sample, start and end == 0.
     */
    public boolean hasSample() {
        return start == 0 && end == 0;
    }

    /**
     * Returns the name of the channel's sample.
     */
    public final String getName() {
        if (name == null)
            name = queryName();
        return name;
    }

    public String queryName() {
        return PCMSynthMessage.QUERY_SAMPLE_NAME.queryString(getRack(), getMachineIndex());
    }

    /**
     * Sets the name of the sample.
     * 
     * @param value Sample name.
     */
    public final void setName(String value) {
        name = value;
    }

    //----------------------------------
    // level
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_LEVEL
     */
    public final float getLevel() {
        return level;
    }

    public float queryLevel() {
        return PCMSynthMessage.SAMPLE_LEVEL.query(getRack(), getMachineIndex());
    }

    /**
     * @param level (0.0..1.0)
     * @see PCMSynthMessage#SAMPLE_LEVEL
     */
    public final void setLevel(float level) {
        if (level == this.level)
            return;
        // XXX UNIT TEST had value of 1.2
        //        if (level < 0f || level > 1f)
        //            throw newRangeException(PCMSynthMessage.SAMPLE_LEVEL, "0..1", level);
        this.level = level;
        PCMSynthMessage.SAMPLE_LEVEL.send(getRack(), getMachineIndex(), level);
    }

    //----------------------------------
    // pan
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_PAN
     */
    public final float getPan() {
        return pan;
    }

    public float queryPan() {
        return PCMSynthMessage.SAMPLE_PAN.query(getRack(), getMachineIndex());
    }

    /**
     * @param pan (0.0..1.0)
     * @see PCMSynthMessage#SAMPLE_PAN
     */
    public final void setPan(float pan) {
        if (pan == this.pan)
            return;
        if (pan < 0f || pan > 1f)
            throw newRangeException(PCMSynthMessage.SAMPLE_PAN, "0..1", pan);
        this.pan = pan;
        PCMSynthMessage.SAMPLE_PAN.send(getRack(), getMachineIndex(), pan);
    }

    //----------------------------------
    // tune
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_TUNE
     */
    public final int getTune() {
        return tune;
    }

    public int queryTune() {
        return (int)PCMSynthMessage.SAMPLE_TUNE.query(getRack(), getMachineIndex());
    }

    /**
     * @param tune (-50..50)
     * @see PCMSynthMessage#SAMPLE_LEVEL
     */
    public final void setTune(int tune) {
        if (tune == this.tune)
            return;
        if (tune < -50 || tune > 50)
            throw newRangeException(PCMSynthMessage.SAMPLE_TUNE, "-50.0..50.0", tune);
        this.tune = tune;
        PCMSynthMessage.SAMPLE_TUNE.send(getRack(), getMachineIndex(), tune);
    }

    //----------------------------------
    // rootKey
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_ROOTKEY
     */
    public final int getRootKey() {
        return rootKey;
    }

    public int queryRootKey() {
        return (int)PCMSynthMessage.SAMPLE_ROOTKEY.query(getRack(), getMachineIndex());
    }

    /**
     * @param rootKey (24..108)
     * @see PCMSynthMessage#SAMPLE_ROOTKEY
     */
    public final void setRootKey(int rootKey) {
        if (rootKey == this.rootKey)
            return;
        if (rootKey < 24 || rootKey > 108)
            throw newRangeException(PCMSynthMessage.SAMPLE_ROOTKEY, "24..108", rootKey);
        this.rootKey = rootKey;
        PCMSynthMessage.SAMPLE_ROOTKEY.send(getRack(), getMachineIndex(), rootKey);
    }

    //----------------------------------
    // lowKey
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_LOWKEY
     */
    public final int getLowKey() {
        return lowKey;
    }

    public int queryLowKey() {
        return (int)PCMSynthMessage.SAMPLE_LOWKEY.query(getRack(), getMachineIndex());
    }

    /**
     * @param lowKey (24..108)
     * @see PCMSynthMessage#SAMPLE_LOWKEY
     */
    public final void setLowKey(int lowKey) {
        if (lowKey == this.lowKey)
            return;
        if (lowKey < 24 || lowKey > 108)
            throw newRangeException(PCMSynthMessage.SAMPLE_LOWKEY, "24..108", lowKey);
        this.lowKey = lowKey;
        PCMSynthMessage.SAMPLE_LOWKEY.send(getRack(), getMachineIndex(), lowKey);
    }

    //----------------------------------
    // highKey
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_HIGHKEY
     */
    public final int getHighKey() {
        return highKey;
    }

    public int queryHighKey() {
        return (int)PCMSynthMessage.SAMPLE_HIGHKEY.query(getRack(), getMachineIndex());
    }

    /**
     * @param highKey (24..108)
     * @see PCMSynthMessage#SAMPLE_HIGHKEY
     */
    public final void setHighKey(int highKey) {
        if (highKey == this.highKey)
            return;
        if (highKey < 24 || highKey > 108)
            throw newRangeException(PCMSynthMessage.SAMPLE_HIGHKEY, "24..108", highKey);
        this.highKey = highKey;
        PCMSynthMessage.SAMPLE_HIGHKEY.send(getRack(), getMachineIndex(), highKey);
    }

    //----------------------------------
    // mode
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_MODE
     */
    public final PlayMode getMode() {
        return mode;
    }

    public PlayMode queryMode() {
        return PlayMode
                .toType((int)PCMSynthMessage.SAMPLE_MODE.query(getRack(), getMachineIndex()));
    }

    /**
     * @param mode PlayMode
     * @see PCMSynthMessage#SAMPLE_MODE
     */
    public final void setMode(PlayMode mode) {
        if (mode == this.mode)
            return;
        this.mode = mode;
        PCMSynthMessage.SAMPLE_MODE.send(getRack(), getMachineIndex(), mode.getValue());
    }

    //----------------------------------
    // start
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_START
     */
    public final int getStart() {
        return start;
    }

    public int queryStart() {
        return (int)PCMSynthMessage.SAMPLE_START.query(getRack(), getMachineIndex());
    }

    /**
     * @param start Less then {@link #getEnd()}
     * @see PCMSynthMessage#SAMPLE_START
     */
    public final void setStart(int start) {
        if (start == this.start)
            return;
        this.start = start;
        PCMSynthMessage.SAMPLE_START.send(getRack(), getMachineIndex(), start);
    }

    //----------------------------------
    // end
    //----------------------------------

    /**
     * @see PCMSynthMessage#SAMPLE_END
     */
    public final int getEnd() {
        return end;
    }

    public int queryEnd() {
        return (int)PCMSynthMessage.SAMPLE_END.query(getRack(), getMachineIndex());
    }

    /**
     * @param end Sample length (not impl)
     * @see PCMSynthMessage#SAMPLE_END
     */
    public final void setEnd(int end) {
        if (end == this.end)
            return;
        this.end = end;
        PCMSynthMessage.SAMPLE_END.send(getRack(), getMachineIndex(), end);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public PCMSamplerChannel() {
    }

    /**
     * Creates a sampler channel.
     * 
     * @param machineNode The {@link PCMSynthMachine}.
     * @param channelIndex The sample channel index (0..63).
     */
    public PCMSamplerChannel(MachineNode machineNode, int channelIndex) {
        super(machineNode);
        this.channelIndex = channelIndex;
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
        // XXX can't set samples name?

        PCMSynthMessage.SAMPLE_LEVEL.send(getRack(), getMachineIndex(), level);
        PCMSynthMessage.SAMPLE_PAN.send(getRack(), getMachineIndex(), pan);
        PCMSynthMessage.SAMPLE_TUNE.send(getRack(), getMachineIndex(), tune);
        PCMSynthMessage.SAMPLE_ROOTKEY.send(getRack(), getMachineIndex(), rootKey);
        PCMSynthMessage.SAMPLE_LOWKEY.send(getRack(), getMachineIndex(), lowKey);
        PCMSynthMessage.SAMPLE_HIGHKEY.send(getRack(), getMachineIndex(), highKey);
        PCMSynthMessage.SAMPLE_MODE.send(getRack(), getMachineIndex(), mode.getValue());
        PCMSynthMessage.SAMPLE_START.send(getRack(), getMachineIndex(), start);
        PCMSynthMessage.SAMPLE_END.send(getRack(), getMachineIndex(), end);
    }

    @Override
    protected void restoreComponents() {
        // name can be null with presets
        name = queryName();

        setLevel(queryLevel());
        setPan(queryPan());
        setTune(queryTune());
        setRootKey(queryRootKey());
        setLowKey(queryLowKey());
        setHighKey(queryHighKey());
        setMode(queryMode());
        setStart(queryStart());
        setEnd(queryEnd());
    }
}
