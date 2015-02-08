
package com.teotigraphix.caustk.groove.library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.internal.CaustkRuntime;
import com.teotigraphix.caustk.groove.importer.CausticSound;
import com.teotigraphix.caustk.groove.manifest.LibraryGroupManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;
import com.teotigraphix.caustk.node.machine.Machine;
import com.teotigraphix.caustk.utils.groove.LibraryGroupUtils;
import com.teotigraphix.caustk.utils.groove.LibraryPatternBankUtils;
import com.teotigraphix.caustk.utils.groove.LibraryProductUtils;
import com.teotigraphix.caustk.utils.groove.LibrarySoundUtils;

public class LibraryProductAccess {

    private LibraryProduct product;

    //----------------------------------
    // descriptors
    //----------------------------------

    /**
     * Returns a list of {@link LibraryItemManifest} instances that match the
     * {@link LibraryItemFormat}.
     * <p>
     * The list order is not relevant.
     * 
     * @param format The format to match manifest instances against.
     * @return A list of manifests matching the passed format.
     */
    public List<LibraryItemManifest> getDescriptors(LibraryItemFormat format) {
        ArrayList<LibraryItemManifest> result = new ArrayList<LibraryItemManifest>();
        for (LibraryItemManifest manifest : product.getMap().values()) {
            if (manifest.getFormat() == format)
                result.add(manifest);
        }
        return result;
    }

    public LibraryProductAccess(LibraryProduct product) {
        this.product = product;
    }

    public LibrarySound importSound(LibraryItemManifest manifest) throws CausticException,
            IOException {
        File uncompressDirectory = getTempChangeDirectory();
        File archive = product.resolveInternalArchive(manifest);
        LibrarySound sound = LibrarySoundUtils.importSound(uncompressDirectory, archive);
        FileUtils.forceDeleteOnExit(uncompressDirectory);
        return sound;
    }

    private static File getTempChangeDirectory() {
        return CaustkRuntime.getInstance().getFactory()
                .getCacheDirectory("extract/" + UUID.randomUUID().toString());
    }

    /**
     * Returns an array of {@link CausticSound}s sorted in display name order.
     * 
     * @throws IOException
     * @throws CausticException
     */
    public Array<CausticSound> getSounds() throws IOException, CausticException {
        Array<CausticSound> result = new Array<CausticSound>();
        for (LibraryItemManifest manifest : getDescriptors(LibraryItemFormat.Sound)) {
            CausticSound causticSound = readXMLSoundManifest(manifest);
            result.add(causticSound);

            // resolve the sound archive absolute location
            File archiveFile = product.resolveInternalArchive(manifest);

            // extract full sound into directory, effect.gfx, instrument.ginst, manifest.xml
            // patterns.gptbk, sound.bin
            File uncompressDirectory = getTempChangeDirectory();
            FileUtils.forceMkdir(uncompressDirectory);

            LibrarySound sound = LibrarySoundUtils.importSound(uncompressDirectory, archiveFile);

            Machine machine = sound.getInstrument().getMachine();
            causticSound.getInstrument().setMachine(machine);
            File presetFile = sound.getInstrument().getPendingPresetFile();
            machine.getPreset().fill(presetFile);
            causticSound.getPatternBank().setPatterns(sound.getPatternBank().getPatterns());
            causticSound.getEffect().putEffect(0, sound.getEffect().get(0));
            causticSound.getEffect().putEffect(1, sound.getEffect().get(1));
        }

        Comparator<CausticSound> comparator = new Comparator<CausticSound>() {
            @Override
            public int compare(CausticSound o1, CausticSound o2) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            }
        };
        result.sort(comparator);
        return result;
    }

    //    public Array<CausticPatternBank> getPatterns() {
    //        
    //    }

    //--------------------------------------------------------------------------

    @SuppressWarnings("unused")
    private LibraryGroup _loadGroup(String name) throws CausticException, IOException {
        List<LibraryItemManifest> descriptors = getDescriptors(LibraryItemFormat.Group);
        LibraryGroupManifest groupManifest = null;
        LibraryGroup group = null;
        for (LibraryItemManifest manifest : descriptors) {
            if (manifest.getName().equals(name)) {
                groupManifest = (LibraryGroupManifest)manifest;
                break;
            }
        }

        if (groupManifest != null) {
            group = _loadGroup(groupManifest);
        }

        return group;
    }

    private LibraryGroup _loadGroup(LibraryItemManifest manifest) throws CausticException,
            IOException {
        File groupArchive = new File(product.getDirectory(), manifest.getProductPath());
        File uncompressDirectory = null;
        LibraryGroup instance = LibraryGroupUtils.importGroup(groupArchive, uncompressDirectory);
        return instance;
    }

    @SuppressWarnings("unused")
    private LibraryProductItem _createFromManifest(LibraryItemManifest libraryItemManifest)
            throws CausticException, IOException {
        File archive = libraryItemManifest.getAbsoluteProductPath(product);
        if (!archive.exists())
            throw new IOException("Archive doe snot exist: " + archive);
        File uncompressDirectory = CaustkRuntime.getInstance().getFactory()
                .getCacheDirectory("imports/" + UUID.randomUUID());
        LibraryPatternBank instance = LibraryPatternBankUtils.importPatternBank(
                uncompressDirectory, archive);
        return instance;
    }

    private CausticSound readXMLSoundManifest(LibraryItemManifest manifest) throws IOException {
        File archive = product.resolveInternalArchive(manifest);
        CausticSound instance = LibraryProductUtils.readXMLManifest(product, archive,
                CausticSound.class);
        instance.setManifest(manifest);
        return instance;
    }
}
