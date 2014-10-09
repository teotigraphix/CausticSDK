
package com.teotigraphix.gdx.groove.ui.behavior;

import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.groove.ui.components.ScenePane;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;

public class ScenePaneBehavior extends CaustkBehavior {

    private ScenePane view;

    public ScenePaneBehavior() {
    }

    public ScenePane create() {
        view = new ScenePane(getSkin());
        view.create(StylesDefault.ScenePane);
        return view;
    }
}
