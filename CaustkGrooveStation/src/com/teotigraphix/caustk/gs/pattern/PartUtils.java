
package com.teotigraphix.caustk.gs.pattern;

import com.teotigraphix.caustk.rack.tone.Tone;

public class PartUtils {

    public static void setBankPattern(Part part, int bank, int pattern) {
        part.getTone().getPatternSequencer().setSelectedBankPattern(bank, pattern);
    }

    public static Tone getTone(Part part) {
        return part.getTone();
    }

}
