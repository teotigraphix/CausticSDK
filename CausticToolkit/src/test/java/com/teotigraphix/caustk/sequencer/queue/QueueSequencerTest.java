
package com.teotigraphix.caustk.sequencer.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.queue.QueueData.QueueDataState;
import com.teotigraphix.caustk.sequencer.track.TrackChannel;
import com.teotigraphix.caustk.sequencer.track.TrackItem;
import com.teotigraphix.caustk.sequencer.track.TrackPhrase;
import com.teotigraphix.caustk.sequencer.track.TrackSong;

@SuppressWarnings("unused")
public class QueueSequencerTest extends CaustkTestBase {

    private ITrackSequencer trackSequencer;

    private QueueSequencer queueSequencer;

    private TrackSong trackSong;

    private QueuePlayer player;

    private File causticFile = new File(
            "src/test/java/com/teotigraphix/caustk/sequencer/track/C2DEMO.caustic")
            .getAbsoluteFile();

    private QueueData queueDataA01;

    private TrackChannel track0;

    private TrackChannel track1;

    private TrackChannel track2;

    private TrackChannel track3;

    private TrackChannel track4;

    private TrackChannel track5;

    private Library library;

    private QueueData queueDataA02;

    private QueueData queueDataC01;

    @Override
    protected void start() throws CausticException, IOException {
        controller.getProjectManager().createProject("QueueSequencerTestProject");
        trackSequencer = controller.getTrackSequencer();
        trackSong = trackSequencer.createSong("TestSong.ctks");
        queueSequencer = (QueueSequencer)controller.getQueueSequencer();
        queueSequencer.setAudioEnabled(false);
        player = queueSequencer.getPlayer();

        createMockData();
    }

    @Override
    protected void end() {
        trackSequencer = null;
        queueSequencer = null;
    }

    private void createMockData() throws CausticException, IOException {
        final ILibraryManager libraryManager = controller.getLibraryManager();
        library = libraryManager.createLibrary("User");
        libraryManager.setSelectedLibrary(library);

        importSong(controller, library, causticFile);

        // load tones 
        // load patches
        controller.getSoundSource().createScene(library.getScenes().get(0));
        assertEquals(6, trackSequencer.getTracks().size());

        track0 = trackSequencer.getTrack(0);
        track1 = trackSequencer.getTrack(1);
        track2 = trackSequencer.getTrack(2);
        track3 = trackSequencer.getTrack(3);
        track4 = trackSequencer.getTrack(4);
        track5 = trackSequencer.getTrack(5);

        queueDataA01 = queueSequencer.getQueueData(0, 0);
        queueDataA02 = queueSequencer.getQueueData(0, 1);
        queueDataC01 = queueSequencer.getQueueData(2, 0);

        queueDataA01.setViewChannelIndex(0);
        queueDataA02.setViewChannelIndex(0);
        queueDataC01.setViewChannelIndex(0);
    }

