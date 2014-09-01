
package com.teotigraphix.caustk.groove.sequencer;

public class NoteMatrixEntry {

    private int index;

    private int column;

    private int row;

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

    public NoteMatrixEntry(int index, int column, int row) {
        this.index = index;
        this.column = column;
        this.row = row;
    }
}
