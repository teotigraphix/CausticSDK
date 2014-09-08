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

package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.core.ICaustkFactory;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibraryProductItem;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.VocoderMachine;
import com.teotigraphix.caustk.utils.ZipUncompress;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryInstrumentUtils {

    //--------------------------------------------------------------------------
    // Public Creation API
    //--------------------------------------------------------------------------

    public static LibraryInstrument createInstrument(LibraryProduct product,
            MachineNode machineNode, CausticSound causticSound) {

        //------------------------------

        String name = machineNode.getName();
        if (causticSound != null)
            name = causticSound.getDisplayName() + "$" + name;
        String relativePath = "";

        //------------------------------

        ICaustkFactory factory = CaustkRuntime.getInstance().getFactory();
        LibraryInstrument instrument = factory.createLibraryInstrument(product, name, relativePath,
                machineNode);

        return instrument;
    }

    //--------------------------------------------------------------------------
    // Public State API
    //--------------------------------------------------------------------------

    public static void saveInstrument(LibraryProductItem item, LibraryProduct product,
            File tempDirectory) throws IOException {
        //File location = resolveAbsoluteArchive(node.getInfo());
        String json = CaustkRuntime.getInstance().getFactory().serialize(item, true);

        //String fileName = FilenameUtils.getBaseName(location.getName());
        //File sourceDirectory = new File(RuntimeUtils.getApplicationTempDirectory(), fileName);
        //sourceDirectory.mkdirs();
        String fileName = "preset";
        File stateFile = new File(tempDirectory, "manifest.json");
        FileUtils.writeStringToFile(stateFile, json);

        // XXX construct the archive for the specific node type
        // for now the only special node type is MachineNode that needs
        // the presets directory
        if (item instanceof LibraryInstrument
                && !(((LibraryInstrument)item).getMachineNode() instanceof VocoderMachine)) {
            File presetsDirectory = tempDirectory;
            MachineNode machineNode = ((LibraryInstrument)item).getMachineNode();
            File file = new File(presetsDirectory, fileName + "."
                    + machineNode.getType().getExtension());
            FileUtils.writeByteArrayToFile(file, machineNode.getPreset().getRestoredData());
            //            machineNode.getPreset().exportPreset(presetsDirectory,
            //                    machineNode.getPreset().getName());
            // XXX Must have the bytes for this to work in this state
            // who knows where the rack is and we can't just "save" a preset
            // for a machine we don't even know exists
            //machineNode.getPreset().fill();
        }
    }

    public static LibraryInstrument importInstrument(File soundDirectory) throws CausticException,
            IOException {
        File tempDirectory = new File(soundDirectory, "instrument/");

        File instrumentFile = new File(soundDirectory, "instrument.ginst");
        ZipUncompress uncompress = new ZipUncompress(instrumentFile);
        uncompress.unzip(tempDirectory);

        File manifest = new File(tempDirectory, "manifest.json");
        if (!manifest.exists())
            throw new CausticException("manifest.json does not exist");

        String json = FileUtils.readFileToString(manifest);
        LibraryInstrument libraryInstrument = CaustkRuntime.getInstance().getFactory()
                .deserialize(json, LibraryInstrument.class);
        String type = libraryInstrument.getMachineNode().getType().getExtension();
        libraryInstrument.setPendingPresetFile(new File(tempDirectory, "preset." + type));

        return libraryInstrument;
    }

    public static File toInstrumentFile(File parent) {
        return new File(parent, "instrument.ginst");
    }
}
