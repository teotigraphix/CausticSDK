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

package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustk.controller.core.Rack;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.tone.components.SynthComponent;
import com.teotigraphix.caustk.tone.components.SynthFilterComponent;
import com.teotigraphix.caustk.tone.components.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.tone.components.subsynth.LFO1Component;
import com.teotigraphix.caustk.tone.components.subsynth.LFO2Component;
import com.teotigraphix.caustk.tone.components.subsynth.Osc1Component;
import com.teotigraphix.caustk.tone.components.subsynth.Osc2Component;

public class SubSynthTone extends Tone {

    private static final long serialVersionUID = -2297739026093635478L;

    public VolumeEnvelopeComponent getVolume() {
        return getComponent(VolumeEnvelopeComponent.class);
    }

    public SynthFilterComponent getFilter() {
        return getComponent(SynthFilterComponent.class);
    }

    public Osc1Component getOsc1() {
        return getComponent(Osc1Component.class);
    }

    public Osc2Component getOsc2() {
        return getComponent(Osc2Component.class);
    }

    public LFO1Component getLFO1() {
        return getComponent(LFO1Component.class);
    }

    public LFO2Component getLFO2() {
        return getComponent(LFO2Component.class);
    }

    public SubSynthTone(Rack rack) {
        super(rack, ToneType.SubSynth);
    }

    public static void setup(Tone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeEnvelopeComponent.class, new VolumeEnvelopeComponent());
        tone.addComponent(SynthFilterComponent.class, new SynthFilterComponent());
        tone.addComponent(Osc1Component.class, new Osc1Component());
        tone.addComponent(Osc2Component.class, new Osc2Component());
        tone.addComponent(LFO1Component.class, new LFO1Component());
        tone.addComponent(LFO2Component.class, new LFO2Component());
    }

}