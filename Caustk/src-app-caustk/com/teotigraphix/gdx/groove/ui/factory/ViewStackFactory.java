
package com.teotigraphix.gdx.groove.ui.factory;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.gdx.groove.ui.components.ViewStack;

public abstract class ViewStackFactory extends UIFactoryChild {

    public ViewStackFactory() {
    }

    public abstract ViewStack createViewStack(Skin skin);

}
