
package com.teotigraphix.caustic.internal.song;

import java.io.File;

import roboguice.RoboGuice;
import android.test.ActivityInstrumentationTestCase2;

import com.google.inject.Inject;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.caustic.test.CausticTestActivity;

public class Test_Workspace extends ActivityInstrumentationTestCase2<CausticTestActivity> {

    private CausticTestActivity mActivity;

    @Inject
    IWorkspace workspace;

    public Test_Workspace() {
        super(CausticTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
        RoboGuice.injectMembers(mActivity, this);
    }

    public void test_initialize() {
        // applicationName, applicationRoot, FileService not set until properties load
        assertNotNull(workspace);
        assertNull(workspace.getApplicationName());

        //       assertFalse(workspace.getApplicationRoot().exists());
        assertEquals(mActivity, workspace.getActivity());
        assertNotNull(workspace.getRack());
        assertNull(workspace.getProject());
        assertNotNull(workspace.getGenerator());

        // get loaded in startAndRun()
        assertNull(workspace.getProperties());
        assertNull(workspace.getFileService());
    }

    public void test_startAndRun() throws CausticException {
        assertNull(workspace.getProperties());
        workspace.startAndRun();
        assertNotNull(workspace.getProperties());
        assertNotNull(workspace.getFileService());
        assertEquals("UNIT_TEST", workspace.getProperties().get("app.name"));
        assertEquals("UNIT_TEST", workspace.getApplicationName());
        assertTrue(workspace.getApplicationRoot().exists());
    }

    public void test_loadProject() throws CausticException {
        workspace.startAndRun();

        File projectFile = workspace.getFileService().getProjectFile("FooProject");
        IProject project = workspace.loadProject(projectFile);
        project.setName("My Foo Bar");
        assertEquals(workspace, project.getWorkspace());
        assertEquals(project, workspace.getProject());
        assertEquals("FooProject", project.getFileName());
        assertEquals("My Foo Bar", project.getName());
        assertEquals("projects", project.getFile().getParentFile().getName());
        // this project does not exist on disk
        assertFalse(project.getFile().exists());

    }

    public void test_save() throws CausticException {
        workspace.startAndRun();

        File projectFile = workspace.getFileService().getProjectFile("FooProject");
        IProject project = workspace.loadProject(projectFile);
        project.setName("My Foo Bar");
        assertFalse(project.getFile().exists());
        // this is firing quicksave
        //workspace.save();
        //assertTrue(project.getFile().exists());
        //assertEquals("projects", project.getFile().getParentFile().getName());
        //workspace.deleteProject(project);
        assertFalse(project.getFile().exists());
    }

    public void test_stopAndSutdown() throws CausticException {
        // TODO IMPL UNIT TESTS
    }
}
