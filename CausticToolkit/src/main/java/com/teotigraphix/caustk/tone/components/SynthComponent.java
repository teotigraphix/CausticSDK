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

package com.teotigraphix.caustk.tone.components;

import com.teotigraphix.caustk.core.osc.SynthMessage;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.ModularTone;
import com.teotigraphix.caustk.tone.ToneComponent;

public class SynthComponent extends ToneComponent {

    private transient String absolutePresetPath;

    public String getPresetPath() {
        return absolutePresetPath;
    }

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    public String getPresetName() {
        return SynthMessage.QUERY_PRESET.queryString(getEngine(), getTone().getIndex());
    }

    //----------------------------------
    // polyphony
    //----------------------------------

    private int polyphony = 4;

    public int getPolyphony() {
        return polyphony;
    }

    int getPolyphony(boolean restore) {
        return (int)SynthMessage.POLYPHONY.query(getEngine(), getToneIndex());
    }

    public void setPolyphony(int value) {
        if (getTone() instanceof BasslineTone || getTone() instanceof ModularTone) {
            polyphony = 1;
            return;
        }
        if (getTone() instanceof BeatboxTone) {
            polyphony = 8;
            return;
        }
        if (value == polyphony)
            return;
        // 0 is lagato in SubSynth
        if (value < 0 || value > 16)
            throw newRangeException(SynthMessage.POLYPHONY.toString(), "0..16", value);
        polyphony = value;
        SynthMessage.POLYPHONY.send(getEngine(), getToneIndex(), polyphony);
    }

    public SynthComponent() {
    }

    //--------------------------------------------------------------------------
    //
    // API :: Methods
    //
    //--------------------------------------------------------------------------

    public void loadPreset(String path) {
        absolutePresetPath = path;
        SynthMessage.LOAD_PRESET.send(getEngine(), getToneIndex(), path);
    }

    public void savePreset(String name) {
        SynthMessage.SAVE_PRESET.send(getEngine(), getToneIndex(), name);
    }

    public void noteOn(int pitch) {
        noteOn(pitch, 1f);
    }

    public void noteOn(int pitch, float velocity) {
        SynthMessage.NOTE.send(getEngine(), getToneIndex(), pitch, 1, velocity);
    }

    public void noteOff(int pitch) {
        SynthMessage.NOTE.send(getEngine(), getToneIndex(), pitch, 0);
    }

    public void notePreview(int pitch, boolean oneshot) {
        SynthMessage.NOTE_PREVIEW.send(getEngine(), getToneIndex(), pitch, oneshot);
    }

    @Override
    public void restore() {
        setPolyphony(getPolyphony(true));
    }

}
