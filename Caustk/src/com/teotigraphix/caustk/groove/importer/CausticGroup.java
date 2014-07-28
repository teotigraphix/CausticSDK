
package com.teotigraphix.caustk.groove.importer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.core.CaustkFactory;
import com.teotigraphix.caustk.core.CaustkRuntime;
import com.teotigraphix.caustk.groove.library.LibraryGroup;
import com.teotigraphix.caustk.groove.library.LibraryProduct;

/**
 * Imported from a .caustic file.
 */
public class CausticGroup extends CausticItem {

    private String name;

    private File sourceFile;

    private String displayName;

    Map<Integer, CausticSound> sounds = new HashMap<Integer, CausticSound>();

    public String getName() {
        return name;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<Integer, CausticSound> getSounds() {
        return sounds;
    }

    public CausticGroup(File sourceFile, String displayName) {
        this.sourceFile = sourceFile;
        this.displayName = displayName;

    }

    public CausticSound addSound(int index, String soundName, String effectName) {
        CausticSound machine = new CausticSound(index, soundName, effectName);
        sounds.put(index, machine);
        return machine;
    }

    public LibraryGroup create(LibraryProduct product) {
        LibraryGroup libraryGroup = getFactory().createLibraryGroup(product, getDisplayName(),
                getPath());
        libraryGroup.setCausticGroup(this);
        return libraryGroup;
    }

    private CaustkFactory getFactory() {
        return CaustkRuntime.getInstance().getFactory();
    }

}
