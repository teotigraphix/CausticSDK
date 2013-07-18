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
import com.teotigraphix.caustk.core.components.bassline.DistortionComponent;
import com.teotigraphix.caustk.core.components.bassline.FilterComponent;
import com.teotigraphix.caustk.core.components.bassline.LFO1Component;
import com.teotigraphix.caustk.core.components.bassline.OSC1Component;

public class BasslineTone extends SynthTone {

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

    public BasslineTone(ICaustkController controller) {
        super(controller);
    }

}
