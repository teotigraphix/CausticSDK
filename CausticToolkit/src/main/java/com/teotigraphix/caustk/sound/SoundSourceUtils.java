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
import com.teotigraphix.caustk.core.components.beatbox.WavSamplerComponent;
import com.teotigraphix.caustk.core.components.modular.ModularBayComponent;
import com.teotigraphix.caustk.core.components.padsynth.HarmonicsComponent;
import com.teotigraphix.caustk.core.components.pcmsynth.PCMSamplerComponent;
import com.teotigraphix.caustk.core.components.pcmsynth.PCMTunerComponent;
import com.teotigraphix.caustk.core.components.subsynth.LFO2Component;
import com.teotigraphix.caustk.core.components.subsynth.Osc1Component;
import com.teotigraphix.caustk.core.components.subsynth.Osc2Component;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.EightBitSynth;
import com.teotigraphix.caustk.tone.FMSynthTone;
import com.teotigraphix.caustk.tone.ModularTone;
import com.teotigraphix.caustk.tone.OrganTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.PadSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.VocoderTone;

public class SoundSourceUtils {

    public static void setup(PadSynthTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeComponent.class, new VolumeComponent());
        tone.addComponent(HarmonicsComponent.class, new HarmonicsComponent());
    }

    public static void setup(OrganTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());

    }

    public static void setup(VocoderTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());

    }

    public static void setup(EightBitSynth tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());

    }

    public static void setup(FMSynthTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());

    }

    public static void setup(ModularTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        //tone.addComponent(VolumeComponent.class, new VolumeComponent());
        tone.addComponent(ModularBayComponent.class, new ModularBayComponent());
    }

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

    public static void setup(PCMSynthTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeEnvelopeComponent.class, new VolumeEnvelopeComponent());
        tone.addComponent(SynthFilterComponent.class, new SynthFilterComponent());
        tone.addComponent(com.teotigraphix.caustk.core.components.pcmsynth.LFO1Component.class,
                new com.teotigraphix.caustk.core.components.pcmsynth.LFO1Component());
        tone.addComponent(PCMSamplerComponent.class, new PCMSamplerComponent());
        tone.addComponent(PCMTunerComponent.class, new PCMTunerComponent());
    }

    public static void setup(BeatboxTone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeComponent.class, new VolumeComponent());
        tone.addComponent(WavSamplerComponent.class, new WavSamplerComponent());
    }

}
