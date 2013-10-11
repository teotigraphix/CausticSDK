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

package com.teotigraphix.caustk.sequencer.queue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.IQueueSequencer.OnQueueSequencerDataChange;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.sequencer.queue.QueueData.QueueDataState;
import com.teotigraphix.caustk.sequencer.track.Phrase;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.caustk.sequencer.track.TrackItem;
import com.teotigraphix.caustk.sequencer.track.TrackSong;

public class QueuePlayer implements Serializable {

    private static final long serialVersionUID = -2644355130242774038L;

    private final ICaustkController getController() {
        return queueSequencer.getRack().getController();
    }

    public final TrackSong getTrackSong() {
        return getController().getRack().getTrackSequencer().getTrackSong();
    }

    /**
     * Temp, whether the sequencer just got put back to 0 and needs to queue the
     * PLAY state items.
     */
    private boolean restarting;

    private int currentLocalBeat;

    private List<QueueData> playQueue = new ArrayList<QueueData>();

    private List<QueueData> tempPlayQueue = new ArrayList<QueueData>();

    private List<QueueData> queued = new ArrayList<QueueData>();

    private List<QueueData> flushedQueue = new ArrayList<QueueData>();

    List<QueueData> getPlayQueue() {
        return playQueue;
    }

    List<QueueData> getQueued() {
        return queued;
    }

    List<QueueData> getFlushedQueue() {
        return flushedQueue;
    }

    int getCurrentLocalBeat() {
        return currentLocalBeat;
    }

    private IQueueSequencer queueSequencer;

    public QueuePlayer(IQueueSequencer queueSequencer) {
        this.queueSequencer = queueSequencer;
    }

    public final boolean isLockBeat() {
        return currentLocalBeat == 3;
    }

    public final boolean isStartBeat() {
        return currentLocalBeat == 0;
    }

    public void play() throws CausticException {
        ArrayList<QueueData> copied = new ArrayList<QueueData>(queued);
        for (QueueData data : copied) {
            if (data.getState() == QueueDataState.Queue) {

                // add to sequencer
                Track track = getTrackSong().getTrack(data.getViewChannelIndex());
                addPhraseAt(track, 0, data);

                // this is special instance since everything is started
                setState(data, QueueDataState.Play);
                // automatically add to the playQueue
                playQueue.add(data);
                queued.remove(data);
            }
        }
        if (restarting) {
            for (QueueData data : playQueue) {
                Track track = getTrackSong().getTrack(data.getViewChannelIndex());
                addPhraseAt(track, 0, data);
            }
            restarting = false;
        }
        if (queueSequencer.isAudioEnabled()) {
            getController().execute(ISystemSequencer.COMMAND_PLAY, SequencerMode.SONG.getValue());
        }
    }

