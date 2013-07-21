
package com.teotigraphix.libraryeditor.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import com.cathive.fx.guice.FXMLController;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.MediatorBase;

@FXMLController
public class RootPaneController extends MediatorBase {

    @FXML
    BorderPane borderPane;

    @Inject
    GuiceFXMLLoader loader;
    
    @Inject
    MainToolBar mainToolBar;
    
    @Inject
    ICaustkApplicationProvider provider;

    public RootPaneController() {
        super();
    }

    public void initialize() {
        // no sub components are created
        setController(provider.get().getController());
        registerObservers();

        VBox frame = null;
        try {
            frame = loader.load(
                    getClass().getResource(
                            "/com/teotigraphix/libraryeditor/view/LibraryItemPane.fxml")).getRoot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setLeft(frame);
        
        borderPane.setTop(mainToolBar);
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
