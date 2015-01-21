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
import com.teotigraphix.caustk.core.ICaustkFactory;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.groove.importer.CausticInstrument;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.node.machine.VocoderMachine;
import com.teotigraphix.caustk.utils.core.SerializeUtils;
import com.teotigraphix.caustk.utils.core.ZipUncompress;
import com.thoughtworks.xstream.XStream;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public class LibraryInstrumentUtils {

    private static final String SOUND_INSTRUMENT_ARCHIVE = "instrument.ginst";

    private static final String SOUND_INSTRUMENT_DIR = "instrument/";

    private static final String PRESET = "preset";

    private static final String INSTRUMENT_BIN = "instrument.bin";

    public static void configureXStream(XStream xstream) {
        xstream.alias("instrument", CausticInstrument.class);
        xstream.useAttributeFor(CausticInstrument.class, "type");
    }

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
        LibraryInstrument instrument = factory.getLibraryFactory().createInstrument(product, name,
                relativePath, machineNode);

        return instrument;
    }

    //--------------------------------------------------------------------------
    // Public State API
    //--------------------------------------------------------------------------

    public static void serialize(LibraryInstrument item, LibraryProduct product, File tempDirectory)
            throws IOException {

        FileUtils.writeStringToFile(new File(tempDirectory, LibraryProductUtils.MANIFEST_XML),
                LibraryProductUtils.toInstrumentXML(item));
        SerializeUtils.pack(new File(tempDirectory, INSTRUMENT_BIN), item);

        // XXX construct the archive for the specific node type
        // for now the only special node type is MachineNode that needs
        // the presets directory
        if (item instanceof LibraryInstrument && !(item.getMachineNode() instanceof VocoderMachine)) {
            File presetsDirectory = tempDirectory;
            MachineNode machineNode = item.getMachineNode();
            File file = new File(presetsDirectory, PRESET + "."
                    + machineNode.getType().getExtension());
            FileUtils.writeByteArrayToFile(file, machineNode.getPreset().getRestoredData());
        }
    }

    public static LibraryInstrument importInstrumentFromSoundDirectory(File soundDirectory)
            throws CausticException, IOException {
        File uncompressDirectory = new File(soundDirectory, SOUND_INSTRUMENT_DIR);
        File instrumentFile = new File(soundDirectory, SOUND_INSTRUMENT_ARCHIVE);
        return importInstrument(uncompressDirectory, instrumentFile);
    }

    public static LibraryInstrument importInstrument(File uncompressDirectory, File instrumentFile)
            throws CausticException, IOException {
        ZipUncompress uncompress = new ZipUncompress(instrumentFile);
        uncompress.unzip(uncompressDirectory);

        File manifest = new File(uncompressDirectory, INSTRUMENT_BIN);
        if (!manifest.exists())
            throw new CausticException(INSTRUMENT_BIN + " does not exist");

        LibraryInstrument libraryInstrument = SerializeUtils.unpack(manifest,
                LibraryInstrument.class);

        String type = libraryInstrument.getMachineNode().getType().getExtension();
        libraryInstrument.setPendingPresetFile(new File(uncompressDirectory, "preset." + type));

        return libraryInstrument;
    }

}
