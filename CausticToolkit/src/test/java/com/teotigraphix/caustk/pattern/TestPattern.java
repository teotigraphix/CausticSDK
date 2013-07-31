
package com.teotigraphix.caustk.pattern;

import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;

public class TestPattern {

    private ICaustkApplication application;

    private ICaustkController controller;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
    }

    @After
    public void tearDown() throws Exception {
        application = null;
    }

    @Test
    public void test_patternChange() throws CausticException {
        assertNull(controller.getMemoryManager().getTemporaryMemory().getCurrentPattern());

        //        controller.api(SequencerAPI.class).play();
        //        controller.api(ControllerAPI.class).setNextPattern(0);
        //
        //        Pattern pendingPattern = controller.api(ControllerAPI.class).getPendingPattern();
        //        assertSame(controller.getMemoryManager().getTemporaryMemory().getPendingPattern(),
        //                pendingPattern);
        //        assertNull(controller.getMemoryManager().getTemporaryMemory().getCurrentPattern());
        //
        //        assertNull(controller.api(ControllerAPI.class).getPattern());
        //        assertNotNull(pendingPattern);
        //
        //        controller.api(ControllerAPI.class).playNextPattern();
        //
        //        assertSame(pendingPattern, controller.api(ControllerAPI.class).getPattern());
        //        assertSame(controller.getMemoryManager().getTemporaryMemory().getCurrentPattern(),
        //                controller.api(ControllerAPI.class).getPattern());
        //        assertNull(controller.api(ControllerAPI.class).getPendingPattern());
    }

    @Test
    public void test_parts() throws CausticException {
        //        controller.getPatternManager().playPattern(0);
        //
        //        Pattern pattern = controller.getPatternManager().getPattern();
        //
        //        assertEquals(3, pattern.getPartCount());
        //        assertEquals(0, pattern.getPart(0).getIndex());
        //        assertEquals(1, pattern.getPart(1).getIndex());
        //        assertEquals(2, pattern.getPart(2).getIndex());
        //
        //        assertSame(pattern.getPart(0), pattern.getSelectedPart());
    }
}
