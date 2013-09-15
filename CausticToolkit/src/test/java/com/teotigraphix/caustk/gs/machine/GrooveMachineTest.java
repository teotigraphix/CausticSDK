
package com.teotigraphix.caustk.gs.machine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gs.machine.DrumMachine.ChannelProperty;
import com.teotigraphix.caustk.gs.machine.GrooveStation.GrooveMachineDescriptor;
import com.teotigraphix.caustk.gs.machine.GrooveStation.GrooveStationSetup;
import com.teotigraphix.caustk.gs.pattern.Pattern;
import com.teotigraphix.caustk.gs.pattern.SynthPart;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.tone.ToneType;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class GrooveMachineTest extends CaustkTestBase {

    @SuppressWarnings("unused")
    private Pattern pattern1;

    private GrooveStation grooveStation;

    @SuppressWarnings("unused")
    private GrooveMachine bassline;

    private BasslineMachine basslineMachine;

    private DrumMachine drumMachine;

    @Override
    protected void start() throws CausticException, IOException {
        grooveStation = new GrooveStation(controller);

        GrooveStationSetup setup = new GrooveStationSetup();
        setupBassline(setup);
        grooveStation.setup(setup);

        basslineMachine = (BasslineMachine)grooveStation.getMachines().get(0);
        drumMachine = (DrumMachine)grooveStation.getMachines().get(1);
    }

    private void setupBassline(GrooveStationSetup setup) {
        GrooveMachineDescriptor bassline = new GrooveMachineDescriptor(MachineType.Bassline);
        bassline.addPart("part1", ToneType.Bassline);
        bassline.addPart("part2", ToneType.Bassline);
        setup.addDescriptor(bassline);

        GrooveMachineDescriptor drummachine = new GrooveMachineDescriptor(MachineType.Drum);
        drummachine.addPart("part3", ToneType.Beatbox);
        drummachine.addPart("part4", ToneType.Beatbox);
        setup.addDescriptor(drummachine);
    }

    @Override
    protected void end() {

    }

    @Test
    public void test_bassline_drummachine() throws CausticException {
        //basslineMachine.setMute(true);

        basslineMachine.setNextPatternIndex(0);
        drumMachine.setNextPatternIndex(0);

        File preset1a = RuntimeUtils.getCausticPresetsFile("bassline", "CLASSIC GROWL");
        File preset2a = RuntimeUtils.getCausticPresetsFile("bassline", "PWM TRANCE");
        basslineMachine.getParts().get(0).getPatch().loadPreset(preset1a);
        basslineMachine.getParts().get(1).getPatch().loadPreset(preset2a);

        basslineMachine.getParts().get(0).getPhrase().triggerOn(2, 45, 0.5f, 1f, 1);
        controller.getSoundMixer().getChannel(0).setDelaySend(0.5f);
        controller.getSoundMixer().getChannel(2).setReverbSend(0.15f);

        File preset1 = RuntimeUtils.getCausticPresetsFile("beatbox", "808");
        File preset2 = RuntimeUtils.getCausticPresetsFile("beatbox", "909");
        drumMachine.loadPreset(0, preset1);
        drumMachine.loadPreset(1, preset2);

        // [0]48, 1[49], 2[50], 3[51], 4[52], 5[53], 6[54], 7[55]

        drumMachine.triggerOn(0, 1, 2, 1);
        drumMachine.triggerOn(0, 1, 6, 1);
        drumMachine.triggerOn(0, 1, 10, 1);
        drumMachine.triggerOn(0, 1, 11, 1f);
        drumMachine.triggerOn(0, 1, 12, 1f);
        drumMachine.triggerOn(0, 1, 13, 1f);
        drumMachine.triggerOn(0, 1, 14, 1);
        drumMachine.triggerOn(0, 1, 15, 1f);

        drumMachine.setSwing(0, 0.5f);

        //drumMachine.getParts().get(1).setMute(true);
        //drumMachine.setChannelProperty(0, 0, ChannelProperty.Pan, -1f);
        drumMachine.setChannelProperty(0, 0, ChannelProperty.Volume, 0.25f);

        controller.getSystemSequencer().play(SequencerMode.PATTERN);
    }

    @Test
    public void test_init() throws CausticException {
        // the GrooveMachine will handle callbacks from the GrooveStation with beat change events
        // The GrooveStation will be listening to the SystemSequencer for beatchange events.

        // how to add init data to patterns?
        basslineMachine.setNextPatternIndex(0);

        Pattern currentPattern = basslineMachine.getPattern();
        assertEquals(2, currentPattern.getPartCount());
        assertEquals(4, currentPattern.getPart(0).getPhrase().getTriggers().size());
        assertEquals(4, currentPattern.getPart(1).getPhrase().getTriggers().size());

        assertEquals(1, currentPattern.getPart(0).getPhrase().getTrigger(0f).getNotes().size());
        assertEquals(1, currentPattern.getPart(1).getPhrase().getTrigger(0f).getNotes().size());

        SynthPart selectedPart = currentPattern.getSelectedPart();
        // test a default trigger/note is created for a "non" existing trigger location
        selectedPart.getPhrase().triggerOn(1);
        assertTrue(selectedPart.getPhrase().containsTrigger(1));
        assertTrue(selectedPart.getPhrase().getTrigger(1).isSelected());
        selectedPart.getPhrase().triggerOff(1);
        assertFalse(selectedPart.getPhrase().getTrigger(1).isSelected());

        // in memory patterns are those that are written to disk when current
        // once the pattern is changed without a write operation, the pattern
        // is recreated from the init data when reselected. If a pattern is reselected
        // that was written to disk, the original pattern created will be used.
        assertFalse(currentPattern.isInMemory());
        assertEquals(0, basslineMachine.getMemory().getSelectedMemoryBank()
                .getInMemoryPatterns().size());
        basslineMachine.write();
        assertTrue(currentPattern.isInMemory());
        assertEquals(1, basslineMachine.getMemory().getSelectedMemoryBank()
                .getInMemoryPatterns().size());

        basslineMachine.setNextPatternIndex(42);

        currentPattern = basslineMachine.getPattern();
        assertFalse(currentPattern.isInMemory());

        currentPattern.getSelectedPart().getPhrase().triggerOn(1);

        basslineMachine.setNextPatternIndex(0);
        currentPattern = basslineMachine.getPattern();
        assertTrue(currentPattern.isInMemory());

        File preset1 = RuntimeUtils.getCausticPresetsFile("bassline", "CLASSIC GROWL");
        File preset2 = RuntimeUtils.getCausticPresetsFile("bassline", "PWM TRANCE");
        basslineMachine.getParts().get(0).getPatch().loadPreset(preset1);
        basslineMachine.getParts().get(1).getPatch().loadPreset(preset2);

        selectedPart.getPhrase().triggerOn(2, 45, 0.5f, 1f, 1);

        controller.getSystemSequencer().play(SequencerMode.PATTERN);

        controller.getSoundMixer().getChannel(0).setDelaySend(0.5f);
    }
}
