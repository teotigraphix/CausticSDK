
package com.teotigraphix.caustk.sequencer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.osc.RackMessage;

public class SongPlayerTest {

    private ICaustkApplication application;

    private ICaustkController controller;

    private ISongPlayer songPlayer;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();

        controller = application.getController();
        songPlayer = controller.getSongPlayer();
    }

    @After
    public void tearDown() throws Exception {
        File applicationRoot = application.getConfiguration().getApplicationRoot();
        Thread.sleep(420);
        FileUtils.deleteDirectory(applicationRoot);
    }

    @Test
    public void test_create() throws IOException {
        assertNull(songPlayer.getSong());
        TrackSong song = songPlayer.create();
        assertNotNull(song);
        assertSame(controller, song.getController());

    }

    /*
    M:0 C:0
    M:0 C:1
    M:0 C:2
    M:0 C:3
    M:1 C:0
    M:1 C:1
    M:1 C:2
    M:1 C:3
    M:2 C:0
    */

    @Test
    public void test_beat() throws IOException {
        assertNull(songPlayer.getSong());
        TrackSong song = songPlayer.create();
        assertNotNull(song);
        assertSame(controller, song.getController());
        assertEquals(-1, song.getCurrentBeat());
        // the sequencer always starts at -1
        // when the sequencer is played, nextBeat() happensinstantly to signify
        // the first beat 0
        song.nextBeat();
        assertEquals(0, song.getCurrentBeat());

        song.seek(243);
        assertEquals(toLocalBeat(song.getCurrentBeat(), 8), 19f, 0f);
        song.seek(0);
        assertEquals(toLocalBeat(song.getCurrentBeat(), 8), 0f, 0f);
        song.nextBeat();

    }

    @Test
    public void test_() throws IOException {
        RackMessage.LOAD_SONG.send(controller,
                "src/test/java/com/teotigraphix/caustk/project/C2DEMO.caustic");
        
        //controller.getSystemSequencer().play(SequencerMode.SONG);
        
    }

    public static float toLocalBeat(float beat, int length) {
        float r = (beat % (length * 4));
        return r;
    }

}
