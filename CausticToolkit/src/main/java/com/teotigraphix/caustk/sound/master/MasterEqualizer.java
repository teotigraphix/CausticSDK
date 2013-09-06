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

package com.teotigraphix.caustk.sound.master;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;

public class MasterEqualizer extends MasterComponent {

    //--------------------------------------------------------------------------
    // API
    //--------------------------------------------------------------------------

    //----------------------------------
    // bass
    //----------------------------------

    private float bass = 1.1f;

    public float getBass() {
        return bass;
    }

    float getBass(boolean restore) {
        return MasterMixerMessage.EQ_BASS.query(getEngine());
    }

    public void setBass(float value) {
        if (bass == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("bass", "0..2", value);
        bass = value;
        MasterMixerMessage.EQ_BASS.send(getEngine(), value);
    }

    //----------------------------------
    // bassMidFreq
    //----------------------------------

    private float bassMidFreq = 0.5f;

    public float getBassMidFreq() {
        return bassMidFreq;
    }

    float getBassMidFreq(boolean restore) {
        return MasterMixerMessage.EQ_BASSMID_FREQ.query(getEngine());
    }

    public void setBassMidFreq(float value) {
        if (bassMidFreq == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("bassmid_freq", "0..1", value);
        bassMidFreq = value;
        MasterMixerMessage.EQ_BASSMID_FREQ.send(getEngine(), value);
    }

    //----------------------------------
    // mid
    //----------------------------------

    private float mid = 1f;

    public float getMid() {
        return mid;
    }

    float getMid(boolean restore) {
        return MasterMixerMessage.EQ_MID.query(getEngine());
    }

    public void setMid(float value) {
        if (mid == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("mid ", "0..2", value);
        mid = value;
        MasterMixerMessage.EQ_MID.send(getEngine(), value);
    }

    //----------------------------------
    // midHighFreq
    //----------------------------------

    private float midHighFreq = 0.5f;

    public float getMidHighFreq() {
        return midHighFreq;
    }

    float getMidHighFreq(boolean restore) {
        return MasterMixerMessage.EQ_MIDHIGH_FREQ.query(getEngine());
    }

    public void setMidHighFreq(float value) {
        if (midHighFreq == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("midhigh_freq ", "0..1", value);
        midHighFreq = value;
        MasterMixerMessage.EQ_MIDHIGH_FREQ.send(getEngine(), value);
    }

    //----------------------------------
    // high
    //----------------------------------

    private float high = 1.1f;

    public float getHigh() {
        return high;
    }

    float getHigh(boolean restore) {
        return MasterMixerMessage.EQ_HIGH.query(getEngine());
    }

    public void setHigh(float value) {
        if (high == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("high ", "0..2", value);
        high = value;
        MasterMixerMessage.EQ_HIGH.send(getEngine(), value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterEqualizer() {
        bypassMessage = MasterMixerMessage.EQ_BYPASS;
    }

    public MasterEqualizer(ICaustkController controller) {
        super(controller);
        bypassMessage = MasterMixerMessage.EQ_BYPASS;
    }

    @Override
    public void restore() {
        super.restore();
        setBass(getBass(true));
        setBassMidFreq(getBassMidFreq(true));
        setHigh(getHigh(true));
        setMid(getMid(true));
        setMidHighFreq(getMidHighFreq(true));
    }

}
