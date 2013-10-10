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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.controller.core.RackComponent;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.sequencer.ITrackSequencer.OnTrackSongChange;
import com.teotigraphix.caustk.sequencer.track.TrackSong;

public class QueueSequencer extends RackComponent implements IQueueSequencer {

    private static final long serialVersionUID = -4761805431570067279L;

    private boolean audioEnabled = true;

    /*
     * for unit testing
     */
    @Override
    public boolean isAudioEnabled() {
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

    public QueueSequencer(IRack rack) {
        super(rack);
        player = new QueuePlayer(this);
    }

    @Override
    public void registerObservers() {
        getController().register(OnSystemSequencerBeatChange.class,
                new EventObserver<OnSystemSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSystemSequencerBeatChange object) {
                        beatChange(object.getMeasure(), object.getBeat());
                    }
                });

        getController().register(OnTrackSongChange.class, new EventObserver<OnTrackSongChange>() {
            @Override
            public void trigger(OnTrackSongChange object) {
                switch (object.getKind()) {
                    case Create:
                        // XXX                        create(object.getTrackSong());
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
        @SuppressWarnings("unused")
        File absoluteTargetSongFile = localFile.getAbsoluteFile();
        //        getController().getSerializeService().save(absoluteTargetSongFile, queueSong);
    }

    private File toFile(TrackSong trackSong) {
        File songsDirectory = trackSong.getDirectory();
        String fileName = trackSong.getFileName() + "Queue.ctks";
        File file = new File(songsDirectory, fileName);
        return file;
    }

    public void create(TrackSong trackSong) {
        File queueFile = toFile(trackSong);
        queueSong = new QueueSong(queueFile);
        queueSong.setQueueSequencer(this);
    }

    protected void load(TrackSong trackSong) {
        File songsDirectory = trackSong.getAbsoluteFile().getParentFile();
        String fileName = trackSong.getFileName() + "Queue.ctks";
        @SuppressWarnings("unused")
        File file = new File(songsDirectory, fileName);

        //        queueSong = getController().getSerializeService().fromFile(file, QueueSong.class);
    }

    @Override
    public boolean touch(QueueData data) {
        return player.touch(data);
    }

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
}
