
package com.teotigraphix.caustk.sequencer.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;

public class TrackSequencerTest extends CaustkTestBase {

    private ITrackSequencer trackSequencer;

    private ISoundSource soundSource;

    private File causticFile = new File(
            "src/test/java/com/teotigraphix/caustk/sequencer/track/C2DEMO.caustic")
            .getAbsoluteFile();

    @Override
    protected void start() throws CausticException, IOException {
        soundSource = controller.getSoundSource();
        trackSequencer = controller.getTrackSequencer();
    }

    @Override
    protected void end() {
        soundSource = null;
        trackSequencer = null;
    }

    @Test
    public void test_() throws IOException, CausticException {
        // create the absolute project directory
        controller.getProjectManager().create(new File("TrackSequencerTestProject"));
        assertResourceExists("projects/TrackSequencerTestProject");

        // create the absolute songs directory
        controller.getTrackSequencer().create(new File("songs/Foo.ctks"));
        assertResourceExists("projects/TrackSequencerTestProject/songs/Foo.ctks");

        // TrackSequencer listens the TrackAdd from SoundSource
        // adds a TrackChannel
        soundSource.createTone("part1", SubSynthTone.class);

        assertEquals(1, trackSequencer.getTracks().size());

        //TrackChannel channel = trackSequencer.getTrack(0);
        //TrackPhrase phrase = channel.getPhrase(0, 0);

        /*
         * - Load the caustic file into the sound source
         *   - No restore, all clients will restore what they need
         * -  
         */

        trackSequencer.load(causticFile);

        // test the tracks and tones restored correctly
        assertEquals(6, soundSource.getTones().size());
        assertEquals(6, trackSequencer.getTracks().size());

        testTone0(trackSequencer.getTrack(0));
        testTone1(trackSequencer.getTrack(1));
        testTone2(trackSequencer.getTrack(2));
        testTone3(trackSequencer.getTrack(3));
        testTone4(trackSequencer.getTrack(4));
        testTone5(trackSequencer.getTrack(5));

        testSongSequencer();
    }

    private void testSongSequencer() {

        TrackChannel channel1 = trackSequencer.getTrack(0);
        channel1.getCurrentBank();
    }

    private void testTone5(TrackChannel channel) {
        // TIGHTKIT Bassline
        BeatboxTone tone = (BeatboxTone)channel.getTone();
        assertNotNull(tone);
        assertEquals("TIGHTKIT", tone.getName());

        Map<Integer, TrackPhrase> BankA = channel.getPhraseMap(0);
        assertEquals(2, BankA.size());

        TrackPhrase A01 = BankA.get(0);
        TrackPhrase A02 = BankA.get(1);

        assertEquals(14, A01.getNotes().size());
        assertEquals(4, A02.getNotes().size());

        // test TrackItems
        Map<Integer, TrackItem> items = channel.getTrackItems();
        assertEquals(18, items.size());
    }

    private void testTone4(TrackChannel channel) {
        // HIGH_LEAD Bassline
        BasslineTone tone = (BasslineTone)channel.getTone();
        assertNotNull(tone);
        assertEquals("HIGH_LEAD", tone.getName());

        Map<Integer, TrackPhrase> BankA = channel.getPhraseMap(0);
        assertEquals(4, BankA.size());

        TrackPhrase A01 = BankA.get(0);
        TrackPhrase A02 = BankA.get(1);
        TrackPhrase A03 = BankA.get(2);
        TrackPhrase A04 = BankA.get(3);

        assertEquals(7, A01.getNotes().size());
        assertEquals(8, A02.getNotes().size());
        assertEquals(8, A03.getNotes().size());
        assertEquals(10, A04.getNotes().size());

        // test TrackItems
        Map<Integer, TrackItem> items = channel.getTrackItems();
        assertEquals(24, items.size());
    }

    private void testTone3(TrackChannel channel) {
        // DRONE Bassline
        BasslineTone tone = (BasslineTone)channel.getTone();
        assertNotNull(tone);
        assertEquals("DRONE", tone.getName());

        Map<Integer, TrackPhrase> BankA = channel.getPhraseMap(0);
        assertEquals(1, BankA.size());

        TrackPhrase A01 = BankA.get(0);
        assertEquals(16, A01.getNotes().size());

        // test TrackItems
        Map<Integer, TrackItem> items = channel.getTrackItems();
        assertEquals(33, items.size());
    }

