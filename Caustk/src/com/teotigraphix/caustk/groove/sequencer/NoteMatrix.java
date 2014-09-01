
package com.teotigraphix.caustk.groove.sequencer;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.core.midi.MidiScale;
import com.teotigraphix.caustk.core.midi.MidiScale.OnMidiScaleListener;

/*

Some application model will hold this instance, listen to the callbacks
and dispatch EventBus application events which mediators will handle

*/

public class NoteMatrix {

    private MidiScale scale;

    private int numColumns;

    private int numRows;

    private float cellWidth;

    private float cellHeight;

    private List<NoteMatrixEntry> entries;

    private int currentColumn;

    /**
     * @param scale
     * @see OnNoteMatrixListener#onScaleChanged(MidiScale, MidiScale)
     */
    public void setScale(MidiScale scale) {
        MidiScale oldScale = this.scale;
        if (oldScale != null) {
            oldScale.setListener(null);
        }
        this.scale = scale;
        scale.setListener(new OnMidiScaleListener() {
            @Override
            public void onUpdate(MidiScale midiScale) {
                // TODO Impl OnMidiScaleListener#onUpdate()
            }
        });
        initialize();
        if (listener != null) {
            listener.onScaleChanged(scale, oldScale);
        }
    }

    public MidiScale getScale() {
        return scale;
    }

    public final List<NoteMatrixEntry> getEntries() {
        return entries;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    /**
     * @param currentColumn
     * @see OnNoteMatrixListener#onCurrentColumnChanged(int)
     */
    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
        if (listener != null) {
            listener.onCurrentColumnChanged(currentColumn);
        }
    }

    public NoteMatrix(int numColumns, int numRows, float cellWidth, float cellHeight,
            MidiScale scale) {
        this.numColumns = numColumns;
        this.numRows = numRows;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        setScale(scale);
    }

    private void initialize() {
        entries = new ArrayList<NoteMatrixEntry>();
        int index = 0;
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                NoteMatrixEntry entry = new NoteMatrixEntry(index, i, j);
                entries.add(entry);
                index++;
            }
        }
    }

    public final NoteMatrixEntry getEntry(int column, int row) {
        for (NoteMatrixEntry entry : entries) {
            if (entry.getColumn() == column && entry.getRow() == row)
                return entry;
        }
        return null;
    }

    public final NoteMatrixEntry getEntry(float x, float y) {
        int tx = (int)(Math.floor(x / cellWidth));
        int ty = (int)(Math.floor(y / cellHeight));
        return getEntry(tx, ty);
    }

    public final int getPitch(NoteMatrixEntry entry) {
        return scale.getIntervals().get(entry.getRow());
    }

    /**
     * Updates the selection of an entry located at the x and y.
     * 
     * @param selected
     * @param x
     * @param y
     * @see OnNoteMatrixListener#onSelectionChanged(NoteMatrixEntry)
     */
    public NoteMatrixEntry setSelected(boolean selected, float x, float y) {
        final NoteMatrixEntry entry = getEntry(x, y);
        if (entry.isSelected() != selected) {
            entry.setSelected(selected);
            if (listener != null) {
                int pitch = getPitch(entry);
                listener.onSelectionChanged(entry, pitch);
            }
        }
        return entry;
    }

    public void clear() {
        initialize();
    }

    private OnNoteMatrixListener listener;

    public void setListener(OnNoteMatrixListener listener) {
        this.listener = listener;
    }

    public interface OnNoteMatrixListener {
        void onSelectionChanged(NoteMatrixEntry entry, int pitch);

        void onScaleChanged(MidiScale scale, MidiScale oldScale);

        void onCurrentColumnChanged(int column);
    }
}
