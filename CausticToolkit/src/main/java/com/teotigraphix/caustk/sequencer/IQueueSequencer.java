
package com.teotigraphix.caustk.sequencer;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.sequencer.queue.QueueData;

public interface IQueueSequencer extends IControllerComponent {

    boolean queue(QueueData data);

    boolean unqueue(QueueData data);

}
