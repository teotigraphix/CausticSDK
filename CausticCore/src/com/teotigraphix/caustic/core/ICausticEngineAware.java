////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.core;

/**
 * The {@link ICausticEngineAware} interface allows subcomponents of a composite
 * device/machine to share the single engine reference without the Singleton
 * pattern.
 * <p>
 * Using composition, the engine can be injected into the aware device/machine
 * instead of the device/machine asking for it (Inversion of Control).
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface ICausticEngineAware
{

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // engine
    //----------------------------------

    /**
     * Returns the single {@link ICausticEngine} instance running in the
     * application that was given to the device.
     */
    ICausticEngine getEngine();
}
