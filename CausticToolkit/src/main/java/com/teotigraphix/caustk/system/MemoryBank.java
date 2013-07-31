
package com.teotigraphix.caustk.system;

import com.teotigraphix.caustk.controller.ICaustkController;

public class MemoryBank extends Memory {

    private String name;

    public String getName() {
        return name;
    }

    public MemoryBank(ICaustkController controller, Type type, String name) {
        super(controller, type);
        this.name = name;
    }

}
