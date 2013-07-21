
package com.teotigraphix.libraryeditor.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import com.cathive.fx.guice.FXMLComponent;
import com.google.inject.Inject;

@FXMLComponent
public class MainToolBar extends HBox {

    @Inject
    MainToolBarMediator mainToolBarMediator;

    @FXML
    Button createButton;

    @FXML
    Button saveButton;

    public MainToolBar() {
    }

    @FXML
    public void onCreateButtonAction(ActionEvent event) {
        mainToolBarMediator.createLibrary();
    }

    @FXML
    public void onSaveButtonAction(ActionEvent event) {
        mainToolBarMediator.importFile();
    }

    public void initialize() {
        System.out.println("");
    }

}
