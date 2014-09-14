////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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
    private IApplicationStateHandlers applicationStates;

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    public ApplicationController() {
        System.out.println("ApplicationController");
    }

    //--------------------------------------------------------------------------
    // Public IApplicationController :: API
    //--------------------------------------------------------------------------

    @Override
    public void setup() {
        getApplication().getLogger().log(TAG, "setup()");
    }

    @Override
    public void startup() {
        getApplication().getLogger().log(TAG, "startup()");

        try {
            applicationStates.startup();

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
