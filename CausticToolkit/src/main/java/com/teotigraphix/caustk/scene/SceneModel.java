
package com.teotigraphix.caustk.scene;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustic.internal.rack.Rack;

public class SceneModel {

    private List<ScenePatch> patches = new ArrayList<ScenePatch>();

    private List<ScenePattern> patterns = new ArrayList<ScenePattern>();

    public SceneModel() {
    }

    void load(Rack loader) {
        // Load the model from the rack loader creating;
        // - ToneDescriptors
        // - ScenePatch
        // - ScenePattern

    }

}
