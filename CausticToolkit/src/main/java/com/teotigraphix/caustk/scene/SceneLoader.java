
package com.teotigraphix.caustk.scene;

import java.io.File;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.rack.Rack;
import com.teotigraphix.caustic.machine.IMachine;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustk.controller.ICaustkController;
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

    private Rack loader;

    private ICaustkController controller;

    public SceneLoader(ICaustkController controller) {
        this.controller = controller;
    }

    public void load(File file) {
        if (file.getName().endsWith(".caustic"))
            loadCausticFile(file);
    }

    private void loadCausticFile(File file) {
        // creates a full rack with outputpanel, mixerpanel, effectsrack and sequencer
        loader = new Rack(controller.getConfiguration().getDeviceFactory(controller), true);
        loader.setEngine(controller);
        
        // restore the rack loader with the .caustic file
        restoreLoader(loader, file);

        // create a new SceneModel and load it with the rack loader
        SceneModel sceneModel = createSceneModel();
        sceneModel.load(loader);

        // create a new Scene and set its sceneModel
        Scene scene = createScene();
        scene.setSceneModel(sceneModel);

        // clear the core sound generator (CausticEngine)
        controller.getSoundGenerator().sendMessage("/caustc/blankrack");

        loader = null;

        // load the new Scene using the newly created SceneModel
        loadScene(scene);
    }

    private void restoreLoader(Rack loader2, File file) {

        try {
            loader.loadSong(file.getAbsolutePath());
        } catch (CausticException e) {
            e.printStackTrace();
        }

        loader.restore();
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
