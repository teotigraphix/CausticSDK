
package com.teotigraphix.caustk.sequencer.queue;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.track.TrackSequencer;

public class QueueSequencerTest extends CaustkTestBase {

    private TrackSequencer trackSequencer;

    private ISoundSource soundSource;

    private QueueSequencer queueSequencer;

    @Override
    protected void start() throws CausticException, IOException {
        soundSource = controller.getSoundSource();
        trackSequencer = (TrackSequencer)controller.getTrackSequencer();
        queueSequencer = (QueueSequencer)controller.getQueueSequencer();
    }

    @Override
    protected void end() {
        soundSource = null;
        trackSequencer = null;
        queueSequencer = null;
    }

    @Test
    public void test_init() throws IOException, CausticException {
        // - Create a Project or Load existing
        // - Create a new TrackSong
        //   - This will create a QueueSong, both will be saved

        controller.getProjectManager().create(new File("QueueSequencerTest"));
        controller.getTrackSequencer().create(new File("songs/Foo.ctks"));
        soundSource.createTone("part1", SubSynthTone.class);

        assertTrue(trackSequencer.getTrackSong().getAbsoluteCausticFile().exists());
        assertTrue(trackSequencer.getTrackSong().getAbsoluteFile().exists());
        assertTrue(queueSequencer.getQueueSong().getAbsoluteFile().exists());
    }
}
