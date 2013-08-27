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

import java.io.File;
import java.io.IOException;

import org.androidtransfuse.event.EventObserver;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sound.ISoundSource.OnSoundSourceSongLoad;
import com.teotigraphix.caustk.tone.Tone;

public class SongPlayer extends SubControllerBase implements ISongPlayer {

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return SongPlayerModel.class;
    }

    //----------------------------------
    // model
    //----------------------------------

    SongPlayerModel getModel() {
        return (SongPlayerModel)getInternalModel();
    }

    //----------------------------------
    // song
    //----------------------------------

    private TrackSong song;

    public TrackSong getSong() {
        return song;
    }

    //----------------------------------
    // song
    //----------------------------------

    private float currentBeat;

    @Override
    public final float getCurrentbeat() {
        return currentBeat;
    }

    @Override
    public final void setCurrentBeat(float value) {
        if (value == currentBeat)
            return;
        // this is the ONLY place the beat is set from the native song sequencer
        currentBeat = value;
        System.out.println("Beat:" + value);
        if (song != null) {
            song.setCurrentBeat(currentBeat);
        }
        getController().getDispatcher().trigger(new OnSongPlayerBeatChange(currentBeat));
    }

    public SongPlayer(ICaustkController controller) {
        super(controller);
        getController().getSoundSource().getDispatcher()
                .register(OnSoundSourceSongLoad.class, new EventObserver<OnSoundSourceSongLoad>() {
                    @Override
                    public void trigger(OnSoundSourceSongLoad object) {
                        try {
                            loadSong(object.getFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (CausticException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void loadSong(File file) throws IOException, CausticException {
        song = create(file);
        // SoundSource just loaded song
        // create tracks
        song.clearTracks();
        song.setNumTracks(14);

        for (Tone tone : getController().getSoundSource().getTones()) {
            int index = tone.getIndex();
            Track track = song.getTrack(index);
            track.restore(getController());
        }
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public TrackSong create() throws IOException {
        if (song != null) {
            unload(song);
        }
        song = new TrackSong();

        // File stays null until it has an explicit location set
        //song.setFile(file);
        song.wakeup(getController());
        return song;
    }

    public TrackSong create(File file) throws IOException {
        TrackSong song = create();
        song.setFile(file);
        return song;
    }

    private void unload(TrackSong song) {
        song.setController(null);
        // song.dispose();
    }
}
