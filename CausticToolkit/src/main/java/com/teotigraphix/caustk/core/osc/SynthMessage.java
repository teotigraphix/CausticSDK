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

import com.teotigraphix.caustk.core.components.SynthComponent;

/**
 * The {@link SynthMessage} holds all OSC messages associated with the
 * {@link ISynth}, {@link ISynth#getSynth()} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class SynthMessage extends CausticMessage {

    /**
     * Message: <code>/caustic/[machine_index]/polyphony [value]</code>
     * <p>
     * The amount of notes played simultaneously in the machine's synth.
     * <p>
     * <strong>Default</strong>: <code>4</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>polyphony</strong>: (1..8) The number of notes played at
     * once.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see SynthComponent#setPolyphony(int)
     * @see SynthComponent#getPolyphony()
     */
    public static final SynthMessage POLYPHONY = new SynthMessage("/caustic/${0}/polyphony ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/note/[pitch] [mode]</code>
     * <p>
     * Triggers a MIDI note on or off in the machine's synth.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>pitch</strong>: The MIDI pitch value.</li>
     * <li><strong>mode</strong>: The play mode (0)off, (1)on.</li>
     * <li><strong>velcoty</strong>: The note velocity (0..1), default 1.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see SynthComponent#noteOn(int)
     * @see SynthComponent#noteOff(int)
     */
    public static final SynthMessage NOTE = new SynthMessage("/caustic/${0}/note/${1} ${2} ${3}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/note/preview [pitch] [oneshot]</code>
     * <p>
     * Previews a sample in the {@link IBeatbox} and {@link IPCMSynth} not
     * available in other sysnths.
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>pitch</strong>: The MIDI pitch value.</li>
     * <li><strong>oneshot</strong>: (0)oneshot, (1)loop.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>N/A</code>
     * 
     * @see SynthComponent#notePreview(int, boolean)
     */
    public static final SynthMessage NOTE_PREVIEW = new SynthMessage(
            "/caustic/${0}/note/preview ${1} ${2}");

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
    public static final SynthMessage LOAD_PRESET = new SynthMessage(
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
    public static final SynthMessage SAVE_PRESET = new SynthMessage(
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
    public static final SynthMessage QUERY_PRESET = new SynthMessage("/caustic/${0}/preset");

    SynthMessage(String message) {
        super(message);
    }
}
