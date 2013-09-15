
package com.teotigraphix.caustk.gs.pattern;

public class PartUtils {

    public static void setBankPattern(Part part, int bank, int pattern) {
        part.getTone().getPatternSequencer().setSelectedBankPattern(bank, pattern);
    }

}