    private void testTone2(TrackChannel channel) {
        // STRINGS SubSynthTone
        SubSynthTone tone = (SubSynthTone)channel.getTone();
        assertNotNull(tone);
        assertEquals("STRINGS", tone.getName());

        Map<Integer, TrackPhrase> BankA = channel.getPhraseMap(0);
        assertEquals(1, BankA.size());

        TrackPhrase A01 = BankA.get(0);
        assertEquals(3, A01.getNotes().size());

        // test TrackItems
        Map<Integer, TrackItem> items = channel.getTrackItems();
        assertEquals(33, items.size());
    }

    private void testTone1(TrackChannel channel) {
        // RHODES PCMSynth
        PCMSynthTone tone = (PCMSynthTone)channel.getTone();
        assertNotNull(tone);
        assertEquals("RHODES", tone.getName());

        Map<Integer, TrackPhrase> BankA = channel.getPhraseMap(0);
        assertEquals(4, BankA.size());

        TrackPhrase A01 = BankA.get(0);
        TrackPhrase A02 = BankA.get(1);
        TrackPhrase A03 = BankA.get(2);
        TrackPhrase A04 = BankA.get(3);

        assertEquals(4, A01.getNotes().size());
        assertEquals(1, A02.getNotes().size());
        assertEquals(3, A03.getNotes().size());
        assertEquals(4, A04.getNotes().size());

        // test TrackItems
        Map<Integer, TrackItem> items = channel.getTrackItems();
        assertEquals(36, items.size());
    }

    private void testTone0(TrackChannel channel) {
        // SQUARE SubSynth
        SubSynthTone tone = (SubSynthTone)channel.getTone();
        assertNotNull(tone);
        assertEquals("SQUARE", tone.getName());

        Map<Integer, TrackPhrase> BankA = channel.getPhraseMap(0);
        assertEquals(1, BankA.size());

        TrackPhrase A01 = BankA.get(0);
        assertEquals(8, A01.getNotes().size());

        // test TrackItems
        Map<Integer, TrackItem> items = channel.getTrackItems();
        assertEquals(33, items.size());
    }

    //@Test
    public void test_addRemove() throws CausticException {
        //trackSequencer.hasTracks()

        assertFalse(trackSequencer.hasTracks());
        soundSource.createTone("part1", SubSynthTone.class);
        assertEquals(1, trackSequencer.getTracks().size());

    }

    //@Test
    public void test_TrackPhrase_setBankPattern() throws CausticException, IOException {
        File projectDir = new File("TrackSequencerTestProject");
        @SuppressWarnings("unused")
        Project project = controller.getProjectManager().create(projectDir);
        controller.getTrackSequencer().create(new File("songs/Foo.ctks"));

        soundSource.createTone("part1", SubSynthTone.class);
        TrackChannel channel1 = trackSequencer.getTrack(0);
        channel1.setCurrentBankPattern(1, 14);

        channel1.getPhrase().setNoteData("1.000000 48 1.00 1.250000 0|1.250000 48 1.00 1.500000 0");
        channel1.getPhrase().setLength(8);
        channel1.getPhrase().setEditMeasure(4);
        channel1.getPhrase().setPlayMeasure(3);

        project = controller.getProjectManager().getProject();
        controller.getApplication().save();

        controller.getProjectManager().clear();
        assertNull(controller.getProjectManager().getProject());
        controller.getProjectManager().load(projectDir);

        assertTrue(trackSequencer.hasTracks());

        channel1 = trackSequencer.getTrack(0);
        assertNotNull(channel1);

        assertEquals(1, channel1.getCurrentBank());
        assertEquals(14, channel1.getCurrentPattern());

        TrackPhrase phrase = channel1.getPhrase();
        assertNotNull(channel1);

        assertEquals(8, phrase.getLength());
        assertEquals(4, phrase.getEditMeasure());
        assertEquals(3, phrase.getPlayMeasure());
    }
}
