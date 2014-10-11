
package com.teotigraphix.gdx.groove.ui.behavior;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.groove.ui.components.SceneViewChildData;

/**
 * A child of the Main scene's viewstack.
 */
public abstract class SceneViewChildBehavior extends CaustkBehavior {

    private SceneViewChildData data;

    public SceneViewChildData getData() {
        return data;
    }

    public void setData(SceneViewChildData data) {
        this.data = data;
    }

    public SceneViewChildBehavior() {
    }

    public abstract Table create();

}