    @Test
    public void test_replace_unqueued_data() throws IOException, CausticException {
        LibraryPhrase phrase1 = library.findPhrasesByTag("length-1").get(0);
        LibraryPhrase phrase2 = library.findPhrasesByTag("length-1").get(1);
        LibraryPhrase phrase3 = library.findPhrasesByTag("length-1").get(1);
        assignPhrase(queueDataA01, track0, phrase1);
        assignPhrase(queueDataA02, track0, phrase2);
        assignPhrase(queueDataC01, track0, phrase3);

        // play loop with A01
        queueSequencer.touch(queueDataA01);
        queueSequencer.play();

        // queue A02
        queueSequencer.touch(queueDataA02);

        queueSequencer.beatChange(0, 0);
        queueSequencer.beatChange(0, 1);

        assertQueue(queueDataA01, 1, 1, 0, QueueDataState.PlayUnqueued); // A02 unqueued it
        assertQueue(queueDataA02, 1, 1, 0, QueueDataState.Queue);
        assertQueue(queueDataC01, 1, 1, 0, QueueDataState.Idle);

        // queue C01, replaces A02
        queueSequencer.touch(queueDataC01);

        assertQueue(queueDataA01, 1, 1, 0, QueueDataState.PlayUnqueued); // C01 unqueued it
        assertQueue(queueDataA02, 1, 1, 0, QueueDataState.Idle);
        assertQueue(queueDataC01, 1, 1, 0, QueueDataState.Queue);

        queueSequencer.beatChange(0, 2);
        queueSequencer.beatChange(0, 3);
        queueSequencer.beatChange(1, 4);

        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Idle);
        assertQueue(queueDataA02, 0, 1, 0, QueueDataState.Idle);
        assertQueue(queueDataC01, 0, 1, 0, QueueDataState.Play);
    }

    @Test
    public void test_unqueue_self_when_playing() throws IOException, CausticException {
        LibraryPhrase phrase1 = library.findPhrasesByTag("length-1").get(0);
        LibraryPhrase phrase4 = library.findPhrasesByTag("length-1").get(1);
        phrase4.setLength(4);
        assignPhrase(queueDataA01, track0, phrase1);
        assignPhrase(queueDataA02, track0, phrase4);

        queueSequencer.touch(queueDataA01);
        queueSequencer.play();
        assertTrackItem(0, 0, 1, queueDataA01);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Play);

        queueSequencer.beatChange(0, 0);
        queueSequencer.beatChange(0, 1);

        // unqueue self
        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.PlayUnqueued);
        queueSequencer.beatChange(0, 2);

        // queue again
        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Play);

        // take it out again
        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.PlayUnqueued);
        queueSequencer.beatChange(0, 3);
        assertQueue(queueDataA01, 0, 0, 1, QueueDataState.PlayUnqueued);
        queueSequencer.beatChange(1, 4);
        assertQueue(queueDataA01, 0, 0, 0, QueueDataState.Idle);
    }

    @Test
    public void test_unqueue_4measure_length() throws IOException, CausticException {
        LibraryPhrase phrase1 = library.findPhrasesByTag("length-1").get(1);
        phrase1.setLength(4);
        assignPhrase(queueDataA01, track0, phrase1);

        queueSequencer.touch(queueDataA01);
        queueSequencer.play();
        assertTrackItem(0, 0, 4, queueDataA01);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Play);

        queueSequencer.beatChange(0, 0);
        queueSequencer.beatChange(0, 1);
        queueSequencer.beatChange(0, 2);

        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.PlayUnqueued);

        queueSequencer.beatChange(0, 3);
        queueSequencer.beatChange(1, 4);
        queueSequencer.beatChange(1, 5);
        queueSequencer.beatChange(1, 6);
        queueSequencer.beatChange(1, 7);
        queueSequencer.beatChange(2, 8);
        queueSequencer.beatChange(2, 9);
        queueSequencer.beatChange(2, 10);
        queueSequencer.beatChange(2, 11);
        queueSequencer.beatChange(3, 12);
        queueSequencer.beatChange(3, 13);
        queueSequencer.beatChange(3, 14);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.PlayUnqueued);
        queueSequencer.beatChange(3, 15); // puts into tempPlayQueue
        assertQueue(queueDataA01, 0, 0, 1, QueueDataState.PlayUnqueued);
        queueSequencer.beatChange(4, 16);
        assertQueue(queueDataA01, 0, 0, 0, QueueDataState.Idle);
    }

    @Test
    public void test_one_mesure_queue_unqueue_self() throws IOException, CausticException {
        LibraryPhrase phrase1 = library.findPhrasesByTag("length-1").get(0);
        LibraryPhrase phrase4 = library.findPhrasesByTag("length-1").get(1);
        phrase4.setLength(4);
        assignPhrase(queueDataA01, track0, phrase1);
        assignPhrase(queueDataA02, track0, phrase4);

        queueSequencer.touch(queueDataA02);
        queueSequencer.play();
        assertTrackItem(0, 0, 4, queueDataA02);

        queueSequencer.beatChange(0, 0);
        queueSequencer.beatChange(0, 1);

        // queue the 1 measure lenth, this won't be played until
        // the end of measure 3
        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 1, 1, 0, QueueDataState.Queue);
        queueSequencer.beatChange(0, 2);

        // turn the queued back of and revalidate the playing data
        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Idle);

        queueSequencer.touch(queueDataA01);
        queueSequencer.beatChange(0, 3);
        queueSequencer.beatChange(1, 4);

        assertQueue(queueDataA01, 1, 1, 0, QueueDataState.Queue);

        queueSequencer.beatChange(1, 5);
        queueSequencer.beatChange(1, 6);
        queueSequencer.beatChange(1, 7);
        queueSequencer.beatChange(2, 8);
        queueSequencer.beatChange(2, 9);
        queueSequencer.beatChange(2, 10);
        queueSequencer.beatChange(2, 11);
        queueSequencer.beatChange(3, 12);
        queueSequencer.beatChange(3, 13);
        queueSequencer.beatChange(3, 14);
        queueSequencer.beatChange(3, 15); // Add A01
        assertTrackItem(0, 4, 1, queueDataA01);
        // there is nothing in playQueue but 1 in tempPlayQueue
        assertQueue(queueDataA01, 0, 0, 1, QueueDataState.Queue);

        queueSequencer.beatChange(4, 16);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Play);
        assertQueue(queueDataA02, 0, 1, 0, QueueDataState.Idle);
    }

    @Test
    public void test_loop_off() throws IOException, CausticException {
        LibraryPhrase phrase1 = library.findPhrasesByTag("subsynth").get(0);
        assignPhrase(queueDataA01, track0, phrase1);

        queueDataA01.getViewChannel().setLoopEnabled(false);
        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 1, 0, 0, QueueDataState.Queue);
        queueSequencer.play();
        assertTrackItem(0, 0, 1, queueDataA01);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Play);
        queueSequencer.beatChange(0, 0);
        queueSequencer.beatChange(0, 1);
        queueSequencer.beatChange(0, 2);
        queueSequencer.beatChange(0, 3);
        assertQueue(queueDataA01, 0, 0, 1, QueueDataState.PlayUnqueued);
        queueSequencer.beatChange(1, 4);
        assertQueue(queueDataA01, 0, 0, 0, QueueDataState.Idle);
    }

    @Test
    public void test_simple() throws IOException, CausticException {
        LibraryPhrase phrase1 = library.findPhrasesByTag("subsynth").get(0);
        assignPhrase(queueDataA01, track0, phrase1);

        assertQueue(queueDataA01, 0, 0, 0, QueueDataState.Idle);
        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 1, 0, 0, QueueDataState.Queue);
        // try and add it again, if in the queue and not playing, goes back to Idle
        queueSequencer.touch(queueDataA01);
        assertQueue(queueDataA01, 0, 0, 0, QueueDataState.Idle);

        // the start of recording has to have play() because the sequencer
        // needs pattern data before it starts
        queueSequencer.play();

        assertQueue(queueDataA01, 0, 0, 0, QueueDataState.Idle);

        queueSequencer.beatChange(0, 0);
        queueSequencer.beatChange(0, 1);
        queueSequencer.beatChange(0, 2);
        queueSequencer.touch(queueDataA01);
        // added the TrackItem but state is not Play until the start measure
        queueSequencer.beatChange(0, 3);
        assertTrue(player.isLockBeat());
        // there is 1 in the tempPlayQueue
        assertQueue(queueDataA01, 0, 0, 0, QueueDataState.Queue);
        queueSequencer.beatChange(1, 4);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Play);
        assertTrackItem(0, 1, 1, queueDataA01);

    }

    @Test
    public void test_turn_playing_tone_off() throws IOException, CausticException {
        LibraryPhrase phrase1 = library.findPhrasesByTag("subsynth").get(0);
        //LibraryPhrase phrase2 = library.findPhrasesByTag("pcmsynth").get(0);
        assignPhrase(queueDataA01, track0, phrase1);
        assignPhrase(queueDataA02, track0, phrase1);

        queueSequencer.touch(queueDataA01);
        player.play();
        assertTrackItem(0, 0, 1, queueDataA01);

        queueSequencer.beatChange(0, 0);
        queueSequencer.beatChange(0, 1);
        queueSequencer.beatChange(0, 2);
        queueSequencer.beatChange(0, 3);
        assertTrackItem(0, 1, 1, queueDataA01);

        queueSequencer.beatChange(1, 4); // start measure
        // queue another pad that shares the same channel
        queueSequencer.touch(queueDataA02);

        assertQueue(queueDataA01, 1, 1, 0, QueueDataState.PlayUnqueued);
        assertQueue(queueDataA02, 1, 1, 0, QueueDataState.Queue);

        // the rule is, if a channel is playing and another that shares 
        // the same channel is queued, the queued channel will turn off
        // the playing channel

        queueSequencer.beatChange(1, 5);
        queueSequencer.beatChange(1, 6);
        queueSequencer.beatChange(1, 7);
        assertTrue(player.isLockBeat());
        assertQueue(queueDataA01, 0, 0, 1, QueueDataState.PlayUnqueued);
        assertQueue(queueDataA02, 0, 0, 1, QueueDataState.Queue);
        queueSequencer.beatChange(2, 8);
        assertQueue(queueDataA01, 0, 1, 0, QueueDataState.Idle);
        assertQueue(queueDataA02, 0, 1, 0, QueueDataState.Play);
    }

    private void assertTrackItem(int trackIndex, int startMeasure, int length, QueueData data) {
        TrackChannel track = trackSequencer.getTrack(trackIndex);
        assertTrue(track.contains(startMeasure));
        TrackItem item = track.getItemOnMeasure(startMeasure);
        QueueDataChannel channel = data.getChannel(trackIndex);
        assertEquals(item.getPhraseId(), channel.getPhraseId());
        assertSame(item, track.getItemAtEndMeasure(startMeasure + length));
    }

    private void assertQueue(QueueData data, int queuedSize, int playSize, int flushSize,
            QueueDataState state) {
        assertEquals(queuedSize, player.getQueued().size());
        assertEquals(playSize, player.getPlayQueue().size());
        assertEquals(flushSize, player.getFlushedQueue().size());
        assertEquals(state, data.getState());
    }

    public static void assignPhrase(QueueData data, TrackChannel trackChannel,
            LibraryPhrase libraryPhrase) {
        QueueDataChannel channel = data.getChannel(trackChannel.getIndex());
        TrackPhrase trackPhrase = trackChannel.getPhrase(channel.getBankIndex(),
                channel.getPatternIndex());
        // TrackSequencerHandlers sets this on the pattern sequencer
        trackPhrase.setPhraseId(libraryPhrase.getId());
        trackPhrase.setLength(libraryPhrase.getLength());
        trackPhrase.setNoteData(libraryPhrase.getNoteData());
        channel.assignPhrase(trackPhrase);
    }

    public static void importSong(ICaustkController controller, Library library, File file)
            throws IOException, CausticException {
        final ILibraryManager libraryManager = controller.getLibraryManager();

        final Library tempLibrary = libraryManager.createLibrary();
        tempLibrary.setDirectory(library.getDirectory());

        libraryManager.importSong(tempLibrary, file);

        List<LibraryPatch> patches = tempLibrary.getPatches();
        List<LibraryPhrase> phrases = tempLibrary.getPhrases();

        // only one scene
        List<LibraryScene> scenes = tempLibrary.getScenes();
        LibraryScene scene = scenes.get(0);
        library.addScene(scene);

        library.getPatches().addAll(patches);
        library.getPhrases().addAll(phrases);

        libraryManager.saveLibrary(library);
    }
}
