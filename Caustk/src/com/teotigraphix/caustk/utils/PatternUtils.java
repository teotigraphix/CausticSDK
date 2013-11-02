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

package com.teotigraphix.caustk.utils;

import com.teotigraphix.caustk.live.Phrase;

/**
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public final class PatternUtils {

    private static final String[] chars = {
            "A", "B", "C", "D"
    };

    public static final String toPattern(int index) {
        index++;
        if (index < 10)
            return "0" + Integer.toString(index);
        return Integer.toString(index);
    }

    public static final int toPattern(String patternName) {
        String end = patternName.substring(1);
        int pattern = Integer.valueOf(end);
        return pattern - 1;
    }

    public static final int toBank(String patternName) {
        for (int i = 0; i < chars.length; i++) {
            String bank = chars[i];
            if (patternName.indexOf(bank) == 0)
                return i;
        }
        return -1;
    }

    public static final String toBank(int bank) {
        return chars[bank];
    }

    public static final String toString(int bank, int index) {
        return toBank(bank) + toPattern(index);
    }

    public static String toString(Phrase phrase) {
        return toString(phrase.getBankIndex(), phrase.getPatternIndex());
    }

    public static int getPattern(int index) {
        return index % 16;
    }

    public static int getBank(int index) {
        float num = index / 16;
        return (int)num % 4;
    }

    public static int getIndex(int bankIndex, int patternIndex) {
        return (bankIndex * 16) + patternIndex;
    }

}
