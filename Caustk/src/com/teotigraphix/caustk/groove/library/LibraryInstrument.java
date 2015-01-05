////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.groove.library;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.groove.manifest.LibraryInstrumentManifest;
import com.teotigraphix.caustk.node.machine.MachineNode;

import java.io.File;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryInstrument extends LibraryProductItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(50)
    private LibraryInstrumentManifest manifest;

    @Tag(51)
    private MachineNode machineNode;

    //--------------------------------------------------------------------------
    // Transient :: Variables
    //--------------------------------------------------------------------------

    private transient LibrarySound sound;

    private transient File pendingPresetFile;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // manifest
    //----------------------------------

    @Override
    public LibraryInstrumentManifest getManifest() {
        return manifest;
    }

    //----------------------------------
    // pendingPresetFile
    //----------------------------------

    public File getPendingPresetFile() {
        return pendingPresetFile;
    }

    public void setPendingPresetFile(File pendingPresetFile) {
        this.pendingPresetFile = pendingPresetFile;
    }

    //----------------------------------
    // sound
    //----------------------------------

    public LibrarySound getSound() {
        return sound;
    }

    public void setSound(LibrarySound sound) {
        this.sound = sound;
    }

    //----------------------------------
    // machineNode
    //----------------------------------

    public MachineNode getMachineNode() {
        return machineNode;
    }

    public void setMachineNode(MachineNode machineNode) {
        this.machineNode = machineNode;
        if (machineNode != null) {
            machineNode.setRackNode(null);
        }
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Serialization
     */
    LibraryInstrument() {
    }

    public LibraryInstrument(LibraryInstrumentManifest manifest) {
        this.manifest = manifest;
    }
}
