
package com.teotigraphix.libraryeditor.view;

import java.io.IOException;

import org.androidtransfuse.event.EventObserver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.Inject;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.MediatorBase;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerImportComplete;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerSelectedLibraryChange;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;

@FXMLController
public class LibraryItemPaneController extends MediatorBase {

    @Inject
    ICaustkApplicationProvider provider;

    @FXML
    public ToggleButton sceneButton;

    @FXML
    public ToggleButton patchButton;

    @FXML
    public ToggleButton phraseButton;

    @FXML
    Label titleLabel;

    @FXML
    ListView<LibraryPatch> itemList;

    public LibraryItemPaneController() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        getController().getDispatcher().register(OnLibraryManagerSelectedLibraryChange.class,
                new EventObserver<OnLibraryManagerSelectedLibraryChange>() {
                    @Override
                    public void trigger(OnLibraryManagerSelectedLibraryChange object) {
                        selectedLibraryChanged();
                    }
                });

        getController().getDispatcher().register(OnLibraryManagerImportComplete.class,
                new EventObserver<OnLibraryManagerImportComplete>() {
                    @Override
                    public void trigger(OnLibraryManagerImportComplete object) {
                        selectedLibraryChanged();
                    }
                });

    }

    protected void selectedLibraryChanged() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        titleLabel.setText(library.getName());

        ObservableList<LibraryPatch> list = FXCollections.observableArrayList(library.getPatches());
        itemList.setItems(list);
        
        try {
            getController().getLibraryManager().saveLibrary(library);
            getController().getApplication().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // no sub components are created
        setController(provider.get().getController());
        registerObservers();
    }
}
