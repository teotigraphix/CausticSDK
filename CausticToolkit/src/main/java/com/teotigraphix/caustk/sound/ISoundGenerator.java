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

package com.teotigraphix.caustk.sound;

import com.teotigraphix.caustk.core.CausticEventListener;
import com.teotigraphix.caustk.core.ICausticEngine;

/**
 * The {@link ISoundGenerator} API wraps the Native CausticCore audio loop and
 * JNI interface.
 * 
 * @author Michael Schmalle
 */
public interface ISoundGenerator extends ICausticEngine {

    /**
     * Creates and starts the native Caustic core instance event loop.
     */
    void initialize();

    /**
     * Closes the CausticCore event loop and cleans up.
     */
    void close();

    /**
     * Adds an event listener to the Caustic core event loop.
     * 
     * @param l The listener add.
     */
    void addEventListener(CausticEventListener l);

    /**
     * Removes an event listener from the Caustic core event loop.
     * 
     * @param l The listener to remove.
     */
    void removeEventListener(CausticEventListener l);

}
