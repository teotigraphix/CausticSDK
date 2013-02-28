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

package com.teotigraphix.caustic.dialog;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.teotigraphix.common.utils.RuntimeUtils;

public class CausticUtils {

    private static final String MESSAGE_LOAD_SONG = "Load a Caustic Song file";

    private static final String CAUSTIC_EXT = ".caustic";

    public static IOFileFilter createCausticSongFilter() {
        IOFileFilter filter = new IOFileFilter() {
            @Override
            public boolean accept(File file, String path) {
                return false;
            }

            @Override
            public boolean accept(File file) {
                if (file.isDirectory())
                    return false;
                return file.getName().indexOf(CAUSTIC_EXT) != -1;
            }
        };
        return filter;
    }

    // (mschmalle)  buildSongChooser() needs to be in a service eventually
    // (mschmalle)  buildSongChooser() give ability to load song other than /caustic/songs
    public static AlertDialog buildSongChooser(Context context, final ISongChooserListener listener) {
        File songsDirectory = RuntimeUtils.getCausticSongsDirectory();
        IOFileFilter filter = CausticUtils.createCausticSongFilter();
        Collection<File> songs = FileUtils.listFiles(songsDirectory, filter, filter);
        Collections.sort((List<File>)songs);
        final String[] items = new String[songs.size()];
        int i = 0;
        for (File file : songs) {
            items[i] = file.getName().replace(CAUSTIC_EXT, "");
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(MESSAGE_LOAD_SONG);
        builder.setItems(items, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                listener.onSongSelect(items[index]);
            }
        });
        return builder.create();
    }

    public interface ISongChooserListener {
        void onSongSelect(String songName);
    }
}
