////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.internal.actvity;

import java.io.IOException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.activity.IApplicationRuntime;
import com.teotigraphix.caustic.service.IFileService;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.common.utils.RuntimeUtils;

public class DefaultApplicationRuntime implements IApplicationRuntime {
    private static final String PROPERTY_INSTALLED = "installed";

    private static final String CONFIG_APP_VERSION = "app.version";

    private static final String PREF_VERSION = "version";

    @Inject
    protected IWorkspace workspace;

    public DefaultApplicationRuntime() {
    }

    @Override
    public boolean install() throws IOException {
        Editor edit = workspace.getPreferences().edit();
        // use the preferences to figure out if the app has been installed
        boolean installed = workspace.getPreferences().getBoolean(PROPERTY_INSTALLED, false);

        if (!installed) {
            // copy all first run assets, an application will give a 
            // custom implementation for this method.
            copyFirstRun();
            edit.putBoolean(PROPERTY_INSTALLED, true);
        } else {
            update();
        }

        edit.commit();
        return true;
    }

    boolean update() {
        // check if the private data preferences has been created
        SharedPreferences settings = workspace.getPreferences();

        Editor edit = settings.edit();
        // startup.clearPreferences();

        float lastVersion = settings.getFloat(PREF_VERSION, 0f);
        float version = Float.valueOf((String)workspace.getProperties().get(CONFIG_APP_VERSION));
        if (lastVersion < version) {
            // updating
            update(lastVersion, version);
        } else if (lastVersion == version) {
            // no change, same version
            current(lastVersion, version);
        } else {
            // version installed is newer that version running
            inconsistant(lastVersion, version);
        }
        edit.putFloat(PREF_VERSION, version);

        edit.commit();

        return true;
    }

    protected void update(float oldVersion, float version) {
        Log.d("VERSION", "update old:v" + oldVersion + " current:v" + version);
    }

    protected void current(float oldVersion, float version) {
        Log.d("VERSION", "current old:v" + oldVersion + " current:v" + version);
    }

    protected void inconsistant(float oldVersion, float version) {
        Log.d("VERSION", "inconsistant old:v" + oldVersion + " current:v" + version);
    }

    /**
     * Use {@link RuntimeUtils} to copy assets from assets or root to the
     * {@link IFileService#getApplicationDirectory()} for use in the
     * application.
     * 
     * @throws IOException
     */
    protected void copyFirstRun() throws IOException {
    }

    @Override
    public void boot() throws IOException {
    }

    @Override
    public void run() {
    }

}
