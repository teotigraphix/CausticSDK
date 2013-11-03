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

package com.teotigraphix.caustk.rack.tone.components.beatbox;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.osc.BeatboxMessage;
import com.teotigraphix.caustk.rack.tone.RackToneComponent;

public class WavSamplerChannel extends RackToneComponent {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(100)
    private WavSamplerComponent sampler;

    @Tag(101)
    private int index;

    @Tag(102)
    private String name;

    @Tag(103)
    private boolean isSelected;

    @Tag(104)
    private boolean isMute;

    @Tag(105)
    private boolean isSolo;

    @Tag(106)
    private float tune;

    @Tag(107)
    private float punch;

    @Tag(108)
    private float decay;

    @Tag(109)
    private float pan;

    @Tag(110)
    private float volume;

    @Tag(111)
    private int muteGroups;

    //--------------------------------------------------------------------------
    // Public API :: Properties
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
        BeatboxMessage.CHANNEL_MUTE.send(getEngine(), getToneIndex(), index, isMute ? 1 : 0);
    }

    //----------------------------------
    // muteGroups
    //----------------------------------

    public int getMuteGroups() {
        return (int)BeatboxMessage.CHANNEL_MUTE_GROUPS.query(getEngine(), getToneIndex(), index);
    }

    public void getMuteGroups(int value) {
        if (value == muteGroups)
            return;
        muteGroups = value;
        BeatboxMessage.CHANNEL_MUTE_GROUPS.send(getEngine(), getToneIndex(), index, muteGroups);
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
        BeatboxMessage.CHANNEL_SOLO.send(getEngine(), getToneIndex(), index, isSolo ? 1 : 0);
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
        BeatboxMessage.CHANNEL_TUNE.send(getEngine(), getToneIndex(), index, tune);
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
        BeatboxMessage.CHANNEL_PUNCH.send(getEngine(), getToneIndex(), index, punch);
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
        BeatboxMessage.CHANNEL_DECAY.send(getEngine(), getToneIndex(), index, decay);
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
        BeatboxMessage.CHANNEL_PAN.send(getEngine(), getToneIndex(), index, pan);
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
        BeatboxMessage.CHANNEL_VOLUME.send(getEngine(), getToneIndex(), index, volume);
    }

    WavSamplerChannel() {
    }

    public WavSamplerChannel(WavSamplerComponent sampler) {
        this.sampler = sampler;
    }

    @Override
    public void restore() {
        name = sampler.getSampleName(index);
        if (name == null)
            return;

        decay = BeatboxMessage.CHANNEL_DECAY.query(getEngine(), getToneIndex(), index);
        pan = BeatboxMessage.CHANNEL_PAN.query(getEngine(), getToneIndex(), index);
        punch = BeatboxMessage.CHANNEL_PUNCH.query(getEngine(), getToneIndex(), index);
        tune = BeatboxMessage.CHANNEL_TUNE.query(getEngine(), getToneIndex(), index);
        volume = BeatboxMessage.CHANNEL_VOLUME.query(getEngine(), getToneIndex(), index);
        isMute = BeatboxMessage.CHANNEL_MUTE.query(getEngine(), getToneIndex(), index) == 1f;
        isSolo = BeatboxMessage.CHANNEL_SOLO.query(getEngine(), getToneIndex(), index) == 1f;
    }

}
