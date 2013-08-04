
package com.teotigraphix.libraryeditor;

import java.util.List;

import javafx.application.Application;

import com.google.inject.Module;
import com.teotigraphix.caustic.application.JavaFXApplication;
import com.teotigraphix.caustic.screen.IScreenView;
import com.teotigraphix.libraryeditor.config.ApplicationModule;

public class LibraryEditorApplication extends JavaFXApplication {

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
