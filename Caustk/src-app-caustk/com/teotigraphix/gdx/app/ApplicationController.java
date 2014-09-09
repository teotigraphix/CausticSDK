
package com.teotigraphix.gdx.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.IApplicationModel.ApplicationModelProjectCreateEvent;
import com.teotigraphix.gdx.app.IApplicationModel.ApplicationModelProjectLoadEvent;
import com.teotigraphix.gdx.controller.IPreferenceManager;

@Singleton
public class ApplicationController extends ApplicationComponent implements IApplicationController {

    private static final String TAG = "ApplicationController";

    @Inject
    private ICaustkApplication application;

    @Inject
    private IPreferenceManager preferenceManager;

    private IApplicationModel applicationModel;

    @Inject
    public void setApplicationModel(IApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
        this.applicationModel.getEventBus().register(this);
    }

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
        try {
            applicationStates.loadLastProjectState();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onApplicationModelProjectCreateHandler(ApplicationModelProjectCreateEvent event) {
        getApplication().getLogger().log(TAG, "onApplicationModelProjectCreateHandler()");

        applicationStates.onProjectCreate(event.getProject());

        save();
    }

    @Subscribe
    public void onApplicationModelProjectLoadHandler(ApplicationModelProjectLoadEvent event) {
        getApplication().getLogger().log(TAG, "onApplicationModelProjectLoadHandler()");

        applicationStates.onProjectLoad(event.getProject());

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
    public void save() {
        applicationStates.onProjectSave(applicationModel.getProject());

        try {
            applicationStates.save(applicationModel.getProject());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        preferenceManager.save();
    }

    @Override
    public void dispose() {
        getApplication().getLogger().log(TAG, "exit()");
        //exitStrategy.exit();
        save();
    }

    @Override
    public void shutdown() {
        getApplication().getLogger().log(TAG, "shutdown()");
        //exitStrategy.shutdown();
    }

}
