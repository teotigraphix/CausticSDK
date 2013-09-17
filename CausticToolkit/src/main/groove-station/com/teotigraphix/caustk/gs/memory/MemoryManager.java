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

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.memory.Memory.Category;
import com.teotigraphix.caustk.gs.memory.Memory.Type;

public class MemoryManager {

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

    public TemporaryMemory getTemporaryMemory() {
        return temporaryMemory;
    }

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

    private Type selectedType;

    private MemoryBank selectedMemoryBank;

    private MemoryBank userMemoryBank;

    private MemoryBank presetMemoryBank;

    public MemoryBank getSelectedMemoryBank() {
        return selectedMemoryBank;
    }

    public Type getSelectedMemoryType() {
        return selectedType;
    }

    public void setSelectedType(Type value) {
        if (selectedType == value)
            return;

        selectedType = value;

        if (selectedType == Type.PRESET)
            selectedMemoryBank = presetMemoryBank;
        else if (selectedType == Type.USER)
            selectedMemoryBank = userMemoryBank;

        getController().trigger(
                new OnMemoryManagerCurrentBankChange(selectedType, selectedMemoryBank));
    }

    //----------------------------------
    // memoryCategory
    //----------------------------------

    private Category selectedCategory;

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category value) {
        if (selectedCategory == value)
            return;
        selectedCategory = value;
        // for now we are just proxing to the current memory slot
        // this changes the strategy inside
        selectedMemoryBank.setCategory(selectedCategory);
    }

    //----------------------------------
    // systemController
    //----------------------------------

    private GrooveMachine machine;

    public GrooveMachine getMachine() {
        return machine;
    }

    public ICaustkController getController() {
        return machine.getController();
    }

    public MemoryManager(GrooveMachine machine) {
        this.machine = machine;

        userMemoryBank = new UserMemoryBank(machine);
        presetMemoryBank = new PresetMemoryBank(machine);

        // XXX Do we want the controller passed to the banks or the manager?
        systemMemory = new SystemMemory();
        temporaryMemory = new TemporaryMemory(machine);
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
