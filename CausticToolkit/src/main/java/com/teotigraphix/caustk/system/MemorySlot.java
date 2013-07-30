
package com.teotigraphix.caustk.system;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.system.Memory.Category;

/**
 * A {@link MemorySlot} holds items such as patterns, songs, patches and phrase
 * that were loaded during startup.
 * <p>
 * Each slot is independent of the memory bank. Each {@link Memory} instance IE
 * for each {@link Memory.Type} there exists and instance of {@link Memory}.
 * <p>
 * A slot is held within the instance of {@link Memory}, accessed through the
 */
public class MemorySlot {
    private List<MemorySlotItem> items = new ArrayList<MemorySlotItem>();

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    public int size() {
        return items.size();
    }

    //----------------------------------
    // items
    //----------------------------------

    public List<MemorySlotItem> getItems() {
        return items;
    }

    //----------------------------------
    // category
    //----------------------------------

    private final Category category;

    public Category getCategory() {
        return category;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MemorySlot(Category category) {
        this.category = category;
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    public void addItem(MemorySlotItem item) {
        if (items.contains(item))
            return;
        items.add(item);
    }

    public void addAll(List<? extends MemorySlotItem> items) {
        for (MemorySlotItem item : items) {
            item.setIndex(size());
            addItem(item);
        }
    }

    public MemorySlotItem getItem(int index) {
        return items.get(index);
    }

    @Override
    public String toString() {
        return "MemorySlot{" + items.toString() + "}";
    }
}
