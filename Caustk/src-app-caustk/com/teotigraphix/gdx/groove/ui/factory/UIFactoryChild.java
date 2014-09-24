
package com.teotigraphix.gdx.groove.ui.factory;

import com.google.inject.Inject;
import com.teotigraphix.gdx.groove.ui.model.IUIModel;

public class UIFactoryChild {

    @Inject
    private IUIModel uiModel;

    @SuppressWarnings("unchecked")
    protected <T extends IUIModel> T getModel() {
        return (T)uiModel;
    }

    public UIFactoryChild() {
    }

}
