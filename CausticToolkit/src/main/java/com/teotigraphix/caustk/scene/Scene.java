
package com.teotigraphix.caustk.scene;

import com.teotigraphix.caustk.controller.ICaustkController;

public class Scene {

    private ICaustkController controller;

    private SceneModel sceneModel;

    public SceneModel getSceneModel() {
        return sceneModel;
    }

    public void setSceneModel(SceneModel value) {
        sceneModel = value;
    }

    public Scene(ICaustkController controller) {
        this.controller = controller;
    }

    public void load() {
        // Load the Scene from the SceneModel
        
    }

}
