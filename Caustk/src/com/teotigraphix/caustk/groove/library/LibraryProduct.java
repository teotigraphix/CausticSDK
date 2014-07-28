
package com.teotigraphix.caustk.groove.library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.groove.manifest.LibraryItemManifest;
import com.teotigraphix.caustk.groove.manifest.LibraryProductManifest;
import com.teotigraphix.caustk.groove.utils.LibraryProductUtils;

public class LibraryProduct extends LibraryItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(20)
    private LibraryProductManifest manifest;

    // has to describe its entire contents without having
    // the contents extracted, so what do I need to do this?

    @Tag(21)
    private List<LibraryItemManifest> list = new ArrayList<LibraryItemManifest>();

    @Override
    public LibraryProductManifest getManifest() {
        return manifest;
    }

    public final File getDirectory() {
        return manifest.getDirectory();
    }

    //----------------------------------
    // Descriptors
    //----------------------------------

    public Collection<LibraryItemManifest> getDescriptors(LibraryItemFormat format) {
        ArrayList<LibraryItemManifest> result = new ArrayList<LibraryItemManifest>();
        for (LibraryItemManifest manifest : list) {
            if (manifest.getFormat() == format)
                result.add(manifest);
        }
        return result;
    }

    LibraryProduct() {
    }

    public LibraryProduct(UUID id, LibraryProductManifest manifest) {
        super();
        this.manifest = manifest;
    }

    public LibraryItemManifest addItem(LibraryProductItem item) throws CausticException {
        // if (map.containsKey(libraryItem.getId()))
        //    throw new CausticException("Product contains item.");

        LibraryItemManifest manifest = item.getManifest();
        list.add(manifest);

        return manifest;
    }

    public final boolean exists() {
        return manifest.exists();
    }

    public final void create() throws IOException {
        if (exists())
            throw new IOException("Directory exists; " + getDirectory());

        FileUtils.forceMkdir(getDirectory());
    }

    public final void destroy() throws IOException {
        if (!exists())
            throw new IOException("Directory does not exist; " + getDirectory());

        FileUtils.forceDelete(getDirectory());
    }

    public final File resolveInternalArchive(LibraryProductItem item) {
        File archive = new File(getDirectory(), item.getProductPath().getPath());
        return archive;
    }

    public void saveItem(LibraryProductItem item) throws IOException {
        LibraryProductUtils.addArchiveToProduct(item, this);
    }
}
