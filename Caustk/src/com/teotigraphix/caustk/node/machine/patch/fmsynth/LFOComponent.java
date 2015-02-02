////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.node.machine.patch.fmsynth;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.FMSynthMessage;
import com.teotigraphix.caustk.node.machine.MachineChannel;
import com.teotigraphix.caustk.node.machine.Machine;

/**
 * The fm lfo component.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class LFOComponent extends MachineChannel {

    // LFO_A1, LFO_A2, LFO_A3
    // LFO_F1, LFO_F2, LFO_F3
    // LFO_AOUT

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private int rate;

    @Tag(101)
    private float depth;

    @Tag(102)
    private boolean[] amplitudes = new boolean[3];

    @Tag(103)
    private boolean[] frequencies = new boolean[3];

    @Tag(104)
    private boolean output;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rate
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_RATE
     */
    public int getRate() {
        return rate;
    }

    public int queryRate() {
        return (int)FMSynthMessage.LFO_RATE.query(getRack(), getMachineIndex());
    }

    /**
     * @param rate (0..12)
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_RATE
     */
    public void setRate(int rate) {
        if (rate == this.rate)
            return;
        if (rate < 0 || rate > 12)
            throw newRangeException(FMSynthMessage.LFO_RATE, "0..12", rate);
        this.rate = rate;
        FMSynthMessage.LFO_RATE.send(getRack(), getMachineIndex(), rate);
    }

    //----------------------------------
    // depth
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_DEPTH
     */
    public float getDepth() {
        return depth;
    }

    public float queryDepth() {
        return FMSynthMessage.LFO_DEPTH.query(getRack(), getMachineIndex());
    }

    /**
     * @param depth (0.0..1.0)
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_DEPTH
     */
    public void setDepth(float depth) {
        if (depth == this.depth)
            return;
        if (depth < 0 || depth > 1f)
            throw newRangeException(FMSynthMessage.LFO_DEPTH, "0..1", depth);
        this.depth = depth;
        FMSynthMessage.LFO_DEPTH.send(getRack(), getMachineIndex(), depth);
    }

    //----------------------------------
    // apmlitudes
    //----------------------------------

    /**
     * @param index (0,1,2)
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_F1
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_F2
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_F3
     */
    public boolean isFrequencyEnabled(int index) {
        return frequencies[index];
    }

    public boolean queryFrequencyEnabled(int index) {
        FMSynthMessage message = null;
        switch (index) {
            case 0:
                message = FMSynthMessage.LFO_F1;
                break;
            case 1:
                message = FMSynthMessage.LFO_F2;
                break;
            case 2:
                message = FMSynthMessage.LFO_F3;
                break;
        }
        return message.query(getRack(), getMachineIndex()) == 0f ? false : true;
    }

    /**
     * @param index (0,1,2)
     * @param enabled
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_F1
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_F2
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_F3
     */
    public void setFrequencyEnabled(int index, boolean enabled) {
        //        if (enabled == frequencies[index])
        //            return;
        frequencies[index] = enabled;
        FMSynthMessage message = null;
        switch (index) {
            case 0:
                message = FMSynthMessage.LFO_F1;
                break;
            case 1:
                message = FMSynthMessage.LFO_F2;
                break;
            case 2:
                message = FMSynthMessage.LFO_F3;
                break;
        }
        message.send(getRack(), getMachineIndex(), enabled ? 1 : 0);
    }

    //----------------------------------
    // apmlitudes
    //----------------------------------

    /**
     * @param index (0,1,2)
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_A1
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_A2
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_A3
     */
    public boolean isAmplitudeEnabled(int index) {
        return amplitudes[index];
    }

    public boolean queryAmplitudeEnabled(int index) {
        FMSynthMessage message = null;
        switch (index) {
            case 0:
                message = FMSynthMessage.LFO_A1;
                break;
            case 1:
                message = FMSynthMessage.LFO_A2;
                break;
            case 2:
                message = FMSynthMessage.LFO_A3;
                break;
        }
        return message.query(getRack(), getMachineIndex()) == 0f ? false : true;
    }

    /**
     * @param index (0,1,2)
     * @param enabled
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_A1
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_A2
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_A3
     */
    public void setAmplitudeEnabled(int index, boolean enabled) {
        //        if (enabled == apmlitudes[index])
        //            return;
        amplitudes[index] = enabled;
        FMSynthMessage message = null;
        switch (index) {
            case 0:
                message = FMSynthMessage.LFO_A1;
                break;
            case 1:
                message = FMSynthMessage.LFO_A2;
                break;
            case 2:
                message = FMSynthMessage.LFO_A3;
                break;
        }
        message.send(getRack(), getMachineIndex(), enabled ? 1 : 0);
    }

    //----------------------------------
    // output
    //----------------------------------

    /**
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_AOUT
     */
    public boolean isOutputEnabled() {
        return output;
    }

    public boolean queryOutputEnabled() {
        return FMSynthMessage.LFO_AOUT.query(getRack(), getMachineIndex()) == 0f ? false : true;
    }

    /**
     * @param enabled (true|false)
     * @see com.teotigraphix.caustk.core.osc.FMSynthMessage#LFO_AOUT
     */
    public void setOutputEnabled(boolean output) {
        if (output == this.output)
            return;
        this.output = output;
        FMSynthMessage.LFO_AOUT.send(getRack(), getMachineIndex(), output ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    public LFOComponent() {
    }

    public LFOComponent(Machine machineNode) {
        super(machineNode);
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
        FMSynthMessage.LFO_RATE.send(getRack(), getMachineIndex(), rate);
        FMSynthMessage.LFO_DEPTH.send(getRack(), getMachineIndex(), depth);
        for (int i = 0; i < 3; i++) {
            setAmplitudeEnabled(i, amplitudes[i]);
            setFrequencyEnabled(i, frequencies[i]);
        }
        FMSynthMessage.LFO_AOUT.send(getRack(), getMachineIndex(), output ? 1 : 0);
    }

    @Override
    protected void restoreComponents() {
        setRate(queryRate());
        setDepth(queryDepth());
        for (int i = 0; i < 3; i++) {
            setAmplitudeEnabled(i, queryAmplitudeEnabled(i));
            setFrequencyEnabled(i, queryFrequencyEnabled(i));
        }
        setOutputEnabled(queryOutputEnabled());
    }
}
