
package com.teotigraphix.caustk.core.components.bassline;

import com.teotigraphix.caustk.core.components.LFOComponentBase;
import com.teotigraphix.caustk.core.osc.BasslineLFOMessage;

public class LFO1Component extends LFOComponentBase {

    //--------------------------------------------------------------------------
    // API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // phase
    //----------------------------------

    private float phase = 0f;

    public float getPhase() {
        return phase;
    }

    float getPhase(boolean restore) {
        return BasslineLFOMessage.LFO_PHASE.query(getEngine(), getToneIndex());
    }

    public void setPhase(float value) {
        if (value == phase)
            return;
        if (value < 0f || value > 1f)
            throw newRangeException(BasslineLFOMessage.LFO_PHASE.toString(), "0..1", value);
        phase = value;
        BasslineLFOMessage.LFO_PHASE.send(getEngine(), getToneIndex(), phase);
    }

    //----------------------------------
    // target
    //----------------------------------

    private LFOTarget target = LFOTarget.OFF;

    public LFOTarget getTarget() {
        return target;
    }

    LFOTarget getTarget(boolean restore) {
        return LFOTarget.toType(BasslineLFOMessage.LFO_TARGET.query(getEngine(), getToneIndex()));
    }

    public void setTarget(LFOTarget value) {
        if (value == target)
            return;
        target = value;
        BasslineLFOMessage.LFO_TARGET.send(getEngine(), getToneIndex(), target.getValue());
    }

    public LFO1Component() {
        // XXX this might not work here
        depthMessage = BasslineLFOMessage.LFO_DEPTH;
        rateMessage = BasslineLFOMessage.LFO_RATE;
        rate = 0;
    }

    @Override
    public void restore() {
        super.restore();
        setPhase(getPhase(true));
        setTarget(getTarget(true));
    }

    /**
     * The {@link IBasslineLFO1#getTarget()} LFO value.
     * 
     * @author Michael Schmalle
     * @copyright Teoti Graphix, LLC
     * @since 1.0
     */
    public enum LFOTarget {

        /**
         * No lfo on the bassline oscillator.
         */
        OFF(0),

        /**
         * Pulse Width Modulation.
         */
        PWM(1),

        /**
         * Voltage Control Frequency.
         */
        VCF(2),

        /**
         * Volume Controled Frequency.
         */
        VCA(3);

        private final int value;

        LFOTarget(int type) {
            value = type;
        }

        /**
         * Returns the int value for the lfo.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns a {@link LFOTarget} based off the passed integer type.
         * 
         * @param type The int type.
         */
        public static LFOTarget toType(Integer type) {
            for (LFOTarget result : values()) {
                if (result.getValue() == type)
                    return result;
            }
            return null;
        }

        /**
         * @see LFOTarget#toType(Integer)
         */
        public static LFOTarget toType(Float type) {
            return toType(type.intValue());
        }
    }
}
