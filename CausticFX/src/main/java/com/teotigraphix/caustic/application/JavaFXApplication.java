
package com.teotigraphix.caustic.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.WindowEvent;

import javax.inject.Named;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustic.mediator.StageMediator;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.caustic.screen.IScreenView;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.core.CtkDebug;

/*
App requirements;

- config
  - ApplicationConfiguration
  - ApplicationConstants
  - ApplicationModule

- FooApplication extends JavaFXApplication
- resources
  - app/package/view/RootLayout.fxml
  - FooApplication.properties
    - APP_TITLE
    - APP_DIRECTORY
  - main.css

Mediators

- DesktopMediatorBase extends MediatorBase



Application.start(Stage)
  - save stage instance to StageModel
  
  - create the ui
    - load the RootPane.fxml
    - add the main.css
  - add mediators to list
  
  - call create() on all mediators
  
  - add app listeners (close etc)
  
  - call preinitialize() on all mediators
  
  - IApplicationController.start()
    - set caustic storage
    - set application root usinf 'APP_DIRECTORY' resource
    - ICaustkApplication.initialize()
      - ICaustkController.initialize()
        - create application root dir
        - create ALL sub controllers
      - fire(OnApplicationInitialize)
    - IProjectManager.initialize()
      - load .settings file
    - ICaustkApplication.start()
      - ICaustkController.start()
      - fire(OnApplicationStart)
    - find lastProject in settings
      if note exists
        - IProjectManager.create()
        - IProjectManager.save()
        - fire(OnProjectManagerChange[LOAD])
        else
        - IProjectManager.load(lastProj)
          - project.open()
          - fire(OnProjectManagerChange[LOAD])
    
    - trigger(OnModelRegister)
    - trigger(OnMediatorRegister)
    


 */

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
    protected IScreenManager screenManager;

    @Inject
    protected StageMediator stageMediator; // is there a proper place for this?

    private List<DesktopMediatorBase> mediators = new ArrayList<>();

    private List<Class<? extends IScreenView>> screens = new ArrayList<>();

    private Pane root;

    @Override
    public void init(List<Module> modules) throws Exception {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stageModel.setStage(primaryStage);

        CtkDebug.log("Create main app UI");
        createRootPane();

        CtkDebug.log("Rack up mediators");
        initMediators(mediators);

        initScreens(screens);
        for (Class<? extends IScreenView> type : screens) {
            screenManager.addScreen(type);
        }

        CtkDebug.log("Create all mediator sub UI components");
        for (DesktopMediatorBase mediator : mediators) {
            mediator.create(root);
        }

        screenManager.create(root);

        addListeners();

        screenManager.onRegisterObservers(); // ??? Somewhere else?

        applicationController.registerMediatorObservers();

        CtkDebug.log("Start application controller");
        applicationController.start();

        applicationController.registerModels();
        applicationController.registerMeditors();
        screenManager.onRegister(); // ??? Somewhere else?

        applicationController.show();

        CtkDebug.log("Show the application");
        primaryStage.show();
    }

    // screenManager.addScreen(MainScreenView.class);
    protected abstract void initScreens(List<Class<? extends IScreenView>> screens);

    protected abstract String getRootPane();

    protected abstract void initMediators(List<DesktopMediatorBase> mediators);

    protected void createRootPane() throws IOException {
        final Stage stage = stageModel.getStage();

        root = loader.load(getClass().getResource(getRootPane())).getRoot();
        StageBuilder.create().title(resourceBundle.getString("APP_TITLE")).resizable(false)
                .scene(SceneBuilder.create().root(root).build()).applyTo(stage);
        stage.getScene().getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        stage.setResizable(true); // this is adding 10 px on width & height
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

    /*
    Sub class applications need the following
    public static void main(String[] args) {
        Application.launch(args);
    }
     */
}
