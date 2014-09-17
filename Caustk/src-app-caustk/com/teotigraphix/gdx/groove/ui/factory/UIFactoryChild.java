
package com.teotigraphix.gdx.groove.ui.factory;

import com.google.inject.Inject;
import com.teotigraphix.gdx.groove.ui.model.UIModel;

public class UIFactoryChild {

    @Inject
    private UIModel uiModel;

    @SuppressWarnings("unchecked")
    protected <T extends UIModel> T getModel() {
        return (T)uiModel;
    }

    public UIFactoryChild() {
    }

}
