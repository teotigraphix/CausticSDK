
package com.teotigraphix.gdx.groove.ui.model;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.caustk.core.CaustkProject;

/**
 * The internal state for the {@link UIModel}.
 * <p>
 * Each {@link CaustkProject} owns an instance of this and will be serialized
 * with the project.
 */
public class UIModelState {

    //--------------------------------------------------------------------------
    // Serialization
    //--------------------------------------------------------------------------

    @Tag(0)
    private int viewIndex = -1;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // viewIndex
    //----------------------------------

    public int getViewIndex() {
        return viewIndex;
    }

    public void setViewIndex(int viewIndex) {
        this.viewIndex = viewIndex;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public UIModelState() {
    }

}
