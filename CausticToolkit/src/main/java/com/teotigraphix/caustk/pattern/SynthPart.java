
package com.teotigraphix.caustk.pattern;

import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.tone.Tone;

public class SynthPart extends Part {

    public SynthPart(Pattern pattern, Tone tone) {
        super(pattern, tone);
    }

    public void noteOn(int pitch, float velocity) {
        SynthComponent synth = getTone().getComponent(SynthComponent.class);
        synth.noteOn(pitch, velocity);
    }

    public void noteOff(int pitch) {
        SynthComponent synth = getTone().getComponent(SynthComponent.class);
        synth.noteOff(pitch);
    }
}
