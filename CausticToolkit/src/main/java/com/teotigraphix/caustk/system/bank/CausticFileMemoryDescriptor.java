
package com.teotigraphix.caustk.system.bank;

import java.io.File;


public class CausticFileMemoryDescriptor extends MemoryDescriptor {

    //----------------------------------
    // file
    //----------------------------------

    private File file;

    public File getFile() {
        return file;
    }

    public CausticFileMemoryDescriptor(String name, File file) {
        super(name);
        this.file = file;
    }

}
