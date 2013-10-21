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

package com.teotigraphix.caustk.sound.mixer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.core.Rack;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage;
import com.teotigraphix.caustk.sound.IEffect;
import com.teotigraphix.caustk.sound.ISoundMixer.OnSoundMixerChannelValueChange;
import com.teotigraphix.caustk.sound.effect.EffectType;
import com.teotigraphix.caustk.sound.effect.EffectUtils;
import com.teotigraphix.caustk.sound.mixer.SoundMixer.MixerInput;
import com.teotigraphix.caustk.utils.ExceptionUtils;

public class SoundMixerChannel implements Serializable, IRestore {

    private static final long serialVersionUID = 2685245138702311763L;

    private Rack rack;

    protected ICausticEngine getEngine() {
        return rack;
    }

    private transient Map<Integer, IEffect> effects = new HashMap<Integer, IEffect>();

    public IEffect getEffect(int slot) {
        return effects.get(slot);
    }

    public boolean hasEffect(int slot) {
        return effects.containsKey(slot);
    }

    public IEffect addEffect(EffectType type, int slot) throws CausticException {
        if (effects.containsKey(slot))
            throw new CausticException("Channel already contains slot:" + slot);
        IEffect effect = EffectUtils.create(type, slot, getIndex());
        effect.setRack(rack);
        EffectRackMessage.CREATE.send(getEngine(), getIndex(), slot, type.getValue());
        effects.put(slot, effect);
        return effect;
    }

    public IEffect removeEffect(int slot) {
        IEffect effect = effects.remove(slot);
        if (effect != null) {
            EffectRackMessage.REMOVE.send(getEngine(), getIndex(), slot);
        }
        return effect;
    }

    //----------------------------------
    // index
    //----------------------------------

    private int index = -1;

    public final int getIndex() {
        return index;
    }

    public final void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // bass
    //----------------------------------

    private float bass = 0f;

    public final float getBass() {
        return bass;
    }

    float getBass(boolean restore) {
        return MixerChannelMessage.EQ_BASS.query(getEngine(), index);
    }

