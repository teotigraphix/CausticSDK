
package com.teotigraphix.caustk.library;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;

public class LibraryManagerTest {

    private static final File PULSAR_CAUSTIC = new File(
            "src/test/java/com/teotigraphix/caustk/sound/PULSAR.caustic");

    private static final File FADING_ENTROPY = new File(
            "src/test/java/com/teotigraphix/caustk/sound/FADING ENTROPY.caustic");

    private static final File ICECANYON = new File(
            "src/test/java/com/teotigraphix/caustk/sound/ICECANYON.caustic");

    private static final File DRIVE = new File(
            "src/test/java/com/teotigraphix/caustk/sound/DRIVE.caustic");

    private ICaustkApplication application;

    private ICaustkController controller;

    private ILibraryManager libraryManager;

    @Before
    public void setUp() throws Exception {
        // plain ole caustk app and config
        application = CaustkApplicationUtils.createAndRun();

        controller = application.getController();
        libraryManager = controller.getLibraryManager();
    }

    @After
    public void tearDown() throws Exception {
        application = null;
        controller = null;
    }

    @Test
    public void test_create_project_save() throws IOException, CausticException {
        File projectDir = new File("LibraryManagerTestProject");
        controller.getProjectManager().create(projectDir);

        Library library = libraryManager.createLibrary("foo");
        libraryManager.importSong(library, PULSAR_CAUSTIC);

        controller.getProjectManager().save();

    }

    @Test
    public void test_createLibrary() throws CausticException, IOException {
        Library library = libraryManager.createLibrary("baz");
        assertTrue(library.getDirectory().exists());
        libraryManager.importSong(library, PULSAR_CAUSTIC);
        libraryManager.importSong(library, FADING_ENTROPY);
        libraryManager.importSong(library, ICECANYON);
        libraryManager.importSong(library, DRIVE);

        // added 4 scenes and the default scene makes 5
        List<LibraryScene> scenes = new ArrayList<LibraryScene>(library.getScenes());
        Assert.assertEquals(5, scenes.size());

        List<LibraryPatch> patches = new ArrayList<LibraryPatch>(library.getPatches());
        Assert.assertEquals(24, patches.size());

        List<LibraryPhrase> phrases = new ArrayList<LibraryPhrase>(library.getPhrases());
        Assert.assertEquals(97, phrases.size());

        libraryManager.saveLibrary(library);

        //        Library loadedLibrary = libraryManager.loadLibrary("baz");
        //
        //        scenes = new ArrayList<LibraryScene>(loadedLibrary.getScenes());
        //        Assert.assertEquals(5, scenes.size());
        //
        //        patches = new ArrayList<LibraryPatch>(loadedLibrary.getPatches());
        //        Assert.assertEquals(24, patches.size());
        //
        //        phrases = new ArrayList<LibraryPhrase>(loadedLibrary.getPhrases());
        //        Assert.assertEquals(97, phrases.size());
        //
        //        library.delete();
    }
}
