
package com.teotigraphix.gdx.app;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.controller.IPreferenceManager;

@Singleton
public class ApplicationController extends ApplicationComponent implements IApplicationController {

    private static final String TAG = "ApplicationController";

    @Inject
    private ICaustkApplication application;

    @Inject
    private IPreferenceManager preferenceManager;

    @Inject
    private IApplicationStates applicationStates;

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    public ApplicationController() {
        System.out.println("ApplicationController");
    }

    @Override
    protected String getPreferenceId() {
        return null;
    }

    @Override
    protected void construct() {
    }

    @Override
    public void setup() {
        getApplication().getLogger().log(TAG, "setup()");
    }

    @Override
    public void startup() {
        getApplication().getLogger().log(TAG, "startup()");
        //startupStrategy.startup();
        applicationStates.loadLastProjectState();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                application.startScene();
                // this gets called one frame later so behaviors have registered
                // onAwake(), onStart()
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        applicationStates.startUI();
                    }
                });
            }
        });
    }

    @Override
    public void dispose() {
        getApplication().getLogger().log(TAG, "exit()");
        //exitStrategy.exit();
        preferenceManager.save();
    }

    @Override
    public void shutdown() {
        getApplication().getLogger().log(TAG, "shutdown()");
        //exitStrategy.shutdown();
    }

}
