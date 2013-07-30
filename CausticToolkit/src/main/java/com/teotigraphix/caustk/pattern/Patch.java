
package com.teotigraphix.caustk.pattern;

import com.teotigraphix.caustk.library.LibraryPatch;

/**
 * The Patch holds all sound settings that will get serialized to disk and
 * reloaded into the the Tone.
 * <p>
 * Data;
 * <ul>
 * <li>Mixer</li>
 * <li>Mixer</li>
 * <li>Mixer</li>
 * </ul>
 */
public class Patch {
    //--------------------------------------------------------------------------
    // Public Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // patchItem
    //----------------------------------

    private LibraryPatch patchItem;

    public LibraryPatch getPatchItem() {
        return patchItem;
    }

    //----------------------------------
    // part
    //----------------------------------

    private Part part;

    public Part getPart() {
        return part;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public Patch(Part part, LibraryPatch patchItem) {
        this.part = part;
        this.patchItem = patchItem;
        part.setPatch(this);
    }

    public void configure() {
    }

    public void commit() {
    }
}
