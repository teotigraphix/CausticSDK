////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.rack;

import java.io.File;

import com.teotigraphix.caustic.machine.MachineType;
import com.teotigraphix.common.IDispose;

/**
 * The {@link IRackSong} API defines a single song instance that is created and
 * used during an {@link IRack} session.
 * <p>
 * The {@link IRack} will not create a default song instance during a call to
 * {@link IRack#addMachine(String, MachineType)}.
 * <p>
 * Using the {@link IRack#loadSong(String)} will create a new {@link IRackSong}
 * instance.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IRackSong extends IDispose {

    //--------------------------------------------------------------------------
    //
    // Properties
    //
    //--------------------------------------------------------------------------

    //----------------------------------
    // name
    //----------------------------------

    /**
     * Returns the display name of the song.
     */
    String getName();

    /**
     * Sets the display name of the song.
     * 
     * @param value The new display name.
     */
    void setName(String value);

    //----------------------------------
    // path
    //----------------------------------

    /**
     * Returns the absolute path to the .caustic song file.
     */
    String getPath();

    /**
     * Sets the absolute path to the .caustic song file, this File may not yet
     * exists.
     * 
     * @param value The absolute song file path.
     */
    void setPath(String value);

    /**
     * The song file instance if the path is not <code>null</code>.
     */
    File getFile();

    //--------------------------------------------------------------------------
    //
    // Methods
    //
    //--------------------------------------------------------------------------

    void create(IRack rack);

    /**
     * Called when the song is loaded from the {@link IRack#loadSong(String)}.
     * <p>
     * Loaded from the {@link #getFile()} location.
     */
    void load();

    /**
     * Called when the song is saved from the {@link IRack#saveSong(String)}.
     * 
     * @param name The name of the .caustic file in the /songs directory.
     */
    void save(String name);
}
