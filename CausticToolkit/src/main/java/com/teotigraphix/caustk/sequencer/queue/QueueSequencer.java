
package com.teotigraphix.caustk.sequencer.queue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.core.ControllerComponent;
import com.teotigraphix.caustk.controller.core.ControllerComponentState;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackSequencerTrackSongChange;
import com.teotigraphix.caustk.sequencer.queue.QueueData.QueueDataState;
import com.teotigraphix.caustk.sequencer.track.TrackChannel;
import com.teotigraphix.caustk.sequencer.track.TrackItem;
import com.teotigraphix.caustk.sequencer.track.TrackPhrase;
import com.teotigraphix.caustk.sequencer.track.TrackSong;
import com.teotigraphix.caustk.tone.Tone;

public class QueueSequencer extends ControllerComponent implements IQueueSequencer {

    private int currentLocalBeat;

    private List<QueueData> playQueue = new ArrayList<QueueData>();

    private List<QueueData> queued = new ArrayList<QueueData>();

    private List<QueueData> flushedQueue = new ArrayList<QueueData>();

    final ITrackSequencer getTrackSequencer() {
        return getController().getTrackSequencer();
    }

    @Override
    protected Class<? extends ControllerComponentState> getStateType() {
        return QueueSequencerState.class;
    }

    //----------------------------------
    // queueSong
    //----------------------------------

    private QueueSong queueSong;

    public final QueueSong getQueueSong() {
        return queueSong;
    }

    public final TrackSong getTrackSong() {
        return getTrackSequencer().getTrackSong();
    }

    @Override
    public Collection<QueueData> getQueueData(int bankIndex) {
        return queueSong.getQueueData(bankIndex);
    }

    @Override
    public Map<Integer, QueueData> getView(int bankIndex) {
        return queueSong.getView(bankIndex);
    }

    @Override
    public QueueData getQueueData(int bankIndex, int patternIndex) {
        return queueSong.getQueueData(bankIndex, patternIndex);
    }

    @Override
    public QueueDataChannel getChannel(int bankIndex, int patternIndex, int toneIndex) {
        return queueSong.getChannel(bankIndex, patternIndex, toneIndex);
    }

    public QueueSequencer(ICaustkController controller) {
        super(controller);
    }

