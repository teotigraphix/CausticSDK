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

package com.teotigraphix.caustk.rack.mixer;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.ICaustkApplicationContext;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.CausticMessage;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;

/**
 * @author Michael Schmalle
 */
public class MasterEqualizer extends RackMasterComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private float bass = 1.1f;

    @Tag(101)
    private float bassMidFreq = 0.5f;

    @Tag(102)
    private float mid = 1f;

    @Tag(103)
    private float midHighFreq = 0.5f;

    @Tag(104)
    private float high = 1.1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // bass
    //----------------------------------

    public float getBass() {
        return bass;
    }

    float getBass(boolean restore) {
        return MasterMixerMessage.EQ_BASS.query(getRack());
    }

    public void setBass(float value) {
        if (bass == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("bass", "0..2", value);
        bass = value;
        MasterMixerMessage.EQ_BASS.send(getRack(), value);
    }

    //----------------------------------
    // bassMidFreq
    //----------------------------------

    public float getBassMidFreq() {
        return bassMidFreq;
    }

    float getBassMidFreq(boolean restore) {
        return MasterMixerMessage.EQ_BASSMID_FREQ.query(getRack());
    }

    public void setBassMidFreq(float value) {
        if (bassMidFreq == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("bassmid_freq", "0..1", value);
        bassMidFreq = value;
        MasterMixerMessage.EQ_BASSMID_FREQ.send(getRack(), value);
    }

    //----------------------------------
    // mid
    //----------------------------------

    public float getMid() {
        return mid;
    }

    float getMid(boolean restore) {
        return MasterMixerMessage.EQ_MID.query(getRack());
    }

    public void setMid(float value) {
        if (mid == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("mid ", "0..2", value);
        mid = value;
        MasterMixerMessage.EQ_MID.send(getRack(), value);
    }

    //----------------------------------
    // midHighFreq
    //----------------------------------

    public float getMidHighFreq() {
        return midHighFreq;
    }

    float getMidHighFreq(boolean restore) {
        return MasterMixerMessage.EQ_MIDHIGH_FREQ.query(getRack());
    }

    public void setMidHighFreq(float value) {
        if (midHighFreq == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("midhigh_freq ", "0..1", value);
        midHighFreq = value;
        MasterMixerMessage.EQ_MIDHIGH_FREQ.send(getRack(), value);
    }

    //----------------------------------
    // high
    //----------------------------------

    public float getHigh() {
        return high;
    }

    float getHigh(boolean restore) {
        return MasterMixerMessage.EQ_HIGH.query(getRack());
    }

    public void setHigh(float value) {
        if (high == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("high ", "0..2", value);
        high = value;
        MasterMixerMessage.EQ_HIGH.send(getRack(), value);
    }

    @Override
    CausticMessage getBypassMessage() {
        return MasterMixerMessage.EQ_BYPASS;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterEqualizer() {
    }

    @Override
    protected void componentPhaseChange(ICaustkApplicationContext context, ComponentPhase phase)
            throws CausticException {
        super.componentPhaseChange(context, phase);
        switch (phase) {
            case Update:
                MasterMixerMessage.EQ_BASS.send(getRack(), bass);
                MasterMixerMessage.EQ_BASSMID_FREQ.send(getRack(), bassMidFreq);
                MasterMixerMessage.EQ_HIGH.send(getRack(), high);
                MasterMixerMessage.EQ_MID.send(getRack(), mid);
                MasterMixerMessage.EQ_MIDHIGH_FREQ.send(getRack(), midHighFreq);
                break;

            case Restore:
                setBass(getBass(true));
                setBassMidFreq(getBassMidFreq(true));
                setHigh(getHigh(true));
                setMid(getMid(true));
                setMidHighFreq(getMidHighFreq(true));
                break;

            default:
                break;
        }
    }
}
