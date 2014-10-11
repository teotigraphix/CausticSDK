
package com.teotigraphix.gdx.app;

import java.io.IOException;

public interface IProjectModelWrite {

    <T extends Project> void setProject(T project) throws IOException;

    void save();
}
