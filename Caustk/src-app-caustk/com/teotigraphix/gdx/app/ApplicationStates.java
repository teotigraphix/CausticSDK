
package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.teotigraphix.caustk.core.CaustkProject;

public abstract class ApplicationStates extends ApplicationComponent implements IApplicationStates {

    @Inject
    private IApplicationModel applicationModel;

    @Inject
    private IFileManager fileManager;

    @Override
    protected String getPreferenceId() {
        throw new IllegalStateException();
    }

    public ApplicationStates() {
    }

    @Override
    public void loadLastProjectState() throws IOException {
        getApplication().getLogger().log("ApplicationStates", "loadLastProjectState()");
        getApplication().getLogger().log("ApplicationStates", "");

        CaustkProject project = null;

        getApplication().getLogger().log("ApplicationStates",
                "Find last project path from preferences.");

        project = fileManager.createOrLoadStartupProject();

        applicationModel.setProject(project);

    }

    @Override
    public abstract CaustkProject readProject(File projectFile) throws IOException;

    @Override
    public abstract CaustkProject createDefaultProject(String name, File projectFile);

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
