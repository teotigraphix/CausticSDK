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

package com.teotigraphix.caustic.filter;

import com.teotigraphix.caustic.machine.IMachineComponent;
import com.teotigraphix.caustic.osc.VolumeMessage;

/**
 * The {@link IVolumeComponent} interface allows a machine to simple volume out.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IVolumeComponent extends IMachineComponent {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // out
    //----------------------------------

    /**
     * The machine's volume out value.
     * 
     * @see VolumeMessage#VOLUME_OUT
     */
    float getOut();

    /**
     * @see #getOut()
     * @see VolumeMessage#VOLUME_OUT
     */
    void setOut(float value);
}
