
package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.components.beatbox.WavSamplerComponent;

public class BeatboxTone extends SynthTone {

    public WavSamplerComponent getSampler() {
        return getComponent(WavSamplerComponent.class);
    }

    public BeatboxTone(ICaustkController controller) {
        super(controller);
    }

}
