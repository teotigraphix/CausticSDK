
package com.teotigraphix.caustk.machine;

import java.io.File;
import java.util.UUID;

public interface ICaustkComponent {

    /**
     * Returns the unique id of the component.
     * <p>
     * <strong>Assigned only at construction.</strong>
     */
    UUID getId();

    /**
     * Returns the display name of the component.
     */
    String getName();

    /**
     * Returns the relative path from the owning {@link CaustkLibrary}.
     * <p>
     * The path could be something like;
     * <code>machines/subsynth/Trance/FM Synth Setup.ctkmachine</code>
     * 
     * @see CaustkLibrary#getAbsoluteComponentLocation(ICaustkComponent)
     */
    File getFile();
}
