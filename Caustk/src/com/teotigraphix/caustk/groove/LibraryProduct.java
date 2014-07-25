
package com.teotigraphix.caustk.groove;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.teotigraphix.caustk.core.CausticException;

public class LibraryProduct extends LibraryItem {

    // has to describe its entire contents without having
    // the contents extracted, so what do I need to do this?

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
        return getFileInfo().getFile();
    }

    LibraryProduct() {
    }

    public LibraryProduct(UUID id, FileInfo fileInfo, LibraryItemManifest manifest) {
        super(id, fileInfo, manifest);
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
