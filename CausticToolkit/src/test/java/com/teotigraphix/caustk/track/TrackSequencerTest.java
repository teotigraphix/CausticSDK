
package com.teotigraphix.caustk.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.SubSynthTone;

public class TrackSequencerTest extends CaustkTestBase {

    private ITrackSequencer trackSequencer;

    private ISoundSource soundSource;

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
    public void test_addRemove() throws CausticException {
        //trackSequencer.hasTracks()

        assertFalse(trackSequencer.hasTracks());
        soundSource.createTone("part1", SubSynthTone.class);
        assertEquals(1, trackSequencer.getTracks().size());

    }

    @Test
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
