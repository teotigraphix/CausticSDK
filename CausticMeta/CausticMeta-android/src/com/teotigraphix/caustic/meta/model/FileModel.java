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

package com.teotigraphix.caustic.meta.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.core.CausticFile;
import com.teotigraphix.caustk.core.ISoundGenerator;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 */
public class FileModel {

    private CausticFile causticFile;

    private ISoundGenerator soundGenerator;

    public final ISoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // causticFile
    //----------------------------------

    public CausticFile getCausticFile() {
        return causticFile;
    }

    public void setFile(File value) throws FileNotFoundException {
        causticFile = new CausticFile(value);
        if (listener != null)
            listener.onFileChange(causticFile);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public FileModel(ISoundGenerator soundGenerator) {
        this.soundGenerator = soundGenerator;
    }

    //--------------------------------------------------------------------------
    // Public API :: Methods
    //--------------------------------------------------------------------------

    public void reset() throws IOException {
        causticFile.trim();
        causticFile = null;
        if (listener != null)
            listener.onReset();
    }

    public File saveSong(String name) {
        RackMessage.SAVE_SONG.send(getSoundGenerator(), name);
        return RuntimeUtils.getCausticSongFile(name);
    }

    public File saveSongAs(File file) throws IOException {
        File song = saveSong(file.getName().replace(".caustic", ""));
        if (!song.equals(file)) {
            FileUtils.copyFileToDirectory(song, file.getParentFile());
            song.delete();
        }
        return file;
    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    private OnFileModelChangeListener listener;

    public void setOnFileModelChangeListener(OnFileModelChangeListener l) {
        listener = l;
    }

    public static interface OnFileModelChangeListener {

        void onFileChange(CausticFile causticFile);

        void onReset();
    }
}
