
package com.teotigraphix.gdx.app;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.KryoException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApplicationController extends ApplicationComponent implements IApplicationController {

    private static final String TAG = "ApplicationController";

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

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    getApplication().startScene();
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
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KryoException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        getApplication().getLogger().log(TAG, "dispose()");
        applicationModel.dispose();
    }
}
