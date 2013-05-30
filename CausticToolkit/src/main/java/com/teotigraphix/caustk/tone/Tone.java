
package com.teotigraphix.caustk.tone;

import com.teotigraphix.caustic.machine.IMachine;

/**
 * The Tone wraps an {@link IMachine}.
 * <p>
 * Tones are basically the ONLY static instances that are not lightweight in the
 * framework. Unlike Part, Patch, Pattern etc, the Tone is a singleton and only
 * created once at startup(this will change if unlimited machines exist).
 */
public abstract class Tone {
    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // machine
    //----------------------------------

    public abstract IMachine getMachine();

    //----------------------------------
    // index
    //----------------------------------

    public int getIndex() {
        return getMachine().getIndex();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Tone() {
    }
}
