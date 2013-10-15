////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.sequencer;

import java.util.Collection;
import java.util.Map;

import com.teotigraphix.caustk.controller.IRackComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueDataChannel;

public interface IQueueSequencer extends IRackComponent {

    boolean touch(QueueData data);

    //    boolean queue(QueueData data);
    //
    //    boolean unqueue(QueueData data);

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

    boolean isAudioEnabled();

    /**
     * Removes the {@link QueueData} from any current queues and sets it's state
     * to idle.
     * 
     * @param data The data to remove from the sequencer.
     */
    void remove(QueueData data);

}
