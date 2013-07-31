
package com.teotigraphix.caustk.pattern;

import com.teotigraphix.caustk.pattern.Phrase.Trigger;

public class PhraseUtils {

    private static final float STEP_FRACTION = 0.0625f;

    private static final float MIN_STEP = STEP_FRACTION;

    private static final float MAX_STEP = 4.0f;

    private static final float NUM_BEATS = 4.0f;

    public static String toStepDecimalString(float stepFraction) {
        return Float.toString(NUM_BEATS * stepFraction);
    }

    public static float incrementGate(Phrase phrase, Trigger trigger) {
        //        float beat = Resolution.toBeat(trigger.getIndex(), phrase
        //                .getResolution());
        float gate = trigger.getGate();
        gate += STEP_FRACTION;
        gate = Math.max(Math.min(gate, MAX_STEP), MIN_STEP);
        phrase.triggerUpdateGate(trigger.getStep(), gate);
        return trigger.getGate();
    }

    public static float decrementGate(Phrase phrase, Trigger trigger) {
        float gate = trigger.getGate();
        gate -= 0.0625f;
        gate = Math.max(Math.min(gate, MAX_STEP), MIN_STEP);
        phrase.triggerUpdateGate(trigger.getStep(), gate);
        return trigger.getGate();
    }

}
