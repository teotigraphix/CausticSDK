
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
    private int viewIndex = 0;

    @Tag(1)
    private int prefsViewIndex = 0;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // viewIndex
    //----------------------------------

    /**
     * The view stack index of the main ViewStack.
     */
    public int getViewIndex() {
        return viewIndex;
    }

    public void setViewIndex(int viewIndex) {
        this.viewIndex = viewIndex;
    }

    //----------------------------------
    // prefsViewIndex
    //----------------------------------

    /**
     * The view stack index of the Prefs/ViewStack
     */
    public int getPrefsViewIndex() {
        return prefsViewIndex;
    }

    public void setPrefsViewIndex(int prefsViewIndex) {
        this.prefsViewIndex = prefsViewIndex;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public UIModelState() {
    }

}
