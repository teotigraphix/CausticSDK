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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import roboguice.RoboGuice;
import roboguice.event.EventManager;
import android.app.Activity;
import android.content.SharedPreferences;

import com.google.inject.Inject;
import com.teotigraphix.caustic.activity.IApplicationRuntime;
import com.teotigraphix.caustic.activity.ICausticBackend;
import com.teotigraphix.caustic.activity.ICausticConfiguration;
import com.teotigraphix.caustic.core.CausticError;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.service.FileService;
import com.teotigraphix.caustic.part.ISoundGenerator;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.rack.IRack.OnSongStateChangeListener;
import com.teotigraphix.caustic.rack.IRack.SongStateChangeKind;
import com.teotigraphix.caustic.rack.IRackSong;
import com.teotigraphix.caustic.service.IFileService;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.common.utils.RuntimeUtils;

/**
 * The default implementation of the {@link IWorkspace} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class Workspace implements IWorkspace {

    private static final String APP_NAME = "app.name";

    private static final String STARTUP_PREFS = "startup_preferences";

    private static final String CONFIG_PROPERTIES = "config.properties";

    private boolean mRunning = false;

    @SuppressWarnings("unused")
    private ICausticConfiguration mConfiguration;

    private ICausticBackend mBackend;

    private IApplicationRuntime mRuntime;

    private void setRuntime(IApplicationRuntime value) {
        RoboGuice.injectMembers(mActivity, value);
        mRuntime = value;
    }

    //----------------------------------
    // eventManager
    //----------------------------------

    @Inject
    EventManager eventManager;

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    //----------------------------------
    // isRunning
    //----------------------------------

    @Override
    public boolean isRunning() {
        return mRunning;
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
    // applicationName
    //----------------------------------

    @Override
    public String getApplicationName() {
        if (mApplicationRoot == null)
            return null;
        return mApplicationRoot.getName();
    }

    //----------------------------------
    //  activity
    //----------------------------------

    private Activity mActivity;

    @Override
    public final Activity getActivity() {
        return mActivity;
    }

    @Override
    public SharedPreferences getPreferences() {
        return mActivity.getSharedPreferences(STARTUP_PREFS, Activity.MODE_PRIVATE);
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
            eventManager.fire(new OnRackStateChangedEvent(song, kind));
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
        RoboGuice.injectMembers(mActivity, mProject);
        //eventManager.fire(new OnWorkspaceProjectChangeEvent(this, mProject, oldProject));
    }

    //----------------------------------
    // properties
    //----------------------------------

    private Properties mProperties;

    @Override
    public Properties getProperties() {
        return mProperties;
    }

    void setProperties(Properties value) {
        mProperties = value;
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

    //----------------------------------
    // fileService
    //----------------------------------

    private IFileService mFileService;

    @Override
    public IFileService getFileService() {
        return mFileService;
    }

    void setFileService(FileService value) {
        mFileService = value;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Workspace(Activity activity) {
        mActivity = activity;
    }

    public void configure(ICausticConfiguration configuration) {
        mConfiguration = configuration;

        mBackend = configuration.createBackend();

        setRack(mBackend.createRack(this));
        setGenerator(mBackend.createSoundGenerator(this));
        setRuntime(mBackend.createApplicationRuntime(this));
    }

    //--------------------------------------------------------------------------
    // IWorkspace API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void startAndRun() throws CausticException {
        try {
            install();
        } catch (CausticException e) {
            throw e;
        }

        try {
            boot();
        } catch (IOException e) {
            throw new CausticException("IOException in IWorkspace.boot()", e);
        }

        run();

        mRunning = true;
    }

    @Override
    public void stopAndShutdown() throws CausticException {
        // notifies clients, the client is responsible for calling save() before this method
        // if it wants to save the workspace and project state to disk
        eventManager.fire(new OnWorkspaceShutdownEvent(this));
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
        eventManager.fire(new OnWorkspaceQuickSaveEvent(this));
    }

    private void closeProject(IProject project) {
        ((Project)project).close();
        //eventManager.fire(new OnProjectCloseEvent(project));
    }

    protected void install() throws CausticException {
        // load the config.properties on every startup to check for changes
        try {
            mProperties = loadProperties();
        } catch (IOException e) {
            throw new CausticException("Properties failed to load.", e);
        }

        setupApplicationDirectory(getProperties().get(APP_NAME).toString());

        // will install on first run or call update in the installer
        // if the application has already been installed
        try {
            mRuntime.install();
        } catch (IOException e) {
            throw new CausticException("Exception when installing", e);
        }
    }

    protected void boot() throws IOException {
        mRuntime.boot();
    }

    protected void run() {
        mRuntime.run();
    }

    private Properties loadProperties() throws IOException {
        ///data/data/com.teotigraphix.caustic.test/files
        File target = mActivity.getFilesDir();
        // copy the properties file to the private directory
        RuntimeUtils.copyFileOrDir(mActivity, target.getAbsolutePath(), CONFIG_PROPERTIES);

        if (!new File(target, CONFIG_PROPERTIES).exists()) {
            throw new IOException("config.properties file was not copied");
        }

        //load the properties
        FileInputStream in = RuntimeUtils.loadPrivateFile(mActivity, CONFIG_PROPERTIES);
        Properties properties = new Properties();
        properties.load(in);
        in.close();

        return properties;
    }

    public void clearPreferences() {
        getPreferences().edit().clear().commit();
    }

    private void setupApplicationDirectory(String applicationName) {
        setFileService(new FileService(this, applicationName));

        // setup the applicationRoot
        File applicationDirectory = mFileService.getApplicationDirectory();
        if (!applicationDirectory.exists()) {
            throw new CausticError("Application directory '"
                    + applicationDirectory.getAbsolutePath() + "' failed to create");
        }

        setApplicationRoot(applicationDirectory);
    }

}
