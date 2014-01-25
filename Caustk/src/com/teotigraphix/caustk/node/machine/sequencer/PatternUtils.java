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

package com.teotigraphix.caustk.node.machine.sequencer;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.core.CaustkRack;
import com.teotigraphix.caustk.core.osc.PatternSequencerMessage;
import com.teotigraphix.caustk.core.osc.SequencerMessage;

/**
 * @author Michael Schmalle
 * @since 1.0
 */
public final class PatternUtils {

    private static final String[] chars = {
            "A", "B", "C", "D"
    };

    public static final String toPattern(int index) {
        index++;
        // Caustic does not use a leading zero such as 'A01'
        //        if (index < 10)
        //            return "0" + Integer.toString(index);
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

    public static final String toBank(int index) {
        return chars[index];
    }

    public static final String toString(int bank, int index) {
        return toBank(bank) + toPattern(index);
    }

    //    public static String toString(Phrase phrase) {
    //        return toString(phrase.getBankIndex(), phrase.getPatternIndex());
    //    }

    public static String toString(int index) {
        return toString(getBank(index), getPattern(index));
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

    public static float getNumLoops(int numPatternMeasures, int numMeasuresSpanned) {
        int measureSpan = numMeasuresSpanned;
        float remainderMeasures = measureSpan % numPatternMeasures;
        int numLoops = ((int)(measureSpan - remainderMeasures) / numPatternMeasures);
        float fraction = remainderMeasures / numPatternMeasures;
        return numLoops + fraction;
    }

    public static int getNumMeasures(CaustkRack rack, int machineIndex, int bank, int pattern) {
        int originalBank = (int)PatternSequencerMessage.BANK.query(rack, machineIndex);
        int originalPattern = (int)PatternSequencerMessage.PATTERN.query(rack, machineIndex);
        PatternSequencerMessage.BANK.send(rack, machineIndex, bank);
        PatternSequencerMessage.PATTERN.send(rack, machineIndex, pattern);
        int numMeasures = (int)PatternSequencerMessage.NUM_MEASURES.query(rack, machineIndex);
        PatternSequencerMessage.BANK.send(rack, machineIndex, originalBank);
        PatternSequencerMessage.PATTERN.send(rack, machineIndex, originalPattern);
        return numMeasures;
    }

    public static void setNumMeasures(CaustkRack rack, int machineIndex, int bank, int pattern,
            int numMeasures) {
        int originalBank = (int)PatternSequencerMessage.BANK.query(rack, machineIndex);
        int originalPattern = (int)PatternSequencerMessage.PATTERN.query(rack, machineIndex);
        PatternSequencerMessage.BANK.send(rack, machineIndex, bank);
        PatternSequencerMessage.PATTERN.send(rack, machineIndex, pattern);
        PatternSequencerMessage.NUM_MEASURES.send(rack, machineIndex, numMeasures);
        PatternSequencerMessage.BANK.send(rack, machineIndex, originalBank);
        PatternSequencerMessage.PATTERN.send(rack, machineIndex, originalPattern);
    }

    public static List<String> getPatterns(CaustkRack rack, int machineIndex) {
        List<String> result = new ArrayList<String>();
        // [machin_index] [start_measure] [bank] [pattern] [end_measure]
        String patterns = SequencerMessage.QUERY_PATTERN_EVENT.queryString(rack);
        if (patterns != null) {
            for (String pattern : patterns.split("\\|")) {
                // XXX What the heck are the ||||| in the return value
                if (!pattern.equals("")) {
                    String[] split = pattern.split(" ");
                    int index = Integer.valueOf(split[0]);
                    if (index == machineIndex) {
                        result.add(pattern);
                    }
                }
            }
        }
        return result;
    }
}
