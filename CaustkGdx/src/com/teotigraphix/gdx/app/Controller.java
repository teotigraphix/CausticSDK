
package com.teotigraphix.gdx.app;

public abstract class Controller implements IApplicationComponent {

    private IApplication application;

    @Override
    public IApplication getApplication() {
        return application;
    }

    public void setApplication(IApplication application) {
        this.application = application;
    }

    public Controller() {
    }

}
