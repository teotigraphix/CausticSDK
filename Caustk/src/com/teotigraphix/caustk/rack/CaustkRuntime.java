
package com.teotigraphix.caustk.rack;

import com.teotigraphix.caustk.core.ISoundGenerator;

public class CaustkRuntime {

    private ISoundGenerator soundGenerator;

    private Rack rack;

    public final Rack getRack() {
        return rack;
    }

    public CaustkRuntime(ISoundGenerator soundGenerator) {
        this.soundGenerator = soundGenerator;
        initialize();
    }

    private void initialize() {
        rack = RackProvider.createRack(soundGenerator);
        RackProvider.setRack(rack);
    }
}
