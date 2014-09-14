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

package com.teotigraphix.gdx.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.app.ApplicationComponent;

@Singleton
public class PreferenceManager extends ApplicationComponent implements IPreferenceManager {

    private Map<String, Preferences> preferences = new HashMap<String, Preferences>();

    public PreferenceManager() {
    }

    @Override
    public final Preferences get(String name) {
        Preferences instance = preferences.get(name);
        if (instance == null) {
            instance = Gdx.app.getPreferences(name);
            preferences.put(name, instance);
        }
        return instance;
    }

    @Override
    public final void save() {
        for (Preferences preference : preferences.values()) {
            preference.flush();
        }
    }
}
