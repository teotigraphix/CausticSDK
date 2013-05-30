package com.teotigraphix.caustk.controller.command;

public interface ICommandManager {

    void execute(String message, Object ... args);

    int undo();

    int redo();

    void put(String message, Class<?> command);

    void clearHistory();

}
