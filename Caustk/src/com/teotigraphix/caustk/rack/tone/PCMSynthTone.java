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

package com.teotigraphix.caustk.rack.tone;

import com.teotigraphix.caustk.live.Machine;
import com.teotigraphix.caustk.live.MachineType;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.rack.tone.components.SynthComponent;
import com.teotigraphix.caustk.rack.tone.components.SynthFilterComponent;
import com.teotigraphix.caustk.rack.tone.components.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.rack.tone.components.pcmsynth.LFO1Component;
import com.teotigraphix.caustk.rack.tone.components.pcmsynth.PCMSamplerComponent;
import com.teotigraphix.caustk.rack.tone.components.pcmsynth.PCMTunerComponent;

/**
 * The tone impl for the native PCMSynth machine.
 * 
 * @author Michael Schmalle
 */
public class PCMSynthTone extends RackTone {

    public VolumeEnvelopeComponent getVolume() {
        return getComponent(VolumeEnvelopeComponent.class);
    }

    public SynthFilterComponent getFilter() {
        return getComponent(SynthFilterComponent.class);
    }

    public LFO1Component getLFO1() {
        return getComponent(LFO1Component.class);
    }

    public PCMTunerComponent getTuner() {
        return getComponent(PCMTunerComponent.class);
    }

    public PCMSamplerComponent getSampler() {
        return getComponent(PCMSamplerComponent.class);
    }

    public PCMSynthTone() {
    }

    public PCMSynthTone(Machine machine) {
        super(machine, MachineType.PCMSynth);
    }

    public static void setup(RackTone rackTone) {
        rackTone.addComponent(SynthComponent.class, new SynthComponent());
        rackTone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        rackTone.addComponent(VolumeEnvelopeComponent.class, new VolumeEnvelopeComponent());
        rackTone.addComponent(SynthFilterComponent.class, new SynthFilterComponent());
        rackTone.addComponent(LFO1Component.class, new LFO1Component());
        rackTone.addComponent(PCMSamplerComponent.class, new PCMSamplerComponent());
        rackTone.addComponent(PCMTunerComponent.class, new PCMTunerComponent());
    }

}
