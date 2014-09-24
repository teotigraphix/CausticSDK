
package com.teotigraphix.gdx.groove.ui.model;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.gdx.app.IApplicationComponent;
import com.teotigraphix.gdx.groove.ui.components.ViewStackData;
import com.teotigraphix.gdx.scene2d.ui.ButtonBar.ButtonBarItem;

public interface IUIModel extends IApplicationComponent {

    int getViewIndex();

    void setViewIndex(int viewIndex);

    int getPrefsViewIndex();

    void setPrefsViewIndex(int viewIndex);

    Array<ViewStackData> getViews();

    void setViews(Array<ViewStackData> views);

    Array<ButtonBarItem> getButtons();

    void setButtons(Array<ButtonBarItem> buttons);

    void restore(UIModelState state);

}
