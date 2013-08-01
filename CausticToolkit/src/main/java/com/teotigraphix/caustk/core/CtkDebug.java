
package com.teotigraphix.caustk.core;

public final class CtkDebug {
    
    public static void model(String message) {
        System.out.println(message);
    }

    public static void view(String message) {
        System.out.println(message);
    }

    public static void err(String message) {
        System.err.println(message);
    }
    
    public static void log(String message) {
        System.out.println(message);
    }

    public static void osc(String message) {
        System.out.println("OSC: " + message);
    }

}
