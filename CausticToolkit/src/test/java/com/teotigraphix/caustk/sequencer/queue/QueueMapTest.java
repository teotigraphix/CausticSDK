
package com.teotigraphix.caustk.sequencer.queue;

import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;

public class QueueMapTest extends CaustkTestBase {

    private IQueueSequencer queueSequencer;

    @Override
    protected void start() throws CausticException, IOException {
        queueSequencer = controller.getQueueSequencer();
    }

    @Override
    protected void end() {
        queueSequencer = null;
    }

    @Test
    public void test_addRemove() throws CausticException {

    }
}
