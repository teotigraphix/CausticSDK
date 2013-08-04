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

package com.teotigraphix.caustk.pattern;

import java.io.File;

import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.library.LibraryPatch;

/**
 * The Patch holds all sound settings that will get serialized to disk and
 * reloaded into the the Tone.
 * <p>
 * Data;
 * <ul>
 * <li>Mixer</li>
 * <li>Mixer</li>
 * <li>Mixer</li>
 * </ul>
 */
public class Patch {
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
        File presetFile = getPart().getTone().getController().getLibraryManager()
                .getSelectedLibrary().getPresetFile(patchItem.getPresetFile());
        getPart().getTone().getDefaultPatchId();
        getPart().getTone().getComponent(SynthComponent.class)
                .loadPreset(presetFile.getAbsolutePath());
    }
}
