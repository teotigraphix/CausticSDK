
package com.teotigraphix.caustic.internal.output;

import com.teotigraphix.caustic.output.IOutputPanel.Mode;
import com.teotigraphix.caustic.test.EmptyRackTestBase;

public class Test_OutputPanelSetup extends EmptyRackTestBase {

    @Override
    protected void setupValues() {
        assertEquals(120.0f, mOutputPanel.getBPM(true));
        assertEquals(Mode.PATTERN, mOutputPanel.getMode(true));

        mOutputPanel.setBPM(142.0f);
        mOutputPanel.setMode(Mode.SONG);
        assertEquals(142.0f, mOutputPanel.getBPM());
        assertEquals(142.0f, mOutputPanel.getBPM(true));
        assertEquals(Mode.SONG, mOutputPanel.getMode());
    }

    @Override
    protected void assertValues() {
        // TODO (mschmalle) [UnitTest Fail] assertEquals(142.0f, mOutputPanel.getBPM());
        assertEquals(Mode.SONG, mOutputPanel.getMode());
    }
}
