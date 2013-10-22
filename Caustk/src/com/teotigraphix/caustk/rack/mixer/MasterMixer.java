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

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.osc.MasterMixerMessage;
import com.teotigraphix.caustk.rack.Rack;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.utils.ExceptionUtils;

public class MasterMixer implements IRestore {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private Rack rack;

    @Tag(1)
    private MasterDelay delay;

    @Tag(2)
    private MasterReverb reverb;

    @Tag(3)
    private MasterEqualizer equalizer;

    @Tag(4)
    private MasterLimiter limiter;

    @Tag(5)
    private float volume = 1f;

    @Tag(6)
    private Map<Integer, SoundMixerChannel> channels = new HashMap<Integer, SoundMixerChannel>();

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    private ICausticEngine getEngine() {
        return rack;
    }

    //----------------------------------
    // channels
    //----------------------------------

    Map<Integer, SoundMixerChannel> getChannels() {
        return channels;
    }

    //----------------------------------
    // equalizer
    //----------------------------------

    public final MasterEqualizer getEqualizer() {
        return equalizer;
    }

    public final void setEqualizer(MasterEqualizer value) {
        equalizer = value;
    }

    //----------------------------------
    // limiter
    //----------------------------------

    public final MasterLimiter getLimiter() {
        return limiter;
    }

    public final void setLimiter(MasterLimiter value) {
        limiter = value;
    }

    //----------------------------------
    // delay
    //----------------------------------

    public final MasterDelay getDelay() {
        return delay;
    }

    public final void setDelay(MasterDelay value) {
        delay = value;
    }

    //----------------------------------
    // reverb
    //----------------------------------

    public final MasterReverb getReverb() {
        return reverb;
    }

    public final void setReverb(MasterReverb value) {
        reverb = value;
    }

    //----------------------------------
    // volume
    //----------------------------------

    public float getVolume() {
        return volume;
    }

    float getVolume(boolean restore) {
        return MasterMixerMessage.VOLUME.query(getEngine());
    }

    public void setVolume(float value) {
        if (volume == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("volume", "0..2", value);
        volume = value;
        MasterMixerMessage.VOLUME.send(getEngine(), value);
        fireChange(MasterMixerChangeKind.Volume, volume);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MasterMixer() {
    }

    public MasterMixer(Rack rack) {
        this.rack = rack;

        equalizer = new MasterEqualizer(rack);
        limiter = new MasterLimiter(rack);
        delay = new MasterDelay(rack);
        reverb = new MasterReverb(rack);
    }

    public void addTone(Tone tone) {
        SoundMixerChannel channel = new SoundMixerChannel(rack);
        channel.setIndex(tone.getIndex());
        channels.put(tone.getIndex(), channel);
    }

    public void removeTone(Tone tone) {
        channels.remove(tone.getIndex());
    }

    @Override
    public void restore() {
        setVolume(getVolume(true));
        equalizer.restore();
        limiter.restore();
        delay.restore();
        reverb.restore();
        for (SoundMixerChannel mixerChannel : channels.values()) {
            mixerChannel.restore();
        }
    }

    protected void fireChange(MasterMixerChangeKind kind, float value) {
        rack.getGlobalDispatcher().trigger(new OnMasterMixerChange(kind, value));
    }

    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }

    public enum MasterMixerChangeKind {
        Volume;
    }

    public static class OnMasterMixerChange {

        private final Number value;

        public final Number getValue() {
            return value;
        }

        private final MasterMixerChangeKind kind;

        public final MasterMixerChangeKind getKind() {
            return kind;
        }

        public OnMasterMixerChange(MasterMixerChangeKind kind, Number value) {
            this.kind = kind;
            this.value = value;
        }
    }

}
