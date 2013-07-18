
package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustk.controller.ICaustkController;

public class SoundMixer implements ISoundMixer {

    private ICaustkController controller;

    public SoundMixer(ICaustkController controller) {
        this.controller = controller;
    }

}
