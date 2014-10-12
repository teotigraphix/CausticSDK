
package com.teotigraphix.gdx.app;

import java.util.HashMap;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class ProjectProperties {

    @Tag(0)
    private HashMap<String, String> properties = new HashMap<String, String>();

    public ProjectProperties() {
    }

    public ProjectProperties putBoolean(String key, boolean val) {
        properties.put(key, Boolean.toString(val));
        return this;
    }

    public ProjectProperties putInteger(String key, int val) {
        properties.put(key, Integer.toString(val));
        return this;
    }

    public ProjectProperties putLong(String key, long val) {
        properties.put(key, Long.toString(val));
        return this;
    }

    public ProjectProperties putFloat(String key, float val) {
        properties.put(key, Float.toString(val));
        return this;
    }

    public ProjectProperties putString(String key, String val) {
        properties.put(key, val);
        return this;
    }

    //    public ProjectProperties put(Map<String, ?> vals) {
    //        for (Entry<String, ?> val : vals.entrySet()) {
    //            if (val.getValue() instanceof Boolean)
    //                putBoolean(val.getKey(), (Boolean)val.getValue());
    //            if (val.getValue() instanceof Integer)
    //                putInteger(val.getKey(), (Integer)val.getValue());
    //            if (val.getValue() instanceof Long)
    //                putLong(val.getKey(), (Long)val.getValue());
    //            if (val.getValue() instanceof String)
    //                putString(val.getKey(), (String)val.getValue());
    //            if (val.getValue() instanceof Float)
    //                putFloat(val.getKey(), (Float)val.getValue());
    //        }
    //        return this;
    //    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public int getInteger(String key) {
        return getInteger(key, 0);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public boolean getBoolean(String key, boolean defValue) {
        return Boolean.parseBoolean(getProperty(key, Boolean.toString(defValue)));
    }

    public int getInteger(String key, int defValue) {
        return Integer.parseInt(getProperty(key, Integer.toString(defValue)));
    }

    public long getLong(String key, long defValue) {
        return Long.parseLong(getProperty(key, Long.toString(defValue)));
    }

    public float getFloat(String key, float defValue) {
        return Float.parseFloat(getProperty(key, Float.toString(defValue)));
    }

    public String getString(String key, String defValue) {
        return getProperty(key, defValue);
    }

    //    public Map<String, ?> get() {
    //        Map<String, Object> map = new HashMap<String, Object>();
    //        for (Entry<String, String> val : properties.entrySet()) {
    //            if (val.getValue() instanceof Boolean)
    //                map.put(val.getKey(), Boolean.parseBoolean(val.getValue()));
    //            if (val.getValue() instanceof Integer)
    //                map.put(val.getKey(), Integer.parseInt(val.getValue()));
    //            if (val.getValue() instanceof Long)
    //                map.put(val.getKey(), Long.parseLong(val.getValue()));
    //            if (val.getValue() instanceof String)
    //                map.put(val.getKey(), val.getValue());
    //            if (val.getValue() instanceof Float)
    //                map.put(val.getKey(), Float.parseFloat(val.getValue()));
    //        }
    //
    //        return map;
    //    }

    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    public void clear() {
        properties.clear();
    }

    public void remove(String key) {
        properties.remove(key);
    }

    String getProperty(String key) {
        Object oval = properties.get(key);
        String sval = (oval instanceof String) ? (String)oval : null;
        return sval;

    }

    String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;

    }
}
