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

package com.teotigraphix.caustk.system;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.system.Memory.Category;
import com.teotigraphix.caustk.system.Memory.Type;

public class MemoryManager implements IMemoryManager {

    //----------------------------------
    // systemMemory
    //----------------------------------

    private SystemMemory systemMemory;

    public SystemMemory getSystemMemory() {
        return systemMemory;
    }

    //----------------------------------
    // temporaryMemory
    //----------------------------------

    private TemporaryMemory temporaryMemory;

    @Override
    public TemporaryMemory getTemporaryMemory() {
        return temporaryMemory;
    }

    @Override
    public MemoryBank getMemoryBank(Type value) {
        if (value == Type.PRESET)
            return presetMemoryBank;
        else if (value == Type.USER)
            return userMemoryBank;
        return null;
    }

    //----------------------------------
    // memory
    //----------------------------------

    private Type selectedMemoryType;

    private MemoryBank selectedMemoryBank;

    private MemoryBank userMemoryBank;

    private MemoryBank presetMemoryBank;

    @Override
    public MemoryBank getSelectedMemoryBank() {
        return selectedMemoryBank;
    }

    @Override
    public Type getSelectedMemoryType() {
        return selectedMemoryType;
    }

    @Override
    public void setSelectedMemoryType(Type value) {
        if (selectedMemoryType == value)
            return;

        selectedMemoryType = value;

        if (selectedMemoryType == Type.PRESET)
            selectedMemoryBank = presetMemoryBank;
        else if (selectedMemoryType == Type.USER)
            selectedMemoryBank = userMemoryBank;

        getController().getDispatcher().trigger(
                new OnMemoryManagerCurrentBankChange(selectedMemoryType, selectedMemoryBank));
    }

    //----------------------------------
    // memoryCategory
    //----------------------------------

    private Category selectedMemoryCategory;

    @Override
    public Category getSelectedMemoryCategory() {
        return selectedMemoryCategory;
    }

    @Override
    public void setSelectedMemoryCategory(Category value) {
        if (selectedMemoryCategory == value)
            return;
        selectedMemoryCategory = value;
        // for now we are just proxing to the current memory slot
        // this changes the strategy inside
        selectedMemoryBank.setCategory(selectedMemoryCategory);
    }

    //----------------------------------
    // systemController
    //----------------------------------

    private ICaustkController controller;

    public ICaustkController getController() {
        return controller;
    }

    public MemoryManager(ICaustkController controller) {
        this.controller = controller;

        createPresetBank();
        createUserBank();

        // XXX Do we want the controller passed to the banks or the manager?
        systemMemory = new SystemMemory();
        temporaryMemory = new TemporaryMemory(controller);
    }

    private void createPresetBank() {
        presetMemoryBank = new MemoryBank(controller, Type.PRESET, "Preset");
        presetMemoryBank.put(Category.PATCH, new MemorySlot(Category.PATCH));
        presetMemoryBank.put(Category.PATTERN, new MemorySlot(Category.PATTERN));
        presetMemoryBank.put(Category.PHRASE, new MemorySlot(Category.PHRASE));
        presetMemoryBank.put(Category.SONG, new MemorySlot(Category.SONG));
    }

    private void createUserBank() {
        userMemoryBank = new MemoryBank(controller, Type.USER, "User");
        userMemoryBank.put(Category.PATCH, new MemorySlot(Category.PATCH));
        userMemoryBank.put(Category.PATTERN, new MemorySlot(Category.PATTERN));
        userMemoryBank.put(Category.PHRASE, new MemorySlot(Category.PHRASE));
        userMemoryBank.put(Category.SONG, new MemorySlot(Category.SONG));
    }

    public static class OnMemoryManagerCurrentBankChange {
        private Type selectedMemoryType;

        private MemoryBank selectedMemoryBank;

        public Type getSelectedMemoryType() {
            return selectedMemoryType;
        }

        public Memory getSelectedMemoryBank() {
            return selectedMemoryBank;
        }

        public OnMemoryManagerCurrentBankChange(Type selectedMemoryType,
                MemoryBank selectedMemoryBank) {
            this.selectedMemoryType = selectedMemoryType;
            this.selectedMemoryBank = selectedMemoryBank;
        }
    }
}
