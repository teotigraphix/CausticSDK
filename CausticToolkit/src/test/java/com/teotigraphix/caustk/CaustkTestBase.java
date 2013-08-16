
package com.teotigraphix.caustk;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;

public class CaustkTestBase {

    private ICaustkApplication application;

    protected ICaustkController controller;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
        controller.getSoundSource().clearAndReset();
        start();
    }

    protected void start() throws CausticException, IOException {
    }

    @After
    public void tearDown() throws Exception {
        application = null;
        controller = null;
        end();
    }

    protected void end() {
    }

}
