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

package com.teotigraphix.caustk.core.osc;

import com.teotigraphix.caustk.utils.RuntimeUtils;

/**
 * The {@link RackMessage} holds all OSC messages associated with the
 * {@link IRack} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class RackMessage extends CausticMessage {

    /**
     * Message:
     * <code>/caustic/create [machine_type] [machine_name] [machine_index]?</code>
     * <p>
     * Creates a new machine in the rack. Currently only 6 machines are allowed
     * at once.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_type</strong>: The new machine type.</li>
     * <li><strong>machine_name</strong>: The new machine name (1..10) character
     * length.</li>
     * <li><strong>machine_index</strong>: The new machine's index within the
     * rack.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IRack#addMachine(String, MachineType)
     * @see IRack#addMachineAt(int, String, String)
     * @since 1.0
     */
    public static final RackMessage CREATE = new RackMessage("/caustic/create ${0} ${1} ${2}");

    /**
     * Message: <code>/caustic/remove [machine_index]</code>
     * <p>
     * Removes a machine in the rack at the specified index.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The index of the machine to remove,
     * currently (0..5).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IRack#removeMachineAt(int)
     * @since 1.0
     */
    public static final RackMessage REMOVE = new RackMessage("/caustic/remove ${0}");

    /**
     * Message: <code>/caustic/load_song [file_path]</code>
     * <p>
     * Loads a <strong>.caustic</strong> file into the core, removes last
     * session.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>file_path</strong>: The file's absolute path.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IRack#loadSong(String)
     * @since 1.0
     */
    public static final RackMessage LOAD_SONG = new RackMessage("/caustic/load_song ${0}");

    /**
     * Message: <code>/caustic/save_song [file_name]</code>
     * <p>
     * Saves a <strong>.caustic</strong> file into the
     * <code>/sdcard/caustic/songs</code> directory.
     * <p>
     * Note: The core will always save to the songs directory, if the location
     * needs to change it is up to the client to move the song file to the new
     * location.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>file_name</strong>: The file's name without the
     * <strong>.caustic</strong> extension.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IRack#saveSong(String)
     * @see RuntimeUtils#getCausticSongsDirectory()
     * @since 1.0
     */
    public static final RackMessage SAVE_SONG = new RackMessage("/caustic/save_song ${0}");

    /**
     * Query: <code>/caustic/machine_name [machine_index]</code>
     * <p>
     * Queries the core for the string machine name located at the specified
     * index.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index to retrieve the
     * String machine name for.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code> The machine name if it
     * exists, <code>null</code> if not.
     * 
     * @see IRack#queryMachineName(int)
     * @since 1.0
     */
    public static final RackMessage QUERY_MACHINE_NAME = new RackMessage(
            "/caustic/machine_name ${0}");

    /**
     * Query: <code>/caustic/machine_type [machine_index]</code>
     * <p>
     * Queries the core for the string machine type located at the specified
     * index.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index to retrieve the
     * String machine name for.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code> The machine type (
     * <code>bassline, beatbox, pcmsynth, subsynth</code>)
     * 
     * @see IRack#queryMachineType(int)
     * @since 1.0
     */
    public static final RackMessage QUERY_MACHINE_TYPE = new RackMessage(
            "/caustic/machine_type ${0}");

    /**
     * Message: <code>/caustic/blankrack</code>
     * <p>
     * Resets the entire rack state, removing all machines, effects and
     * reinitializing its state.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li>N/A</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @since 1.0
     */
    public static final RackMessage BLANKRACK = new RackMessage("/caustic/blankrack");

    RackMessage(String message) {
        super(message);
    }

}
