
package com.teotigraphix.caustic.application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.WindowEvent;

import javax.inject.Named;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.teotigraphix.caustic.application.IPreferenceManager.Editor;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.mediator.StageMediator;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.caustic.screen.IScreenView;
import com.teotigraphix.caustic.utils.FileUtil;
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
    protected IPreferenceManager preferenceManager;

    @Inject
    protected IApplicationPreferences applicationPreferences;

    @Inject
    protected StageMediator stageMediator; // is there a proper place for this?

    private List<Class<? extends IScreenView>> screens = new ArrayList<>();

    private Pane root;

    private Editor editor;

    Pane getRoot() {
        return root;
    }

    @Override
    public void init(List<Module> modules) throws Exception {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stageModel.setStage(primaryStage);

        CtkDebug.log("Create main app UI");
        createRootPane();

        addListeners();

        setupWorkingDirectory();

        CtkDebug.log("Create IScreenView instances");
        initScreens(screens);
        for (Class<? extends IScreenView> type : screens) {
            screenManager.addScreen(type);
        }
        
        CtkDebug.log("Create ScreenManager");
        screenManager.create(getRoot());

        // registers screenManager which then will loop through all screens
        applicationController.registerMediatorObservers();

        CtkDebug.log("Start application controller");
        // set roots, call initialize(), start() on application, start app model
        // create or load last project
        applicationController.start();
        
        applicationController.load();
        
        applicationController.registerModels();
        applicationController.registerMeditors();

        applicationController.show();

        CtkDebug.log("Show the application");

        show();
    }

    public void show() {
        double x = applicationPreferences.getFloat("x", -10f);
        double y = applicationPreferences.getFloat("y", -10f);
        double width = applicationPreferences.getFloat("width", -10f);
        double height = applicationPreferences.getFloat("height", -10f);

        Stage stage = stageModel.getStage();
        x = (x < 0) ? 0 : x;
        y = (y < 0) ? 0 : y;
        width = (width == -10) ? stage.getWidth() : width;
        height = (height == -10) ? stage.getHeight() : height;

        stage.setX(x);
        stage.setY(y);
        stage.setWidth(width);
        stage.setHeight(height);

        editor = applicationPreferences.edit();

        stage.xProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                    Number newValue) {
                editor.putFloat("x", newValue.floatValue());
            }
        });
        stage.yProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                    Number newValue) {
                editor.putFloat("y", newValue.floatValue());
            }
        });
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                    Number newValue) {
                editor.putFloat("width", newValue.floatValue());
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                    Number newValue) {
                editor.putFloat("height", newValue.floatValue());
            }
        });
        stage.show();
    }

    private void setupWorkingDirectory() {
        String causticDirectory = preferenceManager.getString("causticRoot", null);
        if (causticDirectory == null) {
            DirectoryChooser chooser = FileUtil.createDefaultDirectoryChooser(null,
                    "Choose Caustic application root");
            File causticFile = chooser.showDialog(null);
            if (causticFile != null && causticFile.isDirectory()) {
                causticDirectory = causticFile.getPath();
                preferenceManager.edit().putString("causticRoot", causticDirectory).commit();
            } else {
                throw new RuntimeException("Caustic directory invalid.");
            }
        }

        File workingDirectory = new File(causticDirectory).getParentFile();
        applicationProvider.get().getConfiguration().setCausticStorage(workingDirectory);
        File applicationRoot = new File(workingDirectory, resourceBundle.getString("APP_DIRECTORY"));
        applicationProvider.get().getConfiguration().setApplicationRoot(applicationRoot);
    }

    // screenManager.addScreen(MainScreenView.class);
    protected abstract void initScreens(List<Class<? extends IScreenView>> screens);

    protected abstract String getRootPane();

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
                    editor.commit();
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
