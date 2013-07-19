
package com.teotigraphix.caustk.sound;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.service.ISerializeService;
import com.teotigraphix.caustk.tone.Tone;

public class SoundMixerState implements ISerialize {

    transient ICaustkController controller;

    Map<Integer, SoundMixerChannel> channels = new HashMap<Integer, SoundMixerChannel>();

    Map<Integer, SoundMixerChannel> getChannels() {
        return channels;
    }

    /**
     * Called from {@link ISerializeService}, the controller gets set in
     * {@link #wakeup(ICaustkController)}.
     */
    public SoundMixerState() {
    }

    /**
     * Called when explicitly creating and instance in {@link SoundMixer}.
     * 
     * @param controller
     */
    public SoundMixerState(ICaustkController controller) {
        this.controller = controller;
    }

    void toneAdded(Tone tone) {
        SoundMixerChannel channel = new SoundMixerChannel(controller);
        channel.setIndex(tone.getIndex());
        channels.put(tone.getIndex(), channel);
    }

    void toneRemoved(Tone tone) {
        channels.remove(tone.getIndex());
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (SoundMixerChannel channel : channels.values()) {
            channel.wakeup(controller);
        }
    }

}
