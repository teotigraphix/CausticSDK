
package com.teotigraphix.caustk.gs.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.gs.machine.BasslineMachine;
import com.teotigraphix.caustk.gs.machine.GrooveMachine;
import com.teotigraphix.caustk.gs.machine.GrooveStation;
import com.teotigraphix.caustk.gs.machine.GrooveStation.GrooveMachineDescriptor;
import com.teotigraphix.caustk.gs.machine.GrooveStation.GrooveStationSetup;
import com.teotigraphix.caustk.gs.machine.MachineType;
import com.teotigraphix.caustk.tone.ToneType;

public class GrooveMachineTest extends CaustkTestBase {

    @SuppressWarnings("unused")
    private Pattern pattern1;

    private GrooveStation grooveStation;

    @SuppressWarnings("unused")
    private GrooveMachine bassline;

    private BasslineMachine basslineMachine;

    @Override
    protected void start() throws CausticException, IOException {
        grooveStation = new GrooveStation(controller);

        GrooveStationSetup setup = new GrooveStationSetup();
        setupBassline(setup);
        grooveStation.setup(setup);

        basslineMachine = (BasslineMachine)grooveStation.getMachines().get(0);
    }

    private void setupBassline(GrooveStationSetup setup) {
        GrooveMachineDescriptor bassline = new GrooveMachineDescriptor(MachineType.Bassline);
        bassline.addPart("part1", ToneType.Bassline);
        bassline.addPart("part2", ToneType.Bassline);

        setup.addDescriptor(bassline);
    }

    @Override
    protected void end() {

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
        //controller.getSystemSequencer().play(SequencerMode.PATTERN);

        // in memory patterns are those that are written to disk when current
        // once the pattern is changed without a write operation, the pattern
        // is recreated from the init data when reselected. If a pattern is reselected
        // that was written to disk, the original pattern created will be used.
        assertFalse(currentPattern.isInMemory());
        assertEquals(0, basslineMachine.getMemoryManager().getSelectedMemoryBank()
                .getInMemoryPatterns().size());
        basslineMachine.write();
        assertTrue(currentPattern.isInMemory());
        assertEquals(1, basslineMachine.getMemoryManager().getSelectedMemoryBank()
                .getInMemoryPatterns().size());

        basslineMachine.setNextPatternIndex(42);

        currentPattern = basslineMachine.getPattern();
        assertFalse(currentPattern.isInMemory());

        currentPattern.getSelectedPart().getPhrase().triggerOn(1);

        basslineMachine.setNextPatternIndex(0);
        currentPattern = basslineMachine.getPattern();
        assertTrue(currentPattern.isInMemory());
    }
}
