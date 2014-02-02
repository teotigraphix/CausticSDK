
package com.teotigraphix.gdx.app;

import com.teotigraphix.gdx.IGdxApplication;

public abstract class GdxController implements IGdxApplicationComponent {

    private IGdxApplication application;

    @Override
    public IGdxApplication getApplication() {
        return application;
    }

    public void setApplication(IGdxApplication application) {
        this.application = application;
    }

    public GdxController() {
    }

    @Override
    public abstract void onAttach();

    @Override
    public abstract void onDetach();

}
