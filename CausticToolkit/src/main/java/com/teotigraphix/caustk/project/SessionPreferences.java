
package com.teotigraphix.caustk.project;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link SessionPreferences} contain all session data that is preserved
 * through each startup.
 * <p>
 * There is only one instance of this for all project, so this is more like
 * global cookies.
 */
public class SessionPreferences {

    private Map<String, Object> map = new HashMap<String, Object>();

    public SessionPreferences() {
    }

    /**
     * Adds a key/value pair to the session map.
     * <p>
     * The value must be of primitive type.
     * 
     * @param key The String key.
     * @param value The primitive value.
     */
    public void put(String key, Object value) {
        map.put(key, value);
    }

    /**
     * Returns a String for the key, <code>null</code> if the key does not
     * exist.
     * 
     * @param key The String key.
     */
    public String getString(String key) {
        if (!map.containsKey(key))
            return null;
        return String.valueOf(map.get(key));
    }

    /**
     * Returns a Integer for the key, <code>null</code> if the key does not
     * exist.
     * 
     * @param key The String key.
     */
    public Integer getInterger(String key) {
        if (!map.containsKey(key))
            return null;
        return Integer.parseInt((String)map.get(key));
    }

    /**
     * Returns a Float for the key, <code>null</code> if the key does not exist.
     * 
     * @param key The String key.
     */
    public Float getFloat(String key) {
        if (!map.containsKey(key))
            return null;
        return Float.parseFloat((String)map.get(key));
    }

}
