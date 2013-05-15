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

package com.teotigraphix.caustic.osc;

/**
 * The {@link OutputPanelMessage} holds all OSC messages associated with the
 * {@link IOutputPanel} API.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public class OutputPanelMessage extends CausticMessage {

    /**
     * Message: <code>/caustic/outputpanel/bpm [value]</code>
     * <p>
     * <strong>Default</strong>: <code>120.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (60.0..250.0) The new beats per minute value.
     * </li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see IOutputPanel#getBPM()
     * @see IOutputPanel#setBPM(float)
     */
    public static final OutputPanelMessage BPM = new OutputPanelMessage(
            "/caustic/outputpanel/bpm ${0}");

    /**
     * Message: <code>/caustic/outputpanel/mode [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1) 0 (<code>PATTERN</code>), 1 (
     * <code>SONG</code>) mode.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IOutputPanel#getMode()
     * @see IOutputPanel#setMode(OutputPanelMode)
     */
    public static final OutputPanelMessage MODE = new OutputPanelMessage(
            "/caustic/outputpanel/mode ${0}");

    /**
     * Message: <code>/caustic/outputpanel/play [value]</code>
     * <p>
     * <strong>Default</strong>: <code>N/A</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>value</strong>: (0,1) 0 <code>play</code>, 1
     * <code>stop</code>.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see IOutputPanel#play()
     * @see IOutputPanel#stop()
     */
    public static final OutputPanelMessage PLAY = new OutputPanelMessage(
            "/caustic/outputpanel/play ${0}");

    OutputPanelMessage(String message) {
        super(message);
    }
}
