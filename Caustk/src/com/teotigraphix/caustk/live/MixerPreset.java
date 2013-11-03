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

package com.teotigraphix.caustk.live;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.controller.IRackContext;
import com.teotigraphix.caustk.controller.IRackSerializer;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.utils.ExceptionUtils;

public class MixerPreset implements IRackSerializer {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(0)
    private Patch patch;

    @Tag(3)
    private float bass = 0f;

    @Tag(4)
    private float mid = 0f;

    @Tag(5)
    private float high = 0f;

    @Tag(6)
    private float delaySend = 0f;

    @Tag(7)
    private float reverbSend = 0f;

    @Tag(8)
    private float pan = 0f;

    @Tag(9)
    private float stereoWidth = 0f;

    @Tag(10)
    private boolean mute = false;

    @Tag(11)
    private boolean solo = false;

    @Tag(12)
    private float volume = 1f;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // rack
    //----------------------------------

    public final IRack getRack() {
        return patch.getMachine().getRack();
    }

    //----------------------------------
    // patch
    //----------------------------------

    /**
     * Returns the id of the {@link Patch} that created the preset.
     */
    public Patch getPatch() {
        return patch;
    }

    //----------------------------------
    // bass
    //----------------------------------

    public final float getBass() {
        return bass;
    }

    float getBass(boolean restore) {
        return MixerChannelMessage.EQ_BASS.query(getRack(), getIndex());
    }

