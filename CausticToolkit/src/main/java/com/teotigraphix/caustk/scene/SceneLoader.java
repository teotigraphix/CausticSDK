
package com.teotigraphix.caustk.scene;

import java.io.File;

import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.system.bank.CausticFileMemoryDescriptor;
import com.teotigraphix.caustk.system.bank.MemoryLoader;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.caustk.tone.ToneDescriptor;

/**
 * The {@link SceneLoader} is the root manager that will load
 * <code>.caustic</code> files and create and initialize a {@link SceneModel}.
 * <p>
 * Loads a <code>.caustic</code> file.
 * <p>
 * Restores the {@link IRack} based on the loaded file.
 * <p>
 * Loops through all {@link IMachine}s and creates a {@link SceneModel} with
 * {@link ToneDescriptor}, {@link ScenePattern}, {@link ScenePatch}.
 * <p>
 * Passes the model to the {@link Scene}.
 * <p>
 * Clears the {@link IRack} of all loading data.
 * <p>
 * Calls load on the {@link Scene} which creates the {@link Tone}s using the
 * {@link ToneDescriptor}s and initializes the IMachines using the
 * {@link SceneModel}s {@link ScenePattern} and {@link ScenePatch} data.
 */
public class SceneLoader {

    private ICaustkController controller;

    private MemoryLoader memoryLoader;

    public SceneLoader(ICaustkController controller) {
        this.controller = controller;

        memoryLoader = new MemoryLoader(controller);
    }

    public void load(File file) {
        if (file.getName().endsWith(".caustic"))
            loadCausticFile(file);
    }

    private void loadCausticFile(File file) {

        CausticFileMemoryDescriptor descriptor = new CausticFileMemoryDescriptor("Foo", file);
        // passing null for the tone descriptors forces the loader to
        // create them when it first loads the song file
        memoryLoader.load(descriptor, null);

        // create a new SceneModel and load it with the rack loader
        SceneModel sceneModel = createSceneModel();
        sceneModel.load(descriptor);

        // create a new Scene and set its sceneModel
        Scene scene = createScene();
        scene.setSceneModel(sceneModel);

        // clear the core sound generator (CausticEngine)
        controller.getSoundGenerator().sendMessage("/caustc/blankrack");

        // load the new Scene using the newly created SceneModel
        loadScene(scene);
    }

    private SceneModel createSceneModel() {
        SceneModel sceneModel = new SceneModel();
        return sceneModel;
    }

    public Scene createScene() {
        Scene scene = new Scene(controller);
        return scene;
    }

    private void loadScene(Scene scene) {
        scene.load();
    }

}
