
package com.teotigraphix.gdx.groove.ui.model;

import java.util.ArrayList;
import java.util.List;

public class MainMode {

    private static List<MainMode> modes = new ArrayList<MainMode>();

    public static MainMode create(int index, String id, String label) {
        MainMode mode = new MainMode(index, id, label);
        modes.add(mode);
        return mode;
    }

    public static List<MainMode> values() {
        return new ArrayList<MainMode>(modes);
    }

    private int index;

    private String id;

    private String label;

    public int getIndex() {
        return index;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    protected MainMode(int index, String id, String label) {
        this.index = index;
        this.id = id;
        this.label = label;
    }

    public static MainMode fromId(String id) {
        for (MainMode mainMode : values()) {
            if (mainMode.getId().equals(id))
                return mainMode;
        }
        return null;
    }

    public static MainMode fromIndex(int index) {
        for (MainMode mainMode : values()) {
            if (mainMode.getIndex() == index)
                return mainMode;
        }
        return null;
    }
}
