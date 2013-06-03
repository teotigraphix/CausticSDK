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

package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.machine.IMachine;

/**
 * The Tone wraps an {@link IMachine}.
 * <p>
 * Tones are basically the ONLY static instances that are not lightweight in the
 * framework. Unlike Part, Patch, Pattern etc, the Tone is a singleton and only
 * created once at startup(this will change if unlimited machines exist).
 */
public abstract class Tone {
    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // machine
    //----------------------------------

    public abstract IMachine getMachine();

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return getMachine().getIndex();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Tone() {
    }
}
