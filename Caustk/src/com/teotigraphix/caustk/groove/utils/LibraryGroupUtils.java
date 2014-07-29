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
import com.teotigraphix.caustk.core.CaustkFactory;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.importer.CausticEffect;
import com.teotigraphix.caustk.groove.importer.CausticGroup;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryInstrument;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibrarySound;
import com.teotigraphix.caustk.node.RackNode;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibraryGroupUtils {

    public static void fillGroup(LibraryProduct product, LibraryGroup libraryGroup)
            throws CausticException {

        File causticFile = libraryGroup.getCausticGroup().getSourceFile();
        RackNode rackNode = CaustkRuntime.getInstance().getRack().create(causticFile);

        for (MachineNode machineNode : rackNode.getMachines()) {

            LibrarySound librarySound = fillSound(product, libraryGroup, machineNode);
            libraryGroup.addSound(machineNode.getIndex(), librarySound);
        }
    }

    public static void saveGroup(LibraryGroup item, LibraryProduct product, File tempDirectory)
            throws IOException {

        for (LibrarySound librarySound : item.getSounds()) {
            File tempSoundDir = new File(tempDirectory, "sounds/sound-" + librarySound.getIndex());
            LibrarySoundUtils.saveSound(librarySound, product, tempSoundDir);

            ZipCompress compress = new ZipCompress(tempSoundDir);
            File zipFile = new File(tempDirectory, "sounds/sound-" + librarySound.getIndex()
                    + ".gsnd");
            compress.zip(zipFile);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            FileUtils.forceDelete(tempSoundDir);
        }

        String json = CaustkRuntime.getInstance().getFactory().serialize(item, true);
        FileUtils.write(new File(tempDirectory, "manifest.json"), json);
    }

    public static LibraryGroup importGroup(File sourceFile) throws CausticException, IOException {
        File tempDirectory = new File(RuntimeUtils.getApplicationTempDirectory(), "__Group__");
        tempDirectory.mkdirs();

        ZipUncompress uncompress = new ZipUncompress(sourceFile);
        uncompress.unzip(tempDirectory);

        File manifest = new File(tempDirectory, "manifest.json");
        if (!manifest.exists())
            throw new CausticException("manifest does not exist");

        String json = FileUtils.readFileToString(manifest);
        LibraryGroup libraryGroup = CaustkRuntime.getInstance().getFactory()
                ._deserialize(json, LibraryGroup.class);

        for (LibrarySound librarySound : libraryGroup.getSounds()) {
            librarySound.setGroup(libraryGroup);
            LibrarySoundUtils.importSound(librarySound, tempDirectory);
        }

        return libraryGroup;
    }

    //--------------------------------------------------------------------------

    private static LibrarySound fillSound(LibraryProduct product, LibraryGroup libraryGroup,
            MachineNode machineNode) {
        CausticGroup causticGroup = libraryGroup.getCausticGroup();
        CausticSound causticSound = libraryGroup.getCausticGroup().getSounds()
                .get(machineNode.getIndex());

        String groupName = causticGroup.getDisplayName();
        String name = groupName + "-" + machineNode.getName();
        String relativePath = "";
        if (causticSound != null) {
            name = causticSound.getDisplayName();
            relativePath = causticSound.getPath();
        }

        LibraryEffect libraryEffect = fillEffect(machineNode.getEffects(), product,
                machineNode.getName(), groupName, causticSound);
        LibraryInstrument libraryInstrument = fillInstrument(machineNode, product, name, groupName);

        LibrarySound librarySound = getFactory().createLibrarySound(product, name, relativePath);

        librarySound.setEffect(libraryEffect);
        librarySound.setInstrument(libraryInstrument);

        return librarySound;
    }

    private static LibraryEffect fillEffect(EffectsChannel effectsChannel, LibraryProduct product,
            String machineName, String groupName, CausticSound causticSound) {
        String name = machineName + " FX";
        String relativePath = groupName;
        if (causticSound != null) {
            CausticEffect causticEffect = causticSound.getEffect();
            if (causticEffect != null) {
                name = causticEffect.getDisplayName();
                relativePath = causticEffect.getPath();
            }
        }

        EffectNode efffect0 = effectsChannel.getEfffect(0);
        EffectNode efffect1 = effectsChannel.getEfffect(1);

        LibraryEffect libraryEffect = getFactory().createLibraryEffect(product, name, relativePath,
                efffect0, efffect1);

        return libraryEffect;
    }

    private static LibraryInstrument fillInstrument(MachineNode machineNode,
            LibraryProduct product, String name, String groupName) {
        String relativePath = groupName;
        LibraryInstrument libraryInstrument = getFactory().createLibraryInstrument(product, name,
                relativePath, machineNode);

        return libraryInstrument;
    }

    private static CaustkFactory getFactory() {
        return CaustkRuntime.getInstance().getFactory();
    }
}
