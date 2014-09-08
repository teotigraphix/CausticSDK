////////////////////////////////////////////////////////////////////////////////
// Copyright 2014 Michael Schmalle - Teoti Graphix, LLC
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
 * The native OSC messages for the <strong>Vocoder</strong>.
 * 
 * @author Michael Schmalle
 * @since 1.0
 * @see VocoderMachine
 */
public class VocoderMessage extends CausticMessage {

    /**
     * Message:
     * <code>/caustic/[machine_index]/preview_modulator [mod_index]</code>
     * <p>
     * Preview modulator : the modulator used for live/preview playing via
     * "note".
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>mod_index</strong>: (0,1,2,3,4,5) the modulator used for
     * live/preview playing via "note"</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage PREVIEW_MODULATOR = new VocoderMessage(
            "/caustic/${0}/preview_modulator ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/clear_modulator [mod_index]</code>
     * <p>
     * Clear modulator : clears all sample data and resets this modulator slot.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>mod_index</strong>: (0,1,2,3,4,5)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage CLEAR_MODULATOR = new VocoderMessage(
            "/caustic/${0}/clear_modulator ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/modulator [mod_index] [machine_or_wav]</code>
     * <p>
     * Select between 6 different modulation sources. The selected modulator
     * will be the one to play when notes on the preview keyboard are played.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>mod_index</strong>: (0,1,2,3,4,5)</li>
     * <li><strong>machine_or_wav</strong>: If the param is a string it will
     * load it as WAV, if it's a number it will use the machine at that idx as
     * the mod. It's an error to reference itself or an empty slot.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>To query, be sure to use the
     * response string. a return value of -1 indicates it's using a WAV file and
     * the sample name will be copied to the response. If it's a machine, the
     * return value indicates the slot# and the response is not touched.
     */
    public static final VocoderMessage MODULATOR = new VocoderMessage(
            "/caustic/${0}/modulator ${1} ${2}");

    /**
     * Message: <code>/caustic/[machine_index]/ccontrol [eq_index] [gain]</code>
     * <p>
     * Character control EQ : Control the balance of frequencies in the
     * resulting vocoded carrier sound.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>eq_index</strong>: (0,1,2,3,4,5,6,7)</li>
     * <li><strong>gain</strong>: (-1.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage CCONTROL = new VocoderMessage(
            "/caustic/${0}/ccontrol ${1} ${2}");

    public static final VocoderMessage QUERY_CCONTROL = new VocoderMessage(
            "/caustic/${0}/ccontrol ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/carrier_source [carrier_index]</code>
     * <p>
     * Carrier selector: Select between the internal oscillator or any other
     * machine in your rack as the carrier signal to be vocoded.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>carrier_index</strong>: (-1,0..13) -1 uses internal, 0..13
     * for targeting machine slots. It's an error to reference itself or an
     * empty slot.</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final VocoderMessage CARRIER_SOURCE = new VocoderMessage(
            "/caustic/${0}/carrier_source ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/send_notes [value]</code>
     * <p>
     * Send notes to carrier button: When pressed, notes from the vocoder's
     * pattern editor will be sent to the external carrier (if selected in
     * carrier_source)
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1) false, true</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final VocoderMessage SND_NOTES = new VocoderMessage(
            "/caustic/${0}/send_notes ${1}");

    //----------------------------------
    // Controls
    //----------------------------------

    /**
     * Message: <code>/caustic/[machine_index]/waveform [value]</code>
     * <p>
     * Waveform selector: Select between sawtooth or squarewave as the waveform
     * for the internal carrier synth's oscillators.
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1) Saw, Square</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     */
    public static final VocoderMessage WAVEFORM = new VocoderMessage("/caustic/${0}/waveform ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/unison [value]</code>
     * <p>
     * Unison : Controls the +/- detuning between the twin oscillators in the
     * internal carrier synth.
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage UNISON = new VocoderMessage("/caustic/${0}/unison ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/sub [value]</code>
     * <p>
     * Sub : Controls the mix volume of the sub-oscillator (1 octave below note)
     * in the internal carrier synth.
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage SUB = new VocoderMessage("/caustic/${0}/sub ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/noise [value]</code>
     * <p>
     * Noise : Controls the mix volume of the white noise generator in the
     * internal carrier synth.
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage NOISE = new VocoderMessage("/caustic/${0}/noise ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/slew [value]</code>
     * <p>
     * Slew : Controls the maximum rate of change for the frequency response
     * (articulation) between modulator and carrier.
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..0.4)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage SLEW = new VocoderMessage("/caustic/${0}/slew ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/hf_bypass [value]</code>
     * <p>
     * High Frequency Bypass : Controls the volume of high frequency content in
     * the modulator that gets mixed into the final vocoded result.
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage HF_BYPASS = new VocoderMessage(
            "/caustic/${0}/hf_bypass ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/dry [value]</code>
     * <p>
     * Dry : Controls the volume of modulator that gets mixed into the final
     * vocoded result.
     * <p>
     * <strong>Default</strong>: <code>TODO</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     */
    public static final VocoderMessage DRY = new VocoderMessage("/caustic/${0}/dry ${1}");

    VocoderMessage(String message) {
        super(message);
    }

    public enum CarrierOscWaveform {

        Saw(0),

        Square(1);

        private int value;

        public int getValue() {
            return value;
        }

        private CarrierOscWaveform(int value) {
            this.value = value;
        }

        public static CarrierOscWaveform fromInt(int mode) {
            for (CarrierOscWaveform item : values()) {
                if (item.getValue() == mode)
                    return item;
            }
            return null;
        }
    }
}
