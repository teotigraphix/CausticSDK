////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.song;

import java.util.Collection;

import com.teotigraphix.caustic.part.IPart;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ITrackSong extends ISong {

    //----------------------------------
    //  tracks
    //----------------------------------

    /**
     * Returns the current {@link ITrack}s in the song.
     */
    Collection<ITrack> getTracks();

    /**
     * Returns an {@link ITrack} that is registered against the {@link IPart}.
     * 
     * @param part The part the track owns.
     */
    ITrack getTrack(IPart part);

    /**
     * Returns an {@link ITrack} that is registered at the specified index.
     * 
     * @param index The track index.
     */
    ITrack getTrack(int index);

    /**
     * Adds an {@link ITrack} to the song using the specific part.
     * <p>
     * Note: THe track will be assigned the part's index.
     * 
     * @param part The part to assign to the track.
     * @return A new {@link ITrack} instance wrapping the part.
     */
    ITrack addTrack(IPart part);

    /**
     * Removes a {@link ITrack} that exists in the song.
     * 
     * @param index The index to remove.
     * @return The existing {@link ITrack} in the song.
     */
    ITrack removeTrack(int index);

    /**
     * Removes all {@link ITrack} instances within the song.
     */
    void removeAllTracks();

    /**
     * Adds an {@link ITrackSongListener} to the song.
     * 
     * @param value The {@link ITrackSongListener} to add.
     */
    void addTrackSongListener(ITrackSongListener listener);

    /**
     * Removes an {@link ITrackSongListener} from the song.
     * 
     * @param value The {@link ITrackSongListener} to remove.
     */
    void removeTrackSongListener(ITrackSongListener listener);

    /**
     * The {@link ITrackSongListener} allows the {@link ITrackSong} to notify
     * listeners about track add and remove events.
     */
    public interface ITrackSongListener {

        /**
         * Dispatched when an {@link ITrack} is added to the {@link ITrackSong}.
         * 
         * @param track The added track.
         */
        void onTrackAdded(ITrack track);

        /**
         * Dispatched when an {@link ITrack} is removed from the
         * {@link ITrackSong}.
         * 
         * @param track The removed track.
         */
        void onTrackRemoved(ITrack track);
    }

}
