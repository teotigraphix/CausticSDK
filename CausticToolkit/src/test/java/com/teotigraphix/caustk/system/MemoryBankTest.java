
package com.teotigraphix.caustk.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.system.Memory.Category;
import com.teotigraphix.caustk.system.Memory.Type;
import com.teotigraphix.caustk.system.SystemState.SystemMode;

public class MemoryBankTest {
    private ICaustkApplication application;

    private ICaustkController controller;

    private IMemoryManager memoryManager;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();

        controller = application.getController();
        memoryManager = controller.getMemoryManager();
    }

    @Test
    public void test_setup() {
        // pattern mode
        // dispatches OnSystemControllerSystemModeChange
        controller.getSystemState().setSystemMode(SystemMode.PATTERN);
        assertEquals(SystemMode.PATTERN, controller.getSystemState().getSystemMode());

        // preset bank for memory items to copy
        // dispatches OnMemoryManagerCurrentBankChange
        memoryManager.setSelectedMemoryType(Type.PRESET);
        assertEquals(Type.PRESET, memoryManager.getSelectedMemoryType());

        // select and copy patch items
        memoryManager.setSelectedMemoryCategory(Category.PATCH);
        assertEquals(Category.PATCH, memoryManager.getSelectedMemoryCategory());
        assertEquals(Category.PATCH, memoryManager.getSelectedMemoryBank().getCategory());

        // the setup is now PRESET/PATCH
        MemorySlot slot = memoryManager.getSelectedMemoryBank().getCurrentMemorySlot();
        assertNotNull(slot);
        assertEquals(Category.PATCH, slot.getCategory());
    }

}
