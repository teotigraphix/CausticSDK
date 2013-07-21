
package com.teotigraphix.libraryeditor;

import java.util.List;

import javafx.application.Application;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.libraryeditor.config.ApplicationModule;
import com.teotigraphix.libraryeditor.controller.ApplicationController;

public class LibraryEditorApplication extends GuiceApplication {

    @Inject
    GuiceFXMLLoader loader;

    @Inject
    ICaustkApplicationProvider applicationProvider;

    @Inject
    ApplicationController applicationController;

    @Override
    public void init(List<Module> modules) throws Exception {
        modules.add(new ApplicationModule());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane frame = loader.load(
                getClass().getResource("/com/teotigraphix/libraryeditor/view/RootPane.fxml"))
                .getRoot();
        StageBuilder.create().title("Caustic Library Editor").resizable(true)
                .scene(SceneBuilder.create().root(frame).build()).applyTo(primaryStage);
        
        applicationController.start();
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
