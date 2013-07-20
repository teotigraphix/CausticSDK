
package com.teotigraphix.caustk.project;

import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;

public class ProjectManagerTest {

    @SuppressWarnings("unused")
    private static final File APPLICATION_ROOT_DIR = new File(
            "src/test/java/com/teotigraphix/caustk/project");

    private final static File PROJECT_FILE = new File("Test.ctk");

    private ICaustkApplication application;

    private ICaustkController controller;

    private ProjectManager projectManager;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
        projectManager = (ProjectManager)controller.getProjectManager();
    }

    @After
    public void tearDown() throws Exception {
        //controller.sendMessage("/caustic/blankrack");
        File applicationRoot = application.getConfiguration().getApplicationRoot();
        FileUtils.deleteDirectory(applicationRoot);
        application = null;
        controller = null;
    }

    @Test
    public void test_create_empty_project() throws CausticException, IOException {
        Project project = projectManager.create(PROJECT_FILE);
        projectManager.save();
        Assert.assertTrue(project.getFile().exists());
    }

    @Test
    public void test_create_project() throws CausticException, IOException {
        Project project1 = projectManager.create(PROJECT_FILE);
        projectManager.save();
        assertNotNull(project1);
        Assert.assertTrue(project1.getFile().exists());
        Project project2 = projectManager.load(PROJECT_FILE);
        assertNotNull(project2);

        String serialized1 = controller.getSerializeService().toPrettyString(project1);
        String serialized2 = controller.getSerializeService().toPrettyString(project2);
        Assert.assertEquals(serialized1, serialized2);
    }
}
