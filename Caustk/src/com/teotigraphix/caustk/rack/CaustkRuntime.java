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

package com.teotigraphix.caustk.rack;

import com.teotigraphix.caustk.core.ISoundGenerator;

/**
 * The {@link CaustkRuntime} encapsulates the {@link ISoundGenerator} and
 * {@link Rack} creation and initialization.
 * <p>
 * Holds the single {@link Rack} instance for an application session.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public class CaustkRuntime {

    private ISoundGenerator soundGenerator;

    private Rack rack;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    /**
     * Returns the session {@link Rack} instance.
     */
    public final Rack getRack() {
        return rack;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    /**
     * Creates a new runtime with {@link Rack}.
     * 
     * @param soundGenerator The platform sound engine.
     */
    public CaustkRuntime(ISoundGenerator soundGenerator) {
        this.soundGenerator = soundGenerator;
        initialize();
    }

    private void initialize() {
        rack = RackProvider.createRack(soundGenerator);
        RackProvider.setRack(rack);
    }
}
