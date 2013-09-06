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

package com.teotigraphix.caustk.system;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.controller.core.ControllerComponent;

public class SystemState extends ControllerComponent implements ISystemState {

    //----------------------------------
    // shiftEnabled
    //----------------------------------

    private boolean shiftEnabled;

    @Override
    public boolean isShiftEnabled() {
        return shiftEnabled;
    }

    @Override
    public void setShiftEnabled(boolean value) {
        if (shiftEnabled == value)
            return;
        shiftEnabled = value;
        getController().getDispatcher().trigger(new OnSystemStateShiftModeChange(shiftEnabled));
    }

    //----------------------------------
    // recording
    //----------------------------------

    private boolean recording;

    /**
     * Returns whether the controller is in record mode.
     */
    @Override
    public boolean isRecording() {
        return recording;
    }

    /**
     * Sets the controllers record mode.
     * 
     * @param value The new record mode.
     * @see OnSystemStateRecordModeChange
     */
    @Override
    public void setRecording(boolean value) {
        if (recording == value)
            return;
        recording = value;
        getController().getDispatcher().trigger(new OnSystemStateRecordModeChange(recording));
    }

    //----------------------------------
    // systemMode
    //----------------------------------

    private SystemMode systemMode;

    /**
     * Returns the current {@link SystemMode} of the controller.
     */
    @Override
    public SystemMode getSystemMode() {
        return systemMode;
    }

    /**
     * Sets the global system mode of the controller.
     * 
     * @param value The new {@link SystemMode}.
     * @see OnSystemStateSystemModeChange
     */
    @Override
    public void setSystemMode(SystemMode value) {
        if (systemMode == value)
            return;
        systemMode = value;
        getController().getDispatcher().trigger(new OnSystemStateSystemModeChange(systemMode));
    }

    //----------------------------------
    // keyboardMode
    //----------------------------------

    private KeyboardMode keyboardMode;

    /**
     * Returns the current {@link KeyboardMode} of the controller.
     */
    @Override
    public KeyboardMode getKeyboardMode() {
        return keyboardMode;
    }

    /**
     * Sets the global keyboard mode of the controller.
     * 
     * @param value The new {@link KeyboardMode}.
     * @see OnSystemStateKeyboardModeChange
     */
    @Override
    public void setKeyboardMode(KeyboardMode value) {
        if (keyboardMode == value)
            return;
        keyboardMode = value;
        getController().getDispatcher().trigger(new OnSystemStateKeyboardModeChange(keyboardMode));
    }

    public SystemState(ICaustkController controller) {
        super(controller);
    }

    //--------------------------------------------------------------------------
    // Public Method API
    //--------------------------------------------------------------------------

    /**
     * Selects the current {@link IShiftFunction} only if the
     * {@link #isShiftEnabled()} is true.
     * 
     * @param shiftFunction The current function state for shift.
     */
    public void select(IShiftFunction shiftFunction) {
        if (!isShiftEnabled())
            return;
    }

    //--------------------------------------------------------------------------
    // Observer API
    //--------------------------------------------------------------------------

    public static class OnSystemStateShiftModeChange {
        private boolean enabled;

        public boolean getEnabled() {
            return enabled;
        }

        public OnSystemStateShiftModeChange(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class OnSystemStateSystemModeChange {
        private SystemMode mode;

        public SystemMode getMode() {
            return mode;
        }

        public OnSystemStateSystemModeChange(SystemMode mode) {
            this.mode = mode;
        }
    }

    public static class OnSystemStateKeyboardModeChange {
        private KeyboardMode mode;

        public KeyboardMode getMode() {
            return mode;
        }

        public OnSystemStateKeyboardModeChange(KeyboardMode mode) {
            this.mode = mode;
        }
    }

    public static class OnSystemStateRecordModeChange {
        private boolean recording;

        public boolean isRecording() {
            return recording;
        }

        public OnSystemStateRecordModeChange(boolean recording) {
            this.recording = recording;
        }
    }

    public enum SystemMode {
        PATTERN, SONG, GLOBAL;
    }

    public enum KeyboardMode {
        KEYBOARD, STEP;
    }

    @Override
    public void restore() {
    }

    @Override
    public void onRegister() {
    }

}
