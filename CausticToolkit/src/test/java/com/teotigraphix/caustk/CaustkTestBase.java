
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
        FileUtils.forceDelete(getUnitTestResource());
        assertFalse(getUnitTestResource().exists());
    }

    protected void end() {
    }

    protected static File getUnitTestResource() {
        return new File("src/test/resources/unit_test").getAbsoluteFile();
    }

    protected void assertResourceExists(String path) {
        assertTrue(new File(getUnitTestResource(), path).exists());
    }

}
