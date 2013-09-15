////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustk.gs.machine.part;

import com.teotigraphix.caustk.gs.machine.GrooveMachine;

public class MachineKeyboard extends MachineComponentPart {

    //--------------------------------------------------------------------------
    // Property :: API
    //--------------------------------------------------------------------------

    private KeyboardMode keyboardMode;

    public KeyboardMode getKeyboardMode() {
        return keyboardMode;
    }

    /**
     * Sets the current state of the keyboard, step, keyboard or shift.
     * 
     * @param value The {@link KeyboardMode}.
     */
    public void setKeyboardMode(KeyboardMode value) {
        if (value == keyboardMode)
            return;
        keyboardMode = value;
        if (onMachineKeyboardListener != null)
            onMachineKeyboardListener.onKeyboardModeChange(keyboardMode);
    }

    //----------------------------------
    // octave
    //----------------------------------

    private int octave = 0;

    public int getOctave() {
        return octave;
    }

    /**
     * Sets the relative octave of the keyboard.
     * 
     * @param value (-4..4)
     */
    public void setOctave(int value) {
        if (value == octave)
            return;
        octave = value;
        if (onMachineKeyboardListener != null)
            onMachineKeyboardListener.onOctaveChange(octave);
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public MachineKeyboard(GrooveMachine machine) {
        super(machine);
    }

    //--------------------------------------------------------------------------
    // Method :: API
    //--------------------------------------------------------------------------

    public void noteOn(int relativePitch) {
        int pitch = relativePitch + (12 * octave);
        getMachine().getSelectedPatch().noteOn(pitch, 1f);
    }

    public void noteOff(int relativePitch) {
        int pitch = relativePitch + (12 * octave);
        getMachine().getSelectedPatch().noteOff(pitch);
    }

    public enum KeyboardMode {
        Step,

        Keyboard,

        Shift;
    }

    //--------------------------------------------------------------------------
    // Event :: API
    //--------------------------------------------------------------------------

    private OnMachineKeyboardListener onMachineKeyboardListener;

    public void setOnMachineKeyboardListener(OnMachineKeyboardListener l) {
        this.onMachineKeyboardListener = l;

    }

    public interface OnMachineKeyboardListener {
        void onOctaveChange(int octave);

        void onKeyboardModeChange(KeyboardMode keyboardMode);
    }
}
