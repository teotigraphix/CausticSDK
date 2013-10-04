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

import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.tone.components.SynthComponent;
import com.teotigraphix.caustk.tone.components.padsynth.HarmonicsComponent;
import com.teotigraphix.caustk.tone.components.padsynth.LFO1Component;
import com.teotigraphix.caustk.tone.components.padsynth.LFO2Component;
import com.teotigraphix.caustk.tone.components.padsynth.MorphComponent;
import com.teotigraphix.caustk.tone.components.padsynth.VolumeComponent;

public class PadSynthTone extends Tone {

    private static final long serialVersionUID = -2333431858291077409L;

    public HarmonicsComponent getHarmonics() {
        return getComponent(HarmonicsComponent.class);
    }

    public LFO1Component getLFO1() {
        return getComponent(LFO1Component.class);
    }

    public LFO2Component getLFO2() {
        return getComponent(LFO2Component.class);
    }

    public MorphComponent getMorph() {
        return getComponent(MorphComponent.class);
    }

    public VolumeComponent getVolume() {
        return getComponent(VolumeComponent.class);
    }

    public PadSynthTone(IRack rack) {
        super(rack, ToneType.PadSynth);
    }

    public static void setup(Tone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeComponent.class, new VolumeComponent());
        tone.addComponent(HarmonicsComponent.class, new HarmonicsComponent());
        tone.addComponent(LFO1Component.class, new LFO1Component());
        tone.addComponent(LFO2Component.class, new LFO2Component());
        tone.addComponent(MorphComponent.class, new MorphComponent());
    }

}
