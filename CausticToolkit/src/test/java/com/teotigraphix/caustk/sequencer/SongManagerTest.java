
package com.teotigraphix.caustk.sequencer;

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
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.project.ProjectManager;

public class SongManagerTest {

    private final static File PROJECT_FILE = new File("Test.ctk");

    private ICaustkApplication application;

    private ICaustkController controller;

    private ProjectManager projectManager;

    private ISongManager songManager;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();

        controller = application.getController();
        projectManager = (ProjectManager)controller.getProjectManager();
        songManager = controller.getSongManager();
    }

    @After
    public void tearDown() throws Exception {
        //controller.sendMessage("/caustic/blankrack");
        File applicationRoot = application.getConfiguration().getApplicationRoot();
        Thread.sleep(420);
        FileUtils.deleteDirectory(applicationRoot);
        application = null;
        controller = null;
    }

    @Test
    public void test_create_song() throws CausticException, IOException {
        Project project1 = projectManager.create(PROJECT_FILE);
        projectManager.save();
        Assert.assertFalse(songManager.songExists("Foo.ctks"));
        songManager.create("Foo.ctks");
        Assert.assertFalse(songManager.songExists("Foo.ctks"));
        // song is not saved until the project or sound manager is save() or exit()
        // the song will not save if there are no tracks assigned either
        songManager.getTrackSong().setNumTracks(6);
        projectManager.save();
        Assert.assertNotNull(songManager.getTrackSong().getController());
        projectManager.exit();
        Assert.assertTrue(songManager.songExists("Foo.ctks"));
        Assert.assertTrue(project1.isClosed());
        Assert.assertNull(songManager.getTrackSong());
        @SuppressWarnings("unused")
        Project project2 = projectManager.load(PROJECT_FILE);
        Assert.assertNotNull(songManager.getTrackSong());
    }
}
