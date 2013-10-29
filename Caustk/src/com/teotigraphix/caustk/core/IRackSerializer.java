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

package com.teotigraphix.caustk.core;

import com.teotigraphix.caustk.machine.CaustkFactory;
import com.teotigraphix.caustk.rack.IRack;

/**
 * @author Michael Schmalle
 */
public interface IRackSerializer {

    /**
     * Loads the rack component from a <code>.caustic</code> file.
     * <p>
     * The class that implements this method if {@link IRackAware} will call
     * {@link IRackAware#setRack(IRack)} on itself using the
     * {@link CaustkFactory#getRack()} which is the current rack loading
     * the file.
     * <p>
     * Calling this method will wipe out all state, rack references and sub
     * components creating new components and loading from a caustic file.
     * <p>
     * The quickest way to wipe state is loading a blank <code>.caustic</code>
     * file where all defaults are loaded by query from a restore() call after
     * the load.
     * 
     * @param factory The library factory.
     * @throws CausticException
     */
    void load(CaustkFactory factory) throws CausticException;

    /**
     * Restores the rack, each component implementing the method will use OSC
     * message queries to set instance properties.
     * <p>
     * The restore() method differs from the load() in that the load() method
     * will actually create sub components. Where restore() just updates state
     * on the existing components.
     */
    void restore();

    /**
     * Updates the native rack with instance property state that exists on the
     * rack component, the component will send setter commands to the native
     * rack.
     */
    void update();

}
