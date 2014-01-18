
package com.teotigraphix.caustk.rack;

import com.teotigraphix.caustk.core.ISoundGenerator;

public class RackProvider {

    private static Rack rack;

    public static void setRack(Rack rack) {
        RackProvider.rack = rack;
    }

    public static Rack getRack() {
        return rack;
    }

    public static Rack createRack(ISoundGenerator soundGenerator) {
        return new Rack(soundGenerator);
    }

    public RackProvider() {
    }
}
