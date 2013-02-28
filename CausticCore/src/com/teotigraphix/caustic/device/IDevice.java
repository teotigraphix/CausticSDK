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

package com.teotigraphix.caustic.device;

import com.teotigraphix.caustic.core.ICausticEngineAware;
import com.teotigraphix.common.IDispose;
import com.teotigraphix.common.IPersist;
import com.teotigraphix.common.IRestore;

/**
 * The {@link IDevice} interface is a top-level device that holds a unique
 * identifier based in it's component category, a unique index and a human
 * readable name.
 * <p>
 * An {@link IDevice} is always {@link ICausticEngineAware}.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IDevice extends ICausticEngineAware, IPersist, IRestore, IDispose {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // id
    //----------------------------------

    /**
     * The device's String unique identifier.
     * <p>
     * This name is usually set on the device itself. The device will use this
     * id as the state id when it's persistable is saved or loaded.
     */
    String getId();

    //----------------------------------
    // index
    //----------------------------------

    /**
     * Returns the index of the device in relation to it's parent component.
     */
    int getIndex();

    //----------------------------------
    // name
    //----------------------------------

    /**
     * The human readable name, this can be set to whatever the client needs.
     * <p>
     * The name is usually saved in the memento with the <code>name</code>
     * attribute.
     * <p>
     * This value is also designed to be show in UI views in the application
     * itself as it can contain spaces, upper and lower case letters.
     */
    String getName();

    /**
     * Sets the human readable name.
     * 
     * @param value The new name.
     */
    void setName(String value);
}
