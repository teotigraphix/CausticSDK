
package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.ExceptionUtils;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.osc.MixerMessage;
import com.teotigraphix.caustk.service.ISerialize;

public class SoundMixerChannel implements ISerialize {

    private transient ICaustkController controller;

    private ICausticEngine getEngine() {
        return controller;
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

    float getBass(int index, boolean restore) {
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

    float getMid(int index, boolean restore) {
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

    float getHigh(int index, boolean restore) {
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

}
