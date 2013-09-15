
package com.teotigraphix.caustk.gs.pattern;

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
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
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
    public void test_init() {
        // the GrooveMachine will handle callbacks from the GrooveStation with beat change events
        // The GrooveStation will be listening to the SystemSequencer for beatchange events.

        // how to add init data to patterns?
        basslineMachine.setNextPatternIndex(0);

        controller.getSystemSequencer().play(SequencerMode.PATTERN);
    }
}
