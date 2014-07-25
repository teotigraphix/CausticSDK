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
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.FileInfo;
import com.teotigraphix.caustk.groove.LibraryInstrument;
import com.teotigraphix.caustk.groove.LibraryItem;
import com.teotigraphix.caustk.groove.LibraryItemManifest;
import com.teotigraphix.caustk.groove.LibraryProduct;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.VocoderMachine;
import com.teotigraphix.caustk.utils.ZipCompress;
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
            MachineNode machineNode, FileInfo fileInfo, LibraryItemManifest manifest) {
        LibraryInstrument instrument = new LibraryInstrument(UUID.randomUUID(), product.getId(),
                fileInfo, manifest);
        instrument.setMachineNode(machineNode);
        return instrument;
    }

    public static LibraryInstrument createInstrument(LibraryProduct product, MachineNode machineNode) {
        LibraryItemManifest manifest = new LibraryItemManifest(machineNode.getName());
        LibraryInstrument instrument = new LibraryInstrument(UUID.randomUUID(), product.getId(),
                null, manifest);
        instrument.setMachineNode(machineNode);
        return instrument;
    }

    //--------------------------------------------------------------------------
    // Public State API
    //--------------------------------------------------------------------------

    public static void saveInstrument(LibraryInstrument instrument, File tempDirectory, File file)
            throws IOException {
        //String json = getFactory().serialize(instrument, true);
        //FileUtils.write(file, json);
        save(instrument, tempDirectory, file);
    }

    public static File save(LibraryItem item, File tempDirectory, File location) throws IOException {
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

        ZipCompress compress = new ZipCompress(tempDirectory);
        compress.zip(location);
        //        try {
        //            Thread.sleep(200);
        //        } catch (InterruptedException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }

        return location;
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
                ._deserialize(json, LibraryInstrument.class);
        String type = libraryInstrument.getMachineNode().getType().getExtension();
        libraryInstrument.setPendingPresetFile(new File(tempDirectory, "preset." + type));

        return libraryInstrument;
    }

    public static File toInstrumentFile(File parent) {
        return new File(parent, "instrument.ginst");
    }
}
