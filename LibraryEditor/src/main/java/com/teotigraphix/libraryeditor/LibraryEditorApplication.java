
package com.teotigraphix.libraryeditor;

import java.util.List;

import javafx.application.Application;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.teotigraphix.caustic.application.JavaFXApplication;
import com.teotigraphix.caustic.screen.IScreenView;
import com.teotigraphix.libraryeditor.config.ApplicationModule;
import com.teotigraphix.libraryeditor.mediator.ApplicationMediator;

public class LibraryEditorApplication extends JavaFXApplication {

    @Inject
    ApplicationMediator applicationMediator;

    @Override
    protected String getRootPane() {
        return "/com/teotigraphix/libraryeditor/view/RootPane.fxml";
    }

    @Override
    protected void initScreens(List<Class<? extends IScreenView>> screens) {
    }

    @Override
    public void init(List<Module> modules) throws Exception {
        super.init(modules);
        modules.add(new ApplicationModule());
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
