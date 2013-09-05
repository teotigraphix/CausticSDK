
package com.teotigraphix.caustk.sequencer.queue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.core.ControllerComponent;
import com.teotigraphix.caustk.controller.core.ControllerComponentState;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackSequencerTrackSongChange;
import com.teotigraphix.caustk.sequencer.track.TrackSong;

public class QueueSequencer extends ControllerComponent implements IQueueSequencer {

    private boolean audioEnabled = true;

    /*
     * for unit testing
     */
    boolean isAudioEnabled() {
        return audioEnabled;
    }

    void setAudioEnabled(boolean value) {
        audioEnabled = value;
    }

    private QueuePlayer player;

    QueuePlayer getPlayer() {
        return player;
    }

    private boolean recordMode;

    @Override
    public boolean isRecordMode() {
        return recordMode;
    }

    @Override
    public void setRecordMode(boolean value) {
        recordMode = value;
    }

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
        player = new QueuePlayer(this);
    }

    @Override
    public void onRegister() {
        super.onRegister();

        final ISystemSequencer systemSequencer = getController().getSystemSequencer();

        systemSequencer.getDispatcher().register(OnSystemSequencerBeatChange.class,
                new EventObserver<OnSystemSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSystemSequencerBeatChange object) {
                        beatChange(object.getMeasure(), object.getBeat());
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
                .getAbsoluteResource(new File("songs", queueSong.getFile().getPath()).getPath());
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
    public boolean touch(QueueData data) {
        return player.touch(data);
    }

    //    @Override
    //    public boolean queue(QueueData data) {
    //        return player.queue(data);
    //    }
    //
    //    @Override
    //    public boolean unqueue(QueueData data) {
    //        return player.unqueue(data);
    //    }

    public void beatChange(int measure, float beat) {
        player.beatChange(measure, beat);
    }

    @Override
    public void play() throws CausticException {
        player.play();
    }

    @Override
    public void stop() {
        player.stop();
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
