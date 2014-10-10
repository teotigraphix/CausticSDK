
package com.teotigraphix.gdx.groove.ui.model;

import java.util.Collection;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.app.IApplicationComponent;
import com.teotigraphix.gdx.controller.ViewBase;
import com.teotigraphix.gdx.groove.ui.components.ViewStackData;
import com.teotigraphix.gdx.groove.ui.factory.UIFactory;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

public interface IUIModel extends IApplicationComponent {

    UIFactory getUIFactory();

    //----------------------------------
    // mainMode
    //----------------------------------

    Collection<ViewBase> getViews();

    ViewBase getSelectedView();

    void setSelectedViewId(int viewid);

    int getViewIndex();

    void setViewIndex(int viewIndex);

    int getPrefsViewIndex();

    void setPrefsViewIndex(int viewIndex);

    Array<ViewStackData> getSceneViews();

    void setSceneViews(Array<ViewStackData> views);

    Array<ButtonBarItem> getButtons();

    void setButtons(Array<ButtonBarItem> buttons);

    Array<ButtonBarItem> getViewButtons();

    void setViewButtons(Array<ButtonBarItem> mainModes);

    void restore(UIState state);

}
