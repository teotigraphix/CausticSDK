
package com.teotigraphix.libraryeditor;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.WindowEvent;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.mediator.StageMediator;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.libraryeditor.config.ApplicationModule;

public class LibraryEditorApplication extends GuiceApplication {

    @Inject
    GuiceFXMLLoader loader;

    @Inject
    ICaustkApplicationProvider applicationProvider;

    @Inject
    IApplicationController applicationController;

    @Inject
    IStageModel stageModel;

    @Inject
    StageMediator stageMediator; // is there a proper place for this?

    @Override
    public void init(List<Module> modules) throws Exception {
        modules.add(new ApplicationModule());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stageModel.setStage(primaryStage);

        AnchorPane frame = loader.load(
                getClass().getResource("/com/teotigraphix/libraryeditor/view/RootPane.fxml"))
                .getRoot();
        StageBuilder.create().title("Caustic Library Editor").resizable(true)
                .scene(SceneBuilder.create().root(frame).build()).applyTo(primaryStage);

        applicationController.start();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    applicationProvider.get().save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                applicationProvider.get().close();
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
