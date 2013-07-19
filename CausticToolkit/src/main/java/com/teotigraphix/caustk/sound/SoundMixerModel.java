
package com.teotigraphix.caustk.sound;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.tone.Tone;

public class SoundMixerModel extends SubControllerModel {

    Map<Integer, SoundMixerChannel> channels = new HashMap<Integer, SoundMixerChannel>();

    Map<Integer, SoundMixerChannel> getChannels() {
        return channels;
    }

    public SoundMixerModel() {
    }

    public SoundMixerModel(ICaustkController controller) {
        super(controller);
    }

    void toneAdded(Tone tone) {
        SoundMixerChannel channel = new SoundMixerChannel(getController());
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
        super.wakeup(controller);
        for (SoundMixerChannel channel : channels.values()) {
            channel.wakeup(controller);
        }
    }

}
