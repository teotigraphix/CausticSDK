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

import com.teotigraphix.caustk.core.MachineType;
import com.teotigraphix.caustk.node.machine.PCMSynthMachine;

/**
 * The global enumeration for the {@link PCMSynthMachine}.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public enum PCMSynthControls implements IMachineControl {

    Volume_Out("volume_out", OSCControlKind.Float, 0f, 8f, 2f),

    Volume_Attack("volume_attack", OSCControlKind.Float, 0f, 1f, 0f),

    Volume_Decay("volume_decay", OSCControlKind.Float, 0f, 1f, 0f),

    Volume_Sustain("volume_sustain", OSCControlKind.Float, 0f, 1f, 0f),

    Volume_Release("volume_release", OSCControlKind.Float, 0f, 1f, 0f),

    LFO_Depth("lfo_depth", OSCControlKind.Float, 0f, 1f, 0f),

    LFO_Rate("lfo_rate", OSCControlKind.Enum_Int, 1, 12, 1),

    LFO_Target("lfo_target", OSCControlKind.Enum_Int, 0, 3, 0),

    LFO_Waveform("lfo_waveform", OSCControlKind.Enum_Int, 0, 3, 0),

    Pitch_Octave("pitch_octave", OSCControlKind.Int, -4, 4, 0),

    Pitch_Semis("pitch_semis", OSCControlKind.Int, -12, 12, 0),

    Pitch_Cents("pitch_cents", OSCControlKind.Int, -50, 50, 0),

    Filter_Cutoff("filter_cutoff", OSCControlKind.Float, 0f, 1f, 1f),

    Filter_Resonance("filter_resonance", OSCControlKind.Float, 0f, 1f, 0f),

    Filter_Type("filter_type", OSCControlKind.Enum_Int, 0, 6, 0),

    Filter_Attack("filter_attack", OSCControlKind.Float, 0f, 1f, 0f),

    Filter_Decay("filter_decay", OSCControlKind.Float, 0f, 1f, 0f),

    Filter_Sustain("filter_sustain", OSCControlKind.Float, 0f, 1f, 1f),

    Filter_Release("filter_release", OSCControlKind.Float, 0f, 1f, 1f);

    static void initialize() {
        for (PCMSynthControls control : values()) {
            OSCControlsMap.add(MachineType.PCMSynth, control);
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

    private PCMSynthControls(String control, OSCControlKind kind, float min, float max,
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
