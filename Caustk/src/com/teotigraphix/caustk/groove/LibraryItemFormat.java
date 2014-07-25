
package com.teotigraphix.caustk.groove;

public enum LibraryItemFormat {

    Product("gprd"),

    Project("gprj"),

    Group("ggrp"),

    Sound("gsnd"),

    Instrument("ginst"),

    Effect("gfx"),

    Sample("wav");

    private String extension;

    public String getExtension() {
        return extension;
    }

    LibraryItemFormat(String extension) {
        this.extension = extension;
    }

    public static LibraryItemFormat fromString(String extension) {
        for (LibraryItemFormat format : values()) {
            if (format.getExtension().equals(extension))
                return format;
        }
        return null;
    }
}
