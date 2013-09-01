
package com.teotigraphix.caustk.sequencer.queue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ControllerComponent;
import com.teotigraphix.caustk.controller.ControllerComponentState;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.queue.QueueData.PadDataState;
import com.teotigraphix.caustk.track.ITrackSequencer;
import com.teotigraphix.caustk.track.ITrackSequencer.OnTrackSequencerTrackSongChange;
import com.teotigraphix.caustk.track.TrackSong;

public class QueueSequencer extends ControllerComponent implements IQueueSequencer {

    private int currentLocalBeat;

    private List<QueueData> playQueue = new ArrayList<QueueData>();

    private List<QueueData> queued = new ArrayList<QueueData>();

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

    public QueueSequencer(ICaustkController controller) {
        super(controller);
    }

    @Override
    public void onRegister() {
        super.onRegister();

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

    protected void create(TrackSong trackSong) {
        File songsDirectory = trackSong.getDirectory();
        String fileName = trackSong.getFileName() + "Queue.ctks";
        File queueFile = new File(songsDirectory, fileName);
        queueSong = new QueueSong(queueFile);
        queueSong.wakeup(getController());
    }

    protected void load(TrackSong trackSong) {
        queueSong = new QueueSong();
        queueSong.wakeup(getController());
    }

    public boolean queue(QueueData data) {
        if (currentLocalBeat == 3)
            return false;

        if (!queued.contains(data)) {
            CtkDebug.log("Queue:" + data);
            data.setState(PadDataState.QUEUED);
            queued.add(data);
        }
        return true;
    }

    public boolean unqueue(QueueData data) {
        if (currentLocalBeat == 3)
            return false;
        if (playQueue.contains(data)) {
            if (data.getState() == PadDataState.QUEUED) {
                data.setState(PadDataState.IDLE);
                playQueue.remove(data);
            } else {
                data.setState(PadDataState.UNQUEUED);
            }
        } else {
            queued.remove(data);
            data.setState(PadDataState.IDLE);
        }

        return true;
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
