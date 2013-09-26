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

import com.teotigraphix.caustk.controller.core.Rack;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;

public class MasterReverb extends MasterComponent {

    private static final long serialVersionUID = 5246569152430913050L;

    //--------------------------------------------------------------------------
    // API
    //--------------------------------------------------------------------------

    //----------------------------------
    // preDelay
    //----------------------------------

    private float preDelay = 0.02f;

    public float getPreDelay() {
        return preDelay;
    }

    float getPreDelay(boolean restore) {
        return MasterMixerMessage.REVERB_PRE_DELAY.query(getEngine());
    }

    public void setPreDelay(float value) {
        if (preDelay == value)
            return;
        if (value < 0f || value > 0.1f)
            throw newRangeException("pre_delay", "0..0.1", value);
        preDelay = value;
        MasterMixerMessage.REVERB_PRE_DELAY.send(getEngine(), value);
    }

    //----------------------------------
    // roomSize
    //----------------------------------

    private float roomSize = 0.75f;

    public float getRoomSize() {
        return roomSize;
    }

    float getRoomSize(boolean restore) {
        return MasterMixerMessage.REVERB_ROOM_SIZE.query(getEngine());
    }

    public void setRoomSize(float value) {
        if (roomSize == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("room_size", "0..1", value);
        roomSize = value;
        MasterMixerMessage.REVERB_ROOM_SIZE.send(getEngine(), value);
    }

    //----------------------------------
    // hfDamping
    //----------------------------------

    private float hfDamping = 0.156f;

    public float getHFDamping() {
        return hfDamping;
    }

    float getHFDamping(boolean restore) {
        return MasterMixerMessage.REVERB_HF_DAMPING.query(getEngine());
    }

    public void setHFDamping(float value) {
        if (hfDamping == value)
            return;
        if (value < 0f || value > 0.8f)
            throw newRangeException("hf_damping", "0..0.8", value);
        hfDamping = value;
        MasterMixerMessage.REVERB_HF_DAMPING.send(getEngine(), value);
    }

    //----------------------------------
    // diffuse
    //----------------------------------

    private float diffuse = 0.7f;

    public float getDiffuse() {
        return diffuse;
    }

    float getDiffuse(boolean restore) {
        return MasterMixerMessage.REVERB_DIFFUSE.query(getEngine());
    }

    public void setDiffuse(float value) {
        if (diffuse == value)
            return;
        if (value < 0f || value > 0.7f)
            throw newRangeException("diffuse", "0..0.7", value);
        diffuse = value;
        MasterMixerMessage.REVERB_DIFFUSE.send(getEngine(), value);
    }

    //----------------------------------
    // ditherEchoes
    //----------------------------------

    private int ditherEchoes = 0;

    public int getDitherEchoes() {
        return ditherEchoes;
    }

    int getDitherEchoes(boolean restore) {
        return (int)MasterMixerMessage.REVERB_DITHER_ECHOS.query(getEngine());
    }

    public void setDitherEchoes(int value) {
        if (ditherEchoes == value)
            return;
        if (value < 0 || value > 1)
            throw newRangeException("dither_echoes", "0,1", value);
        ditherEchoes = value;
        MasterMixerMessage.REVERB_DITHER_ECHOS.send(getEngine(), value);
    }

    //----------------------------------
    // erGain
    //----------------------------------

    private float erGain = 1f;

    public float getERGain() {
        return erGain;
    }

    float getERGain(boolean restore) {
        return MasterMixerMessage.REVERB_ER_GAIN.query(getEngine());
    }

    public void setERGain(float value) {
        if (erGain == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("er_gain", "0..1", value);
        erGain = value;
        MasterMixerMessage.REVERB_ER_GAIN.send(getEngine(), value);
    }

    //----------------------------------
    // erDecay
    //----------------------------------

    private float erDecay = 0.25f;

    public float getERDecay() {
        return erDecay;
    }

    float getERDecay(boolean restore) {
        return MasterMixerMessage.REVERB_ER_DECAY.query(getEngine());
    }

    public void setERDecay(float value) {
        if (erDecay == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("er_decay", "0..1", value);
        erDecay = value;
        MasterMixerMessage.REVERB_ER_DECAY.send(getEngine(), value);
    }

    //----------------------------------
    // stereoDelay
    //----------------------------------

    private float stereoDelay = 0.5f;

    public float getStereoDelay() {
        return stereoDelay;
    }

    float getStereoDelay(boolean restore) {
        return MasterMixerMessage.REVERB_STEREO_DELAY.query(getEngine());
    }

    public void setStereoDelay(float value) {
        if (stereoDelay == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("stereo_delay", "0..1", value);
        stereoDelay = value;
        MasterMixerMessage.REVERB_STEREO_DELAY.send(getEngine(), value);
    }

    //----------------------------------
    // stereoSpread
    //----------------------------------

    private float stereoSpread = 0.25f;

    public float getStereoSpread() {
        return stereoSpread;
    }

    float getStereoSpread(boolean restore) {
        return MasterMixerMessage.REVERB_STEREO_SPREAD.query(getEngine());
    }

    public void setStereoSpread(float value) {
        if (stereoSpread == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("stereo_spread", "0..1", value);
        stereoSpread = value;
        MasterMixerMessage.REVERB_STEREO_SPREAD.send(getEngine(), value);
    }

    //----------------------------------
    // wet
    //----------------------------------

    private float wet = 0.25f;

    public float getWet() {
        return wet;
    }

    float getWet(boolean restore) {
        return MasterMixerMessage.REVERB_WET.query(getEngine());
    }

    public void setWet(float value) {
        if (wet == value)
            return;
        if (value < 0f || value > 0.5f)
            throw newRangeException("wet", "0..0.5", value);
        wet = value;
        MasterMixerMessage.REVERB_WET.send(getEngine(), value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterReverb() {
        bypassMessage = MasterMixerMessage.REVERB_BYPASS;
    }

    public MasterReverb(Rack rack) {
        super(rack);
        bypassMessage = MasterMixerMessage.REVERB_BYPASS;
    }

    @Override
    public void restore() {
        super.restore();
        setDiffuse(getDiffuse(true));
        setDitherEchoes(getDitherEchoes(true));
        setERDecay(getERDecay(true));
        setERGain(getERGain(true));
        setHFDamping(getHFDamping(true));
        setPreDelay(getPreDelay(true));
        setRoomSize(getRoomSize(true));
        setStereoDelay(getStereoDelay(true));
        setStereoSpread(getStereoSpread(true));
        setWet(getWet(true));
    }
}
