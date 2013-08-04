
package com.teotigraphix.caustic.application;

import java.util.Map;

public interface IPreferenceManager {

    Editor edit();

    boolean contains(String key);

    /**
     * Retrieve all values from the preferences.
     */
    Map<String, ?> getAll();

    boolean getBoolean(String key, boolean defValue);

    float getFloat(String key, float defValue);

    int getInt(String key, int defValue);

    long getLong(String key, long defValue);

    String getString(String key, String defValue);

    public interface Editor {

        void apply();

        Editor clear();

        boolean commit();

        Editor putBoolean(String key, boolean value);

        Editor putFloat(String key, float value);

        Editor putInt(String key, int value);

        Editor putLong(String key, long value);

        Editor putString(String key, String value);

        Editor remove(String key);

    }

}
