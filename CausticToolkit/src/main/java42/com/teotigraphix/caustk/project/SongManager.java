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

package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;

import org.androidtransfuse.event.EventObserver;
import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.project.IProjectManager.OnProjectManagerChange;
import com.teotigraphix.caustk.project.IProjectManager.ProjectManagerChangeKind;

public class SongManager implements ISongManager {

    private static final String PROJECT_SESSION_LAST_SONG = "lastSong";

    private final ICaustkController controller;

    private File songDirectory;

    @SuppressWarnings("unused")
    private File applicationRoot;

    //----------------------------------
    // trackSong
    //----------------------------------

    private TrackSong trackSong;

    @Override
    public TrackSong getTrackSong() {
        return trackSong;
    }

    public SongManager(ICaustkController controller, File applicationRoot) {
        this.controller = controller;
        this.applicationRoot = applicationRoot;

        songDirectory = new File(applicationRoot, "songs");

        controller.getDispatcher().register(OnProjectManagerChange.class,
                new EventObserver<OnProjectManagerChange>() {
                    @Override
                    public void trigger(OnProjectManagerChange object) {
                        if (object.getKind() == ProjectManagerChangeKind.CREATE) {
                            onProjectManagerCreateHandler();
                        } else if (object.getKind() == ProjectManagerChangeKind.LOAD) {
                            onProjectManagerLoadHandler();
                        } else if (object.getKind() == ProjectManagerChangeKind.SAVE) {
                            onProjectManagerSaveHandler();
                        } else if (object.getKind() == ProjectManagerChangeKind.EXIT) {
                            onProjectManagerExitHandler();
                        }
                    }
                });
    }

    protected void onProjectManagerExitHandler() {
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
        controller.getDispatcher().trigger(new OnSongManagerLoadComplete(trackSong));
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
            controller.getSerializeService().save(target, trackSong);
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

    protected void onProjectManagerCreateHandler() {
        trackSong = createTrackSong(createUntitledFile());
    }

    protected void onProjectManagerLoadHandler() {
        String path = controller.getProjectManager().getSessionPreferences()
                .getString(PROJECT_SESSION_LAST_SONG);
        if (path == null) {
            throw new RuntimeException("TrackSong path null");
        } else if (new File(path).exists()) {
            deserializeTrackSong(toSongFile(new File(path)));
        }
    }

    protected void onProjectManagerSaveHandler() {
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TrackSong createTrackSong(File file) {
        TrackSong song = new TrackSong();
        song.setFile(file);
        song.setController(controller);
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
        controller.getProjectManager().getSessionPreferences()
                .put(PROJECT_SESSION_LAST_SONG, trackSong.getFile().getPath());
    }

    private void deserializeTrackSong(File file) {
        trackSong = controller.getSerializeService().fromFile(file, TrackSong.class);
        trackSong.setController(controller);
    }

}
