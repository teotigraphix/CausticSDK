
package com.teotigraphix.caustk.sound;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.teotigraphix.caustk.utils.RuntimeUtils;

public class SynthToneTest extends ToneBaseTest {

    @Test
    public void test_presets() {
        File file = null;

        //        subsynth.getSynth().savePreset("subsynthTest");
        //        file = RuntimeUtils.getCausticPresetsFile("subsynth", "subsynthTest");
        //        Assert.assertTrue(file.exists());

        pcmsynth.getSynth().savePreset("pcmsynthTest");
        file = RuntimeUtils.getCausticPresetsFile("pcmsynth", "pcmsynthTest");
        Assert.assertTrue(file.exists());
    }

    @Test
    public void test_SynthComponent() {
        Assert.assertEquals(4, subsynth.getSynth().getPolyphony());
        Assert.assertEquals(8, pcmsynth.getSynth().getPolyphony());
        Assert.assertEquals(1, bassline.getSynth().getPolyphony());
        Assert.assertEquals(8, beatbox.getSynth().getPolyphony());

        //subsynth.getSynth().noteOn(pitch, velocity);
        //subsynth.getSynth().noteOff(pitch);
        //subsynth.getSynth().notePreview(pitch, oneshot);
    }

    @Test
    public void test_PatternSequencerComponent() {
        subsynth.getPatternSequencer().setSelectedPattern(2, 4);
        Assert.assertEquals(2, subsynth.getPatternSequencer().getSelectedBank());
        Assert.assertEquals(4, subsynth.getPatternSequencer().getSelectedIndex());
        List<String> listing = subsynth.getPatternSequencer().getPatternListing();
        Assert.assertEquals(0, listing.size());

        subsynth.getPatternSequencer().addNote(60, 0f, 1f, 0.5f, 2);
        listing = subsynth.getPatternSequencer().getPatternListing();
        Assert.assertEquals(1, listing.size());
        Assert.assertEquals(listing.get(0), "C5");

        subsynth.getPatternSequencer().clear();
        listing = subsynth.getPatternSequencer().getPatternListing();
        Assert.assertEquals(0, listing.size());

        Assert.assertEquals(2, subsynth.getPatternSequencer().getSelectedBank(true));
        Assert.assertEquals(4, subsynth.getPatternSequencer().getSelectedIndex(true));

        subsynth.getPatternSequencer().setLength(0, 1, 8);
        Assert.assertEquals(1, subsynth.getPatternSequencer().getLength(0, 0));
        Assert.assertEquals(8, subsynth.getPatternSequencer().getLength(0, 1));
        
        Assert.assertEquals(2, subsynth.getPatternSequencer().getSelectedBank(true));
        Assert.assertEquals(4, subsynth.getPatternSequencer().getSelectedIndex(true));
    }
}
