
package com.teotigraphix.libraryeditor.view;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import org.androidtransfuse.event.EventObserver;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.Inject;
import com.teotigraphix.caustic.model.BeanPathAdapter;
import com.teotigraphix.caustic.ui.controller.ViewStackController;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerImportComplete;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerSelectedLibraryChange;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.libraryeditor.model.LibraryModel;
import com.teotigraphix.libraryeditor.model.LibraryModel.ItemKind;
import com.teotigraphix.libraryeditor.model.LibraryModel.LibraryItemProxy;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelRefresh;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelSelectedKindChange;

@FXMLController
public class LibraryItemPaneController extends ViewStackController {

    @Inject
    LibraryModel libraryModel;

    @FXML
    public HBox toggleBar;

    @FXML
    public StackPane viewStack;

    @FXML
    public ToggleButton sceneButton;

    @FXML
    public ToggleButton patchButton;

    @FXML
    public ToggleButton phraseButton;

    @FXML
    Label titleLabel;

    @FXML
    ListView<LibraryItemProxy> sceneList;

    @FXML
    ListView<LibraryItemProxy> patchList;

    @FXML
    ListView<LibraryItemProxy> phraseList;

    BeanPathAdapter<Library> libraryHolder;

    public LibraryItemPaneController() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();
        //System.out.println("LibraryItemPaneController.registerObservers()");

        // OnLibraryModelRefresh
        getController().getDispatcher().register(OnLibraryModelRefresh.class,
                new EventObserver<OnLibraryModelRefresh>() {
                    @Override
                    public void trigger(OnLibraryModelRefresh object) {
                        onLibraryModelRefreshHandler();
                    }
                });

        // OnLibraryManagerSelectedLibraryChange
        getController().getDispatcher().register(OnLibraryManagerSelectedLibraryChange.class,
                new EventObserver<OnLibraryManagerSelectedLibraryChange>() {
                    @Override
                    public void trigger(OnLibraryManagerSelectedLibraryChange object) {
                        onLibraryManagerSelectedLibraryChangeHandler();
                    }
                });

        // OnLibraryManagerImportComplete
        getController().getDispatcher().register(OnLibraryManagerImportComplete.class,
                new EventObserver<OnLibraryManagerImportComplete>() {
                    @Override
                    public void trigger(OnLibraryManagerImportComplete object) {
                        onLibraryModelRefreshHandler();
                    }
                });

        // OnLibraryItemModelSelectedKindChange
        getController().getDispatcher().register(OnLibraryModelSelectedKindChange.class,
                new EventObserver<OnLibraryModelSelectedKindChange>() {
                    @Override
                    public void trigger(OnLibraryModelSelectedKindChange object) {
                        selectedKindChangeHandler(object.getKind(), object.getOldKind());
                    }
                });
    }

    @FXML
    public void buttonClickHandler(ActionEvent event) {
        System.out.println("LibraryPane.buttonClickHandler()");
        ToggleButton button = (ToggleButton)event.getSource();
        int index = getButtonIndex(button);
        libraryModel.setSelectedKind(ItemKind.fromInt(index));
    }

    protected void selectedKindChangeHandler(ItemKind kind, ItemKind oldKind) {
        setSelectedIndex(kind.getIndex());
    }

    protected void onLibraryManagerSelectedLibraryChangeHandler() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        titleLabel.setText(library.getName());

        libraryModel.refresh();

        // Scenes
        sceneList.setItems(FXCollections.observableArrayList(libraryModel.getScenes()));
        sceneList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        int index = newValue.intValue();
                        List<LibraryItemProxy> items = libraryModel.getScenes();
                        if (index >= 0 && index < items.size()) {
                            List<LibraryItemProxy> item = libraryModel.getScenes();
                            libraryModel.setSelectedItem(item.get(index));
                        }
                    }
                });

        // Patches
        patchList.setItems(FXCollections.observableArrayList(libraryModel.getPatches()));
        patchList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        int index = newValue.intValue();
                        List<LibraryItemProxy> items = libraryModel.getPatches();
                        if (index >= 0 && index < items.size()) {
                            List<LibraryItemProxy> item = libraryModel.getPatches();
                            libraryModel.setSelectedItem(item.get(index));
                        }
                    }
                });

        // Phrases
        phraseList.setItems(FXCollections.observableArrayList(libraryModel.getPhrases()));
        phraseList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        int index = newValue.intValue();
                        List<LibraryItemProxy> items = libraryModel.getPhrases();
                        if (index >= 0 && index < items.size()) {
                            List<LibraryItemProxy> item = libraryModel.getPhrases();
                            libraryModel.setSelectedItem(item.get(index));
                        }
                    }
                });
    }

    protected void onLibraryModelRefreshHandler() {
        switch (libraryModel.getSelectedKind()) {
            case SCENE:
                sceneList.setItems(null);
                sceneList.setItems(FXCollections.observableArrayList(libraryModel.getScenes()));
                break;

            case PATCH:
                patchList.setItems(null);
                patchList.setItems(FXCollections.observableArrayList(libraryModel.getPatches()));
                break;

            case PHRASE:
                phraseList.setItems(null);
                phraseList.setItems(FXCollections.observableArrayList(libraryModel.getPhrases()));
                break;
        }
    }

    @Override
    public void onRegister() {
        setToggleBar(toggleBar);
        setStackPane(viewStack);
    }
}
