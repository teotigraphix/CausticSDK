
package com.teotigraphix.libraryeditor.view;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.cathive.fx.guice.FXMLController;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustic.preferences.WindowPreferences;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.MediatorBase;

@FXMLController
public class RootPaneController extends MediatorBase {

    private static final String LIBRARY_PANE = "/com/teotigraphix/libraryeditor/view/LibraryItemPane.fxml";

    private static final String EDITOR_PANE = "/com/teotigraphix/libraryeditor/view/EditorViewStack.fxml";

    @FXML
    BorderPane borderPane;

    @Inject
    GuiceFXMLLoader loader;

    @Inject
    MainToolBar mainToolBar;

    @Inject
    IStageModel stageModel;

    @Inject
    WindowPreferences preference;

    @Inject
    ICaustkApplicationProvider provider;

    public RootPaneController() {
        super();
    }

    public void initialize() {
        // no sub components are created
        setController(provider.get().getController());
        registerObservers();

        Parent libraryPane = null;
        Parent editorPane = null;
        try {
            libraryPane = loader.load(getClass().getResource(LIBRARY_PANE)).getRoot();
            editorPane = loader.load(getClass().getResource(EDITOR_PANE)).getRoot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setLeft(libraryPane);

        borderPane.setTop(mainToolBar);
        borderPane.setCenter(editorPane);
    }

    public void start(Stage value) {
        stageModel.setStage(value);

        value.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                getController().getApplication().close();
            }
        });
    }

    public void show() {
        double x = preference.xProperty().get();
        double y = preference.yProperty().get();
        double width = preference.widthProperty().get();
        double height = preference.heightProperty().get();

        Stage stage = stageModel.getStage();
        x = (x < 0) ? 0 : x;
        y = (y < 0) ? 0 : y;
        stage.setX(x);
        stage.setY(y);
        stage.setWidth(width);
        stage.setHeight(height);

        preference.xProperty().bind(stage.xProperty());
        preference.yProperty().bind(stage.yProperty());
        preference.widthProperty().bind(stage.widthProperty());
        preference.heightProperty().bind(stage.heightProperty());

        stage.show();
    }

    @Override
    protected void onProjectLoad() {
        super.onProjectLoad();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

}
