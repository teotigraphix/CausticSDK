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
import com.teotigraphix.caustk.tone.components.PatternSequencerComponent;
import com.teotigraphix.caustk.tone.components.SynthComponent;
import com.teotigraphix.caustk.tone.components.VolumeComponent;
import com.teotigraphix.caustk.tone.components.organ.LeslieComponent;

public class OrganTone extends Tone {

    public VolumeComponent getVolume() {
        return getComponent(VolumeComponent.class);
    }

    public LeslieComponent getLeslie() {
        return getComponent(LeslieComponent.class);
    }

    public OrganTone(ICaustkController controller) {
        super(ToneType.Organ, controller);
    }

    public static void setup(Tone tone) {
        tone.addComponent(SynthComponent.class, new SynthComponent());
        tone.addComponent(PatternSequencerComponent.class, new PatternSequencerComponent());
        tone.addComponent(VolumeComponent.class, new VolumeComponent());
        tone.addComponent(LeslieComponent.class, new LeslieComponent());
    }

}
