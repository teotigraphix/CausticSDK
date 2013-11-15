
package com.teotigraphix.caustk.workstation.system;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.controller.command.ICommand;

public class SystemState {

    private List<SystemStateItem> items = new ArrayList<SystemStateItem>();

    public List<SystemStateItem> getItems() {
        return items;
    }

    private String name;

    private int index;

    public int getIndex() {
        return index;
    }

    public int getItemCount() {
        return items.size();
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

    public void addItem(String name, String message, Class<? extends ICommand> command) {
        int index = items.size();
        SystemStateItem item = new SystemStateItem(index, name, message, command);
        items.add(item);
    }

    @Override
    public String toString() {
        return "[SystemState|" + name + "]";
    }

}
