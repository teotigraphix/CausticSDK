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

package com.teotigraphix.caustk.core.osc;

import com.teotigraphix.caustk.tone.OrganTone;

/**
 * The {@link OrganMessage} holds all OSC messages associated with the
 * {@link OrganTone} API.
 * 
 * @author Michael Schmalle
 * @since 3.0
 */
public class OrganMessage extends CausticMessage {

    /**
     * Message:
     * <code>/caustic/[machine_index]/drawbar/[index] [amplitude]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>index</strong>: (0..8)</li>
     * <li><strong>amplitude</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final OrganMessage DRAWBAR = new OrganMessage("/caustic/${0}/drawbar/${1} ${2}");

    /**
     * Message: <code>/caustic/[machine_index]/perc_tone [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final OrganMessage PERC_TONE = new OrganMessage("/caustic/${0}/perc_tone ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/perc_decay [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..0.5)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final OrganMessage PERC_DECAY = new OrganMessage("/caustic/${0}/perc_decay ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/leslie_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..0.7)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final OrganMessage LESLIE_RATE = new OrganMessage(
            "/caustic/${0}/leslie_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/leslie_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final OrganMessage LESLIE_DEPTH = new OrganMessage(
            "/caustic/${0}/leslie_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/drive [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final OrganMessage DRIVE = new OrganMessage("/caustic/${0}/drive ${1}");

    OrganMessage(String message) {
        super(message);
    }

}
