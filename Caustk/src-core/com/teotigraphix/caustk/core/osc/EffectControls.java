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

package com.teotigraphix.caustk.core.osc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.IEffectControl;
import com.teotigraphix.caustk.node.effect.EffectType;
import com.teotigraphix.caustk.utils.ExceptionUtils;

/**
 * The global enumeration for the {@link EffectType}s.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public enum EffectControls implements IEffectControl {

    Global_Bypass("bypass", false),

    //----------------------------------
    // Autowah
    //----------------------------------

    /**
     * Values <code>0.5..4.0</code>; default <code>2.23</code>
     */
    Autowah_Cutoff("cutoff", 0.5f, 4f, 2.23f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Autowah_Depth("depth", 0.0f, 1f, 1f),

    /**
     * Values <code>0.0..0.1</code>; default <code>0.5</code>
     */
    Autowah_Resonance("resonance", 0f, 1f, 0.5f),

    /**
     * Values <code>0.0.5.0</code>; default <code>0.4</code>
     */
    Autowah_Speed("speed", 0f, 0.5f, 0.4f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Autowah_Wet("wet", 0f, 1f, 1f);

    //----------------------------------
    // Bitcrusher
    //----------------------------------

    private static Map<EffectType, Collection<IEffectControl>> map = new HashMap<EffectType, Collection<IEffectControl>>();

    static {
        // Autowah
        add(EffectType.Autowah, Global_Bypass);
        add(EffectType.Autowah, Autowah_Cutoff);
        add(EffectType.Autowah, Autowah_Depth);
        add(EffectType.Autowah, Autowah_Resonance);
        add(EffectType.Autowah, Autowah_Speed);
        add(EffectType.Autowah, Autowah_Wet);
    }

    private static void add(EffectType effectType, IEffectControl control) {
        Collection<IEffectControl> collection = map.get(effectType);
        if (collection == null) {
            collection = new ArrayList<IEffectControl>();
            map.put(effectType, collection);
        }
        collection.add(control);
    }

    public static Collection<IEffectControl> get(EffectType effectType) {
        return map.get(effectType);
    }

    private String control;

    private float min;

    private float max;

    private Float defaultValue = null;

    private Boolean defaultBooleanValue = null;

    @Override
    public String getControl() {
        return control;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public boolean isBoolean() {
        return defaultBooleanValue != null;
    }

    public boolean isFloat() {
        return defaultValue != null;
    }

    public float getDefaultValue() {
        if (defaultBooleanValue != null)
            return defaultBooleanValue ? 1f : 0f;
        return defaultValue;
    }

    private EffectControls(String control, boolean defaultValue) {
        defaultBooleanValue = defaultValue;
    }

    private EffectControls(String control, float min, float max, float defaultValue) {
        this.control = control;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
    }

    public String toRangeString() {
        return min + ".." + max;
    }

    public boolean set(float value, float oldValue) throws RuntimeException {
        if (value == oldValue)
            return false;
        if (value < min || value > max)
            throw newRangeException(this, toRangeString(), value);
        return true;
    }

    protected final RuntimeException newRangeException(IEffectControl control, String range,
            Object value) {
        return ExceptionUtils.newRangeException(control.getControl(), range, value);
    }

}
