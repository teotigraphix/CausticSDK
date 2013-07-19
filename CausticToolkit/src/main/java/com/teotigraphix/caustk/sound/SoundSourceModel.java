
package com.teotigraphix.caustk.sound;

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.tone.Tone;

public class SoundSourceModel extends SubControllerModel {

    private Map<Integer, Tone> tones = new HashMap<Integer, Tone>();

    public SoundSourceModel() {
    }

    public SoundSourceModel(ICaustkController controller) {
        super(controller);
    }

    Map<Integer, Tone> getTones() {
        return tones;
    }

    @Override
    public void sleep() {
        super.sleep();
        for (Tone tone : tones.values()) {
            tone.sleep();
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        super.wakeup(controller);
        for (Tone tone : tones.values()) {
            tone.wakeup(controller);
        }
    }

}
