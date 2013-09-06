
package com.teotigraphix.caustk.library;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.library.item.LibraryScene;

public class LibraryManagerTest extends CaustkTestBase {

    private static final File PULSAR_CAUSTIC = new File(
            "src/test/java/com/teotigraphix/caustk/sound/PULSAR.caustic");

    private static final File FADING_ENTROPY = new File(
            "src/test/java/com/teotigraphix/caustk/sound/FADING ENTROPY.caustic");

    private static final File ICECANYON = new File(
            "src/test/java/com/teotigraphix/caustk/sound/ICECANYON.caustic");

    private static final File DRIVE = new File(
            "src/test/java/com/teotigraphix/caustk/sound/DRIVE.caustic");

    private ILibraryManager libraryManager;

    @Override
    protected void start() throws CausticException, IOException {
        libraryManager = controller.getLibraryManager();
    }

    @Override
    protected void end() {
        controller = null;
        libraryManager = null;
    }

    @Test
    public void test_createLibrary() throws CausticException, IOException {
        controller.getProjectManager().createProject(new File("LibraryManagerTestProject"));
        assertResourceExists("projects/LibraryManagerTestProject");

        Library library = libraryManager.createLibrary("baz");
        assertTrue(library.getAbsoluteDirectory().exists());
        libraryManager.importSong(library, PULSAR_CAUSTIC);
        libraryManager.importSong(library, FADING_ENTROPY);
        libraryManager.importSong(library, ICECANYON);
        libraryManager.importSong(library, DRIVE);

        // added 4 scenes
        List<LibraryScene> scenes = new ArrayList<LibraryScene>(library.getScenes());
        Assert.assertEquals(4, scenes.size());

        List<LibraryPatch> patches = new ArrayList<LibraryPatch>(library.getPatches());
        Assert.assertEquals(24, patches.size());

        List<LibraryPhrase> phrases = new ArrayList<LibraryPhrase>(library.getPhrases());
        Assert.assertEquals(97, phrases.size());

        libraryManager.saveLibrary(library);

        Library loadedLibrary = libraryManager.loadLibrary("baz");

        scenes = new ArrayList<LibraryScene>(loadedLibrary.getScenes());
        Assert.assertEquals(4, scenes.size());

        patches = new ArrayList<LibraryPatch>(loadedLibrary.getPatches());
        Assert.assertEquals(24, patches.size());

        phrases = new ArrayList<LibraryPhrase>(loadedLibrary.getPhrases());
        Assert.assertEquals(97, phrases.size());

        library.delete();
    }
}
