
package com.teotigraphix.gdx.groove.ui.behavior;

import com.teotigraphix.gdx.app.CaustkBehavior;
import com.teotigraphix.gdx.groove.ui.components.SoundPane;
import com.teotigraphix.gdx.groove.ui.factory.StylesDefault;

public class SoundPaneBehavior extends CaustkBehavior {

    private SoundPane view;

    public SoundPaneBehavior() {
        // TODO Auto-generated constructor stub
    }

    public SoundPane create() {
        view = new SoundPane(getSkin());
        view.create(StylesDefault.SoundPane);
        return view;
    }
}
