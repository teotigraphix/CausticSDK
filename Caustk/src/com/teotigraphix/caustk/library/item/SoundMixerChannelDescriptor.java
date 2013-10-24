
package com.teotigraphix.caustk.library.item;

import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.osc.MixerChannelMessage;
import com.teotigraphix.caustk.rack.mixer.SoundMixerChannel;
import com.teotigraphix.caustk.rack.tone.Tone;

public class SoundMixerChannelDescriptor {

    // holds the 2 effects

    // holds the mixer channel properties

    private float bass;

    public float getBass() {
        return bass;
    }

    private float mid;

    public float getMid() {
        return mid;
    }

    private float high;

    public float getHigh() {
        return high;
    }

    private float delaySend;

    public float getDelaySend() {
        return delaySend;
    }

    private float reverbSend;

    public float getReverbSend() {
        return reverbSend;
    }

    private float pan;

    public float getPan() {
        return pan;
    }

    private float steroWidth;

    public float getSteroWidth() {
        return steroWidth;
    }

    private float volume;

    public float getVolume() {
        return volume;
    }

    public SoundMixerChannelDescriptor(ICausticEngine engine, int toneIndex) {
        bass = MixerChannelMessage.EQ_BASS.query(engine, toneIndex);
        mid = MixerChannelMessage.EQ_MID.query(engine, toneIndex);
        high = MixerChannelMessage.EQ_HIGH.query(engine, toneIndex);
        delaySend = MixerChannelMessage.DELAY_SEND.query(engine, toneIndex);
        reverbSend = MixerChannelMessage.REVERB_SEND.query(engine, toneIndex);
        pan = MixerChannelMessage.PAN.query(engine, toneIndex);
        steroWidth = MixerChannelMessage.STEREO_WIDTH.query(engine, toneIndex);
        volume = MixerChannelMessage.VOLUME.query(engine, toneIndex);
    }

    public SoundMixerChannelDescriptor(Tone tone) {
        final SoundMixerChannel channel = tone.getMixerChannel();
        channel.restore();
        bass = channel.getBass();
        mid = channel.getMid();
        high = channel.getHigh();
        delaySend = channel.getDelaySend();
        reverbSend = channel.getReverbSend();
        pan = channel.getPan();
        steroWidth = channel.getStereoWidth();
        volume = channel.getVolume();
    }

    public void update(Tone tone) {
        final SoundMixerChannel channel = tone.getMixerChannel();
        channel.setBass(bass);
        channel.setMid(mid);
        channel.setHigh(high);
        channel.setDelaySend(delaySend);
        channel.setReverbSend(reverbSend);
        channel.setPan(pan);
        channel.setStereoWidth(steroWidth);
        channel.setVolume(volume);
    }
}
