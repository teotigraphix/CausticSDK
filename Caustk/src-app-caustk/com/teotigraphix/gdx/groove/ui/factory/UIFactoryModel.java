
package com.teotigraphix.gdx.groove.ui.factory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.gdx.groove.ui.factory.TopBarFactory.TopBarFactoryInfo;
import com.teotigraphix.gdx.groove.ui.factory.ViewStackFactory.ViewStackFactoryInfo;

@Singleton
public class UIFactoryModel {

    @SuppressWarnings("unused")
    private UIFactory factory;

    private TopBarFactoryInfo topBarInfo;

    private ViewStackFactoryInfo viewStackInfo;

    //--------------------------------------------------------------------------
    // Public :: Properites
    //--------------------------------------------------------------------------

    //----------------------------------
    // factory
    //----------------------------------

    @Inject
    public void setFactory(UIFactory factory) {
        this.factory = factory;
    }

    //----------------------------------
    // topBarInfo
    //----------------------------------

    public TopBarFactoryInfo getTopBarInfo() {
        return topBarInfo;
    }

    public void setTopBarInfo(TopBarFactoryInfo topBarInfo) {
        this.topBarInfo = topBarInfo;
    }

    //----------------------------------
    // viewStackInfo
    //----------------------------------

    public ViewStackFactoryInfo getViewStackInfo() {
        return viewStackInfo;
    }

    public void setViewStackInfo(ViewStackFactoryInfo viewStackInfo) {
        this.viewStackInfo = viewStackInfo;
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    public UIFactoryModel() {
    }
}
