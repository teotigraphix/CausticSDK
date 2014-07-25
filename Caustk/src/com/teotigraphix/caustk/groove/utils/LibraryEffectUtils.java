
package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.FileInfo;
import com.teotigraphix.caustk.groove.LibraryEffect;
import com.teotigraphix.caustk.groove.LibraryItemManifest;
import com.teotigraphix.caustk.groove.LibraryProduct;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;

public class LibraryEffectUtils {

    public static LibraryEffect createEffect(LibraryProduct product, MachineNode machineNode) {
        EffectsChannel effects = machineNode.getEffects();
        EffectNode efffect0 = effects.getEfffect(0);
        EffectNode efffect1 = effects.getEfffect(1);

        FileInfo fileInfo = new FileInfo(null);
        LibraryItemManifest manifest = new LibraryItemManifest(machineNode.getName() + "-FX");
        LibraryEffect effect = new LibraryEffect(UUID.randomUUID(), product.getId(), fileInfo,
                manifest);

        if (efffect0 != null) {
            effect.add(0, efffect0);
        }
        if (efffect1 != null) {
            effect.add(1, efffect1);
        }
        return effect;
    }

    public static void saveEffect(LibraryEffect effect, File file) throws IOException {
        String json = CaustkRuntime.getInstance().getFactory().serialize(effect, true);
        FileUtils.write(file, json);
    }

    public static LibraryEffect importEffect(File soundDirectory) throws CausticException,
            IOException {
        File manifest = new File(soundDirectory, "effect.gfx");
        if (!manifest.exists())
            throw new CausticException("effect.gfx does not exist");

        String json = FileUtils.readFileToString(manifest);
        LibraryEffect libraryEffect = CaustkRuntime.getInstance().getFactory()
                ._deserialize(json, LibraryEffect.class);
        return libraryEffect;
    }

    public static File toEffectFile(File parent) {
        return new File(parent, "effect.gfx");
    }

}
