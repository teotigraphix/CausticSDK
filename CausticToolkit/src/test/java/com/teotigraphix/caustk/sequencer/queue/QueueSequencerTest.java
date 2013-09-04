
package com.teotigraphix.caustk.sequencer.queue;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.track.TrackChannel;
import com.teotigraphix.caustk.sequencer.track.TrackPhrase;
import com.teotigraphix.caustk.sequencer.track.TrackSequencer;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.SubSynthTone;

public class QueueSequencerTest extends CaustkTestBase {

    private TrackSequencer trackSequencer;

    private ISoundSource soundSource;

    @SuppressWarnings("unused")
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
    public void test_() throws IOException, CausticException {
        // create the absolute project directory
        controller.getProjectManager().create(new File("QueueSequencerTest"));
        assertResourceExists("projects/QueueSequencerTest");

        // create the absolute songs directory
        controller.getTrackSequencer().create(new File("songs/Foo.ctks"));
        assertResourceExists("projects/QueueSequencerTest/songs/Foo.ctks");

        // TrackSequencer listens the TrackAdd from SoundSource
        // adds a TrackChannel
        soundSource.createTone("part1", SubSynthTone.class);

        assertEquals(1, trackSequencer.getTracks().size());

        TrackChannel channel = trackSequencer.getTrack(0);
        @SuppressWarnings("unused")
        TrackPhrase phrase = channel.getPhrase(0, 0);

    }

    //@Test
    public void test_init() throws IOException, CausticException {
        // - Create a Project or Load existing
        // - Create a new TrackSong
        //   - This will create a QueueSong, both will be saved

        //        controller.getProjectManager().create(new File("QueueSequencerTest"));
        //        controller.getTrackSequencer().create(new File("songs/Foo.ctks"));
        //        soundSource.createTone("part1", SubSynthTone.class);
        //
        //        assertTrue(trackSequencer.getTrackSong().getAbsoluteCausticFile().exists());
        //        assertTrue(trackSequencer.getTrackSong().getAbsoluteFile().exists());
        //        assertTrue(queueSequencer.getQueueSong().getAbsoluteFile().exists());
        //
        //        QueueData data = queueSequencer.getQueueSong().getQueueData(0, 0);
        //        queueSequencer.queue(data);
        //
        //        controller.getApplication().save();
    }
}
