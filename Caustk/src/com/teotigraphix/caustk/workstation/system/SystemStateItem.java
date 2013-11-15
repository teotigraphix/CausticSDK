
package com.teotigraphix.caustk.workstation.system;

import com.teotigraphix.caustk.controller.command.ICommand;

public class SystemStateItem {

    private String name;

    private int index;

    private String message;

    private Class<? extends ICommand> command;

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasCommand() {
        return command != null;
    }

    public Class<? extends ICommand> getCommand() {
        return command;
    }

    public SystemStateItem(int index, String name, String message, Class<? extends ICommand> command) {
        this.index = index;
        this.name = name;
        this.message = message;
        this.command = command;
    }

    @Override
    public String toString() {
        return "[SystemStateItem|" + name + "]";
    }

}
