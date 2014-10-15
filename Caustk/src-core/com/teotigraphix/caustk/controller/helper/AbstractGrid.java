
package com.teotigraphix.caustk.controller.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public abstract class AbstractGrid {

    public AbstractGrid() {
    }

    public abstract void lightEx(int x, int y, Color color);

    public abstract void flush();

    public abstract void clear();

    public void setGrid(WidgetGroup grid) {
        // TODO Auto-generated method stub

    }

}
