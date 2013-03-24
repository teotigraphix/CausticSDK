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

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.activity.IApplicationConfiguration;
import com.teotigraphix.caustic.activity.IApplicationPreferences;
import com.teotigraphix.caustic.activity.ICausticBackend;
import com.teotigraphix.caustic.core.CausticError;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.part.ISoundGenerator;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRack.OnSongStateChangeListener;
import com.teotigraphix.caustic.rack.IRack.SongStateChangeKind;
import com.teotigraphix.caustic.rack.IRackSong;
import com.teotigraphix.caustic.service.IFileService;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;

/**
 * The default implementation of the {@link IWorkspace} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
@Singleton
public class Workspace implements IWorkspace {

    private static final String STARTUP_PREFS = "startup_preferences";

    private ICausticBackend mBackend;

    //--------------------------------------------------------------------------
    // Application Level Model
    //--------------------------------------------------------------------------

    //----------------------------------
    // applicationConfiguration
    //----------------------------------

    private IApplicationConfiguration mApplicationConfiguration;

    @Override
    public IApplicationConfiguration getApplicationConfiguration() {
        return mApplicationConfiguration;
    }

    //----------------------------------
    // applicationPreferences
    //----------------------------------

    private IApplicationPreferences mApplicationPreferences;

    @Override
    public IApplicationPreferences getApplicationPreferences() {
        return mApplicationPreferences;
    }

    //--------------------------------------------------------------------------
    // IWorkspace API
    //--------------------------------------------------------------------------

    //----------------------------------
    // isRunning
    //----------------------------------

    private boolean mRunning = false;

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    //----------------------------------
    // application
    //----------------------------------

    private Application mApplication;

    @Override
    public final Application getApplication() {
        return mApplication;
    }

    //----------------------------------
    // applicationName
    //----------------------------------

    @Override
    public String getApplicationName() {
        return mApplicationConfiguration.getApplicationName();
    }

    //----------------------------------
    // applicationRoot
    //----------------------------------

    private File mApplicationRoot;

    @Override
    public File getApplicationRoot() {
        return mApplicationRoot;
    }

    void setApplicationRoot(File file) {
        mApplicationRoot = file;
    }

    //----------------------------------
    // fileService
    //----------------------------------

    IFileService mFileService;

    @Override
    public IFileService getFileService() {
        return mFileService;
    }

    //----------------------------------
    // sharedPreferences
    //----------------------------------

    @Override
    public SharedPreferences getSharedPreferences() {
        return mApplication.getSharedPreferences(STARTUP_PREFS, Application.MODE_PRIVATE);
    }

    //----------------------------------
    //  rack
    //----------------------------------

    private IRack mRack;

    @Override
    public IRack getRack() {
        return mRack;
    }

    void setRack(IRack value) {
        mRack = value;
        // the IWorkspace events decouple clients from the core components
        // such as the IRack
        mRack.setOnSongStateChangeListener(listener);
    }

    private OnSongStateChangeListener listener = new OnSongStateChangeListener() {
        @Override
        public void onSongStateChanged(IRackSong song, SongStateChangeKind kind) {
            //eventManager.fire(new OnRackStateChangedEvent(song, kind));
        }
    };

    //----------------------------------
    //  project
    //----------------------------------

    private Project mProject;

    @Override
    public IProject getProject() {
        return mProject;
    }

    private void setProject(IProject value) {
        //IProject oldProject = mProject;
        mProject = (Project)value;
        // XXX this is temp until I create an injector service
        // RoboGuice.injectMembers(mApplication, mProject);
        //eventManager.fire(new OnWorkspaceProjectChangeEvent(this, mProject, oldProject));
    }

    //----------------------------------
    // soundGenerator
    //----------------------------------

    private ISoundGenerator mGenerator;

    @Override
    public ISoundGenerator getGenerator() {
        return mGenerator;
    }

    void setGenerator(ISoundGenerator value) {
        mGenerator = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    @Inject
    public Workspace(Application application, IApplicationConfiguration applicationConfiguration,
            IApplicationPreferences applicationPreferences) {
        Log.d("Workspace", "Workspace()");
        mApplication = application;
        mApplicationConfiguration = applicationConfiguration;
        mApplicationPreferences = applicationPreferences;
    }

    //--------------------------------------------------------------------------
    // IWorkspace API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void startAndRun() throws CausticException {
        Log.d("Workspace", "startAndRun()");

        mFileService = mApplicationConfiguration.createFileService(mApplication);
        mBackend = mApplicationConfiguration.createBackend();

        setupApplicationDirectory(mApplicationConfiguration.getApplicationName());

        //setRack(mBackend.createRack(this));
        setGenerator(mBackend.createSoundGenerator(this));

        mRunning = true;
    }

    @Override
    public void stopAndShutdown() throws CausticException {
        Log.d("Workspace", "stopAndShutdown()");
        // Clear out ALL state created in startAndRun()
        mBackend = null;
        mRunning = false;
        mGenerator = null;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public IProject loadProject(File projectFile) {
        if (mProject != null) {
            closeProject(mProject);
        }

        Project project = null;

        if (!projectFile.exists()) {
            project = new Project(this, projectFile);
            project.setData(mBackend.createProjectData());
            // tell clients that this project is brand new
            project.getData().setIsNewProject();
            // The project is only in memory right now until an explicit save()
            // is called on it from the workspace.
        } else {

            // load the existing project state
            project = new Project(this, projectFile);
            project.setData(mBackend.createProjectData());
            project.getData().setIsSavedProject();
        }

        setProject(project);

        return project;
    }

    @Override
    public void restoreProjectState() {
        try {
            // only will load a project if the XML file exists in the filesystem
            // the project DOES NOT call restore on the IRack, just loads it's
            // own properties
            mProject.restore();
        } catch (CausticError e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProject(IProject project) throws CausticException {
        boolean success = mProject.getFile().delete();
        if (!success)
            throw new CausticException("Project was not deleted from disk");
        //eventManager.fire(new OnProjectDeleteEvent(project));
    }

    @Override
    public void save() throws CausticException {
        //eventManager.fire(new OnWorkspaceQuickSaveEvent(this));
    }

    private void closeProject(IProject project) {
        ((Project)project).close();
        //eventManager.fire(new OnProjectCloseEvent(project));
    }

    public void clearPreferences() {
        getSharedPreferences().edit().clear().commit();
    }

    private void setupApplicationDirectory(String applicationName) {
        // setup the applicationRoot
        File applicationDirectory = mFileService.getApplicationDirectory();
        if (!applicationDirectory.exists()) {
            throw new CausticError("Application directory '"
                    + applicationDirectory.getAbsolutePath() + "' failed to create");
        }

        setApplicationRoot(applicationDirectory);
    }

}
