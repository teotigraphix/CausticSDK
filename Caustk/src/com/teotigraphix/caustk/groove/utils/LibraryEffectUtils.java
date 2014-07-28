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
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.library.LibraryEffect;
import com.teotigraphix.caustk.groove.library.LibraryProduct;
import com.teotigraphix.caustk.groove.library.LibraryProductItem;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibraryEffectUtils {

    private static final String MANIFEST_JSON = "manifest.json";

    public static LibraryEffect createEffect(LibraryProduct product, MachineNode machineNode,
            CausticSound causticSound) {
        EffectsChannel effects = machineNode.getEffects();
        EffectNode efffect0 = effects.getEfffect(0);
        EffectNode efffect1 = effects.getEfffect(1);

        //------------------------------

        String name = machineNode.getName() + "-FX";
        if (causticSound != null)
            name = causticSound.getEffect().getDisplayName();
        String relativePath = "";

        //------------------------------
        CaustkFactory factory = CaustkRuntime.getInstance().getFactory();
        LibraryEffect libraryEffect = factory.createLibraryEffect(product, name, relativePath,
                efffect0, efffect1);

        return libraryEffect;
    }

    public static void saveEffect(LibraryProductItem item, LibraryProduct product,
            File tempDirectory) throws IOException {
        String json = CaustkRuntime.getInstance().getFactory().serialize(item, true);
        FileUtils.write(new File(tempDirectory, MANIFEST_JSON), json);
    }

    public static LibraryEffect importEffect(File soundDirectory) throws CausticException,
            IOException {
        File tempDirectory = new File(soundDirectory, "effect/");

        File effectFile = new File(soundDirectory, "effect.gfx");
        ZipUncompress uncompress = new ZipUncompress(effectFile);
        uncompress.unzip(tempDirectory);

        File manifest = new File(tempDirectory, MANIFEST_JSON);
        if (!manifest.exists())
            throw new CausticException("manifest.json does not exist");

        String json = FileUtils.readFileToString(manifest);
        LibraryEffect libraryEffect = CaustkRuntime.getInstance().getFactory()
                ._deserialize(json, LibraryEffect.class);

        return libraryEffect;
    }

    public static File toEffectFile(File parent) {
        return new File(parent, "effect.gfx");
    }

}
