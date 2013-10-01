////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.gs.memory;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.gs.memory.Memory.Category;

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
        item.setIndex(items.size());
        items.add(item);
    }

    public void addAll(List<? extends MemorySlotItem> items) {
        for (MemorySlotItem item : items) {
            item.setIndex(size());
            addItem(item);
        }
    }

    public MemorySlotItem getItem(int index) {
        if (index > items.size() - 1)
            return null;
        return items.get(index);
    }

    @Override
    public String toString() {
        return "MemorySlot{" + items.toString() + "}";
    }
}
