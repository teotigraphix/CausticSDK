
package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.teotigraphix.caustk.core.CaustkEngine;
import com.teotigraphix.caustk.core.CaustkProject;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public abstract class ApplicationStates extends ApplicationComponent implements IApplicationStates {

    @Inject
    private IApplicationModel applicationModel;

    @Override
    protected String getPreferenceId() {
        throw new IllegalStateException();
    }

    public ApplicationStates() {
    }

    @Override
    public void loadLastProjectState() throws IOException {
        CaustkProject project = null;

        String path = applicationModel.getPreferences().getString(
                ApplicationModel.LAST_PROJECT_PATH, null);
        File rootDirectory = RuntimeUtils.getApplicationDirectory();
        File projectsDirectory = new File(rootDirectory, "projects");
        File projectLocation = null;

        String projectName = getNextProjectName();

        File projectBaseDirectory = new File(projectsDirectory, projectName);

        if (!projectBaseDirectory.exists())
            projectBaseDirectory.mkdirs();

        if (path != null) {
            projectLocation = new File(path);
        } else {
            projectLocation = new File(projectBaseDirectory, projectName + ".prj");
        }
        // C:\Users\Teoti\Documents\Tones\projects\Untitled.prj
        if (!projectLocation.exists()) {
            project = createDefaultProject(projectName, projectLocation.getAbsolutePath());
        } else {
            project = loadProject(projectLocation);
        }

        getApplication().getLogger().log(">>>>> ApplicationStates", "loadLastProjectState()");
        CaustkEngine.DEBUG_MESSAGES = false;
        CaustkEngine.DEBUG_QUERIES = false;
        // XXX        model.importSoundKit(RuntimeUtils.getSongFile("TESTLIB1"));
        CaustkEngine.DEBUG_MESSAGES = true;
        CaustkEngine.DEBUG_QUERIES = true;

        applicationModel.setProject(project);

    }

    private String getNextProjectName() {
        return "Untitled";
    }

    @Override
    public void save(CaustkProject project) throws IOException {
        project.save();
    }

    private CaustkProject loadProject(File projectLocation) throws IOException {
        CaustkProject instance = readProject(projectLocation);
        return instance;
    }

    protected abstract CaustkProject readProject(File file) throws IOException;

    protected abstract CaustkProject createDefaultProject(String name, String location);

    @Override
    public void startUI() {
        getApplication().getLogger().log(">>>>> ApplicationStates", "startUI()");
        // XXX TEMP until I figure out where Project prefs are
        //        restartUI();
        //testMidi();
    }

    @Override
    public void restartUI() {
        getApplication().getLogger().log(">>>>> ApplicationStates", "restartUI()");
        try {
            applicationModel.setProject(applicationModel.getProject());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void construct() {
        getApplication().getEventBus().register(this);
        File tempDirectory = getApplicationTempDirectory();
        if (!tempDirectory.exists())
            tempDirectory.mkdirs();
    }

    private static File getApplicationTempDirectory() {
        String storagePath = Gdx.files.getExternalStoragePath();
        return new File(new File(storagePath), ".caustk");
    }

}
