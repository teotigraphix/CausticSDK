
package com.teotigraphix.caustk.core.components;

import com.teotigraphix.caustk.core.ExceptionUtils;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.core.IRestore;
import com.teotigraphix.caustk.tone.Tone;

public abstract class ToneComponent implements IRestore {

    //----------------------------------
    // tone
    //----------------------------------

    private transient Tone tone;

    public Tone getTone() {
        return tone;
    }

    public void setTone(Tone value) {
        tone = value;
    }

    protected final int getToneIndex() {
        return tone.getIndex();
    }

    protected final ICausticEngine getEngine() {
        return tone.getEngine();
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ToneComponent() {
    }

    //--------------------------------------------------------------------------
    // ISerialize API :: Methods
    //--------------------------------------------------------------------------

    public String serialize() {
        return tone.getController().getSerializeService().toString(this);
    }

    /**
     * Returns a new {@link IllegalArgumentException} for an error in OSC range.
     * 
     * @param control The OSC control involved.
     * @param range The accepted range.
     * @param value The value that is throwing the range exception.
     * @return A new {@link IllegalArgumentException}.
     */
    protected final RuntimeException newRangeException(String control, String range, Object value) {
        return ExceptionUtils.newRangeException(control, range, value);
    }
}
