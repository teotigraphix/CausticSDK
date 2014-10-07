
package com.teotigraphix.caustk.groove.session;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class SceneInfo {

    //--------------------------------------------------------------------------
    // Serialized :: Variables
    //--------------------------------------------------------------------------

    @Tag(10)
    private String name;

    //--------------------------------------------------------------------------
    // Public API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // name
    //----------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * Serialized.
     */
    SceneInfo() {
    }

    public SceneInfo(String name) {
        this.name = name;
    }

}
