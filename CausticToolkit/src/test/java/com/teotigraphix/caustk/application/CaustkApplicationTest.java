
package com.teotigraphix.caustk.application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.ToneType;

public class CaustkApplicationTest {
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
        controller = null;
    }

    @Test
    public void test_me() throws CausticException {
        SubSynthTone tone = (SubSynthTone)controller.getSoundSource().createTone("tone1",
                ToneType.SubSynth);
        tone.getSynth().noteOn(60, 1f);
    }

}
