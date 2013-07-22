
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
    Button newButton;

    @FXML
    Button loadButton;

    @FXML
    Button saveButton;

    public MainToolBar() {
    }

    @FXML
    public void onNewButtonHandler(ActionEvent event) {
        mainToolBarMediator.createLibrary();
    }

    @FXML
    public void onLoadButtonHandler(ActionEvent event) {
        mainToolBarMediator.loadLibrary();
    }

    @FXML
    public void onSaveButtonHandler(ActionEvent event) {
        mainToolBarMediator.saveLibrary();
    }

    @FXML
    public void onImportButtonHandler(ActionEvent event) {
        mainToolBarMediator.importFile();
    }

    @FXML
    public void onPreviewButtonHandler(ActionEvent event) {
        mainToolBarMediator.previewItem();
    }

    public void initialize() {
        System.out.println("");
    }

}
