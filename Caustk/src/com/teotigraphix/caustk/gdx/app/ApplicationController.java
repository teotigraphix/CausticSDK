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

package com.teotigraphix.caustk.gdx.app;

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
    }

    @Inject
    private IApplicationStateHandlers applicationStates;

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    public ApplicationController() {
    }

    //--------------------------------------------------------------------------
    // IApplicationController API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void setup() {
        log(TAG, "setup() Not Implemented");
    }

    @Override
    public void startup() {
        log(TAG, "startup( BEG )");
        try {
            //setup();
            applicationStates.startup();
            if (!Application.TEST) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        log(TAG, "    ------------------------------------");
                        log(TAG, "    $$$$ NEXT FRAME");

                        getApplication().startScene();
                        // this gets called one frame later so behaviors have registered
                        // onAwake(), onStart()
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                log(TAG, "    ------------------------------------");
                                log(TAG, "    %%%% NEXT FRAME");

                                applicationStates.startUI();
                            }
                        });
                    }
                });
            }
        } catch (KryoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log(TAG, "startup( END )");
    }

    @Override
    public void dispose() {
        getApplication().getLogger().log(TAG, "dispose()");
        applicationModel.dispose();
    }
}