    public final void setBass(float value) {
        if (bass == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("bass", "-1.0..1.0", value);
        bass = value;
        MixerChannelMessage.EQ_BASS.send(getEngine(), getIndex(), value);
        fireValueChange(MixerInput.Bass, bass);
    }

    //----------------------------------
    // mid
    //----------------------------------

    private float mid = 0f;

    public float getMid() {
        return mid;
    }

    float getMid(boolean restore) {
        return MixerChannelMessage.EQ_MID.query(getEngine(), index);
    }

    public void setMid(float value) {
        if (mid == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("mid", "-1.0..1.0", value);
        mid = value;
        MixerChannelMessage.EQ_MID.send(getEngine(), getIndex(), value);
        fireValueChange(MixerInput.Mid, mid);
    }

    //----------------------------------
    // high
    //----------------------------------

    private float high = 0f;

    public final float getHigh() {
        return high;
    }

    float getHigh(boolean restore) {
        return MixerChannelMessage.EQ_HIGH.query(getEngine(), index);
    }

    public final void setHigh(float value) {
        if (high == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("high", "-1.0..1.0", value);
        high = value;
        MixerChannelMessage.EQ_HIGH.send(getEngine(), getIndex(), value);
        fireValueChange(MixerInput.High, high);
    }

    //----------------------------------
    // delaySend
    //----------------------------------

    private float delaySend = 0f;

    public final float getDelaySend() {
        return delaySend;
    }

    float getDelaySend(boolean restore) {
        return MixerChannelMessage.DELAY_SEND.query(getEngine(), index);
    }

    public void setDelaySend(float value) {
        if (delaySend == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("delay_send", "0.0..1.0", value);
        delaySend = value;
        MixerChannelMessage.DELAY_SEND.send(getEngine(), getIndex(), value);
        fireValueChange(MixerInput.DelaySend, delaySend);
    }

    //----------------------------------
    // reverbSend
    //----------------------------------

    private float reverbSend = 0f;

    public final float getReverbSend() {
        return reverbSend;
    }

    float getReverbSend(boolean restore) {
        return MixerChannelMessage.REVERB_SEND.query(getEngine(), index);
    }

    public void setReverbSend(float value) {
        if (reverbSend == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("reverb_send", "0.0..1.0", value);
        reverbSend = value;
        MixerChannelMessage.REVERB_SEND.send(getEngine(), getIndex(), value);
        fireValueChange(MixerInput.ReverbSend, reverbSend);
    }

    //----------------------------------
    // pan
    //----------------------------------

    private float pan = 0f;

    public final float getPan() {
        return pan;
    }

    float getPan(boolean restore) {
        return MixerChannelMessage.PAN.query(getEngine(), index);
    }

    public void setPan(float value) {
        if (pan == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("pan", "-1.0..1.0", value);
        pan = value;
        MixerChannelMessage.PAN.send(getEngine(), index, value);
        fireValueChange(MixerInput.Pan, pan);
    }

    //----------------------------------
    // stereoWidth
    //----------------------------------

    private float stereoWidth = 0f;

    public final float getStereoWidth() {
        return stereoWidth;
    }

    float getStereoWidth(boolean restore) {
        return MixerChannelMessage.STEREO_WIDTH.query(getEngine(), index);
    }

    public void setStereoWidth(float value) {
        if (stereoWidth == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("stereo_width", "0.0..1.0", value);
        stereoWidth = value;
        MixerChannelMessage.STEREO_WIDTH.send(getEngine(), index, value);
        fireValueChange(MixerInput.StereoWidth, stereoWidth);
    }

    //----------------------------------
    // mute
    //----------------------------------

    private boolean mute = false;

    public final boolean isMute() {
        return mute;
    }

    boolean isMute(boolean restore) {
        return MixerChannelMessage.MUTE.query(getEngine(), index) != 0f;
    }

    public void setMute(boolean muted) {
        if (mute == muted)
            return;
        mute = muted;
        MixerChannelMessage.MUTE.send(getEngine(), index, muted ? 1 : 0);
        fireValueChange(MixerInput.Mute, muted ? 1 : 0);
    }

    //----------------------------------
    // solo
    //----------------------------------

    private boolean solo = false;

    public final boolean isSolo() {
        return solo;
    }

    boolean isSolo(boolean restore) {
        return MixerChannelMessage.SOLO.query(getEngine(), index) != 0f;
    }

    public void setSolo(boolean soloed) {
        if (solo == soloed)
            return;
        solo = soloed;
        MixerChannelMessage.SOLO.send(getEngine(), index, solo ? 1 : 0);
        fireValueChange(MixerInput.Solo, solo ? 1 : 0);
    }

    //----------------------------------
    // volume
    //----------------------------------

    private float volume = 0f;

    public final float getVolume() {
        return volume;
    }

    float getVolume(boolean restore) {
        return MixerChannelMessage.VOLUME.query(getEngine(), index);
    }

    public void setVolume(float value) {
        if (volume == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("volume", "0.0..2.0", value);
        volume = value;
        MixerChannelMessage.VOLUME.send(getEngine(), index, value);
        fireValueChange(MixerInput.Volume, volume);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundMixerChannel() {
    }

    public SoundMixerChannel(Rack rack) {
        this.rack = rack;
    }

    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }

    @Override
    public void restore() {
        setBass(getBass(true));
        setMid(getMid(true));
        setHigh(getHigh(true));
        setReverbSend(getReverbSend(true));
        setDelaySend(getDelaySend(true));
        setStereoWidth(getStereoWidth(true));
        setPan(getPan(true));
        setVolume(getVolume(true));
        // /caustic/effects_rack/type [machine_index] [slot] 
        EffectType effect0 = EffectType.fromInt((int)EffectRackMessage.TYPE.send(getEngine(),
                getIndex(), 0));
        EffectType effect1 = EffectType.fromInt((int)EffectRackMessage.TYPE.send(getEngine(),
                getIndex(), 1));
        if (effect0 != null) {
            restoreEffect(effect0, 0);
        }
        if (effect1 != null) {
            restoreEffect(effect1, 1);
        }
    }

    private void restoreEffect(EffectType type, int slot) {
        IEffect effect = EffectUtils.create(type, slot, getIndex());
        effect.setRack(rack);
        effects.put(slot, effect);
        effect.restore();
    }

    public void update() {
        MixerChannelMessage.EQ_BASS.send(getEngine(), getIndex(), getBass());
        MixerChannelMessage.EQ_MID.send(getEngine(), getIndex(), getMid());
        MixerChannelMessage.EQ_HIGH.send(getEngine(), getIndex(), getHigh());
        MixerChannelMessage.REVERB_SEND.send(getEngine(), getIndex(), getReverbSend());
        MixerChannelMessage.DELAY_SEND.send(getEngine(), getIndex(), getDelaySend());
        MixerChannelMessage.STEREO_WIDTH.send(getEngine(), getIndex(), getStereoWidth());

        MixerChannelMessage.PAN.send(getEngine(), getIndex(), getPan());
        MixerChannelMessage.VOLUME.send(getEngine(), getIndex(), getVolume());
    }

    protected void fireValueChange(MixerInput mixerInput, Number value) {
        rack.trigger(new OnSoundMixerChannelValueChange(mixerInput, value));
    }
}
