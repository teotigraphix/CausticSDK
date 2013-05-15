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

package com.teotigraphix.caustic.osc;

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
     * @see ISynthComponent#setPolyphony(int)
     * @see ISynthComponent#getPolyphony()
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
     * @see ISynthComponent#noteOn(int)
     * @see ISynthComponent#noteOff(int)
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
     * @see ISynthComponent#notePreview(int, boolean)
     */
    public static final SynthMessage NOTE_PREVIEW = new SynthMessage(
            "/caustic/${0}/note/preview ${1} ${2}");

    SynthMessage(String message) {
        super(message);
    }
}
