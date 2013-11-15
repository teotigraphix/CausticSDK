
package com.teotigraphix.caustk.gs.model.system;

public class SystemStateItem {

    private String name;

    private int index;

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public SystemStateItem(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public String toString() {
        return "[SystemStateItem|" + name + "]";
    }
}
