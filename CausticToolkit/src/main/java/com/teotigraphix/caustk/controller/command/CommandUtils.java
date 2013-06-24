
package com.teotigraphix.caustk.controller.command;

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

}
