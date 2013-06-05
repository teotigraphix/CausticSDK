
package com.teotigraphix.caustk.scene;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.caustk.system.bank.MemoryDescriptor;
import com.teotigraphix.caustk.tone.ToneDescriptor;

public class SceneModel {

    private List<ToneDescriptor> descriptors = new ArrayList<ToneDescriptor>();

    private List<ScenePatch> patches = new ArrayList<ScenePatch>();

    private List<ScenePattern> patterns = new ArrayList<ScenePattern>();

    public SceneModel() {
    }

    void load(MemoryDescriptor descriptor) {
        // Load the model from the rack loader creating;
        // - ToneDescriptors
        createToneDescriptors(descriptor);
        // - ScenePatch
        createPatches(descriptor);
        // - ScenePattern
        createPatterns(descriptor);

    }

    private void createToneDescriptors(MemoryDescriptor descriptor) {

    }

    private void createPatches(MemoryDescriptor descriptor) {
        // TODO Auto-generated method stub

    }

    private void createPatterns(MemoryDescriptor descriptor) {
        // TODO Auto-generated method stub

    }

}
