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

package com.teotigraphix.caustic.sequencer;

import com.teotigraphix.caustic.sequencer.IPatternSequencer;

/**
 * The number of measures allowed in an {@link IPatternSequencer}.
 * <p>
 * Currently the CausticCore supports 1, 2, 4 and 8 measure patterns.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public enum PatternMeasures
{

    /**
     * A 1 measure pattern.
     */
    ONE(1),

    /**
     * A 2 measure pattern.
     */
    TWO(2),

    /**
     * A 4 measure pattern.
     */
    FOUR(4),

    /**
     * A 8 measure pattern.
     */
    EIGHT(8);

    private final int mValue;

    PatternMeasures(int value)
    {
        mValue = value;
    }

    /**
     * Returns the integer value of the measure.
     */
    public int getValue()
    {
        return mValue;
    }

    /**
     * Returns a boolean indicating whether the integer value is a valid measure
     * increment in a pattern.
     * 
     * @param value The number of measures to test.
     */
    public static boolean isValid(int value)
    {
        for (PatternMeasures item : values())
        {
            if (item.getValue() == value)
                return true;
        }
        return false;
    }

    /**
     * Returns a {@link PatternMeasures} value based on an integer passed.
     * 
     * @param value The number of measures.
     */
    public static PatternMeasures toValue(int value)
    {
        switch (value)
        {
        case 1:
            return ONE;
        case 2:
            return TWO;
        case 4:
            return FOUR;
        case 8:
            return EIGHT;

        default:
            return ONE;
        }
    }
}
