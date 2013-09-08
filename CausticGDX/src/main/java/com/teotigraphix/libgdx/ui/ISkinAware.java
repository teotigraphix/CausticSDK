
package com.teotigraphix.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public interface ISkinAware {
    Skin getSkin();

    void setSkin(Skin value);

    String getStyleName();

    void setStyleName(String value);
}
