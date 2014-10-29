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

import java.math.BigDecimal;

import com.teotigraphix.caustk.core.ICaustkRack;
import com.teotigraphix.caustk.core.MachineType;

/**
 * Various OSC message utilities.
 * 
 * @author Michael Schmalle
 * @since 1.0
 */
public final class OSCUtils {

    public static final int PRECISION = 4;

    public static Float precision(Float d) {

        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(PRECISION, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private static final String[] REMOVABLE_CHARS = new String[] {
            " ", "e", "a", "u", "i", "o"
    };

    public static String toMachineName(ICaustkRack rack, int machineIndex) {
        String name = RackMessage.QUERY_MACHINE_NAME.queryString(rack, machineIndex);
        if (name != null && name.equals(""))
            name = null;
        return name;
    }

    public static MachineType toMachineType(ICaustkRack rack, int machineIndex) {
        String type = RackMessage.QUERY_MACHINE_TYPE.queryString(rack, machineIndex);
        return MachineType.fromString(type);
    }

    public static String optimizeName(String name, int length) {
        if (name == null)
            return "";

        for (int i = 0; i < REMOVABLE_CHARS.length; i++) {
            if (name.length() <= length)
                return name;
            int pos = -1;
            while ((pos = name.indexOf(REMOVABLE_CHARS[i])) != -1) {
                name = name.substring(0, pos) + name.substring(pos + 1, name.length());
                if (name.length() <= length)
                    return name;
            }
        }
        return name;
    }
}
