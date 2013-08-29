
package com.teotigraphix.caustk.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
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
    public void test_TrackPhrase_setBankPattern() throws CausticException {
        soundSource.createTone("part1", SubSynthTone.class);
        TrackChannel channel1 = trackSequencer.getTrack(0);
        channel1.setCurrentBankPattern(1, 14);

    }
}
