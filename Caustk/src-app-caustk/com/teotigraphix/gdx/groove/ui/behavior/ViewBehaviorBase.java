
package com.teotigraphix.gdx.groove.ui.behavior;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.groove.ui.components.ViewStackData;

public abstract class ViewBehaviorBase extends CaustkBehavior {

    private ViewStackData data;

    public ViewStackData getData() {
        return data;
    }

    public void setData(ViewStackData data) {
        this.data = data;
    }

    public ViewBehaviorBase() {
    }

    public abstract Table create();

}
