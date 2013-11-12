
package com.teotigraphix.caustk.gs.controller;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.controller.command.ICommand;

public interface IFunctionController {

    Array<FunctionGroup> getFunctionGroups();

    void setFunctionGroups(Array<FunctionGroup> value);

    void execute(IKeyFunction key);

    public static class FunctionGroupItem {

        private IKeyFunction function;

        private int index;

        public int getIndex() {
            return index;
        }

        public IKeyFunction getFunction() {
            return function;
        }

        private boolean autoExecute;

        public boolean isAutoExecute() {
            return autoExecute;
        }

        private Class<? extends ICommand> command;

        public Class<? extends ICommand> getCommand() {
            return command;
        }

        public String getName() {
            return function.getTitle();
        }

        public FunctionGroupItem(int index, IKeyFunction function,
                Class<? extends ICommand> command, boolean autoExecute) {
            this.index = index;
            this.function = function;
            this.command = command;
            this.autoExecute = autoExecute;
        }
    }

    public static class FunctionGroup {

        private Array<FunctionGroupItem> functions = new Array<FunctionGroupItem>();

        public Array<FunctionGroupItem> getFunctions() {
            return functions;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void addItem(IKeyFunction function, Class<? extends ICommand> command,
                boolean autoExecute) {
            int index = getFunctions().size;
            FunctionGroupItem item = new FunctionGroupItem(index, function, command, autoExecute);
            getFunctions().add(item);
        }

        public FunctionGroup(String name) {
            this.name = name;
        }
    }

}
