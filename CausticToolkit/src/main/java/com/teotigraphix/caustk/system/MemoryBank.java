
package com.teotigraphix.caustk.system;

import com.teotigraphix.caustk.controller.ICaustkController;

public class MemoryBank extends Memory {

    private String name;

    public String getName() {
        return name;
    }

    public MemoryBank(ICaustkController systemController, Type type, String name) {
        super(systemController, type);
        this.name = name;
    }

}
