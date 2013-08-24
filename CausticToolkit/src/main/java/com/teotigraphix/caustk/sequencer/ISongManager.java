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

import com.teotigraphix.caustk.controller.ICaustkController;

public interface ISongManager {

    TrackSong getTrackSong();

    TrackSong create(File songFile) throws IOException;

    TrackSong create(String path) throws IOException;

    /**
     * Loads a {@link TrackSong} from a {@link File} using the file's relative
     * path from the <code>songs</code> directory.
     * 
     * @param file The relative path of the <code>ctks</code> file.
     * @throws IOException
     * @see OnSongManagerLoadComplete
     */
    TrackSong load(File file) throws IOException;

    /**
     * Saves the current {@link TrackSong}.
     * 
     * @throws IOException
     */
    void save() throws IOException;

    /**
     * Deletes a song file that contains a {@link TrackSong}.
     * 
     * @param file The song file to delete.
     * @throws IOException
     * @see OnSongManagerSongDelete
     */
    void delete(File file) throws IOException;

    /**
     * @see ICaustkController#getDispatcher()
     */
    public static class SongEvent {

        private TrackSong trackSong;

        public TrackSong getTrackSong() {
            return trackSong;
        }

        public SongEvent(TrackSong trackSong) {
            this.trackSong = trackSong;
        }
    }

    /**
     * Dispatched when a {@link TrackSong} is successfully loaded from an
     * external file.
     * 
     * @see ICaustkController#getDispatcher()
     * @see ISongManager#load(File)
     */
    public static class OnSongManagerLoadComplete extends SongEvent {
        public OnSongManagerLoadComplete(TrackSong trackSong) {
            super(trackSong);
        }
    }

    /**
     * Dispatched when a {@link TrackSong} is successfully deleted.
     * 
     * @see ICaustkController#getDispatcher()
     * @see ISongManager#delete(File)
     */
    public static class OnSongManagerSongDelete extends SongEvent {
        public OnSongManagerSongDelete(TrackSong trackSong) {
            super(trackSong);
        }
    }

    boolean songExists(File reletivePath);

    boolean songExists(String path);

    void setCurrentBeat(float beat);

    float getCurrentBeat();

}
