
package com.teotigraphix.libraryeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.libraryeditor.model.LibraryModel;
import com.teotigraphix.libraryeditor.model.LibraryModel.ItemKind;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelRefresh;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelSelectedKindChange;

@FXMLController
public class LibraryItemPaneController extends ViewStackController {

    @Inject
    LibraryModel libraryItemModel;

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
    ListView<LibraryScene> sceneList;

    @FXML
    ListView<LibraryPatch> patchList;

    @FXML
    ListView<LibraryPhrase> phraseList;

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
        libraryItemModel.setSelectedKind(ItemKind.fromInt(index));
    }

    protected void selectedKindChangeHandler(ItemKind kind, ItemKind oldKind) {
        //System.out.println("LibraryPane.selectedKindChangeHandler()");
        setSelectedIndex(kind.getIndex());
    }

    protected void onLibraryManagerSelectedLibraryChangeHandler() {

        //System.out.println("LibraryPane.selectedLibraryChanged()");

        libraryItemModel.setSelectedItem(null);

        Library library = getController().getLibraryManager().getSelectedLibrary();
        titleLabel.setText(library.getName());

        libraryHolder = new BeanPathAdapter<Library>(library);

        setupList(sceneList, library, LibraryScene.class, "scenes");
        setupList(patchList, library, LibraryPatch.class, "patches");
        setupList(phraseList, library, LibraryPhrase.class, "phrases");

    }

    @SuppressWarnings({
            "unchecked", "unused"
    })
    private <T> void forceListRefreshOn(ListView<T> lsv) {
        //System.out.println("forceListRefreshOn()");

        if (libraryItemModel.getSelectedItem() instanceof LibraryScene) {
            ObservableList<T> items = (ObservableList<T>)FXCollections
                    .observableArrayList(getController().getLibraryManager().getSelectedLibrary()
                            .getScenes());
            lsv.<T> setItems(null);
            lsv.<T> setItems(items);
        } else if (libraryItemModel.getSelectedItem() instanceof LibraryPatch) {
            ObservableList<T> items = (ObservableList<T>)FXCollections
                    .observableArrayList(getController().getLibraryManager().getSelectedLibrary()
                            .getPatches());
            lsv.<T> setItems(null);
            lsv.<T> setItems(items);
        } else if (libraryItemModel.getSelectedItem() instanceof LibraryPhrase) {
            ObservableList<T> items = (ObservableList<T>)FXCollections
                    .observableArrayList(getController().getLibraryManager().getSelectedLibrary()
                            .getPhrases());
            lsv.<T> setItems(null);
            lsv.<T> setItems(items);
        }

        libraryHolder.setBean(getController().getLibraryManager().getSelectedLibrary());
    }

    protected void onLibraryModelRefreshHandler() {
        if (libraryItemModel.getSelectedItem() instanceof LibraryScene) {
            forceListRefreshOn(sceneList);
        } else if (libraryItemModel.getSelectedItem() instanceof LibraryPatch) {
            forceListRefreshOn(patchList);
        } else if (libraryItemModel.getSelectedItem() instanceof LibraryPhrase) {
            forceListRefreshOn(phraseList);
        }
    }

    @Override
    public void onRegister() {
        setToggleBar(toggleBar);
        setStackPane(viewStack);
    }

    @SuppressWarnings("unchecked")
    private <T> ObservableList<T> getList(Library library, Class<T> itemType) {
        if (itemType == LibraryScene.class) {
            return (ObservableList<T>)FXCollections.observableArrayList(library.getScenes());
        } else if (itemType == LibraryPatch.class) {
            return (ObservableList<T>)FXCollections.observableArrayList(library.getPatches());
        } else if (itemType == LibraryPhrase.class) {
            return (ObservableList<T>)FXCollections.observableArrayList(library.getPhrases());
        }
        return null;
    }

    private <T extends LibraryItem> void setupList(ListView<T> list, Library library,
            final Class<T> itemType, String propertyName) {
        ObservableList<T> sceneItems = getList(library, itemType);
        list.setItems(sceneItems);
        libraryHolder.bindContentBidirectional(propertyName, "metadataInfo.name", itemType,
                list.getItems(), itemType, null, null);
        list.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                    Number newValue) {
                int index = newValue.intValue();

                Library library = getController().getLibraryManager().getSelectedLibrary();
                ObservableList<T> items = getList(library, itemType);

                if (index >= 0 && index < items.size()) {
                    ObservableList<T> item = getList(library, itemType);
                    libraryItemModel.setSelectedItem(item.get(index));
                }
            }
        });
    }
}
