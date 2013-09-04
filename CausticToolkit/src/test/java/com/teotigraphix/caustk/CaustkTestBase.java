
package com.teotigraphix.caustk;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;

public abstract class CaustkTestBase {

    private ICaustkApplication application;

    protected ICaustkController controller;

    protected boolean deleteOnExit = true;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
        controller.getSoundSource().clearAndReset();
        start();
    }

    protected abstract void start() throws CausticException, IOException;

    @After
    public void tearDown() throws Exception {
        application = null;
        controller = null;
        end();
        if (deleteOnExit) {
            FileUtils.forceDelete(getUnitTestResource());
            assertFalse(getUnitTestResource().exists());
        }
    }

    protected abstract void end();

    protected static File getUnitTestResource() {
        return new File("src/test/resources/unit_test").getAbsoluteFile();
    }

    protected void assertResourceExists(String path) {
        assertTrue(new File(getUnitTestResource(), path).exists());
    }

}
