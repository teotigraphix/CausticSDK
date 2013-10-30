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

package com.teotigraphix.caustk.gs.machine.part.sound;

import java.io.File;

import com.teotigraphix.caustk.gs.pattern.Part;
import com.teotigraphix.caustk.gs.pattern.PartUtils;
import com.teotigraphix.caustk.rack.tone.Tone;
import com.teotigraphix.caustk.rack.tone.components.SynthComponent;

/**
 * The Patch holds all sound settings that will get serialized to disk and
 * reloaded into the the Tone.
 * <p>
 * The Patch also is the API decorating the Tone's sound API.
 * <p>
 * Most application will call methods on the Patch to adjust the Tone's sounds,
 * this can allow for undoable commands.
 */
public class Patch {

    @SuppressWarnings("unchecked")
    protected <T extends Tone> T getTone() {
        return (T)PartUtils.getTone(getPart());
    }

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // patchItem
    //----------------------------------

    private LibraryPatch patchItem;

    public LibraryPatch getPatchItem() {
        return patchItem;
    }

    //----------------------------------
    // part
    //----------------------------------

    private Part part;

    public Part getPart() {
        return part;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Patch(Part part, LibraryPatch patchItem) {
        this.part = part;
        this.patchItem = patchItem;
        part.setPatch(this);
    }

    public void configure() {
    }

    public void commit() {
        try {
            // XXX may tomany access points, this needs to change
            File presetFile = getPart().getMachine().getRack().getLibrary()
                    .getPresetFile(patchItem.getPresetFile());
            loadPreset(presetFile);
        } catch (Exception e) {
            // default library patch proxy, no preset
        }

    }

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    public void loadPreset(File presetFile) {
        getTone().getSynth().loadPreset(presetFile.getAbsolutePath());
    }

    public void savePreset(String name) {
        getTone().getSynth().savePreset(name);
    }

    public void noteOn(int pitch, float velocity) {
        SynthComponent synth = getTone().getComponent(SynthComponent.class);
        synth.noteOn(pitch, velocity);
    }

    public void noteOff(int pitch) {
        SynthComponent synth = getTone().getComponent(SynthComponent.class);
        synth.noteOff(pitch);
    }
}
