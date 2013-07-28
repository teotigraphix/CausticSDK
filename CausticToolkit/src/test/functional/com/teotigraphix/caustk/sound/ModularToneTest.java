
package com.teotigraphix.caustk.sound;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.teotigraphix.caustk.core.components.modular.AREnvelope;
import com.teotigraphix.caustk.core.components.modular.AREnvelope.AREnvelopeJack;
import com.teotigraphix.caustk.core.components.modular.MiniLFO;
import com.teotigraphix.caustk.core.components.modular.MiniLFO.MiniLFOJack;
import com.teotigraphix.caustk.core.components.modular.ModularPanel.ModularPanelJack;
import com.teotigraphix.caustk.core.components.modular.PulseGenerator;
import com.teotigraphix.caustk.core.components.modular.PulseGenerator.PulseGeneratorJack;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.tone.ModularTone.ComponentType;

public class ModularToneTest extends ToneBaseTest {

    @Test
    public void test_SynthComponent() {
        Assert.assertEquals(1, modular.getSynth().getPolyphony());

        File file = new File(
                "C:/Users/Work/Documents/caustic/presets/modular/SIMPLE PWM.modularsynth");
        //modular.getSynth().loadPreset(file.getAbsolutePath());

        // SIMPLE PWM.modularsynth
        // SubOSC bay 1
        MiniLFO lfo1 = (MiniLFO)modular.create(ComponentType.MiniLFO, 0);

        // SubOSC bay 2
        AREnvelope arenvelope = (AREnvelope)modular.create(ComponentType.AREnvelope, 1);

        // Oscillator bay 3-4
        PulseGenerator pg1 = (PulseGenerator)modular.create(ComponentType.PulseGenerator, 2);

        // notecv-> osc note
        pg1.connect(PulseGeneratorJack.InNote, modular.getPanel(), ModularPanelJack.OutNoteCV);
        arenvelope.connect(AREnvelopeJack.Out, pg1, PulseGeneratorJack.InModulation);
        lfo1.connect(MiniLFOJack.OutRight, pg1, PulseGeneratorJack.InWidth);
        pg1.connect(PulseGeneratorJack.Out, modular.getPanel(), ModularPanelJack.InLeft);

        modular.getPatternSequencer().addNote(46, 0f, 0.5f, 1f, 0);
        modular.getPatternSequencer().addNote(45, 1f, 1.25f, 0.5f, 0);
        modular.getPatternSequencer().addNote(46, 3f, 3.15f, 0.8f, 2);

        controller.getSoundMixer().getChannel(modular.getIndex()).setDelaySend(0.7f);
        controller.getSoundMixer().getChannel(modular.getIndex()).setReverbSend(0.35f);

        controller.getSystemSequencer().play(SequencerMode.PATTERN);
    }

}
