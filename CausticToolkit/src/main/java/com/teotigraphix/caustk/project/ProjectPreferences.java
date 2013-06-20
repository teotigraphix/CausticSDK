
package com.teotigraphix.caustk.project;

import java.util.HashMap;
import java.util.Map;

public class ProjectPreferences {
    
    public static final String LAST_PROJECT = "lastProject";
    
    transient private boolean firstRun;
    
    public boolean isFirstRun() {
        return firstRun;
    }

    private Map<String, Object> map = new HashMap<String, Object>();

    public ProjectPreferences() {
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public String getString(String key) {
        if (!map.containsKey(key))
            return null;
        return String.valueOf(map.get(key));
    }

    public Integer getInterger(String key) {
        if (!map.containsKey(key))
            return null;
        return Integer.parseInt((String)map.get(key));
    }

    public Float getFloat(String key) {
        if (!map.containsKey(key))
            return null;
        return Float.parseFloat((String)map.get(key));
    }

}
