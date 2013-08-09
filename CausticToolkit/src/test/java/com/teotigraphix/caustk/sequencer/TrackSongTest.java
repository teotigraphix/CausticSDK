
package com.teotigraphix.caustk.sequencer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.tone.ToneType;

public class TrackSongTest {

    private File CAUSTIC_LIB_FILE = new File(
            "src/test/java/com/teotigraphix/caustk/project/C2DEMO.caustic");

    private ICaustkApplication application;

    private ICaustkController controller;

    private IProjectManager projectManager;

    private ISongManager songManager;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();

        controller = application.getController();
        projectManager = controller.getProjectManager();
        songManager = controller.getSongManager();
    }

    @After
    public void tearDown() throws Exception {
        File applicationRoot = application.getConfiguration().getApplicationRoot();
        Thread.sleep(420);
        FileUtils.deleteDirectory(applicationRoot);
    }

    @Ignore
    @Test
    public void test_default_scene() throws IOException {
        @SuppressWarnings("unused")
        Project project = createMockProject();
        TrackSong trackSong = createMockTrackSong();
        Library library = controller.getLibraryManager().createLibrary("TestLibrary");
        // The client MUST EXPLCIITLY save the library when its created or edited
        controller.getLibraryManager().saveLibrary(library);

        // must select a library before project save
        controller.getLibraryManager().setSelectedLibrary(library);

        projectManager.save();

        // get a handle on the libraries default scene
        LibraryScene libraryScene = library.getDefaultScene();
        // assign the LibraryScene to the TrackSong
        TrackUtils.assignScene(controller, trackSong, libraryScene);

        Track track1 = trackSong.getTrack(0);
        Track track2 = trackSong.getTrack(1);
        Track track3 = trackSong.getTrack(2);
        Track track4 = trackSong.getTrack(3);
        Track track5 = trackSong.getTrack(4);
        Track track6 = trackSong.getTrack(5);

        // test all tracks have been created
        Assert.assertNotNull(track1);
        Assert.assertNotNull(track2);
        Assert.assertNotNull(track3);
        Assert.assertNotNull(track4);
        Assert.assertNotNull(track5);
        Assert.assertNotNull(track6);

        // test all tracks have the correct default machines
        Assert.assertEquals(ToneType.SubSynth, track1.getTone(controller).getToneType());
        Assert.assertEquals(ToneType.PCMSynth, track2.getTone(controller).getToneType());
        Assert.assertEquals(ToneType.PCMSynth, track3.getTone(controller).getToneType());
        Assert.assertEquals(ToneType.Bassline, track4.getTone(controller).getToneType());
        Assert.assertEquals(ToneType.Bassline, track5.getTone(controller).getToneType());
        Assert.assertEquals(ToneType.Beatbox, track6.getTone(controller).getToneType());

        // save the Project and libraries to disk
        projectManager.save();

        // clear the project model
        projectManager.exit();
        Assert.assertNull(controller.getLibraryManager().getSelectedLibrary());

        // reload the project, which will load the libraryManager
        projectManager.load(new File("Test.ctk"));

        // test the selectedLibrary is restored correctly
        Assert.assertNotNull(controller.getLibraryManager().getSelectedLibrary());
    }

    @Ignore
    @Test
    public void test_add_phrase() throws CausticException, IOException {
        //
        //        createMockProject();
        //        Library library = createMockLibrary();
        //        TrackSong trackSong = createMockTrackSong();
        //
        //        LibraryScene libraryScene = library.getDefaultScene();//= library.getScenes().get(0);
        //
        //        TrackUtils.assignScene(controller, trackSong, libraryScene);
        //
        //        projectManager.save();
        //
        //        // Raw data that needs to be copied
        //        LibraryPhrase libraryPhrase1 = library.findPhrasesByTag("length-1").get(0);
        //        LibraryPhrase libraryPhrase2 = library.findPhrasesByTag("subsynth").get(0);
        //
        //        Assert.assertNotNull(libraryPhrase1);
        //
        //        Track track1 = trackSong.getTrack(0);
        //        Track track2 = trackSong.getTrack(1);
        //        Track track3 = trackSong.getTrack(2);
        //        Track track4 = trackSong.getTrack(3);
        //        Track track5 = trackSong.getTrack(4);
        //        Track track6 = trackSong.getTrack(5);
        //
        //        // construct a TrackPhrase for the Library phrase
        //        // the TrackPhrase is a unique instance that points to a assigned
        //        // bank/pattern in a machine. TrackItem instances hold reference ids
        //        // the their TrackPhrase. The TrackPhrase holds the original note data etc.
        //        // Everytime a TrackPhrase is created, it will assign a new bank/pattern 
        //        // in the Track's machine, this means there is only 64 possible TrackPhrases
        //        // that can be created from a Track
        //        TrackPhrase track1PhraseA01 = track1.copyTrackPhrase(libraryPhrase1);
        //        TrackPhrase track2PhraseA01 = track1.copyTrackPhrase(libraryPhrase1);
        //
        //        List<LibraryPatch> patches = library.findPatchByTag("subsynth");
        //        LibraryPatch libraryPatch = patches.get(0);
        //        TrackUtils.assignPatch(controller, track1, libraryPatch);
        //
        //        TrackUtils.assignNotes(controller, track1, track1PhraseA01);
        //
        //        track1.addPhraseAt(0, 2, track1PhraseA01);
        //        track1.addPhraseAt(2, 1, track1PhraseA01);
        //        track1.addPhraseAt(4, 1, track1PhraseA01);
        //
        //        //track1.addPhraseAt(3, 6, phraseA01);
        //
        //        track1.addPhrase(1, track1PhraseA01);
        //        track2.addPhrase(4, track2PhraseA01);
        //        //controller.sendMessage("/caustic/sequencer/pattern_event 1 0 0 0 1");
        //
        //        //track.clearPhrases();
        //        //track.removePhrase(trackPhrase);
        //
        //        //trackSong.clearTracks();
        //
        //        String string1 = controller.getSerializeService().toString(trackSong);
        //        TrackSong restoredSong = controller.getSerializeService().fromString(string1,
        //                TrackSong.class);
        //        restoredSong.setController(controller);
        //
        //        //
        //        //        LibraryPhrase libraryPhrase2 = library.findPhrasesByTag("length-8").get(1);
        //        //        track1 = restoredSong.getTrack(0);
        //        //        track1.addPhrase(5, 12,
        //        //                track1.createTrackPhraseFrom(controller, libraryPhrase2));
        //        //
        //        string1 = controller.getSerializeService().toString(restoredSong);
        //        projectManager.save();
        //        //controller.api(SequencerAPI.class).play(Mode.SONG);
        //
        //        String path = "";
        //        RackMessage.SAVE_SONG.send(controller, "Test420");
    }

    @Test
    public void test_add_6_tracks() {
        //        trackSong.setBPM(140);
        //
        //        // get a scene to initialize the song
        //        LibraryScene libraryScene = library.getScenes().get(0);
        //        // set the scene to initialize, this does not hold a reference
        //        // the scene instance is set to what was copied without a UUID
        //        TrackUtils.assignScene(controller, trackSong, libraryScene);
        //
        //        Assert.assertEquals(6, trackSong.getNumTracks());
        //        Assert.assertEquals(6, trackSong.getTracks().size());
        //        Assert.assertEquals(5, trackSong.getTrack(5).getIndex());
        //
        //        // copies the current scene
        //        // trackSong.initialize();
        //
        //        String string1 = controller.getSerializeService().toString(trackSong);
        //
        //        TrackSong restoredSong = controller.getSerializeService().fromString(string1,
        //                TrackSong.class);
        //        restoredSong.setController(controller);
        //
        //        String string2 = controller.getSerializeService().toString(restoredSong);
        //        Assert.assertEquals(string1, string2);

    }

    private Project createMockProject() throws IOException {
        Project project = projectManager.create(new File("Test.ctk"));
        return project;
    }

    private TrackSong createMockTrackSong() throws IOException {
        TrackSong song = songManager.create("TestSong.ctk");
        return song;
    }

    @SuppressWarnings("unused")
    private Library createMockLibrary() throws IOException, CausticException {
        // create a library
        Library library = controller.getLibraryManager().createLibrary("TrackSongLib");

        // import a file into the library
        controller.getLibraryManager().importSong(library, CAUSTIC_LIB_FILE);
        controller.getLibraryManager().saveLibrary(library);

        // set the current library
        controller.getLibraryManager().setSelectedLibrary(library);

        return library;
    }
}
