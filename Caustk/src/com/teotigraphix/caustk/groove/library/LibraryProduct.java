
package com.teotigraphix.caustk.groove.library;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.groove.manifest.LibraryProductManifest;

public class LibraryProduct extends LibraryItem {

    //--------------------------------------------------------------------------
    // Serialized API
    //--------------------------------------------------------------------------

    @Tag(20)
    private LibraryProductManifest manifest;

    // has to describe its entire contents without having
    // the contents extracted, so what do I need to do this?

    @Tag(21)
    private List<LibraryItemDescriptor> list = new ArrayList<LibraryItemDescriptor>();

    //----------------------------------
    // Descriptors
    //----------------------------------

    public Collection<LibraryItemDescriptor> getDescriptors(LibraryItemFormat format) {
        ArrayList<LibraryItemDescriptor> result = new ArrayList<LibraryItemDescriptor>();
        for (LibraryItemDescriptor descriptor : list) {
            if (descriptor.getFormat() == format)
                result.add(descriptor);
        }
        return result;
    }

    public File getSourceDirectory() {
        return getManifest().getArchiveFile();
    }

    LibraryProduct() {
    }

    public LibraryProduct(UUID id, LibraryProductManifest manifest) {
        super(id);
        this.manifest = manifest;
        setFormat(LibraryItemFormat.Product);
    }

    public LibraryItemDescriptor addItem(LibraryProductItem item) throws CausticException {
        // if (map.containsKey(libraryItem.getId()))
        //    throw new CausticException("Product contains item.");

        LibraryItemDescriptor descriptor = new LibraryItemDescriptor(item);
        list.add(descriptor);

        return descriptor;
    }
}
