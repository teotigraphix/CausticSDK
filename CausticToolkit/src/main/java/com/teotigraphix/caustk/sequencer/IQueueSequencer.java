
package com.teotigraphix.caustk.sequencer;

import java.util.Collection;
import java.util.Map;

import com.teotigraphix.caustk.controller.IControllerComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueDataChannel;

public interface IQueueSequencer extends IControllerComponent {

    boolean queue(QueueData data);

    boolean unqueue(QueueData data);

    QueueData getQueueData(int bankIndex, int patternIndex);

    QueueDataChannel getChannel(int bankIndex, int patternIndex, int toneIndex);

    Collection<QueueData> getQueueData(int bankIndex);

    Map<Integer, QueueData> getView(int bankIndex);

    void play() throws CausticException;

    void stop();

    boolean isRecordMode();

    void setRecordMode(boolean value);

    public static class OnQueueSequencerDataChange {

        private QueueData data;

        public QueueData getData() {
            return data;
        }

        public OnQueueSequencerDataChange(QueueData data) {
            this.data = data;
        }
    }

}
