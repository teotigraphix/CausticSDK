
package com.teotigraphix.gdx.controller;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class ViewBase {

    @Tag(0)
    private int id;

    private int index;

    private String label;

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    ViewBase() {
    }

    public ViewBase(int id, int index, String label) {
        this.id = id;
        this.index = index;
        this.label = label;
    }

}
