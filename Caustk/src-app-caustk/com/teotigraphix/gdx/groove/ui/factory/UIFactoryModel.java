
package com.teotigraphix.gdx.groove.ui.factory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.groove.ui.factory.TopBarFactory.TopBarFactoryInfo;

@Singleton
public class UIFactoryModel {

    @SuppressWarnings("unused")
    private UIFactory factory;

    private TopBarFactoryInfo topBarInfo;

    @Inject
    public void setFactory(UIFactory factory) {
        this.factory = factory;
    }

    public TopBarFactoryInfo getTopBarInfo() {
        return topBarInfo;
    }

    public void setTopBarInfo(TopBarFactoryInfo topBarInfo) {
        this.topBarInfo = topBarInfo;
    }

    public UIFactoryModel() {
    }
}
