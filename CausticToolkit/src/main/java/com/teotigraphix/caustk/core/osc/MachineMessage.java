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

/**
 * The {@link MachineMessage} holds all OSC messages associated with the
 * {@link IMachine} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class MachineMessage extends CausticMessage {

    /**
     * Message: <code>/caustic/[machine_index]/load_preset [preset_path]</code>
     * <p>
     * Loads a preset into the machine from an external file.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>preset_path</strong>: The absolute path to the machine's
     * preset file.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see IMachine#loadPreset(String)
     */
    public static final MachineMessage LOAD_PRESET = new MachineMessage(
            "/caustic/${0}/load_preset ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/save_preset [preset_name]</code>
     * <p>
     * Saves a preset from a machine to an external machine preset file.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>preset_name</strong>: The simple name of the new preset file,
     * without the file extension.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * </p>
     * 
     * @see IMachine#savePreset(String)
     */
    public static final MachineMessage SAVE_PRESET = new MachineMessage(
            "/caustic/${0}/save_preset ${1}");

    /**
     * Query: <code>/caustic/[machine_index]/preset</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>String</code>The name of the preset
     * loaded in the machine if any.
     * 
     * @see IMachine#getPresetName()
     */
    public static final MachineMessage QUERY_PRESET = new MachineMessage("/caustic/${0}/preset");

    MachineMessage(String message) {
        super(message);
    }
}
