
package com.teotigraphix.libgdx.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface IValueAware {

    Actor getActor();

    float getValue();

    float getOriginalValue();

    void setOriginalValue(float value);

    void resetValue();
}
