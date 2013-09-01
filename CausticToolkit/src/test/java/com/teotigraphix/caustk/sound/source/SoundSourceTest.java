
package com.teotigraphix.caustk.sound.source;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sound.source.SoundSource;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.PadSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.ToneType;
import com.teotigraphix.caustk.tone.ToneUtils;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class SoundSourceTest {
    private ICaustkApplication application;

    private SoundSource soundSource;

    private ICaustkController controller;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
        soundSource = (SoundSource)application.getController().getSoundSource();
    }

    @After
    public void tearDown() throws Exception {
        application = null;
        soundSource = null;
    }

    @Test
    public void test_loadSong() throws CausticException {

    }

    @Test
    public void test_saveSong() throws CausticException {
        File file = soundSource.saveSong("Foo");
        Assert.assertTrue(file.exists());
        Assert.assertEquals(RuntimeUtils.getCausticSongFile("Foo"), file);
        file.delete();
        Assert.assertFalse(file.exists());
    }

    @Test
    public void test_saveSongAs() throws CausticException, IOException {
        File song = new File("src/test/java/com/teotigraphix/caustk/sound/Bar.caustic");
        Assert.assertFalse(song.exists());
        File file = soundSource.saveSongAs(song);
        Assert.assertTrue(file.exists());
        file.delete();
        Assert.assertFalse(file.exists());
    }

    @Test
    public void test_setup() throws CausticException {
        soundSource.loadSong(new File("src/test/java/"
                + "com/teotigraphix/caustk/sound/PULSAR.caustic"));

        Assert.assertEquals(6, soundSource.getToneCount());

        Assert.assertEquals(soundSource.getTone(0).getToneType(), ToneType.SubSynth);
        Assert.assertEquals(soundSource.getTone(1).getToneType(), ToneType.SubSynth);
        Assert.assertEquals(soundSource.getTone(2).getToneType(), ToneType.SubSynth);
        Assert.assertEquals(soundSource.getTone(3).getToneType(), ToneType.Bassline);
        Assert.assertEquals(soundSource.getTone(4).getToneType(), ToneType.PCMSynth);
        Assert.assertEquals(soundSource.getTone(5).getToneType(), ToneType.Beatbox);

        //OutputPanelMessage.PLAY.send(application.getController(), 1);

        Assert.assertNotNull(controller.getSoundMixer().getChannel(0));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(1));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(2));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(3));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(4));
        Assert.assertNotNull(controller.getSoundMixer().getChannel(5));

        //controller.getSystemSequencer().play(SequencerMode.SONG);
    }

    @Test
    public void test_Beatbox() throws CausticException {
        BeatboxTone tone = (BeatboxTone)soundSource.createTone("tone1", ToneType.Beatbox);
        Assert.assertEquals(4, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getSampler());
    }

    @Test
    public void test_PCMSynth() throws CausticException {
        PCMSynthTone tone = soundSource.createTone("tone1", PCMSynthTone.class);
        Assert.assertEquals(7, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getSampler());
        Assert.assertNotNull(tone.getTuner());
    }

    @Test
    public void test_SubSynth() throws CausticException {
        SubSynthTone tone = soundSource.createTone("tone1", SubSynthTone.class);
        Assert.assertEquals(8, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getOsc1());
        Assert.assertNotNull(tone.getOsc2());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getLFO2());
    }

    @Test
    public void test_Bassline() throws CausticException {
        BasslineTone tone = soundSource.createTone("tone1", BasslineTone.class);
        Assert.assertEquals(7, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getFilter());
        Assert.assertNotNull(tone.getOsc1());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getDistortion());
    }

    @Test
    public void test_PadSynth() throws CausticException {
        PadSynthTone tone = soundSource.createTone("tone1", PadSynthTone.class);
        Assert.assertEquals(7, ToneUtils.getComponentCount(tone));
        Assert.assertNotNull(tone.getSynth());
        Assert.assertNotNull(tone.getPatternSequencer());
        Assert.assertNotNull(tone.getVolume());
        Assert.assertNotNull(tone.getHarmonics());
        Assert.assertNotNull(tone.getLFO1());
        Assert.assertNotNull(tone.getLFO2());
        Assert.assertNotNull(tone.getMorph());
    }
}
