
package com.teotigraphix.caustk.groove.importer;

public abstract class CausticItem {

    private boolean export;

    private String path;

    public boolean isExport() {
        return export;
    }

    public String getPath() {
        return path;
    }

    public CausticItem() {
    }

}
