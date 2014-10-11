
package com.teotigraphix.gdx.groove.ui.model;

import java.util.Collection;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.app.IApplicationComponent;
import com.teotigraphix.gdx.controller.ViewBase;
import com.teotigraphix.gdx.groove.ui.components.SceneViewChildData;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

public interface IUIModel extends IApplicationComponent {

    //----------------------------------
    // SceneViews
    //----------------------------------

    Array<ButtonBarItem> getSceneButtons();

    void setSceneButtons(Array<ButtonBarItem> buttons);

    Array<SceneViewChildData> getSceneViews();

    void setSceneViews(Array<SceneViewChildData> views);

    int getSceneViewIndex();

    void setSceneViewIndex(int viewIndex);

    //----------------------------------
    // Views
    //----------------------------------

    Collection<ViewBase> getViews();

    ViewBase getViewByIndex(int viewIndex);

    ViewBase getSelectedView();

    void setSelectedViewId(int viewid);

    int getViewIndex();

    void setViewIndex(int viewIndex);

    Array<ButtonBarItem> getViewButtons();

    void setViewButtons(Array<ButtonBarItem> buttons);

    void restore(UIState state);

}
