
package com.teotigraphix.caustk.sequencer.queue;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.sequencer.IQueueSequencer.OnQueueSequencerDataChange;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.sequencer.queue.QueueData.QueueDataState;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.caustk.sequencer.track.TrackItem;
import com.teotigraphix.caustk.sequencer.track.TrackPhrase;
import com.teotigraphix.caustk.sequencer.track.TrackSong;

public class QueuePlayer {

    private final ICaustkController getController() {
        return queueSequencer.getController();
    }

    public final TrackSong getTrackSong() {
        return getController().getTrackSequencer().getTrackSong();
    }

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

    private QueueSequencer queueSequencer;

    public QueuePlayer(QueueSequencer queueSequencer) {
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
        if (queueSequencer.isAudioEnabled())
            getController().getSystemSequencer().play(SequencerMode.SONG);
    }

    public void stop() {
        getController().getSystemSequencer().stop();
    }

    public boolean touch(QueueData data) {

        // QUEUE
        if (isLockBeat())
            return false;
        log("queue(" + data + ")");

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

        //System.out.println("M:" + getTrackSong().getCurrentMeasure() + "B:"
        //        + getTrackSong().getCurrentBeat());

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
        getController().getSoundMixer().getChannel(trackIndex).setMute(true);
        log("Mute[" + trackIndex + "]");
    }

    private void unmute(int trackIndex) {
        getController().getSoundMixer().getChannel(trackIndex).setMute(false);
        log("UnMute[" + trackIndex + "]");
    }

    private void extendOrRemovePlayingTracks() {
        //final float currentBeat = getTrackSong().getCurrentBeat();
        final int currentMeasure = getTrackSong().getCurrentMeasure();

        for (Track track : getController().getTrackSequencer().getTracks()) {

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
            TrackPhrase phrase = track.getPhrase(data.getBankIndex(), data.getPatternIndex());
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
            queueSequencer.getDispatcher().trigger(new OnQueueSequencerDataChange(data));
        }
    }

    private void log(String message) {
        CtkDebug.log(message);
    }
}
