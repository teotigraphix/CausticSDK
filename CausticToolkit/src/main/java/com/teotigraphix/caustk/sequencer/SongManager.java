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

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.SubControllerBase;
import com.teotigraphix.caustk.controller.SubControllerModel;
import com.teotigraphix.caustk.project.Project;

public class SongManager extends SubControllerBase implements ISongManager {

    @Override
    protected Class<? extends SubControllerModel> getModelType() {
        return SongManagerModel.class;
    }

    private static final String PROJECT_SESSION_LAST_SONG = "lastSong";

    private File songDirectory;

    //----------------------------------
    // trackSong
    //----------------------------------

    private TrackSong trackSong;

    @Override
    public TrackSong getTrackSong() {
        return trackSong;
    }

    public SongManager(ICaustkController controller) {
        super(controller);

        songDirectory = new File(controller.getConfiguration().getApplicationRoot(), "songs");
    }

    @Override
    protected void createProject(Project project) {
        super.createProject(project);
        trackSong = createTrackSong(createUntitledFile());
    }

    @Override
    protected void loadState(Project project) {
        super.loadState(project);
        String path = getController().getProjectManager().getSessionPreferences()
                .getString(PROJECT_SESSION_LAST_SONG);
        if (path == null) {
            throw new RuntimeException("TrackSong path null");
        } else if (toSongFile(new File(path)).exists()) {
            deserializeTrackSong(toSongFile(new File(path)));
        }
    }

    @Override
    protected void saveState(Project project) {
        super.saveState(project);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void projectExit(Project project) {
        super.projectExit(project);

        trackSong = null;
    }

    @Override
    public TrackSong create(String path) throws IOException {
        File file = toSongFile(new File(path));
        if (file.exists())
            throw new IOException("TrackSong file exists");
        trackSong = createTrackSong(new File(path));
        return trackSong;
    }

    @Override
    public TrackSong load(File file) throws IOException {
        file = toSongFile(file);
        if (!file.exists())
            throw new IOException("TrackSong file does not exist");

        deserializeTrackSong(file);
        saveSessionProperties();
        getController().getDispatcher().trigger(new OnSongManagerLoadComplete(trackSong));
        return trackSong;
    }

    @Override
    public void save() throws IOException {
        saveSessionProperties();

        if (trackSong == null || trackSong.getTracks().size() == 0) {
            return;
        }

        File target = toSongFile(trackSong.getFile());
        try {
            getController().getSerializeService().save(target, trackSong);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(File file) throws IOException {
        boolean success = FileUtils.deleteQuietly(file);
        if (!success)
            throw new IOException("TrackSong was not deleted");

    }

    private TrackSong createTrackSong(File file) {
        TrackSong song = new TrackSong();
        song.setFile(file);
        song.setController(getController());
        return song;
    }

    /**
     * Creates an Untitled.ctks File checking that there is not an existing
     * file, if their is, a number is appended to the end.
     */
    private File createUntitledFile() {
        // TODO createUntitledFile() need to loop through all files and get 'Untitled'
        // entries and find the next number to append
        File file = new File("Untitled.ctks");
        return file;
    }

    public File toSongFile(File file) {
        return new File(songDirectory, file.getPath());
    }

    @Override
    public boolean songExists(File file) {
        return toSongFile(file).exists();
    }

    @Override
    public boolean songExists(String reletivePath) {
        return toSongFile(new File(reletivePath)).exists();
    }

    private void saveSessionProperties() {
        if (trackSong == null)
            return;
        getController().getProjectManager().getSessionPreferences()
                .put(PROJECT_SESSION_LAST_SONG, trackSong.getFile().getPath());
    }

    private void deserializeTrackSong(File file) {
        trackSong = getController().getSerializeService().fromFile(file, TrackSong.class);
    }

}