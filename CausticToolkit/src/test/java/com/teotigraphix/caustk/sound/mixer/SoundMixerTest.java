
package com.teotigraphix.caustk.sound.mixer;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.controller.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.MixerMessage;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.sound.mixer.SoundMixer.MixerInput;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneType;

public class SoundMixerTest {

    private ICaustkApplication application;

    private SoundMixer soundMixer;

    private ISoundSource soundSource;

    private ICaustkController controller;

    @Before
    public void setUp() throws Exception {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
        soundMixer = (SoundMixer)controller.getSoundMixer();
        soundSource = controller.getSoundSource();
    }

    @After
    public void tearDown() throws Exception {
        application = null;
        controller = null;
        soundMixer = null;
    }

    @Test
    public void test_mixer_command() throws CausticException {
        soundSource.createTone("tone1", ToneType.Bassline);
        soundMixer.executeSetValue(0, MixerInput.High, 0.42f);
        Assert.assertEquals(0.42f, soundMixer.getChannel(0).getHigh());
    }

    @Test
    public void test_mixer_command_undoRedo() throws CausticException {
        soundSource.createTone("tone1", ToneType.Bassline);
        soundMixer.executeSetValue(0, MixerInput.High, 0.42f);
        Assert.assertEquals(0.42f, soundMixer.getChannel(0).getHigh());
        controller.undo();
        Assert.assertEquals(0f, soundMixer.getChannel(0).getHigh());
        controller.redo();
        Assert.assertEquals(0.42f, soundMixer.getChannel(0).getHigh());
    }

    @SuppressWarnings("unused")
    @Test
    public void test_project_save() throws CausticException, IOException {
        File projectDir = new File("SoundMixerTestProject");
        Project project = controller.getProjectManager().createProject(projectDir);

        soundSource.createTone("tone1", ToneType.Bassline);
        soundSource.createTone("tone2", ToneType.Beatbox);
        soundSource.createTone("tone3", ToneType.SubSynth);

        soundMixer.getChannel(0).setHigh(0.42f);

        // save the project to disk
        controller.getProjectManager().save();
        // load the project from disk, this will reinitialize
        // the SoundMixer state instance from deserialization of the project map entry
        Project project2 = controller.getProjectManager().load(new File("SoundMixerTestProject"));

        Assert.assertEquals(0.42f, soundMixer.getChannel(0).getHigh());

        soundMixer.getChannel(1).setHigh(0.222f);

        // test the native core saved the value
        Assert.assertEquals(0.222f, MixerMessage.EQ_HIGH.query(controller, 1));
    }

    @SuppressWarnings("unused")
    @Test
    public void test_addRemove_tones() throws CausticException {
        // create tones
        Tone tone1 = soundSource.createTone("tone1", ToneType.Bassline);
        Tone tone2 = soundSource.createTone("tone2", ToneType.Bassline);
        Tone tone3 = soundSource.createTone("tone3", ToneType.Bassline);

        Assert.assertEquals(3, soundMixer.getChannels().size());

        soundSource.destroyTone(1);
        Assert.assertEquals(2, soundMixer.getChannels().size());
        soundSource.destroyTone(0);
        Assert.assertEquals(1, soundMixer.getChannels().size());
        soundSource.destroyTone(2);
        Assert.assertEquals(0, soundMixer.getChannels().size());
    }
}
