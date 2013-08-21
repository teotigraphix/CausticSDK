
package com.teotigraphix.android.app;

import java.util.Map;

import javax.inject.Inject;

import android.content.Context;
import android.content.SharedPreferences;

import com.teotigraphix.caustic.application.IPreferenceManager;

public class PreferenceManager implements IPreferenceManager {

  
    Context context;

    SharedPreferences preferences;
    
    @Inject
    public PreferenceManager(Context context) {
        this.context = context;
        init();
    }

    void init() {
        preferences = context.getSharedPreferences("Application", Context.MODE_PRIVATE);
    }

    @Override
    public Editor edit() {
        return new AndroidEditor(preferences.edit());
    }

    @Override
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    @Override
    public Map<String, ?> getAll() {
        return preferences.getAll();
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
        return preferences.getString(key, defValue);
    }

    public static class AndroidEditor implements Editor {

        private android.content.SharedPreferences.Editor preferences;

        public AndroidEditor(android.content.SharedPreferences.Editor edit) {
            this.preferences = edit;
        }

        @Override
        public void apply() {
            //preferences.apply();
        }

        @Override
        public Editor clear() {
            preferences.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return preferences.commit();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            preferences.putBoolean(key, value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            preferences.putFloat(key, value);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            preferences.putInt(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            preferences.putLong(key, value);
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            preferences.putString(key, value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            preferences.remove(key);
            return this;
        }
    }
}
