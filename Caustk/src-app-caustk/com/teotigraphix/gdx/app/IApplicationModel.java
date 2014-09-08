
package com.teotigraphix.gdx.app;

public interface IApplicationModel extends IApplicationComponent {

    ApplicationPreferences getApplicationPreferences();

    ApplicationProject getProject();

    void setProject(ApplicationProject state);

}
