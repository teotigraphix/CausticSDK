
package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.core.CaustkProject;

public interface IApplicationStates extends IApplicationComponent {

    void loadLastProjectState() throws IOException;

    /**
     * Called when Scene has been created and user interface behaviors can
     * listen to model changes to get the current view state.
     */
    void startUI();

    void restartUI();

    void onProjectCreate(CaustkProject project);

    void onProjectLoad(CaustkProject project);

    void onProjectSave(CaustkProject project);

    void onProjectClose(CaustkProject project);

    CaustkProject createDefaultProject(String name, File projectFile);

    CaustkProject readProject(File projectFile) throws IOException;

}