    public void stop() {
        try {
            getController().execute(ISystemSequencer.COMMAND_STOP);
            restartSequencer();
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    private void restartSequencer() throws CausticException {
        // XXX until recording is implemented, each time the sequencer
        // is stopped, erase all track items and set position back to 0
        getTrackSong().clear();
        getController().getRack().getSystemSequencer().playPosition(0);
        restarting = true;
    }

    public boolean touch(QueueData data) {
        // QUEUE
        if (isLockBeat())
            return false;

        // if the pattern is still playing but got unqueued, just update the state
        if (playQueue.contains(data)) {

            if (data.getState() == QueueDataState.PlayUnqueued) {
                setState(data, QueueDataState.Play);
                // check for a queued data of the same channel, take it out
                QueueData dataThatInvalidated = data.getDataThatInvalidated();
                if (dataThatInvalidated != null) {
                    setState(dataThatInvalidated, QueueDataState.Idle);
                    queued.remove(dataThatInvalidated);
                    data.setDataThatInvalidated(null);
                    dataThatInvalidated.setTheInvalidatedData(null);
                }

            } else if (data.getState() == QueueDataState.Play) {
                setState(data, QueueDataState.PlayUnqueued);
            }

        } else if (!queued.contains(data)) {

            QueueDataChannel viewChannel = data.getViewChannel();
            // the invalidData is the data that is still playing
            // but the viewChannel is going to trade playing spaces on the next measure
            QueueData invalidData = channelInvalidatesPlayingData(viewChannel);
            // check for the existence of another channel that shares this track
            if (invalidData != null) {

                if (invalidData.getDataThatInvalidated() != null) {
                    QueueData oldData = invalidData.getDataThatInvalidated();
                    queued.remove(oldData);
                    setState(oldData, QueueDataState.Idle);
                    invalidData.setDataThatInvalidated(data);
                    data.setTheInvalidatedData(invalidData);
                    setState(data, QueueDataState.Queue);
                    queued.add(data);
                } else {
                    invalidData.setDataThatInvalidated(data);
                    setState(invalidData, QueueDataState.PlayUnqueued);
                    queued.add(data);
                    setState(data, QueueDataState.Queue);
                    data.setTheInvalidatedData(invalidData);
                }

            } else {
                queued.add(data);
                setState(data, QueueDataState.Queue);
            }
        } else {
            // queued but not playing, just remove and set back to Idle
            queued.remove(data);
            setState(data, QueueDataState.Idle);
            if (data.getTheInvalidatedData() != null) {
                // reset the state
                setState(data.getTheInvalidatedData(), QueueDataState.Play);
                data.getTheInvalidatedData().setDataThatInvalidated(null);
                data.setTheInvalidatedData(null);
            }
        }

        return false;
    }

    public void beatChange(int measure, float beat) {
        getTrackSong().setPosition(measure, beat);

        debug("M:" + getTrackSong().getCurrentMeasure() + "B:" + getTrackSong().getCurrentBeat());

        currentLocalBeat = (int)(beat % 4);

        // start new measure
        if (isStartBeat()) {

            for (QueueData data : tempPlayQueue) {
                setState(data, QueueDataState.Play);
                playQueue.add(data);
                unmute(data.getViewChannelIndex());
            }

            tempPlayQueue.clear();

            for (QueueData data : flushedQueue) {
                if (data.getState() != QueueDataState.Queue) {
                    setState(data, QueueDataState.Idle);
                    mute(data.getViewChannelIndex());
                }
            }

            flushedQueue.clear();
        }

        // last beat in measure
        if (isLockBeat()) {

            extendOrRemovePlayingTracks();

            queueTracks();
        }
    }

    private void mute(int trackIndex) {
        //getController().getRack().getSoundMixer().getChannel(trackIndex).setMute(true);
        debug("Mute[" + trackIndex + "]");
    }

    private void unmute(int trackIndex) {
        //getController().getRack().getSoundMixer().getChannel(trackIndex).setMute(false);
        debug("UnMute[" + trackIndex + "]");
    }

    private void extendOrRemovePlayingTracks() {
        //final float currentBeat = getTrackSong().getCurrentBeat();
        final int currentMeasure = getTrackSong().getCurrentMeasure();

        for (Track track : getController().getRack().getTrackSequencer().getTracks()) {

            // Find all tracks that are ending at the next measure
            TrackItem item = track.getItemAtEndMeasure(currentMeasure + 1);
            // no item queued or playing at the measure
            if (item != null) {
                updateChannel(track, item);
            }
        }
    }

    private void updateChannel(Track track, TrackItem item) {
        final int currentMeasure = getTrackSong().getCurrentMeasure();

        QueueData data = queueSequencer.getQueueData(item.getBankIndex(), item.getPatternIndex());
        QueueDataChannel channel = data.getChannel(data.getViewChannelIndex());

        // data is the Play state
        if (data.getState() == QueueDataState.UnQueued
                || data.getState() == QueueDataState.PlayUnqueued) {

            flushedQueue.add(data);
            playQueue.remove(data);
            // things will get set to idle in flush if not queued again
            setState(data, QueueDataState.PlayUnqueued);

        } else if (channel.isLoopEnabled()) {

            if (data.getState() == QueueDataState.Play) {
                // add the phrase at the very next measure
                addPhraseAt(track, currentMeasure + 1, data);
                // channel.setCurrentBeat(0);
            }

        } else {
            // oneshot remove
            flushedQueue.add(data);
            playQueue.remove(data);
            // things will get set to idle in flush if not queued again
            setState(data, QueueDataState.PlayUnqueued);
        }
    }

    private void queueTracks() {
        //final float currentBeat = getTrackSong().getCurrentBeat();
        final int currentMeasure = getTrackSong().getCurrentMeasure();
        final int nextMeasure = currentMeasure + 1;

        // CtkDebug.log("Setup queued [" + currentBeat + "," + currentMeasure + "]");

        ArrayList<QueueData> copied = new ArrayList<QueueData>(queued);
        // loop through the queue and add queued items
        for (QueueData data : copied) {
            if (data.getState() == QueueDataState.Queue) {
                // add to sequencer
                QueueDataChannel channel = data.getViewChannel();
                Track track = getTrackSong().getTrack(channel.getToneIndex());
                TrackItem item = track.getItemAtEndMeasure(currentMeasure + 1);
                if (item != null) {
                    addPhraseAt(track, nextMeasure, data);
                    queued.remove(data);
                    tempPlayQueue.add(data);
                    if (data.getTheInvalidatedData() != null) {
                        data.getTheInvalidatedData().setDataThatInvalidated(null);
                        data.setTheInvalidatedData(null);
                    }
                } else if (data.getTheInvalidatedData() == null) {
                    addPhraseAt(track, nextMeasure, data);
                    queued.remove(data);
                    tempPlayQueue.add(data);
                }
                //                for (QueueDataChannel channel : data.getChannels()) {
                //                    TrackChannel track = getTrackSong().getTrack(channel.getToneIndex());
                //                    addPhraseAt(track, nextMeasure, data);
                //                }
                //                startPlaying(data);
            }
        }
    }

    // TrackItem item = track.getItemAtEndMeasure(currentMeasure + 1);

    private void addPhraseAt(Track track, int start, QueueData data) {
        try {
            Phrase phrase = track.getPhrase(data.getBankIndex(), data.getPatternIndex());
            track.addPhraseAt(start, 1, phrase, true);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    private QueueData channelInvalidatesPlayingData(QueueDataChannel channel) {
        for (QueueData playingData : playQueue) {
            if (playingData.getViewChannelIndex() == channel.getToneIndex())
                return playingData;
        }
        return null;
    }

    private void setState(QueueData data, QueueDataState state) {
        if (!state.equals(data.getState())) {
            data.setState(state);
            debug("setState(" + data + ")");
            getController().trigger(new OnQueueSequencerDataChange(data));
        }
    }

    private void debug(String message) {
        getController().getLogger().debug("QueuePlayer", message);
    }
}
