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

package com.teotigraphix.caustk.sound;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.core.osc.EffectRackMessage;
import com.teotigraphix.caustk.core.osc.MixerMessage;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.sound.effect.EffectType;
import com.teotigraphix.caustk.sound.effect.EffectUtils;
import com.teotigraphix.caustk.utils.ExceptionUtils;

public class SoundMixerChannel implements ISerialize, IRestore {

    private transient ICaustkController controller;

    private ICausticEngine getEngine() {
        return controller;
    }

    private transient Map<Integer, IEffect> effects = new HashMap<Integer, IEffect>();

    public IEffect getEffect(int slot) {
        return effects.get(slot);
    }

    public IEffect addEffect(EffectType type, int slot) throws CausticException {
        if (effects.containsKey(slot))
            throw new CausticException("Channel already contains slot:" + slot);
        IEffect effect = EffectUtils.create(type, slot, getIndex());
        EffectRackMessage.CREATE.send(controller, getIndex(), slot, type.getValue());
        effects.put(slot, effect);
        effect.wakeup(controller);
        return effect;
    }

    public IEffect removeEffect(int slot) {
        IEffect effect = effects.remove(slot);
        if (effect != null) {
            EffectRackMessage.REMOVE.send(controller, getIndex(), slot);
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
        return MixerMessage.EQ_BASS.query(getEngine(), index);
    }

    public final void setBass(float value) {
        if (bass == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("bass", "-1.0..1.0", value);
        bass = value;
        MixerMessage.EQ_BASS.send(getEngine(), getIndex(), value);
    }

    //----------------------------------
    // mid
    //----------------------------------

    private float mid = 0f;

    public float getMid() {
        return mid;
    }

    float getMid(boolean restore) {
        return MixerMessage.EQ_MID.query(getEngine(), index);
    }

    public void setMid(float value) {
        if (mid == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("mid", "-1.0..1.0", value);
        mid = value;
        MixerMessage.EQ_MID.send(getEngine(), getIndex(), value);
    }

    //----------------------------------
    // high
    //----------------------------------

    private float high = 0f;

    public final float getHigh() {
        return high;
    }

    float getHigh(boolean restore) {
        return MixerMessage.EQ_HIGH.query(getEngine(), index);
    }

    public final void setHigh(float value) {
        if (high == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("high", "-1.0..1.0", value);
        high = value;
        MixerMessage.EQ_HIGH.send(getEngine(), getIndex(), value);
    }

    //----------------------------------
    // delaySend
    //----------------------------------

    private float delaySend = 0f;

    public final float getDelaySend() {
        return delaySend;
    }

    float getDelaySend(boolean restore) {
        return MixerMessage.DELAY_SEND.query(getEngine(), index);
    }

    public void setDelaySend(float value) {
        if (delaySend == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("delay_send", "0.0..1.0", value);
        delaySend = value;
        MixerMessage.DELAY_SEND.send(getEngine(), getIndex(), value);
    }

    //----------------------------------
    // reverbSend
    //----------------------------------

    private float reverbSend = 0f;

    public final float getReverbSend() {
        return reverbSend;
    }

    float getReverbSend(boolean restore) {
        return MixerMessage.REVERB_SEND.query(getEngine(), index);
    }

    public void setReverbSend(float value) {
        if (reverbSend == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("reverb_send", "0.0..1.0", value);
        reverbSend = value;
        MixerMessage.REVERB_SEND.send(getEngine(), getIndex(), value);
    }

    //----------------------------------
    // pan
    //----------------------------------

    private float pan = 0f;

    public final float getPan() {
        return pan;
    }

    float getPan(boolean restore) {
        return MixerMessage.PAN.query(getEngine(), index);
    }

    public void setPan(float value) {
        if (pan == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("pan", "-1.0..1.0", value);
        pan = value;
        MixerMessage.PAN.send(getEngine(), index, value);
    }

    //----------------------------------
    // stereoWidth
    //----------------------------------

    private float stereoWidth = 0f;

    public final float getStereoWidth() {
        return stereoWidth;
    }

    float getStereoWidth(boolean restore) {
        return MixerMessage.STEREO_WIDTH.query(getEngine(), index);
    }

    public void setStereoWidth(float value) {
        if (stereoWidth == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("stereo_width", "0.0..1.0", value);
        stereoWidth = value;
        MixerMessage.STEREO_WIDTH.send(getEngine(), index, value);
    }

    //----------------------------------
    // mute
    //----------------------------------

    private boolean mute = false;

    public final boolean isMute() {
        return mute;
    }

    boolean isMute(boolean restore) {
        return MixerMessage.MUTE.query(getEngine(), index) != 0f;
    }

    public void setMute(boolean muted) {
        if (mute == muted)
            return;
        mute = muted;
        MixerMessage.MUTE.send(getEngine(), index, muted ? 1 : 0);
    }

    //----------------------------------
    // solo
    //----------------------------------

    private boolean solo = false;

    public final boolean isSolo() {
        return solo;
    }

    boolean isSolo(boolean restore) {
        return MixerMessage.SOLO.query(getEngine(), index) != 0f;
    }

    public void setSolo(boolean soloed) {
        if (solo == soloed)
            return;
        solo = soloed;
        MixerMessage.SOLO.send(getEngine(), index, solo ? 1 : 0);
    }

    //----------------------------------
    // volume
    //----------------------------------

    private float volume = 0f;

    public final float getVolume() {
        return volume;
    }

    float getVolume(boolean restore) {
        return MixerMessage.VOLUME.query(getEngine(), index);
    }

    public void setVolume(float value) {
        if (volume == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("volume", "0.0..2.0", value);
        volume = value;
        MixerMessage.VOLUME.send(getEngine(), index, value);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundMixerChannel() {
    }

    public SoundMixerChannel(ICaustkController controller) {
        this.controller = controller;
    }

    //--------------------------------------------------------------------------
    // ISerialize API
    //--------------------------------------------------------------------------

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
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

        for (IEffect effect : effects.values()) {
            effect.restore();
        }
    }

    public void update() {
        MixerMessage.EQ_BASS.send(getEngine(), getIndex(), getBass());
        MixerMessage.EQ_MID.send(getEngine(), getIndex(), getMid());
        MixerMessage.EQ_HIGH.send(getEngine(), getIndex(), getHigh());
        MixerMessage.REVERB_SEND.send(getEngine(), getIndex(), getReverbSend());
        MixerMessage.DELAY_SEND.send(getEngine(), getIndex(), getDelaySend());
        MixerMessage.STEREO_WIDTH.send(getEngine(), getIndex(), getStereoWidth());

        MixerMessage.PAN.send(getEngine(), getIndex(), getPan());
        MixerMessage.VOLUME.send(getEngine(), getIndex(), getVolume());
    }

}