    public final void setBass(float value) {
        if (bass == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("bass", "-1.0..1.0", value);
        bass = value;
        MixerChannelMessage.EQ_BASS.send(getRack(), getIndex(), value);
        //        fireValueChange(MixerInput.Bass, bass);
    }

    //----------------------------------
    // mid
    //----------------------------------

    public float getMid() {
        return mid;
    }

    float getMid(boolean restore) {
        return MixerChannelMessage.EQ_MID.query(getRack(), getIndex());
    }

    public void setMid(float value) {
        if (mid == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("mid", "-1.0..1.0", value);
        mid = value;
        MixerChannelMessage.EQ_MID.send(getRack(), getIndex(), value);
        //        fireValueChange(MixerInput.Mid, mid);
    }

    //----------------------------------
    // high
    //----------------------------------

    public final float getHigh() {
        return high;
    }

    float getHigh(boolean restore) {
        return MixerChannelMessage.EQ_HIGH.query(getRack(), getIndex());
    }

    public final void setHigh(float value) {
        if (high == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("high", "-1.0..1.0", value);
        high = value;
        MixerChannelMessage.EQ_HIGH.send(getRack(), getIndex(), value);
        //        fireValueChange(MixerInput.High, high);
    }

    //----------------------------------
    // delaySend
    //----------------------------------

    public final float getDelaySend() {
        return delaySend;
    }

    float getDelaySend(boolean restore) {
        return MixerChannelMessage.DELAY_SEND.query(getRack(), getIndex());
    }

    public void setDelaySend(float value) {
        if (delaySend == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("delay_send", "0.0..1.0", value);
        delaySend = value;
        MixerChannelMessage.DELAY_SEND.send(getRack(), getIndex(), value);
        //        fireValueChange(MixerInput.DelaySend, delaySend);
    }

    //----------------------------------
    // reverbSend
    //----------------------------------

    public final float getReverbSend() {
        return reverbSend;
    }

    float getReverbSend(boolean restore) {
        return MixerChannelMessage.REVERB_SEND.query(getRack(), getIndex());
    }

    public void setReverbSend(float value) {
        if (reverbSend == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("reverb_send", "0.0..1.0", value);
        reverbSend = value;
        MixerChannelMessage.REVERB_SEND.send(getRack(), getIndex(), value);
        //        fireValueChange(MixerInput.ReverbSend, reverbSend);
    }

    //----------------------------------
    // pan
    //----------------------------------

    public final float getPan() {
        return pan;
    }

    float getPan(boolean restore) {
        return MixerChannelMessage.PAN.query(getRack(), getIndex());
    }

    public void setPan(float value) {
        if (pan == value)
            return;
        if (value < -1f || value > 1f)
            throw newRangeException("pan", "-1.0..1.0", value);
        pan = value;
        MixerChannelMessage.PAN.send(getRack(), getIndex(), value);
        //        fireValueChange(MixerInput.Pan, pan);
    }

    //----------------------------------
    // stereoWidth
    //----------------------------------

    public final float getStereoWidth() {
        return stereoWidth;
    }

    float getStereoWidth(boolean restore) {
        return MixerChannelMessage.STEREO_WIDTH.query(getRack(), getIndex());
    }

    public void setStereoWidth(float value) {
        if (stereoWidth == value)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException("stereo_width", "0.0..1.0", value);
        stereoWidth = value;
        MixerChannelMessage.STEREO_WIDTH.send(getRack(), getIndex(), value);
        //        fireValueChange(MixerInput.StereoWidth, stereoWidth);
    }

    //----------------------------------
    // mute
    //----------------------------------

    public final boolean isMute() {
        return mute;
    }

    boolean isMute(boolean restore) {
        return MixerChannelMessage.MUTE.query(getRack(), getIndex()) != 0f;
    }

    public void setMute(boolean muted) {
        if (mute == muted)
            return;
        mute = muted;
        MixerChannelMessage.MUTE.send(getRack(), getIndex(), muted ? 1 : 0);
        //        fireValueChange(MixerInput.Mute, muted ? 1 : 0);
    }

    //----------------------------------
    // solo
    //----------------------------------

    public final boolean isSolo() {
        return solo;
    }

    boolean isSolo(boolean restore) {
        return MixerChannelMessage.SOLO.query(getRack(), getIndex()) != 0f;
    }

    public void setSolo(boolean soloed) {
        if (solo == soloed)
            return;
        solo = soloed;
        MixerChannelMessage.SOLO.send(getRack(), getIndex(), solo ? 1 : 0);
        //        fireValueChange(MixerInput.Solo, solo ? 1 : 0);
    }

    //----------------------------------
    // volume
    //----------------------------------

    public final float getVolume() {
        return volume;
    }

    float getVolume(boolean restore) {
        return MixerChannelMessage.VOLUME.query(getRack(), getIndex());
    }

    public void setVolume(float value) {
        if (volume == value)
            return;
        if (value < 0f || value > 2f)
            throw newRangeException("volume", "0.0..2.0", value);
        volume = value;
        MixerChannelMessage.VOLUME.send(getRack(), getIndex(), value);
        //        fireValueChange(MixerInput.Volume, volume);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /*
     * Serialization.
     */
    MixerPreset() {
    }

    MixerPreset(Patch patch) {
        this.patch = patch;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void create() throws CausticException {
    }

    @Override
    public void load(IRackContext context) {
        restore();
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
        setMute(isMute(true));
        setSolo(isSolo(true));
        setVolume(getVolume(true));
    }

    /**
     * Updates the native rack with the current values of this mixer preset,
     * using it's parent patch's machine index.
     */
    @Override
    public void update() {
        MixerChannelMessage.EQ_BASS.send(getRack(), getIndex(), getBass());
        MixerChannelMessage.EQ_MID.send(getRack(), getIndex(), getMid());
        MixerChannelMessage.EQ_HIGH.send(getRack(), getIndex(), getHigh());
        MixerChannelMessage.REVERB_SEND.send(getRack(), getIndex(), getReverbSend());
        MixerChannelMessage.DELAY_SEND.send(getRack(), getIndex(), getDelaySend());
        MixerChannelMessage.STEREO_WIDTH.send(getRack(), getIndex(), getStereoWidth());

        MixerChannelMessage.PAN.send(getRack(), getIndex(), getPan());
        MixerChannelMessage.VOLUME.send(getRack(), getIndex(), getVolume());

        MixerChannelMessage.MUTE.send(getRack(), getIndex(), mute ? 1 : 0);
        MixerChannelMessage.SOLO.send(getRack(), getIndex(), solo ? 1 : 0);
    }

    //--------------------------------------------------------------------------
    // Protected :: Methods
    //--------------------------------------------------------------------------

    protected final int getIndex() {
        return getPatch().getMachine().getIndex();
    }

    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }
}
