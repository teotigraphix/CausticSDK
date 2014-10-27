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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ChorusMode;
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

    Global_Bypass("bypass", EffectControlKind.Boolean, false),

    //----------------------------------
    // Autowah
    //----------------------------------

    /**
     * Values <code>0.5..4.0</code>; default <code>2.23</code>
     */
    Autowah_Cutoff("cutoff", EffectControlKind.Float, 0.5f, 4f, 2.23f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Autowah_Depth("depth", EffectControlKind.Float, 0f, 1f, 1f),

    /**
     * Values <code>0.0..0.1</code>; default <code>0.5</code>
     */
    Autowah_Resonance("resonance", EffectControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0.0.5.0</code>; default <code>0.4</code>
     */
    Autowah_Speed("speed", EffectControlKind.Float, 0f, 0.5f, 0.4f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Autowah_Wet("wet", EffectControlKind.Float, 0f, 1f, 1f),

    //----------------------------------
    // Bitcrusher
    //----------------------------------

    /**
     * Values <code>1..16</code>; default <code>3</code>
     */
    Bitcrusher_Depth("depth", EffectControlKind.Int, 1, 16, 3),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.0</code>
     */
    Bitcrusher_Jitter("jitter", EffectControlKind.Float, 0f, 1f, 0f),

    /**
     * Values <code>0.01..0.5</code>; default <code>1.0</code>
     */
    Bitcrusher_Rate("rate", EffectControlKind.Float, 0.01f, 0.5f, 1f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Bitcrusher_Wet("wet", EffectControlKind.Float, 0f, 1f, 1f),

    //----------------------------------
    // CabinetSimulator
    //----------------------------------

    /**
     * Values <code>0.25..1</code>; default <code>1.0</code>
     */
    CabinetSimulator_Damping("damping", EffectControlKind.Float, 0.25f, 1f, 1f),

    /**
     * Values <code>0..1</code>; default <code>0</code>
     */
    CabinetSimulator_Height("height", EffectControlKind.Float, 0f, 1f, 0f),

    /**
     * Values <code>0..1</code>; default <code>0.5</code>
     */
    CabinetSimulator_Tone("tone", EffectControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0..1</code>; default <code>0.5</code>
     */
    CabinetSimulator_Wet("wet", EffectControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0..1</code>; default <code>0</code>
     */
    CabinetSimulator_Width("width", EffectControlKind.Float, 0f, 1f, 0f),

    //----------------------------------
    // Chorus
    //----------------------------------

    /**
     * Values <code>0.0..0.7</code>; default <code>0.0</code>
     */
    Chorus_Delay("delay", EffectControlKind.Float, 0f, 0.7f, 0f),

    /**
     * Values <code>0.1..0.95</code>; default <code>0.25</code>
     */
    Chorus_Depth("depth", EffectControlKind.Float, 0.1f, 0.95f, 0.25f),

    /**
     * Values <code>0,1,2,3,4,5,6,7</code>; default <code>0</code>
     * <p>
     * triangleFull, sineFull, triangleHalf, sineHalf, triangleFullOpposed,
     * sineFullOpposed, triangleHalfOpposed, sineHalfOpposed
     * 
     * @see ChorusMode
     */
    Chorus_Mode("mode", EffectControlKind.Enum_Int, 0, 7, 0),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.4</code>
     */
    Chorus_Rate("rate", EffectControlKind.Float, 0f, 1f, 0.4f),

    /**
     * Values <code>0.0..0.5</code>; default <code>0.5</code>
     */
    Chorus_Wet("wet", EffectControlKind.Float, 0f, 0.5f, 0.5f),

    //----------------------------------
    // CombFilter
    //----------------------------------

    // XXX TODO Test this to figure out what is actually happening

    /**
     * Values <code>0.1..0.95</code>; default <code>0.8</code>
     */
    CombFilter_Wet("depth", EffectControlKind.Float, 0.1f, 0.95f, 0.8f), // depth

    /**
     * Values <code>0.1..0.95</code>; default <code>0.475</code>
     */
    CombFilter_Reso("feedback", EffectControlKind.Float, 0.1f, 0.95f, 0.475f), // feedback

    /**
     * Values <code>2..50</code>; default <code>10</code>
     */
    CombFilter_Freq("rate", EffectControlKind.Int, 2, 50, 10), // rate

    //----------------------------------
    // Compressor
    //----------------------------------

    /**
     * Values <code>0.00001..0.2</code>; default <code>0.01</code>
     */
    Compressor_Attack("attack", EffectControlKind.Float, 0.00001f, 0.2f, 0.01f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Compressor_Ratio("ratio", EffectControlKind.Float, 0f, 1f, 1f),

    /**
     * Values <code>0.001..0.2</code>; default <code>0.05</code>
     */
    Compressor_Release("release", EffectControlKind.Float, 0.001f, 0.2f, 0.05f),

    /**
     * Values <code>0..13</code>; default <code>-1</code>
     */
    Compressor_Sidechain("sidechain", EffectControlKind.Int, 0, 13, -1),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.1</code>
     */
    Compressor_Threshold("threshold", EffectControlKind.Float, 0f, 1f, 0.1f),

    //----------------------------------
    // Delay
    //----------------------------------

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    Delay_Feedback("feedback", EffectControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0,1,2,3,4</code> Mono, MonoLR, MonoRL, DualMono, PingPong;
     * default <code>0</code>
     */
    Delay_Mode("mode", EffectControlKind.Enum_Int, 0, 4, 0),

    /**
     * Values <code>1..12</code>; default <code>8</code>
     */
    Delay_Time("time", EffectControlKind.Int, 1, 8, 8),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    Delay_Wet("wet", EffectControlKind.Float, 0f, 1f, 0.5f),

    //----------------------------------
    // Distortion
    //----------------------------------

    /**
     * Values <code>0.0..20.0</code>; default <code>16.3</code>
     */
    Distortion_Amount("amount", EffectControlKind.Float, 0f, 20f, 16.3f),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.1</code>
     */
    Distortion_PostGain("post_gain", EffectControlKind.Float, 0f, 1f, 0.1f),

    /**
     * Values <code>0.0..5.0</code>; default <code>4.05</code>
     */
    Distortion_PreGain("pre_gain", EffectControlKind.Float, 0f, 5f, 4.05f),

    /**
     * Values <code>0..3</code>; default <code>0</code>
     */
    Distortion_Program("program", EffectControlKind.Enum_Int, 0, 3, 0);

    private static Map<EffectType, Collection<IEffectControl>> map = new HashMap<EffectType, Collection<IEffectControl>>();

    static {
        // Autowah
        add(EffectType.Autowah, Global_Bypass);
        add(EffectType.Autowah, Autowah_Cutoff);
        add(EffectType.Autowah, Autowah_Depth);
        add(EffectType.Autowah, Autowah_Resonance);
        add(EffectType.Autowah, Autowah_Speed);
        add(EffectType.Autowah, Autowah_Wet);

        // Bitcrusher
        add(EffectType.Bitcrusher, Global_Bypass);
        add(EffectType.Bitcrusher, Bitcrusher_Depth);
        add(EffectType.Bitcrusher, Bitcrusher_Jitter);
        add(EffectType.Bitcrusher, Bitcrusher_Rate);
        add(EffectType.Bitcrusher, Bitcrusher_Wet);

        // CabinetSimulator
        add(EffectType.CabinetSimulator, Global_Bypass);
        add(EffectType.CabinetSimulator, CabinetSimulator_Damping);
        add(EffectType.CabinetSimulator, CabinetSimulator_Height);
        add(EffectType.CabinetSimulator, CabinetSimulator_Tone);
        add(EffectType.CabinetSimulator, CabinetSimulator_Wet);
        add(EffectType.CabinetSimulator, CabinetSimulator_Width);

        // Chorus
        add(EffectType.Chorus, Global_Bypass);
        add(EffectType.Chorus, Chorus_Delay);
        add(EffectType.Chorus, Chorus_Depth);
        add(EffectType.Chorus, Chorus_Mode);
        add(EffectType.Chorus, Chorus_Rate);
        add(EffectType.Chorus, Chorus_Wet);

        // CombFilter

        // Compressor
        add(EffectType.Compressor, Global_Bypass);
        add(EffectType.Compressor, Compressor_Attack);
        add(EffectType.Compressor, Compressor_Ratio);
        add(EffectType.Compressor, Compressor_Release);
        add(EffectType.Compressor, Compressor_Sidechain);
        add(EffectType.Compressor, Compressor_Threshold);

        // Delay
        add(EffectType.Delay, Global_Bypass);
        add(EffectType.Delay, Delay_Feedback);
        add(EffectType.Delay, Delay_Mode);
        add(EffectType.Delay, Delay_Time);
        add(EffectType.Delay, Delay_Wet);

        // Distortion
        add(EffectType.Distortion, Global_Bypass);
        add(EffectType.Distortion, Distortion_Amount);
        add(EffectType.Distortion, Distortion_PostGain);
        add(EffectType.Distortion, Distortion_PreGain);
        add(EffectType.Distortion, Distortion_Program);

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

    private EffectControlKind kind;

    private float min;

    private float max;

    private Float defaultValue = null;

    private Boolean defaultBooleanValue = null;

    @Override
    public String getControl() {
        return control;
    }

    public EffectControlKind getKind() {
        return kind;
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

    private EffectControls(String control, EffectControlKind kind, boolean defaultValue) {
        this.control = control;
        this.kind = kind;
        defaultBooleanValue = defaultValue;
    }

    private EffectControls(String control, EffectControlKind kind, float min, float max,
            float defaultValue) {
        this.control = control;
        this.kind = kind;
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
