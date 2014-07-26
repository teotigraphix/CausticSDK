
package com.teotigraphix.caustk.groove.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.FileInfo;
import com.teotigraphix.caustk.groove.LibraryEffect;
import com.teotigraphix.caustk.groove.LibraryProduct;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.manifest.LibraryEffectManifest;
import com.teotigraphix.caustk.node.effect.EffectNode;
import com.teotigraphix.caustk.node.effect.EffectsChannel;
import com.teotigraphix.caustk.node.machine.MachineNode;
import com.teotigraphix.caustk.utils.ZipCompress;
import com.teotigraphix.caustk.utils.ZipUncompress;

public class LibraryEffectUtils {

    public static LibraryEffect createEffect(LibraryProduct product, MachineNode machineNode,
            CausticSound causticSound) {
        EffectsChannel effects = machineNode.getEffects();
        EffectNode efffect0 = effects.getEfffect(0);
        EffectNode efffect1 = effects.getEfffect(1);

        String displayName = machineNode.getName() + "-FX";
        if (causticSound != null)
            displayName = causticSound.getEffect().getDisplayName();

        FileInfo fileInfo = new FileInfo(null);

        LibraryEffectManifest manifest = new LibraryEffectManifest(displayName, efffect0, efffect1);
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

    public static void saveEffect(LibraryEffect effect, File sourceDirectory, File zipFile)
            throws IOException {
        String json = CaustkRuntime.getInstance().getFactory().serialize(effect, true);
        FileUtils.write(new File(sourceDirectory, "manifest.json"), json);
        ZipCompress compress = new ZipCompress(sourceDirectory);
        compress.zip(zipFile);
    }

    public static LibraryEffect importEffect(File soundDirectory) throws CausticException,
            IOException {
        File tempDirectory = new File(soundDirectory, "effect/");

        File effectFile = new File(soundDirectory, "effect.gfx");
        ZipUncompress uncompress = new ZipUncompress(effectFile);
        uncompress.unzip(tempDirectory);

        File manifest = new File(tempDirectory, "manifest.json");
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
