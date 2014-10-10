
package com.teotigraphix.gdx.groove.ui.behavior;

import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.groove.ui.components.ScenePane;
import com.teotigraphix.gdx.groove.ui.components.SceneSelectionListener;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;

public abstract class ScenePaneBehavior extends CaustkBehavior {

    private ScenePane view;

    public ScenePane getView() {
        return view;
    }

    public ScenePaneBehavior() {
    }

    public ScenePane create() {
        view = new ScenePane(getSkin());
        view.addListener(new SceneSelectionListener() {
            @Override
            public void bankChange(SceneSelectionEvent event, int index) {
                onBankChange(index);
            }

            @Override
            public void matrixChange(SceneSelectionEvent event, int index) {
                onMatrixChange(index);
            }
        });
        view.create(StylesDefault.ScenePane);
        return view;
    }

    protected abstract void onMatrixChange(int index);

    protected abstract void onBankChange(int index);

    public void disable(boolean disabled) {
        view.disable(disabled);
    }
}
