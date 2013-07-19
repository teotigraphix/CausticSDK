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

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.components.SynthFilterComponent;
import com.teotigraphix.caustk.core.components.VolumeEnvelopeComponent;
import com.teotigraphix.caustk.core.components.pcmsynth.LFO1Component;
import com.teotigraphix.caustk.core.components.pcmsynth.PCMSamplerComponent;
import com.teotigraphix.caustk.core.components.pcmsynth.PCMTunerComponent;

public class PCMSynthTone extends SynthTone {

    @Override
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

    public PCMSynthTone(ICaustkController controller) {
        super(controller);
    }

}
