
package com.teotigraphix.caustk.gs.model.system;

import com.badlogic.gdx.utils.Array;

public class SystemState {

    private Array<SystemStateItem> items = new Array<SystemStateItem>();

    public Array<SystemStateItem> getItems() {
        return items;
    }

    private String name;

    private int index;

    public int getIndex() {
        return index;
    }

    public int getItemCount() {
        return items.size;
    }

    public String getName() {
        return name;
    }

    public SystemState(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public SystemStateItem getItem(int index) {
        return items.get(index);
    }

    public void addItem(String name) {
        int index = items.size;
        SystemStateItem item = new SystemStateItem(index, name);
        items.add(item);
    }

    @Override
    public String toString() {
        return "[SystemState|" + name + "]";
    }

}
