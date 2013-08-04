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

import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.system.SystemState.KeyboardMode;
import com.teotigraphix.caustk.system.SystemState.OnSystemStateKeyboardModeChange;
import com.teotigraphix.caustk.system.SystemState.OnSystemStateRecordModeChange;
import com.teotigraphix.caustk.system.SystemState.OnSystemStateSystemModeChange;
import com.teotigraphix.caustk.system.SystemState.SystemMode;

public interface ISystemState extends IRestore {

    //----------------------------------
    // shiftEnabled
    //----------------------------------

    boolean isShiftEnabled();

    void setShiftEnabled(boolean value);

    //----------------------------------
    // recording
    //----------------------------------

    /**
     * Returns whether the controller is in record mode.
     */
    boolean isRecording();

    /**
     * Sets the controllers record mode.
     * 
     * @param value The new record mode.
     * @see OnSystemStateRecordModeChange
     */
    void setRecording(boolean value);

    //----------------------------------
    // systemMode
    //----------------------------------

    /**
     * Returns the current {@link SystemMode} of the controller.
     */
    public SystemMode getSystemMode();

    /**
     * Sets the global system mode of the controller.
     * 
     * @param value The new {@link SystemMode}.
     * @see OnSystemStateSystemModeChange
     */
    void setSystemMode(SystemMode value);

    //----------------------------------
    // keyboardMode
    //----------------------------------

    /**
     * Returns the current {@link KeyboardMode} of the controller.
     */
    KeyboardMode getKeyboardMode();

    /**
     * Sets the global keyboard mode of the controller.
     * 
     * @param value The new {@link KeyboardMode}.
     * @see OnSystemStateKeyboardModeChange
     */
    void setKeyboardMode(KeyboardMode value);

}