    @Override
    public void onRegister() {
        super.onRegister();

        final ISystemSequencer systemSequencer = getController().getSystemSequencer();

        systemSequencer.getDispatcher().register(OnSystemSequencerBeatChange.class,
                new EventObserver<OnSystemSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSystemSequencerBeatChange object) {
                        beatChange(object.getBeat());
                    }
                });

        getTrackSequencer().getDispatcher().register(OnTrackSequencerTrackSongChange.class,
                new EventObserver<OnTrackSequencerTrackSongChange>() {
                    @Override
                    public void trigger(OnTrackSequencerTrackSongChange object) {
                        switch (object.getKind()) {
                            case Create:
                                create(object.getTrackSong());
                                break;

                            case Load:
                                load(object.getTrackSong());
                                break;

                            case Save:
                                try {
                                    save();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;

                            default:
                                break;
                        }
                    }
                });
    }

    protected void save() throws IOException {
        File localFile = getController().getProjectManager().getProject()
                .getResource(queueSong.getFile().getPath());
        File absoluteTargetSongFile = localFile.getAbsoluteFile();
        getController().getSerializeService().save(absoluteTargetSongFile, queueSong);
    }

    private File toFile(TrackSong trackSong) {
        File songsDirectory = trackSong.getDirectory();
        String fileName = trackSong.getFileName() + "Queue.ctks";
        File file = new File(songsDirectory, fileName);
        return file;
    }

    protected void create(TrackSong trackSong) {
        File queueFile = toFile(trackSong);
        queueSong = new QueueSong(queueFile);
        queueSong.wakeup(getController());
    }

    protected void load(TrackSong trackSong) {
        File songsDirectory = trackSong.getAbsoluteFile().getParentFile();
        String fileName = trackSong.getFileName() + "Queue.ctks";
        File file = new File(songsDirectory, fileName);

        queueSong = getController().getSerializeService().fromFile(file, QueueSong.class);
    }

    @Override
    public boolean queue(QueueData data) {
        if (currentLocalBeat == 3)
            return false;

        if (!queued.contains(data)) {
            CtkDebug.log("Queue:" + data);
            data.setState(QueueDataState.Queued);
            queued.add(data);
        }
        return true;
    }

    @Override
    public boolean unqueue(QueueData data) {
        if (currentLocalBeat == 3)
            return false;
        if (playQueue.contains(data)) {
            if (data.getState() == QueueDataState.Queued) {
                data.setState(QueueDataState.Idle);
                playQueue.remove(data);
            } else {
                data.setState(QueueDataState.UnQueued);
            }
        } else {
            queued.remove(data);
            data.setState(QueueDataState.Idle);
        }

        return true;
    }

    public void beatChange(float beat) {
        getQueueSong().nextBeat();

        currentLocalBeat = (int)(beat % 4);

        // start new measure
        if (currentLocalBeat == 0) {
            for (QueueData data : flushedQueue) {
                if (data.getState() != QueueDataState.Queued) {
                    data.setState(QueueDataState.Idle);
                    QueueDataChannel channel = data.getChannel(data.getViewChannel());
                    //                    channel.setCurrentBeat(0);
                }

                for (QueueDataChannel channel : data.getChannels()) {
                    Tone tone = getController().getSoundSource().getTone(channel.getToneIndex());
                    tone.setMuted(true);
                }
            }

            flushedQueue.clear();

            for (QueueData data : playQueue) {
                for (QueueDataChannel channel : data.getChannels()) {
                    Tone tone = getController().getSoundSource().getTone(channel.getToneIndex());
                    if (tone != null)
                        tone.setMuted(false);
                }
            }

            //CtkDebug.model(">> Remainder:" + remainder);
            lockAndExtendPlayingTracks();
        }

        // last beat in measure
        if (currentLocalBeat == 3) {
            queueTracks();
        }

        for (QueueData data : playQueue) {
            QueueDataChannel channel = data.getChannel(data.getViewChannel());
            //int calcBeatInMeasure = channel.getChannelPhrase().getLength();
            //Track track = getSong().getTrack(channel.getIndex());
            //TrackItem trackItem = track.getTrackItem(measure);
            //if (trackItem != null) {
            //int beats = trackItem.getStartMeasure() * 4;
            //beats = beat - beats;
            //int currentLocalBeat
            //.setCurrentBeat(beat);
            //            channel.setCurrentBeat(channel.getCurrentBeat() + 1);
            //}
        }
    }

    private void lockAndExtendPlayingTracks() {
        final int beat = getTrackSong().getCurrentBeat();
        final int currentMeasure = getTrackSong().getCurrentMeasure();
        @SuppressWarnings("unused")
        final int isNewMeasure = beat % 4;

        // from here on, we have everything correct with current beat and measure
        // the TrackSong's cursor is correct.
        // Check to see if there are any tracks that are in their last beat
        // All tracks in the last beat get extended their length

        for (TrackChannel track : getTrackSequencer().getTracks()) {

            // First try an find a track item at the current measure
            List<TrackItem> list = track.getItemsOnMeasure(currentMeasure);
            for (TrackItem item : list) {
                //String name = PatternUtils.toString(item.getBankIndex(), item.getPatternIndex());

                //int numPhraseMeasures = item.getNumMeasures();
                //int numBeatsInPhrase = (4 * numPhraseMeasures);
                //int startMeasure = beat % numBeatsInPhrase;

                //CtkDebug.model("XXX:" + startMeasure);
                // If the current beat is the last beat in the phrase
                if (item.getEndMeasure() == currentMeasure + 1) {
                    //if (startMeasure == numBeatsInPhrase - 1) {

                    QueueData data = queueSong.getQueueData(item.getBankIndex(),
                            item.getPatternIndex());

                    for (QueueDataChannel channel : data.getChannels()) {

                        if (channel.isLoopEnabled()) {

                            if (data.getState() == QueueDataState.Selected) {
                                // add the phrase at the very next measure
                                addPhraseAt(track, currentMeasure + 1, null);
                                // channel.setCurrentBeat(0);
                            } else if (data.getState() == QueueDataState.UnQueued) {
                                // remove the item from the que
                                stopPlaying(data);
                            }

                        } else {
                            // oneshot remove
                            stopPlaying(data);
                            // set this here since turning off is immediate
                            data.setState(QueueDataState.Idle);
                        }

                    }

                    //CtkDebug.model("Lock:" + beat);
                }
            }
        }
    }

    private void queueTracks() {
        @SuppressWarnings("unused")
        final int currentBeat = getTrackSong().getCurrentBeat();
        final int currentMeasure = getTrackSong().getCurrentMeasure();

        //CtkDebug.log("Setup queued [" + currentBeat + "," + currentMeasure + "]");

        ArrayList<QueueData> copied = new ArrayList<QueueData>(queued);
        // loop through the queue and add queued items
        for (QueueData data : copied) {
            if (data.getState() == QueueDataState.Queued) {
                // add to sequencer
                for (QueueDataChannel channel : data.getChannels()) {
                    @SuppressWarnings("unused")
                    TrackChannel track = getTrackSong().getTrack(channel.getToneIndex());
                    //addPhraseAt(track, currentMeasure + 1, channel.getChannelPhrase());
                }
                startPlaying(data);
            } else if (data.getState() == QueueDataState.Selected) {

            }
        }
    }

    @Override
    public void play() throws CausticException {
        getTrackSong().rewind();

        ArrayList<QueueData> copied = new ArrayList<QueueData>(queued);
        for (QueueData data : copied) {
            if (data.getState() == QueueDataState.Queued) {
                // add to sequencer
                for (QueueDataChannel channel : data.getChannels()) {
                    TrackChannel track = getTrackSong().getTrack(channel.getToneIndex());
                    addPhraseAt(track, 0, data);
                }
                startPlaying(data);
            } else if (data.getState() == QueueDataState.Selected) {

            }
        }
        //getSong().play();
        getController().getSystemSequencer().play(SequencerMode.SONG);
        getController().getDispatcher().trigger(new OnQueueSequencerDataChange());
    }

    @SuppressWarnings("unused")
    private void addPhraseAt(TrackChannel track, int start, QueueData data) {
        try {
            TrackPhrase phrase = track.getPhrase(data.getBankIndex(), data.getPatternIndex());

            track.addPhraseAt(start, 1, phrase, true);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    private void startPlaying(QueueData data) {
        data.setState(QueueDataState.Selected);
        queued.remove(data);
        playQueue.add(data);

    }

    private void stopPlaying(QueueData data) {
        flushedQueue.add(data);
        playQueue.remove(data);
        // things will get set to idle in flush if not queued again
        // data.setState(PadDataState.IDLE);
    }

    public static class OnQueueSequencerDataChange {

    }

    public static class QueueSequencerState extends ControllerComponentState {

        public QueueSequencerState() {
            super();
        }

        public QueueSequencerState(ICaustkController controller) {
            super(controller);
        }

    }

}
