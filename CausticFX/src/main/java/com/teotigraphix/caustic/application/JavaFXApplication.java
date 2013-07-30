
package com.teotigraphix.caustic.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Named;

import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.WindowEvent;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustic.mediator.StageMediator;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;

/**
 * The {@link JavaFXApplication} is the base app for all apps that implement a
 * single Bitmap UI.
 */
public abstract class JavaFXApplication extends GuiceApplication {

    @Inject
    @Named("resources")
    ResourceBundle resourceBundle;

    @Inject
    protected GuiceFXMLLoader loader;

    @Inject
    protected IApplicationController applicationController;

    @Inject
    protected ICaustkApplicationProvider applicationProvider;

    @Inject
    protected IStageModel stageModel;

    @Inject
    StageMediator stageMediator; // is there a proper place for this?

    private List<DesktopMediatorBase> mediators = new ArrayList<>();

    private Pane root;

    @Override
    public void init(List<Module> modules) throws Exception {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stageModel.setStage(primaryStage);

        createUI();

        initMediators(mediators);

        for (DesktopMediatorBase mediator : mediators) {
            mediator.create(root);
        }

        addListeners();

        for (DesktopMediatorBase mediator : mediators) {
            mediator.initialize();
        }

        applicationController.start();

        applicationProvider.get().getController().getDispatcher()
                .trigger(new OnApplicationRegister());

        for (DesktopMediatorBase mediator : mediators) {
            mediator.onRegister();
        }

        primaryStage.show();
    }

    protected abstract String getRootPane();

    protected abstract void initMediators(List<DesktopMediatorBase> mediators);

    protected void createUI() throws IOException {
        final Stage stage = stageModel.getStage();

        root = loader.load(getClass().getResource(getRootPane())).getRoot();
        StageBuilder.create().title(resourceBundle.getString("APP_TITLE")).resizable(false)
                .scene(SceneBuilder.create().root(root).build()).applyTo(stage);
        stage.getScene().getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        //stage.setResizable(false); // this is adding 10 px on width & height
    }

    protected void addListeners() {
        stageModel.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    applicationProvider.get().save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                applicationProvider.get().close();
            }
        });
    }

    public static class OnApplicationRegister {

    }
}
