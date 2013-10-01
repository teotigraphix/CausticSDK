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
