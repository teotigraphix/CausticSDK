
package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.core.components.SynthFilterComponent;
import com.teotigraphix.caustk.core.components.VolumeComponent;
import com.teotigraphix.caustk.core.components.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.core.components.bassline.DistortionComponent;
import com.teotigraphix.caustk.core.components.bassline.FilterComponent;
import com.teotigraphix.caustk.core.components.bassline.LFO1Component;
import com.teotigraphix.caustk.core.components.bassline.OSC1Component;
import com.teotigraphix.caustk.core.components.subsynth.LFO2Component;
import com.teotigraphix.caustk.core.components.subsynth.Osc1Component;
import com.teotigraphix.caustk.core.components.subsynth.Osc2Component;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.SubSynthTone;

public class SoundSourceUtils {

    public static void setup(SubSynthTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeEnvelopeComponent.class, new VolumeEnvelopeComponent());
        tone.addComponent(SynthFilterComponent.class, new SynthFilterComponent());
        tone.addComponent(Osc1Component.class, new Osc1Component());
        tone.addComponent(Osc2Component.class, new Osc2Component());
        tone.addComponent(com.teotigraphix.caustk.core.components.subsynth.LFO1Component.class,
                new com.teotigraphix.caustk.core.components.subsynth.LFO1Component());
        tone.addComponent(LFO2Component.class, new LFO2Component());
    }

    public static void setup(BasslineTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeComponent.class, new VolumeComponent());
        tone.addComponent(DistortionComponent.class, new DistortionComponent());
        tone.addComponent(FilterComponent.class, new FilterComponent());
        tone.addComponent(LFO1Component.class, new LFO1Component());
        tone.addComponent(OSC1Component.class, new OSC1Component());
    }

}
