
package com.teotigraphix.caustk.groove.sequencer;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.midi.MidiScale;
import com.teotigraphix.caustk.core.midi.NoteReference;
import com.teotigraphix.caustk.core.midi.ScaleMatrixUtils;

/*

Some application model will hold this instance, listen to the callbacks
and dispatch EventBus application events which mediators will handle

*/

public class NoteMatrix {

    @Tag(0)
    private MidiScale scale;

    @Tag(1)
    private boolean compact;

    @Tag(2)
    private int numColumns;

    @Tag(3)
    private int numRows;

    @Tag(4)
    private float cellWidth;

    @Tag(5)
    private float compactCellWidth;

    @Tag(6)
    private float cellHeight;

    @Tag(7)
    private float compactCellHeight;

    @Tag(8)
    private List<NoteMatrixEntry> entries;

    @Tag(9)
    private int currentColumn;

    //----------------------------------
    // scale
    //----------------------------------

    /**
     * @param scale
     * @see OnNoteMatrixListener#onScaleChanged(MidiScale, MidiScale)
     */
    public void setScale(MidiScale scale) {
        @SuppressWarnings("unused")
        MidiScale oldScale = this.scale;
        //        if (oldScale != null) {
        //            oldScale.setListener(null);
        //        }
        this.scale = scale;
        //        scale.setListener(new OnMidiScaleListener() {
        //            @Override
        //            public void onUpdate(MidiScale midiScale) {
        //                // TODO Impl OnMidiScaleListener#onUpdate()
        //            }
        //        });
        initialize();
        //        if (listener != null) {
        //            listener.onScaleChanged(scale, oldScale);
        //        }
    }

    public MidiScale getScale() {
        return scale;
    }

    //----------------------------------
    // compact
    //----------------------------------

    public boolean isCompact() {
        return compact;
    }

    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    //----------------------------------
    // numColumns
    //----------------------------------

    public int getNumColumns() {
        return numColumns;
    }

    //----------------------------------
    // numRows
    //----------------------------------

    public int getNumRows() {
        return numRows;
    }

    //----------------------------------
    // cellWidth
    //----------------------------------

    public final float getCellWidth() {
        return compact ? compactCellWidth : cellWidth;
    }

    //----------------------------------
    // cellHeight
    //----------------------------------

    public final float getCellHeight() {
        return compact ? compactCellHeight : cellHeight;
    }

    //----------------------------------
    // entries
    //----------------------------------

    public final List<NoteMatrixEntry> getEntries() {
        return entries;
    }

    //----------------------------------
    // currentColumn
    //----------------------------------

    public int getCurrentColumn() {
        return currentColumn;
    }

    /**
     * @param currentColumn
     * @see OnNoteMatrixListener#onCurrentColumnChanged(int)
     */
    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
        //        if (listener != null) {
        //            listener.onCurrentColumnChanged(currentColumn);
        //        }
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    NoteMatrix() {
    }

    public NoteMatrix(int numColumns, int numRows, float cellWidth, float cellHeight,
            MidiScale scale) {
        this.numColumns = numColumns;
        this.numRows = numRows;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        compactCellWidth = cellWidth * 2;
        compactCellHeight = cellHeight * 2;
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

    public final boolean isRootNote(NoteMatrixEntry entry) {
        return isRootNote(entry.getRow());
    }

    public boolean isRootNote(int row) {
        return getNoteName(row) == scale.getNoteReference();
    }

    public final NoteReference getNoteName(int row) {
        NoteMatrixEntry entry = getEntries().get(row);
        return getNoteName(entry);
    }

    final NoteReference getNoteName(NoteMatrixEntry entry) {
        List<Integer> notes = ScaleMatrixUtils.getNotes(scale.getScaleReference(), 0, 16, 16);
        List<NoteReference> nrefs = new ArrayList<NoteReference>();
        NoteReference noteReference = scale.getNoteReference();
        for (Integer baseNumber : notes) {
            baseNumber += noteReference.getBaseNumber();
            if (baseNumber >= 12 && baseNumber < 24)
                baseNumber = baseNumber - 12;
            else if (baseNumber >= 24 && baseNumber < 36)
                baseNumber = baseNumber - 24;
            else if (baseNumber >= 36 && baseNumber < 48)
                baseNumber = baseNumber - 36;

            nrefs.add(NoteReference.getNote(baseNumber));
        }
        return nrefs.get(entry.getRow());
    }

    public final NoteMatrixEntry getEntry(int column, int row) {
        for (NoteMatrixEntry entry : entries) {
            if (entry.getColumn() == column && entry.getRow() == row)
                return entry;
        }
        return null;
    }

    public final NoteMatrixEntry getEntry(float x, float y) {
        int tx = (int)(Math.floor(x / getCellWidth()));
        int ty = (int)(Math.floor(y / getCellHeight()));

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
            //            if (listener != null) {
            //                int pitch = getPitch(entry);
            //                listener.onSelectionChanged(entry, pitch);
            //            }
        }
        return entry;
    }

    public void clear() {
        initialize();
    }

    //    private transient OnNoteMatrixListener listener;
    //
    //    public void setListener(OnNoteMatrixListener listener) {
    //        this.listener = listener;
    //    }
    //
    //    public interface OnNoteMatrixListener {
    //        void onSelectionChanged(NoteMatrixEntry entry, int pitch);
    //
    //        void onScaleChanged(MidiScale scale, MidiScale oldScale);
    //
    //        void onCurrentColumnChanged(int column);
    //    }

}
