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

package com.teotigraphix.caustk.tone.beatbox;

import com.teotigraphix.caustk.core.osc.BeatboxSamplerMessage;
import com.teotigraphix.caustk.tone.ToneComponent;

public class WavSamplerChannel extends ToneComponent {

    private boolean isSelected;

    private boolean isMute;

    private int index;

    private boolean isSolo;

    private float tune;

    private float punch;

    private float decay;

    private float pan;

    private float volume;

    private String name;

    private transient WavSamplerComponent sampler;

    //--------------------------------------------------------------------------
    //
    // IBeatboxSampler API :: Properties
    //
    //--------------------------------------------------------------------------

    public boolean hasSample() {
        return name != null;
    }

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return index;
    }

    public void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // name
    //----------------------------------

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    //----------------------------------
    // selected
    //----------------------------------

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean value) {
        isSelected = value;
    }

    //----------------------------------
    // mute
    //----------------------------------

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean value) {
        if (value == isMute)
            return;
        isMute = value;
        BeatboxSamplerMessage.CHANNEL_MUTE.send(getEngine(), getToneIndex(), index, isMute ? 1 : 0);
    }

    //----------------------------------
    // solo
    //----------------------------------

    public boolean isSolo() {
        return isSolo;
    }

    public void setSolo(boolean value) {
        if (value == isSolo)
            return;
        isSolo = value;
        BeatboxSamplerMessage.CHANNEL_SOLO.send(getEngine(), getToneIndex(), index, isSolo ? 1 : 0);
    }

    //----------------------------------
    // tune
    //----------------------------------

    public float getTune() {
        return tune;
    }

    public void setTune(float value) {
        if (value == tune)
            return;
        tune = value;
        BeatboxSamplerMessage.CHANNEL_TUNE.send(getEngine(), getToneIndex(), index, tune);
    }

    //----------------------------------
    // punch
    //----------------------------------

    public float getPunch() {
        return punch;
    }

    public void setPunch(float value) {
        if (value == punch)
            return;
        punch = value;
        BeatboxSamplerMessage.CHANNEL_PUNCH.send(getEngine(), getToneIndex(), index, punch);
    }

    //----------------------------------
    // decay
    //----------------------------------

    public float getDecay() {
        return decay;
    }

    public void setDecay(float value) {
        if (value == decay)
            return;
        decay = value;
        BeatboxSamplerMessage.CHANNEL_DECAY.send(getEngine(), getToneIndex(), index, decay);
    }

    //----------------------------------
    // pan
    //----------------------------------

    public float getPan() {
        return pan;
    }

    public void setPan(float value) {
        if (value == pan)
            return;
        pan = value;
        BeatboxSamplerMessage.CHANNEL_PAN.send(getEngine(), getToneIndex(), index, pan);
    }

    //----------------------------------
    // volume
    //----------------------------------

    public float getVolume() {
        return volume;
    }

    public void setVolume(float value) {
        if (value == volume)
            return;
        volume = value;
        BeatboxSamplerMessage.CHANNEL_VOLUME.send(getEngine(), getToneIndex(), index, volume);
    }

    public WavSamplerChannel(WavSamplerComponent sampler) {
        this.sampler = sampler;
    }

    @Override
    public void restore() {
        name = sampler.getSampleName(index);
        if (name == null)
            return;

        decay = BeatboxSamplerMessage.CHANNEL_DECAY.query(getEngine(), getToneIndex(), index);
        pan = BeatboxSamplerMessage.CHANNEL_PAN.query(getEngine(), getToneIndex(), index);
        punch = BeatboxSamplerMessage.CHANNEL_PUNCH.query(getEngine(), getToneIndex(), index);
        tune = BeatboxSamplerMessage.CHANNEL_TUNE.query(getEngine(), getToneIndex(), index);
        volume = BeatboxSamplerMessage.CHANNEL_VOLUME.query(getEngine(), getToneIndex(), index);
        isMute = BeatboxSamplerMessage.CHANNEL_MUTE.query(getEngine(), getToneIndex(), index) == 1f;
        isSolo = BeatboxSamplerMessage.CHANNEL_SOLO.query(getEngine(), getToneIndex(), index) == 1f;
    }

}
