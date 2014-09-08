
package com.teotigraphix.gdx.app;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IApplicationStates extends IApplicationComponent {

    void loadLastProjectState() throws IOException;

    /**
     * Called when Scene has been created and user interface behaviors can
     * listen to model changes to get the current view state.
     */
    void startUI();

    void restartUI();

    void save(ApplicationProject project) throws FileNotFoundException;

}
