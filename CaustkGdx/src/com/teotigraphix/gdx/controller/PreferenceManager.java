
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

    @Override
    protected String getPreferenceId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void construct() {
        // TODO Auto-generated method stub

    }
}
