
package com.teotigraphix.gdx.app;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.teotigraphix.caustk.core.CaustkEngine;

public class ApplicationStates extends ApplicationComponent implements IApplicationStates {

    @Inject
    private IApplicationModel applicationModel;

    @Override
    protected String getPreferenceId() {
        throw new IllegalStateException();
    }

    public ApplicationStates() {

    }

    @Override
    public void loadLastProjectState() {

        //editorTest.testModular();

        getApplication().getLogger().log(">>>>> ApplicationStates", "loadLastProjectState()");
        CaustkEngine.DEBUG_MESSAGES = false;
        CaustkEngine.DEBUG_QUERIES = false;
        // XXX        model.importSoundKit(RuntimeUtils.getSongFile("TESTLIB1"));
        CaustkEngine.DEBUG_MESSAGES = true;
        CaustkEngine.DEBUG_QUERIES = true;
    }

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
        applicationModel.setProject(applicationModel.getProject());
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
