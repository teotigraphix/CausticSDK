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

import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;

/**
 * Serialized - v1.0
 * <ul>
 * <li><code>descriptors</code> - A serialized {@link ToneDescriptor}.</li>
 * </ul>
 */
public class SoundSourceModel extends SubControllerModel {

    private transient Map<Integer, Tone> tones = new HashMap<Integer, Tone>();

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // descriptors
    //----------------------------------

    private Map<Integer, ToneDescriptor> descriptors = new HashMap<Integer, ToneDescriptor>();

    public final Map<Integer, ToneDescriptor> getDescriptors() {
        return descriptors;
    }

    //----------------------------------
    // tones
    //----------------------------------

    Map<Integer, Tone> getTones() {
        return tones;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public SoundSourceModel() {
    }

    public SoundSourceModel(ICaustkController controller) {
        super(controller);
    }

    @Override
    public void sleep() {
        for (Tone tone : tones.values()) {
            ToneDescriptor descriptor = new ToneDescriptor(tone.getIndex(), tone.getName(),
                    tone.getToneType());
            descriptors.put(tone.getIndex(), descriptor);
        }
    }

    @Override
    public void wakeup(ICaustkController controller) {
        super.wakeup(controller);
        //        for (Tone tone : tones.values()) {
        //            tone.wakeup(controller);
        //        }
        for (@SuppressWarnings("unused")
        ToneDescriptor descriptor : descriptors.values()) {
        }
    }
}
