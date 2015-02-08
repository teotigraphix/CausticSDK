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

import com.teotigraphix.caustk.core.osc.EffectsRackMessage.ChorusMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.FlangerMode;
import com.teotigraphix.caustk.core.osc.EffectsRackMessage.StaticFlangerMode;
import com.teotigraphix.caustk.node.effect.EffectType;

/**
 * The global enumeration for the {@link EffectType}s.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public enum EffectControls implements IEffectControl {

    Global_Bypass("bypass", OSCControlKind.Boolean, 0f, 0f, 1f),

    //----------------------------------
    // Autowah
    //----------------------------------

    /**
     * Values <code>0.5..4.0</code>; default <code>2.23</code>
     */
    Autowah_Cutoff("cutoff", OSCControlKind.Float, 0.5f, 4f, 2.23f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Autowah_Depth("depth", OSCControlKind.Float, 0f, 1f, 1f),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    Autowah_Resonance("resonance", OSCControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0.0.0.5</code>; default <code>0.4</code>
     */
    Autowah_Speed("speed", OSCControlKind.Float, 0f, 0.5f, 0.4f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Autowah_Wet("wet", OSCControlKind.Float, 0f, 1f, 1f),

    //----------------------------------
    // Bitcrusher
    //----------------------------------

    /**
     * Values <code>1..16</code>; default <code>3</code>
     */
    Bitcrusher_Depth("depth", OSCControlKind.Int, 1, 16, 3),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.0</code>
     */
    Bitcrusher_Jitter("jitter", OSCControlKind.Float, 0f, 1f, 0f),

    /**
     * Values <code>0.01..0.5</code>; default <code>0.1</code>
     */
    Bitcrusher_Rate("rate", OSCControlKind.Float, 0.01f, 0.5f, 0.1f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Bitcrusher_Wet("wet", OSCControlKind.Float, 0f, 1f, 1f),

    //----------------------------------
    // CabinetSimulator
    //----------------------------------

    /**
     * Values <code>0.25..1</code>; default <code>1.0</code>
     */
    CabinetSimulator_Damping("damping", OSCControlKind.Float, 0.25f, 1f, 1f),

    /**
     * Values <code>0..1</code>; default <code>0</code>
     */
    CabinetSimulator_Height("height", OSCControlKind.Float, 0f, 1f, 0f),

    /**
     * Values <code>0..1</code>; default <code>0.5</code>
     */
    CabinetSimulator_Tone("tone", OSCControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0..1</code>; default <code>0.5</code>
     */
    CabinetSimulator_Wet("wet", OSCControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0..1</code>; default <code>0</code>
     */
    CabinetSimulator_Width("width", OSCControlKind.Float, 0f, 1f, 0f),

    //----------------------------------
    // Chorus
    //----------------------------------

    /**
     * Values <code>0.0..0.7</code>; default <code>0.0</code>
     */
    Chorus_Delay("delay", OSCControlKind.Float, 0f, 0.7f, 0f),

    /**
     * Values <code>0.1..0.95</code>; default <code>0.25</code>
     */
    Chorus_Depth("depth", OSCControlKind.Float, 0.1f, 0.95f, 0.25f),

    /**
     * Values <code>0,1,2,3,4,5,6,7</code>; default <code>0</code>
     * <p>
     * triangleFull, sineFull, triangleHalf, sineHalf, triangleFullOpposed,
     * sineFullOpposed, triangleHalfOpposed, sineHalfOpposed
     * 
     * @see ChorusMode
     */
    Chorus_Mode("mode", OSCControlKind.Enum_Int, 0, 7, 0),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.4</code>
     */
    Chorus_Rate("rate", OSCControlKind.Float, 0f, 1f, 0.4f),

    /**
     * Values <code>0.0..0.5</code>; default <code>0.5</code>
     */
    Chorus_Wet("wet", OSCControlKind.Float, 0f, 0.5f, 0.5f),

    //----------------------------------
    // CombFilter
    //----------------------------------

    /**
     * Values <code>2..50</code>; default <code>10</code>
     */
    CombFilter_Freq("rate", OSCControlKind.Int, 2, 50, 10), // rate

    // XXX CombFilter_Reso BUG with min

    /**
     * Values <code>0.1..0.95</code>; default <code>0.475</code>
     */
    CombFilter_Reso("feedback", OSCControlKind.Float, 0f, 0.95f, 0.475f), // feedback

    /**
     * Values <code>0.1..0.9</code>; default <code>0.8</code>
     */
    CombFilter_Wet("depth", OSCControlKind.Float, 0.1f, 0.9f, 0.8f), // depth

    //----------------------------------
    // Compressor
    //----------------------------------

    // XXX BUG temp change to max 1 from .2 getting 0.866

    /**
     * Values <code>0.00001..0.2</code>; default <code>0.01</code>
     */
    Compressor_Attack("attack", OSCControlKind.Float, 0f, 1f, 0.01f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Compressor_Ratio("ratio", OSCControlKind.Float, 0f, 1f, 1f),

    /**
     * Values <code>0.001..0.2</code>; default <code>0.05</code>
     */
    Compressor_Release("release", OSCControlKind.Float, 0.001f, 0.2f, 0.05f),

    /**
     * Values <code>0..13</code>; default <code>-1</code>
     */
    Compressor_Sidechain("sidechain", OSCControlKind.Int, 0, 13, -1),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.1</code>
     */
    Compressor_Threshold("threshold", OSCControlKind.Float, 0f, 1f, 0.1f),

    //----------------------------------
    // Delay
    //----------------------------------

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    Delay_Feedback("feedback", OSCControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0,1,2,3,4</code> Mono, MonoLR, MonoRL, DualMono, PingPong;
     * default <code>0</code>
     */
    Delay_Mode("mode", OSCControlKind.Enum_Int, 0, 4, 0),

    /**
     * Values <code>1..12</code>; default <code>8</code>
     */
    Delay_Time("time", OSCControlKind.Int, 1, 12, 8),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    Delay_Wet("wet", OSCControlKind.Float, 0f, 1f, 0.5f),

    //----------------------------------
    // Distortion
    //----------------------------------

    /**
     * Values <code>0.0..20.0</code>; default <code>16.3</code>
     */
    Distortion_Amount("amount", OSCControlKind.Float, 0f, 20f, 16.3f),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.1</code>
     */
    Distortion_PostGain("post_gain", OSCControlKind.Float, 0f, 1f, 0.1f),

    /**
     * Values <code>0.0..5.0</code>; default <code>4.05</code>
     */
    Distortion_PreGain("pre_gain", OSCControlKind.Float, 0f, 5f, 4.05f),

    /**
     * Values <code>0..3</code>; default <code>0</code>
     */
    Distortion_Program("program", OSCControlKind.Enum_Int, 0, 3, 0),

    //----------------------------------
    // Flanger
    //----------------------------------

    /**
     * Values <code>0.1..0.95</code>; default <code>0.25</code>
     */
    Flanger_Depth("depth", OSCControlKind.Float, 0.1f, 0.95f, 0.25f),

    /**
     * Values <code>0.25..0.9</code>; default <code>0.4</code>
     */
    Flanger_Feedback("feedback", OSCControlKind.Float, 0.25f, 0.9f, 0.4f),

    /**
     * Values <code>0,1,2,3,4,5,6,7</code>; default <code>0</code>
     * <p>
     * triangleFull, sineFull, triangleHalf, sineHalf, triangleFullOpposed,
     * sineFullOpposed, triangleHalfOpposed, sineHalfOpposed
     * 
     * @see FlangerMode
     */
    Flanger_Mode("mode", OSCControlKind.Enum_Int, 0, 7, 0),

    /**
     * Values <code>0.04..2.0</code>; default <code>0.4</code>
     */
    Flanger_Rate("rate", OSCControlKind.Float, 0.04f, 2f, 0.4f),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    Flanger_Wet("wet", OSCControlKind.Float, 0f, 1f, 0.5f),

    //----------------------------------
    // Limiter
    //----------------------------------

    /**
     * Values <code>0..0.05</code>; default <code>0.01</code>
     */
    Limiter_Attack("attack", OSCControlKind.Float, 0f, 0.05f, 0.01f),

    /**
     * Values <code>0..2</code>; default <code>0.5</code>
     */
    Limiter_PostGain("post_gain", OSCControlKind.Float, 0f, 2f, 0.5f),

    /**
     * Values <code>0..4</code>; default <code>2</code>
     */
    Limiter_PreGain("pre_gain", OSCControlKind.Float, 0f, 4f, 2f),

    /**
     * Values <code>0.01..0.5</code>; default <code>0.5</code>
     */
    Limiter_Release("release", OSCControlKind.Float, 0.01f, 0.5f, 0.5f),

    //----------------------------------
    // MultiFilter
    //----------------------------------

    /**
     * Values <code>0.1..1.0</code>; default <code>0.54</code>
     */
    MultiFilter_Frequency("frequency", OSCControlKind.Float, 0.1f, 1f, 0.54f),

    /**
     * Values <code>-12..12</code>; default <code>0.0</code>
     */
    MultiFilter_Gain("gain", OSCControlKind.Float, -12f, 12f, 0f),

    /**
     * Values <code>0,1,2,3,4,5</code>; default <code>0</code>
     */
    MultiFilter_Mode("mode", OSCControlKind.Enum_Int, 0, 5, 0),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    MultiFilter_Resonance("resonance", OSCControlKind.Float, 0f, 1f, 0.5f),

    //----------------------------------
    // ParametricEQ
    //----------------------------------

    /**
     * Values <code>0.0..1.0</code>; default <code>0.54</code>
     */
    ParametricEQ_Frequency("frequency", OSCControlKind.Float, 0f, 1f, 0.54f),

    /**
     * Values <code>-12.0..12.0</code>; default <code>0.0</code>
     */
    ParametricEQ_Gain("gain", OSCControlKind.Float, -12f, 12f, 0f),

    /**
     * Values <code>0.0..10.0</code>; default <code>0.5</code>
     */
    ParametricEQ_Width("width", OSCControlKind.Float, 0f, 10f, 0.5f),

    //----------------------------------
    // Phaser
    //----------------------------------

    /**
     * Values <code>0.1..0.95</code>; default <code>0.8</code>
     */
    Phaser_Depth("depth", OSCControlKind.Float, 0.1f, 0.95f, 0.8f),

    /**
     * Values <code>0.1..0.95</code>; default <code>0.47</code>
     */
    Phaser_Feedback("feedback", OSCControlKind.Float, 0.1f, 0.95f, 0.47f),

    /**
     * Values <code>0.002..0.5</code>; default <code>0.09</code>
     */
    Phaser_HighFreq("highfreq", OSCControlKind.Float, 0.002f, 0.5f, 0.09f),

    /**
     * Values <code>0.002..0.5</code>; default <code>0.01</code>
     */
    Phaser_LowFreq("lowfreq", OSCControlKind.Float, 0.002f, 0.5f, 0.01f),

    /**
     * Values <code>2..50</code>; default <code>10</code>
     */
    Phaser_Rate("rate", OSCControlKind.Int, 2, 50, 10),

    //----------------------------------
    // Reverb
    //----------------------------------

    /**
     * Values <code>0.0..0.8</code>; default <code>0.25</code>
     */
    Reverb_Damping("damping", OSCControlKind.Float, 0f, 0.8f, 0.25f),

    /**
     * Values <code>0.0..0.0925</code>; default <code>0.04625</code>
     */
    Reverb_Delay("delay", OSCControlKind.Float, 0f, 0.0925f, 0.04625f),

    /**
     * Values <code>0.0..0.925</code>; default <code>0.85</code>
     */
    Reverb_Room("room", OSCControlKind.Float, 0f, 0.925f, 0.85f),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.195</code>
     */
    Reverb_Wet("wet", OSCControlKind.Float, 0f, 1f, 0.195f),

    /**
     * Values <code>0.0..1.0</code>; default <code>1.0</code>
     */
    Reverb_Width("width", OSCControlKind.Float, 0f, 1f, 1f),

    //----------------------------------
    // StaticFlanger
    //----------------------------------

    /**
     * Values <code>-0.95..0.95</code>; default <code>0.0</code>
     */
    StaticFlanger_Depth("depth", OSCControlKind.Float, -0.95f, 0.95f, 0f),

    /**
     * Values <code>0.25..0.9</code>; default <code>0.575</code>
     */
    StaticFlanger_Feedback("feedback", OSCControlKind.Float, 0.25f, 0.9f, 0.575f),

    /**
     * Values <code>0,4</code>; default <code>0</code>
     * <p>
     * triangleFull, triangleFullOpposed
     * 
     * @see StaticFlangerMode
     */
    StaticFlanger_Mode("mode", OSCControlKind.Enum_Int, 0, 4, 0),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    StaticFlanger_Wet("wet", OSCControlKind.Float, 0f, 1f, 0.5f),

    //----------------------------------
    // VinylSimulator
    //----------------------------------

    /**
     * Values <code>0.0..1.0</code>; default <code>0.5</code>
     */
    VinylSimulator_Age("age", OSCControlKind.Float, 0f, 1f, 0.5f),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.75</code>
     */
    VinylSimulator_Dust("dust", OSCControlKind.Float, 0f, 1f, 0.75f),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.33</code>
     */
    VinylSimulator_Noise("noise", OSCControlKind.Float, 0f, 1f, 0.33f),

    /**
     * Values <code>0.0..1.0</code>; default <code>0.25</code>
     */
    VinylSimulator_Scratch("scratch", OSCControlKind.Float, 0f, 1f, 0.25f),

    /**
     * Values <code>0.0..2.0</code>; default <code>1.0</code>
     */
    VinylSimulator_Wet("wet", OSCControlKind.Float, 0f, 2f, 1f);

    static void initialize() {
        // Autowah
        OSCControlsMap.add(EffectType.Autowah, Global_Bypass);
        OSCControlsMap.add(EffectType.Autowah, Autowah_Cutoff);
        OSCControlsMap.add(EffectType.Autowah, Autowah_Depth);
        OSCControlsMap.add(EffectType.Autowah, Autowah_Resonance);
        OSCControlsMap.add(EffectType.Autowah, Autowah_Speed);
        OSCControlsMap.add(EffectType.Autowah, Autowah_Wet);

        // Bitcrusher
        OSCControlsMap.add(EffectType.Bitcrusher, Global_Bypass);
        OSCControlsMap.add(EffectType.Bitcrusher, Bitcrusher_Depth);
        OSCControlsMap.add(EffectType.Bitcrusher, Bitcrusher_Jitter);
        OSCControlsMap.add(EffectType.Bitcrusher, Bitcrusher_Rate);
        OSCControlsMap.add(EffectType.Bitcrusher, Bitcrusher_Wet);

        // CabinetSimulator
        OSCControlsMap.add(EffectType.CabinetSimulator, Global_Bypass);
        OSCControlsMap.add(EffectType.CabinetSimulator, CabinetSimulator_Damping);
        OSCControlsMap.add(EffectType.CabinetSimulator, CabinetSimulator_Height);
        OSCControlsMap.add(EffectType.CabinetSimulator, CabinetSimulator_Tone);
        OSCControlsMap.add(EffectType.CabinetSimulator, CabinetSimulator_Wet);
        OSCControlsMap.add(EffectType.CabinetSimulator, CabinetSimulator_Width);

        // Chorus
        OSCControlsMap.add(EffectType.Chorus, Global_Bypass);
        OSCControlsMap.add(EffectType.Chorus, Chorus_Delay);
        OSCControlsMap.add(EffectType.Chorus, Chorus_Depth);
        OSCControlsMap.add(EffectType.Chorus, Chorus_Mode);
        OSCControlsMap.add(EffectType.Chorus, Chorus_Rate);
        OSCControlsMap.add(EffectType.Chorus, Chorus_Wet);

        // CombFilter
        OSCControlsMap.add(EffectType.CombFilter, Global_Bypass);
        OSCControlsMap.add(EffectType.CombFilter, CombFilter_Freq);
        OSCControlsMap.add(EffectType.CombFilter, CombFilter_Reso);
        OSCControlsMap.add(EffectType.CombFilter, CombFilter_Wet);

        // Compressor
        OSCControlsMap.add(EffectType.Compressor, Global_Bypass);
        OSCControlsMap.add(EffectType.Compressor, Compressor_Attack);
        OSCControlsMap.add(EffectType.Compressor, Compressor_Ratio);
        OSCControlsMap.add(EffectType.Compressor, Compressor_Release);
        OSCControlsMap.add(EffectType.Compressor, Compressor_Sidechain);
        OSCControlsMap.add(EffectType.Compressor, Compressor_Threshold);

        // Delay
        OSCControlsMap.add(EffectType.Delay, Global_Bypass);
        OSCControlsMap.add(EffectType.Delay, Delay_Feedback);
        OSCControlsMap.add(EffectType.Delay, Delay_Mode);
        OSCControlsMap.add(EffectType.Delay, Delay_Time);
        OSCControlsMap.add(EffectType.Delay, Delay_Wet);

        // Distortion
        OSCControlsMap.add(EffectType.Distortion, Global_Bypass);
        OSCControlsMap.add(EffectType.Distortion, Distortion_Amount);
        OSCControlsMap.add(EffectType.Distortion, Distortion_PostGain);
        OSCControlsMap.add(EffectType.Distortion, Distortion_PreGain);
        OSCControlsMap.add(EffectType.Distortion, Distortion_Program);

        // Flanger
        OSCControlsMap.add(EffectType.Flanger, Global_Bypass);
        OSCControlsMap.add(EffectType.Flanger, Flanger_Depth);
        OSCControlsMap.add(EffectType.Flanger, Flanger_Feedback);
        OSCControlsMap.add(EffectType.Flanger, Flanger_Mode);
        OSCControlsMap.add(EffectType.Flanger, Flanger_Rate);
        OSCControlsMap.add(EffectType.Flanger, Flanger_Wet);

        // Limiter
        OSCControlsMap.add(EffectType.Limiter, Global_Bypass);
        OSCControlsMap.add(EffectType.Limiter, Limiter_Attack);
        OSCControlsMap.add(EffectType.Limiter, Limiter_PostGain);
        OSCControlsMap.add(EffectType.Limiter, Limiter_PreGain);
        OSCControlsMap.add(EffectType.Limiter, Limiter_Release);

        // MultiFilter
        OSCControlsMap.add(EffectType.MultiFilter, Global_Bypass);
        OSCControlsMap.add(EffectType.MultiFilter, MultiFilter_Frequency);
        OSCControlsMap.add(EffectType.MultiFilter, MultiFilter_Gain);
        OSCControlsMap.add(EffectType.MultiFilter, MultiFilter_Mode);
        OSCControlsMap.add(EffectType.MultiFilter, MultiFilter_Resonance);

        // ParametricEQ
        OSCControlsMap.add(EffectType.ParametricEQ, Global_Bypass);
        OSCControlsMap.add(EffectType.ParametricEQ, ParametricEQ_Frequency);
        OSCControlsMap.add(EffectType.ParametricEQ, ParametricEQ_Gain);
        OSCControlsMap.add(EffectType.ParametricEQ, ParametricEQ_Width);

        // Phaser
        OSCControlsMap.add(EffectType.Phaser, Global_Bypass);
        OSCControlsMap.add(EffectType.Phaser, Phaser_Depth);
        OSCControlsMap.add(EffectType.Phaser, Phaser_Feedback);
        OSCControlsMap.add(EffectType.Phaser, Phaser_HighFreq);
        OSCControlsMap.add(EffectType.Phaser, Phaser_LowFreq);
        OSCControlsMap.add(EffectType.Phaser, Phaser_Rate);

        // Reverb
        OSCControlsMap.add(EffectType.Reverb, Global_Bypass);
        OSCControlsMap.add(EffectType.Reverb, Reverb_Damping);
        OSCControlsMap.add(EffectType.Reverb, Reverb_Delay);
        OSCControlsMap.add(EffectType.Reverb, Reverb_Room);
        OSCControlsMap.add(EffectType.Reverb, Reverb_Wet);
        OSCControlsMap.add(EffectType.Reverb, Reverb_Width);

        // StaticFlanger
        OSCControlsMap.add(EffectType.StaticFlanger, Global_Bypass);
        OSCControlsMap.add(EffectType.StaticFlanger, StaticFlanger_Depth);
        OSCControlsMap.add(EffectType.StaticFlanger, StaticFlanger_Feedback);
        OSCControlsMap.add(EffectType.StaticFlanger, StaticFlanger_Mode);
        OSCControlsMap.add(EffectType.StaticFlanger, StaticFlanger_Wet);

        // VinylSimulator
        OSCControlsMap.add(EffectType.VinylSimulator, Global_Bypass);
        OSCControlsMap.add(EffectType.VinylSimulator, VinylSimulator_Age);
        OSCControlsMap.add(EffectType.VinylSimulator, VinylSimulator_Dust);
        OSCControlsMap.add(EffectType.VinylSimulator, VinylSimulator_Noise);
        OSCControlsMap.add(EffectType.VinylSimulator, VinylSimulator_Scratch);
        OSCControlsMap.add(EffectType.VinylSimulator, VinylSimulator_Wet);
    }

    //--------------------------------------------------------------------------
    // Private :: Variables
    //--------------------------------------------------------------------------

    private String control;

    private OSCControlKind kind;

    private float min;

    private float max;

    private Float defaultValue = null;

    private String displayName = null;

    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // control
    //----------------------------------

    @Override
    public String getControl() {
        return control;
    }

    //----------------------------------
    // control
    //----------------------------------

    @Override
    public OSCControlKind getKind() {
        return kind;
    }

    //----------------------------------
    // displayName
    //----------------------------------

    @Override
    public String getDisplayName() {
        return displayName;
    }

    //----------------------------------
    // min
    //----------------------------------

    @Override
    public float getMin() {
        return min;
    }

    //----------------------------------
    // max
    //----------------------------------

    @Override
    public float getMax() {
        return max;
    }

    //----------------------------------
    // defaultValue
    //----------------------------------

    @Override
    public float getDefaultValue() {
        return defaultValue;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    private EffectControls(String control, OSCControlKind kind, float min, float max,
            float defaultValue) {
        this.control = control;
        this.kind = kind;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        displayName = name().split("_")[1];
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    public final boolean isValid(float value, float oldValue) throws RuntimeException {
        return OSCUtils.isValid(this, value, oldValue);
    }

}
