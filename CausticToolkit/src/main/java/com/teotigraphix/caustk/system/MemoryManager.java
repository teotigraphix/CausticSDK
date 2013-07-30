
package com.teotigraphix.caustk.system;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.system.Memory.Category;
import com.teotigraphix.caustk.system.Memory.Type;

public class MemoryManager implements IMemoryManager {
    //----------------------------------
    // memoryLoader
    //----------------------------------
//
//    private MemoryLoader memoryLoader;
//
//    public MemoryLoader getMemoryLoader() {
//        return memoryLoader;
//    }

    //    //----------------------------------
    //    // systemMemory
    //    //----------------------------------
    //
    //    private SystemMemory systemMemory;
    //
    //    public SystemMemory getSystemMemory()
    //    {
    //        return systemMemory;
    //    }

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

    private Type selectedMemoryType;

    private MemoryBank selectedMemoryBank;

    private MemoryBank userMemoryBank;

    private MemoryBank presetMemoryBank;

    public MemoryBank getSelectedMemoryBank() {
        return selectedMemoryBank;
    }

    public Type getSelectedMemoryType() {
        return selectedMemoryType;
    }

    public void setSelectedMemoryType(Type value) {
        if (selectedMemoryType == value)
            return;

        selectedMemoryType = value;

        if (selectedMemoryType == Type.PRESET)
            selectedMemoryBank = presetMemoryBank;
        else if (selectedMemoryType == Type.USER)
            selectedMemoryBank = userMemoryBank;

        getSystemController().getDispatcher().trigger(
                new OnMemoryManagerCurrentBankChange(selectedMemoryType, selectedMemoryBank));
    }

    //----------------------------------
    // memoryCategory
    //----------------------------------

    private Category selectedMemoryCategory;

    public Category getSelectedMemoryCategory() {
        return selectedMemoryCategory;
    }

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

    private ICaustkController systemController;

    public ICaustkController getSystemController() {
        return systemController;
    }

    public MemoryManager(ICaustkController systemController) {
        this.systemController = systemController;

        //        memoryLoader = new MemoryLoader(systemController);

        createPresetBank();
        createUserBank();

        // XXX Do we want the controller passed to the banks or the manager?
        //        systemMemory = new SystemMemory();
        temporaryMemory = new TemporaryMemory(systemController);
    }

    private void createPresetBank() {
        presetMemoryBank = new MemoryBank(systemController, Type.PRESET, "Preset");
        presetMemoryBank.put(Category.PATCH, new MemorySlot(Category.PATCH));
        presetMemoryBank.put(Category.PATTERN, new MemorySlot(Category.PATTERN));
        presetMemoryBank.put(Category.PHRASE, new MemorySlot(Category.PHRASE));
        presetMemoryBank.put(Category.SONG, new MemorySlot(Category.SONG));
    }

    private void createUserBank() {
        userMemoryBank = new MemoryBank(systemController, Type.USER, "User");
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
