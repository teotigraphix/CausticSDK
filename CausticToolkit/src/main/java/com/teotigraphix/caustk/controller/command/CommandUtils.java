
package com.teotigraphix.caustk.controller.command;

public class CommandUtils {

    public static String getString(CommandContext context, int index) {
        String result = context.getMessage().getParameters().get(index);
        return result;
    }

    public static Integer getInteger(CommandContext context, int index) {
        int result = Integer.valueOf(context.getMessage().getParameters().get(index));
        return result;
    }

    public static Float getFloat(CommandContext context, int index) {
        float result = Float.valueOf(context.getMessage().getParameters().get(index));
        return result;
    }

}
