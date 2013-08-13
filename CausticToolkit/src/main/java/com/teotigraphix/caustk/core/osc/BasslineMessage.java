
package com.teotigraphix.caustk.core.osc;

import com.teotigraphix.caustk.tone.components.bassline.DistortionComponent;
import com.teotigraphix.caustk.tone.components.bassline.LFO1Component;
import com.teotigraphix.caustk.tone.components.bassline.OSC1Component;
import com.teotigraphix.caustk.tone.components.bassline.DistortionComponent.Program;

public class BasslineMessage extends CausticMessage {

    /**
     * Message: <code>/caustic/[machine_index]/distortion_amount [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (1.0..20.0).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see DistortionComponent#getAmount()
     * @see DistortionComponent#setAmount(float)
     */
    public static final BasslineMessage DISTORTION_AMOUNT = new BasslineMessage(
            "/caustic/${0}/distortion_amount ${1}");

    /**
     * Message:
     * <code>/caustic/[machine_index]/distortion_postgain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see DistortionComponent#getPostGain()
     * @see DistortionComponent#setPostGain(float)
     */
    public static final BasslineMessage DISTORTION_POSTGAIN = new BasslineMessage(
            "/caustic/${0}/distortion_postgain ${1}");

    /**
     * Message: <code>/caustic/[machine_name]/distortion_pregain [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..5.0).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see DistortionComponent#getPreGain()
     * @see DistortionComponent#setPreGain(float)
     */
    public static final BasslineMessage DISTORTION_PREGAIN = new BasslineMessage(
            "/caustic/${0}/distortion_pregain ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/distortion_program [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..4).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see DistortionComponent#getProgram()
     * @see DistortionComponent#setProgram(Program)
     */
    public static final BasslineMessage DISTORTION_PROGRAM = new BasslineMessage(
            "/caustic/${0}/distortion_program ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/waveform [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0,1) SAW(<code>0</code>), SQUARE(
     * <code>1</code>).</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see OSC1Component#getWaveform()
     * @see OSC1Component#setWaveForm(Waveform)
     */
    public static final BasslineMessage WAVEFORM = new BasslineMessage(
            "/caustic/${0}/waveform ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/accent [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     * 
     * @see OSC1Component#getAccent()
     * @see OSC1Component#setAccent(float)
     */
    public static final BasslineMessage ACCENT = new BasslineMessage("/caustic/${0}/accent ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/tune [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (-12..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * </p>
     * 
     * @see OSC1Component#getTune()
     * @see OSC1Component#setTune(int)
     */
    public static final BasslineMessage TUNE = new BasslineMessage("/caustic/${0}/tune ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/pulse_width [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.5</code>
     * </p>
     * <p>
     * <strong>Parameters</strong>:
     * </p>
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.05..0.5)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * </p>
     * 
     * @see OSC1Component#getPulseWidth()
     * @see OSC1Component#setPulseWidth(float)
     */
    public static final BasslineMessage PULSE_WIDTH = new BasslineMessage(
            "/caustic/${0}/pulse_width ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_depth [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0.0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see LFO1Component#getDepth()
     * @see LFO1Component#setDepth(float)
     */
    public static final BasslineMessage LFO_DEPTH = new BasslineMessage(
            "/caustic/${0}/lfo_depth ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_rate [value]</code>
     * <p>
     * <strong>Default</strong>: <code>1</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (1..12)</li>
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see LFO1Component#getRate()
     * @see LFO1Component#setRate(int)
     */
    public static final BasslineMessage LFO_RATE = new BasslineMessage(
            "/caustic/${0}/lfo_rate ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_phase [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0.0..1.0)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>float</code>
     * 
     * @see LFO1Component#getPhase()
     * @see LFO1Component#setPhase(float)
     */
    public static final BasslineMessage LFO_PHASE = new BasslineMessage(
            "/caustic/${0}/lfo_phase ${1}");

    /**
     * Message: <code>/caustic/[machine_index]/lfo_target [value]</code>
     * <p>
     * <strong>Default</strong>: <code>0</code>
     * <p>
     * <strong>Parameters</strong>:
     * <ul>
     * <li><strong>machine_index</strong>: The machine index.</li>
     * <li><strong>value</strong>: (0..3)
     * </ul>
     * <p>
     * <strong>Returns</strong>: <code>int</code>
     * 
     * @see LFO1Component#getTarget()
     * @see LFO1Component#setTarget(LFO1Component.LFOTarget))
     */
    public static final BasslineMessage LFO_TARGET = new BasslineMessage(
            "/caustic/${0}/lfo_target ${1}");

    public BasslineMessage(String message) {
        super(message);
    }

}
