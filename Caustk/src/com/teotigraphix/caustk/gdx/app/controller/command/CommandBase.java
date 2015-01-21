////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.gdx.app.controller.command;

public abstract class CommandBase implements ICommand {

    private CommandContext context;

    @Override
    public CommandContext getContext() {
        return context;
    }

    public void setContext(CommandContext value) {
        context = value;
    }

    public final Boolean getBoolean(int index) {
        return CommandUtils.getBoolean(context, index);
    }

    public final Integer getInteger(int index) {
        return CommandUtils.getInteger(context, index);
    }

    public final Float getFloat(int index) {
        return CommandUtils.getFloat(context, index);
    }

    public final String getString(int index) {
        return CommandUtils.getString(context, index);
    }

    public CommandBase() {
    }

    @Override
    public abstract void execute() throws CommandExecutionException;

}
