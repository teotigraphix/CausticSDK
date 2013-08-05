
package com.teotigraphix.caustic.application;

import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.google.inject.Singleton;

/**
 * User level preferences, all apps have access to these keys.
 */
@Singleton
public class PreferenceManager implements IPreferenceManager {

    private Preferences preferences;

    protected void setupUserNode(String path) {
        preferences = Preferences.userRoot().node(path);
    }

    public PreferenceManager() {
        // User Preference Node: /com.teotigraphix.caustic.application.PreferenceManager
        if (getClass().getName().equals("com.teotigraphix.caustic.application.PreferenceManager"))
            setupUserNode(getClass().getName());
    }

    @Override
    public Editor edit() {
        return new PreferenceEditor(preferences);
    }

    @Override
    public boolean contains(String key) {
        // XXX How to do this?
        return true;
    }

    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    @Override
    public String getString(String key, String defValue) {
        return preferences.get(key, defValue);
    }

    public static class PreferenceEditor implements Editor {

        private Preferences proxy;

        public PreferenceEditor(Preferences prefs) {
            proxy = prefs;
        }

        @Override
        public void apply() {
            try {
                proxy.flush();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Editor clear() {
            try {
                proxy.clear();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
            return this;
        }

        @Override
        public boolean commit() {
            try {
                proxy.flush();
            } catch (BackingStoreException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            proxy.putBoolean(key, value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            proxy.putFloat(key, value);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            proxy.putInt(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            proxy.putLong(key, value);
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            proxy.put(key, value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            proxy.remove(key);
            return this;
        }
    }
}
