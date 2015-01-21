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

public class CommandUtils {

    public static String getString(CommandContext context, int index) {
        final OSCMessage message = context.getMessage();
        String result = message.getParameter(index);
        return result;
    }

    public static Integer getInteger(CommandContext context, int index) {
        final OSCMessage message = context.getMessage();
        int result = Integer.valueOf(message.getParameter(index));
        return result;
    }

    public static Float getFloat(CommandContext context, int index) {
        final OSCMessage message = context.getMessage();
        float result = Float.valueOf(message.getParameter(index));
        return result;
    }

    public static boolean getBoolean(CommandContext context, int index) {
        final OSCMessage message = context.getMessage();
        String parameter = message.getParameter(index);
        if (parameter.equals("true") || parameter.equals("false"))
            return Boolean.valueOf(parameter);
        final int arg = Integer.valueOf(parameter);
        boolean result = (arg == 0) ? false : true;
        return result;
    }

}
