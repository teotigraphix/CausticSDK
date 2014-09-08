
package com.teotigraphix.gdx.app;

import com.teotigraphix.gdx.app.IApplicationComponent;

public interface IApplicationStates extends IApplicationComponent {

    void loadLastProjectState();

    /**
     * Called when Scene has been created and user interface behaviors can
     * listen to model changes to get the current view state.
     */
    void startUI();

    void restartUI();

}
