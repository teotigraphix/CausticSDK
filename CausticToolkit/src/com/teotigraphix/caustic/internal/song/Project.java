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

package com.teotigraphix.caustic.internal.song;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import roboguice.event.EventManager;

import com.google.inject.Inject;
import com.teotigraphix.caustic.core.CausticError;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.ISong;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.common.IMemento;
import com.teotigraphix.common.internal.XMLMemento;
import com.teotigraphix.common.utils.RuntimeUtils;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Project implements IProject {

    private static final String TAG_PROJECT = "project";

    private static final String XML_EXTENSION = ".xml";

    public static final String VERSION = "1.0";

    private boolean restored = false;

    private boolean valid = true;

    @Override
    public boolean isRestored() {
        return restored;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    private IMemento mMemento;

    @Override
    public IMemento getState() {
        return mMemento;
    }

    protected EventManager eventManager;

    @Override
    public IWorkspace getWorkspace() {
        return mWorkspace;
    }

    void setWorkspace(IWorkspace value) {
        mWorkspace = value;
    }

    @Inject
    void setEventManager(EventManager value) {
        eventManager = value;
    }

    //--------------------------------------------------------------------------
    //
    // IProject API :: Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // name
    //----------------------------------

    private String mName = "Untitled Project";

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String value) {
        if (value == null)
            throw new CausticError("IProject name must be not null");
        mName = value;
    }

    @Override
    public String getFileName() {
        if (mFile == null)
            return null;
        return mFile.getName().replace(XML_EXTENSION, "");
    }

    //----------------------------------
    // projectFile
    //----------------------------------

    private File mFile;

    @Override
    public File getFile() {
        return mFile;
    }

    void setFile(File file) {
        mFile = file;
    }

    //----------------------------------
    // songs
    //----------------------------------

    private final List<ISong> mSongs;

    @Override
    public List<ISong> getSongs() {
        return mSongs;
    }

    //----------------------------------
    // currentSong
    //----------------------------------

    private int mCurrentSong = -1;

    @Override
    public int getCurrentSong() {
        return mCurrentSong;
    }

    @Override
    public void setCurrentSong(int index) {
        if (index == mCurrentSong)
            return;

        ISong oldSong = getSongAt(mCurrentSong);
        mCurrentSong = index;
        ISong newSong = getSelectedSong();
        fireSongChanged(newSong, oldSong);
        eventManager.fire(new OnProjectSongChangeEvent(this, ProjectSongChangKind.CHANGE, newSong,
                oldSong));
    }

    //----------------------------------
    // selectedSong
    //----------------------------------

    @Override
    public ISong getSelectedSong() {
        return getSongAt(mCurrentSong);
    }

    //----------------------------------
    // data
    //----------------------------------

    private ProjectData mData;

    private IWorkspace mWorkspace;

    @Override
    public ProjectData getData() {
        return mData;
    }

    @Override
    public void setData(ProjectData value) {
        mData = value;
    }

    //--------------------------------------------------------------------------
    //
    // Constructor
    //
    //--------------------------------------------------------------------------

    Project(IWorkspace workspace, File file) {
        mSongs = new ArrayList<ISong>();
        setWorkspace(workspace);
        setFile(file);
        // this MUST be loaded from the last state or it is "Untitled Project"
        setName("Untitled Project");
        setData(new ProjectData());
    }

    @Override
    public void copy(IMemento memento) {
        memento.putString("version", VERSION);
        memento.putString("name", getName());
        // // SONG MODE
        // memento.putInteger("currentSong", getCurrentSong());
        // // PATTERN MODE
        // memento.putInteger("currentPattern", getCurrentPattern());
        //
        // memento.putString("patchBank", mPatchBank);
        // memento.putString("patternBank", mPatternBank);
        // memento.putString("songBank", mSongBank);

        // what does the Project save?
        // - ProjectData
        // - name
        if (mData != null) {
            mData.copy(memento.createChild("data"));
        }

        mWorkspace.getRack().copy(memento.createChild("rack"));

        saveSongs(memento.createChild("songs"));
    }

    private void saveSongs(IMemento memento) {
        for (ISong song : mSongs) {
            song.copy(memento.createChild("song"));
        }
    }

    @Override
    public void paste(IMemento memento) {
        // String version = memento.getString("version");
        // if (version.equals("1.0")) {
        loadState_1_0(memento);
        // }
    }

    @Override
    public void restore() {
        if (restored)
            return;

        if (!getFile().exists())
            return;

        IMemento state;
        try {
            state = RuntimeUtils.loadMemento(getFile());
            paste(state);
        } catch (IOException e) {
            throw new CausticError("Error loading project state", e);
        }

        restored = true;
    }

    //--------------------------------------------------------------------------
    //
    // IProject API :: Methods
    //
    //--------------------------------------------------------------------------

    @Override
    public void addSong(ISong song) {
        mSongs.add(song);
        song.setProject(this);
        fireSongAdded(song);
        // only set the current song if there is no songs IE -1
        if (mCurrentSong == -1)
            setCurrentSong(0);
    }

    @Override
    public void removeSong(ISong song) {
        if (!mSongs.contains(song))
            return;
        mSongs.remove(song);
        song.setProject(null);
        fireSongRemoved(song);
    }

    @Override
    public ISong getSongAt(int index) {
        if (index < 0 || index > mSongs.size() - 1)
            return null;
        return mSongs.get(index);
    }

    @Override
    public void saveQuick(File quickSave) throws CausticException {
        File original = getFile();
        setFile(quickSave);
        IMemento memento = XMLMemento.createWriteRoot(TAG_PROJECT);
        copy(memento);
        try {
            RuntimeUtils.saveMemento(mFile, memento);
        } catch (IOException e) {
            throw new CausticException(e);
        }
        setFile(original);
    }

    @Override
    public void saveAs(File absoluteFile) throws CausticException {
        setFile(absoluteFile);
        save();
    }

    /**
     * Saves a project file.
     * <p>
     * The caller is responsible for checking whether file exists.
     * </p>
     * 
     * @param file The full path to the file to save.
     * @throws CausticException
     */
    @Override
    public void save() throws CausticException {
        getData().setIsSavedProject();
        IMemento memento = XMLMemento.createWriteRoot(TAG_PROJECT);
        copy(memento);
        try {
            RuntimeUtils.saveMemento(mFile, memento);
        } catch (IOException e) {
            throw new CausticException(e);
        }
    }

    @Override
    public void startup() {
    }

    public void close() {
        valid = false;
    }

    //----------------------------------
    // Project listeners
    //----------------------------------

    private final List<ProjectListener> mProjectListeners = new ArrayList<ProjectListener>();

    @Override
    public final void addProjectListener(ProjectListener value) {
        if (mProjectListeners.contains(value))
            return;
        mProjectListeners.add(value);
    }

    @Override
    public final void removeProjectListener(ProjectListener value) {
        if (!mProjectListeners.contains(value))
            return;
        mProjectListeners.remove(value);
    }

    void fireSongAdded(ISong song) {
        for (ProjectListener listener : mProjectListeners) {
            listener.onSongAdded(song);
        }
    }

    void fireSongChanged(ISong song, ISong oldSong) {
        for (ProjectListener listener : mProjectListeners) {
            listener.onSongChanged(song, oldSong);
        }
    }

    void fireSongRemoved(ISong song) {
        for (ProjectListener listener : mProjectListeners) {
            listener.onSongRemoved(song);
        }
    }

    protected void loadState_1_0(IMemento memento) {
        mMemento = memento;

        // only load project specific stuff, since clients need to instrument
        // how machines and the other panels get resotred.

        setName(memento.getString("name"));
        // setCurrentSong(memento.getInteger("currentSong"));
        // setCurrentPattern(memento.getInteger("currentPattern"));
        // setPatchBank(memento.getString("patchBank"));
        // setPatternBank(memento.getString("patternBank"));
        // setSongBank(memento.getString("songBank"));

        if (mData != null) {
            mData.paste(memento);
        }

        //IMemento child = memento.getChild("rack");
        //        // 08-2012 - loading whole rack into project form here
        //        if (child != null) {
        //            mWorkspace.getRack().paste(child);
        //        } else {
        //            System.err.println("Rack memento is null");
        //        }

    }

    public static class StartupInfo {

        private final Properties mProperties;

        public StartupInfo(Properties properties) {
            mProperties = properties;
        }

        public float getVersion() {
            return Float.valueOf(mProperties.getProperty("app.version"));
        }
    }

}
