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

package com.teotigraphix.caustic.controller;

import java.io.File;

import android.content.Context;

import com.teotigraphix.caustic.rack.IRackSong;

public interface IRackController {

    void showSongChooser(final Context context, OnRackSongLoadListener l);

    void loadSong(String songName, OnRackSongLoadListener l);

    void loadSong(File file, OnRackSongLoadListener l);

    boolean isLoading();

    public interface OnRackSongLoadListener {
        /**
         * Callback when the {@link IRackSong} has been fully loaded and
         * restored from the background task.
         * 
         * @param song
         */
        void onRackSongLoaded(IRackSong song);
    }
}
