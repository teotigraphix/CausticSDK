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

package com.teotigraphix.caustic.internal.controller;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IRackController;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.dialog.CausticUtils;
import com.teotigraphix.caustic.dialog.CausticUtils.ISongChooserListener;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRackSong;
import com.teotigraphix.common.utils.RuntimeUtils;

public class RackController implements IRackController {

    private static final String CAUSTIC_EXT = ".caustic";

    private IRack mRack;

    private LoadRackSongTask mLoadTask;

    private OnRackSongLoadListener mLoadListener;

    @Inject
    public RackController(IRack rack) {
        mRack = rack;
    }

    @Override
    public void loadSong(String songName, OnRackSongLoadListener l) {
        File file = RuntimeUtils.getCausticSongFile(songName);
        doLoadSong(file, l);
    }

    @Override
    public void loadSong(File file, OnRackSongLoadListener l) {
        doLoadSong(file, l);
    }

    @Override
    public boolean isLoading() {
        return (mLoadTask != null && mLoadTask.getStatus() == Status.RUNNING);
    }

    protected void doLoadSong(File file, OnRackSongLoadListener l) {
        mLoadListener = l;
        mLoadTask = new LoadRackSongTask(mRack);
        mLoadTask.execute(file.getAbsolutePath());
    }

    public class LoadRackSongTask extends AsyncTask<String, Void, Void> {

        private IRack mRack;

        private IRackSong mSong;

        public LoadRackSongTask(IRack rack) {
            super();
            mRack = rack;
        }

        @Override
        protected Void doInBackground(String... songs) {
            try {
                mSong = mRack.loadSong(songs[0]);
            } catch (CausticException e) {
                e.printStackTrace();
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onCancelled(Void result) {
            // canceld if there is an exception loading the song
        }

        @Override
        protected void onPostExecute(Void result) {
            songLoaded(mSong);
        }
    }

    public void songLoaded(IRackSong song) {
        mLoadTask = null;
        if (mLoadListener != null) {
            song.setName(song.getFile().getName().replace(CAUSTIC_EXT, ""));
            mLoadListener.onRackSongLoaded(song);
        }
    }

    //--------------------------------------------------------------------------
    // 
    //  SongChooser
    // 
    //--------------------------------------------------------------------------

    @Override
    public void showSongChooser(final Context context, final OnRackSongLoadListener l) {

        if (isLoading())
            return;

        AlertDialog dialog = CausticUtils.buildSongChooser(context, new ISongChooserListener() {
            @Override
            public void onSongSelect(String songName) {
                loadSong(songName, l);
            }
        });
        dialog.show();
    }
}
