////////////////////////////////////////////////////////////////////////////////
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

package com.teotigraphix.caustic.song;

import com.teotigraphix.common.IPersist;

/**
 * The IPreset interface establishes an API that all presets need to be included
 * in a preset library.
 * <p>
 * Currently the ISong, IPattern, IPatch and ITone are all presets.
 * </p>
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IPreset extends IPersist {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    /**
     * The index of the preset, this will vary in meaning relative to the
     * specific implementation.
     */
    int getIndex();

    //----------------------------------
    // id
    //----------------------------------

    /**
     * The unique preset id within it's preset bank.
     * <p>
     * For the IPatc
     */
    String getId();

    /**
     * @see #getId()
     */
    void setId(String value);

    //----------------------------------
    // name
    //----------------------------------

    /**
     * The human readable preset name.
     */
    String getName();

    /**
     * @see #getName()
     */
    void setName(String value);

    //----------------------------------
    // presetBank
    //----------------------------------

    /**
     * The preset bank that the preset was loaded from.
     * <p>
     * This could be DEFAULT, USER, foobar, whatever the applications
     * implementation of a preset bank is.
     * </p>
     */
    String getPresetBank();

    /**
     * @see #getPresetBank()
     */
    void setPresetBank(String value);

}
