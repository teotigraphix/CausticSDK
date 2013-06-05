
package com.teotigraphix.caustk.system.bank;

/**
 * Abstract base class for all {@link MemorySlot} items which include pattern,
 * song, patch, phrase etc.
 * <p>
 * The {@link MemorySlotItem} is a value object that is given state when it is
 * created and this state does not change throughout the application. The reason
 * for this is all memory items are loaded from disk and copied into the
 * {@link TemporaryMemory} when edits need to occur, they are read-only objects.
 */
public abstract class MemorySlotItem {
    private int index = -1;

    public int getIndex() {
        if (index == -1)
            throw new RuntimeException("MemorySlotItem index has not been set");
        return index;
    }

    /**
     * Sets the index of the item.
     * <p>
     * The index will be set when the item is added to the {@link MemorySlot}.
     * 
     * @param value The index within a {@link MemorySlot}.
     */
    public void setIndex(int value) {
        index = value;
    }

    public MemorySlotItem() {
    }

    @Override
    public String toString() {
        return "MemorySlotItem[" + index + "]";
    }
}
