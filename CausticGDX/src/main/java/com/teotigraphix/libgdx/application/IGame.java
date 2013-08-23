
package com.teotigraphix.libgdx.application;

import com.badlogic.gdx.ApplicationListener;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.libgdx.screen.IScreen;

public interface IGame extends ApplicationListener {

    ICaustkController getController();

    void setScreen(IScreen screen);

    IScreen getScreen();

}
