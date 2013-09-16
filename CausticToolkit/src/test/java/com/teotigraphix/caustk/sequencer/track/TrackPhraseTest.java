
package com.teotigraphix.caustk.sequencer.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.Tone;

public class TrackPhraseTest extends CaustkTestBase {

    private ITrackSequencer trackSequencer;

    private ISoundSource soundSource;

    @Override
    protected void start() throws CausticException, IOException {
        controller.getProjectManager().createProject("TrackPhraseTestProject");
        trackSequencer = controller.getTrackSequencer();
        soundSource = controller.getSoundSource();
        deleteOnExit = false;
    }

    @Override
    protected void end() {
        trackSequencer = null;
    }

    public void test_phrase_trigger() {

    }

    //@Test
    public void test_notes() throws IOException, CausticException {
        TrackSong song = trackSequencer.createSong(new File("TrackPhraseTestSong.ctks"));
        assertNotNull(song);
        Tone tone = soundSource.createTone("part1", SubSynthTone.class);
        Track channel = trackSequencer.getTrack(tone);
        TrackPhrase phrase = channel.getPhrase(0, 1);
        PhraseNote note = phrase.addNote(60, 0f, 2f, 0.5f, 1);

        assertEquals(60, note.getPitch());
        assertEquals(0f, note.getStart(), 0f);
        assertEquals(2f, note.getEnd(), 0f);
        assertEquals(0.5f, note.getVelocity(), 0f);
        assertEquals(1, note.getFlags());
        assertEquals(2f, note.getGate(), 0f);

        controller.getProjectManager().save();

        // check to see tht we can reload state from the actual .caustic file
        // that was saved from the TrackSong

        File absoluteCausticFile = new File(getUnitTestResource(),
                "projects/TrackPhraseTestProject/songs/TrackPhraseTestSong.caustic");
        trackSequencer.load(absoluteCausticFile);
        assertNotSame(song, trackSequencer.getTrackSong());

        channel = trackSequencer.getTrack(0);
        assertTrue(channel.hasPhrase(0, 1));
        phrase = channel.getPhrase(0, 1);

        // test the restored note loaded from the .caustic file
        PhraseNote note2 = phrase.getNote(60, 0f);
        assertEquals(60, note2.getPitch());
        assertEquals(0f, note2.getStart(), 0f);
        assertEquals(2f, note2.getEnd(), 0f);
        assertEquals(0.5f, note2.getVelocity(), 0f);
        assertEquals(1, note2.getFlags());
        assertEquals(2f, note2.getGate(), 0f);
    }
}
