
package com.teotigraphix.caustk.groove.sequencer;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class NoteMatrixEntry {

    @Tag(0)
    private int index;

    @Tag(1)
    private int column;

    @Tag(2)
    private int row;

    @Tag(3)
    private boolean selected;

    public int getIndex() {
        return index;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean isSelected() {
        return selected;
    }

    void setSelected(boolean selected) {
        this.selected = selected;
    }

    NoteMatrixEntry() {
    }

    public NoteMatrixEntry(int index, int column, int row) {
        this.index = index;
        this.column = column;
        this.row = row;
    }
}
