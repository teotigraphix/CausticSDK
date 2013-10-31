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

import com.teotigraphix.caustk.machine.Machine;
import com.teotigraphix.caustk.rack.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.rack.tone.components.SynthComponent;
import com.teotigraphix.caustk.rack.tone.components.VolumeComponent;
import com.teotigraphix.caustk.rack.tone.components.bassline.DistortionComponent;
import com.teotigraphix.caustk.rack.tone.components.bassline.FilterComponent;
import com.teotigraphix.caustk.rack.tone.components.bassline.LFO1Component;
import com.teotigraphix.caustk.rack.tone.components.bassline.OSC1Component;

/**
 * The tone impl for the native Bassline machine.
 * 
 * @author Michael Schmalle
 */
public class BasslineTone extends Tone {

    public VolumeComponent getVolume() {
        return getComponent(VolumeComponent.class);
    }

    public FilterComponent getFilter() {
        return getComponent(FilterComponent.class);
    }

    public LFO1Component getLFO1() {
        return getComponent(LFO1Component.class);
    }

    public OSC1Component getOsc1() {
        return getComponent(OSC1Component.class);
    }

    public DistortionComponent getDistortion() {
        return getComponent(DistortionComponent.class);
    }

    public BasslineTone() {
    }

    public BasslineTone(Machine machine) {
        super(machine, ToneType.Bassline);
    }

    public static void setup(Tone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeComponent.class, new VolumeComponent());
        tone.addComponent(DistortionComponent.class, new DistortionComponent());
        tone.addComponent(FilterComponent.class, new FilterComponent());
        tone.addComponent(LFO1Component.class, new LFO1Component());
        tone.addComponent(OSC1Component.class, new OSC1Component());
    }

}
