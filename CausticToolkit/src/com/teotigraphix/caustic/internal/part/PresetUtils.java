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

package com.teotigraphix.caustic.internal.part;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.internal.XMLMemento;
import com.teotigraphix.common.utils.Compress;
import com.teotigraphix.common.utils.Decompress;
import com.teotigraphix.common.utils.RuntimeUtils;

public class PresetUtils {

    private static final String DOT = ".";

    private static final String MANIFEST_XML = "manifest.xml";

    private static final String TEMP = "__temp__";

    public static File createPresetTempDirectory(String presetName, IMachine machine,
            File destination) {
        // create a dir in temp
        File tempRoot = new File(RuntimeUtils.getExternalStorageDirectory(), TEMP);
        tempRoot.mkdir();
        File presetsFile = savePreset(presetName, machine);
        IMemento memento = XMLMemento.createWriteRoot("tone");
        machine.copy(memento);
        File mementoFile = new File(tempRoot, MANIFEST_XML);
        try {
            RuntimeUtils.saveMemento(mementoFile, memento);
            // put the preset file at the root
            FileUtils.copyFile(presetsFile, new File(tempRoot, presetsFile.getName()));
            // put the XML at the root
            //FileUtils.copyFile(mementoFile, tempRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempRoot;
    }

    private static IOFileFilter filter = new IOFileFilter() {
        @Override
        public boolean accept(File arg0, String arg1) {
            return true;
        }

        @Override
        public boolean accept(File arg0) {
            return true;
        }
    };

    public static File createPresetFile(String presetName, IMachine machine, File destination) {
        File root = createPresetTempDirectory(presetName, machine, destination);
        Collection<File> files = FileUtils.listFiles(root, filter, filter);
        Compress compress = new Compress(files, destination.getAbsolutePath());
        compress.zip();
        try {
            FileUtils.deleteDirectory(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }

    public static File savePreset(String name, IMachine machine) {
        machine.savePreset(name);
        File presetsFile = RuntimeUtils.getCausticPresetsFile(machine.getType().getValue(), name);
        return presetsFile;
    }

    public static void loadPresetFile(File presetFile, File unzipLocation, IMachine machine) {
        Decompress decompress = new Decompress(presetFile.getAbsolutePath(),
                unzipLocation.getAbsolutePath());
        decompress.unzip();
        // get the memeto
        IMemento memento = null;
        try {
            memento = RuntimeUtils.loadMemento(new File(unzipLocation, MANIFEST_XML));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        machine.paste(memento);
        machine.loadPreset(new File(unzipLocation, machine.getId()).getAbsolutePath() + DOT
                + machine.getType().getValue());
    }
}
