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

public enum MixerControls implements IMixerControl {

    EqBass("eq_bass", OSCControlKind.Float, -1.0f, 1.0f, 0f),

    EqMid("eq_mid", OSCControlKind.Float, -1.0f, 1.0f, 0f),

    EqHigh("eq_high", OSCControlKind.Float, -1.0f, 1.0f, 0f),

    DelaySend("delay_send", OSCControlKind.Float, 0f, 1.0f, 0f),

    ReverbSend("reverb_send", OSCControlKind.Float, 0f, 0.5f, 0f),

    Pan("pan", OSCControlKind.Float, -1.0f, 1.0f, 0f),

    StereoWidth("stereo_width", OSCControlKind.Float, -1.0f, 1.0f, 0f),

    Mute("mute", OSCControlKind.Boolean, 0, 1, 0),

    Solo("solo", OSCControlKind.Boolean, 0, 1, 0),

    Volume("volume", OSCControlKind.Float, 0f, 2.0f, 0f);

    static void initialize() {
        for (MixerControls control : values()) {
            OSCControlsMap.add(control);
        }
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

    private MixerControls(String control, OSCControlKind kind, float min, float max,
            float defaultValue) {
        this.control = control;
        this.kind = kind;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        displayName = name();
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    public final boolean isValid(float value, float oldValue) throws RuntimeException {
        return OSCUtils.isValid(this, value, oldValue);
    }
}
