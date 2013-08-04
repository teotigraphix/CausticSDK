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
