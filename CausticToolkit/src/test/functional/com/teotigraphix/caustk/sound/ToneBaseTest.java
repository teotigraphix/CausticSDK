
package com.teotigraphix.caustk.sound;

import org.junit.After;
import org.junit.Before;

import com.teotigraphix.caustk.application.CaustkApplicationUtils;
import com.teotigraphix.caustk.application.ICaustkApplication;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.tone.BasslineTone;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.ToneType;

public class ToneBaseTest {

    private ICaustkApplication application;

    protected ICaustkController controller;

    protected ISoundSource soundSource;

    protected SubSynthTone subsynth;

    protected PCMSynthTone pcmsynth;

    protected BasslineTone bassline;

    protected BeatboxTone beatbox;

    @Before
    public void setUp() throws CausticException {
        application = CaustkApplicationUtils.createAndRun();
        controller = application.getController();
        soundSource = controller.getSoundSource();
        
        soundSource.clearAndReset();
        
        subsynth = (SubSynthTone)soundSource.createTone(0, "tone1", ToneType.SubSynth);
        pcmsynth = (PCMSynthTone)soundSource.createTone(1, "tone2", ToneType.PCMSynth);
        bassline = (BasslineTone)soundSource.createTone(2, "tone3", ToneType.Bassline);
        beatbox = (BeatboxTone)soundSource.createTone(3, "tone4", ToneType.Beatbox);
        
        subsynth.restore();
        pcmsynth.restore();
        bassline.restore();
        beatbox.restore();
    }

    @After
    public void tearDown() {

    }

}
