package com.teotigraphix.libgdx.dialog;

import com.teotigraphix.libgdx.screen.IScreen;

public interface IDialogManager {

    IScreen getScreen();

    void setScreen(IScreen screen);

    void create();

}
