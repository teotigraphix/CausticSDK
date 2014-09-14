
package com.teotigraphix.gdx.app;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.core.CaustkProject;

public interface IFileManager {

    File getStartupProjectFile();

    void setStartupProject(CaustkProject project);

    CaustkProject createOrLoadStartupProject() throws IOException;

}
