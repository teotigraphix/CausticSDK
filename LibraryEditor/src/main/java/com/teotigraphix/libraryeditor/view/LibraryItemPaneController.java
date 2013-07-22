
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
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerImportComplete;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerSelectedLibraryChange;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.libraryeditor.model.LibraryModel;
import com.teotigraphix.libraryeditor.model.LibraryModel.ItemKind;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelSelectedKindChange;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelRefresh;

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

    @Inject
    public LibraryItemPaneController(ICaustkApplicationProvider provider) {
        super(provider);
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

        // Setup Scenes
        final ObservableList<LibraryScene> sceneItems = FXCollections.observableArrayList(library
                .getScenes());

        sceneList.setItems(sceneItems);
        libraryHolder.bindContentBidirectional("scenes", "metadataInfo.name", LibraryScene.class,
                sceneList.getItems(), LibraryScene.class, null, null);
        sceneList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        int index = newValue.intValue();
                        if (index >= 0
                                && index < getController().getLibraryManager().getSelectedLibrary()
                                        .getScenes().size()) {
                            LibraryScene item = getController().getLibraryManager()
                                    .getSelectedLibrary().getScenes().get(index);
                            libraryItemModel.setSelectedItem(item);
                        }
                    }
                });

        // Setup Patches
        final ObservableList<LibraryPatch> patchItems = FXCollections.observableArrayList(library
                .getPatches());

        patchList.setItems(patchItems);
        libraryHolder.bindContentBidirectional("patches", "metadataInfo.name", LibraryPatch.class,
                patchList.getItems(), LibraryPatch.class, null, null);
        patchList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        int index = newValue.intValue();
                        if (index >= 0
                                && index < getController().getLibraryManager().getSelectedLibrary()
                                        .getPatches().size()) {
                            LibraryPatch item = getController().getLibraryManager()
                                    .getSelectedLibrary().getPatches().get(index);
                            libraryItemModel.setSelectedItem(item);
                        }
                    }
                });

        // Setup Phrases
        final ObservableList<LibraryPhrase> phraseItems = FXCollections.observableArrayList(library
                .getPhrases());

        phraseList.setItems(phraseItems);
        libraryHolder.bindContentBidirectional("phrases", "metadataInfo.name", LibraryPhrase.class,
                phraseList.getItems(), LibraryPhrase.class, null, null);
        phraseList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        int index = newValue.intValue();
                        if (index >= 0
                                && index < getController().getLibraryManager().getSelectedLibrary()
                                        .getPhrases().size()) {
                            LibraryPhrase item = getController().getLibraryManager()
                                    .getSelectedLibrary().getPhrases().get(index);
                            libraryItemModel.setSelectedItem(item);
                        }
                    }
                });

        //libraryItemModel.setSelectedKind(ItemKind.SCENE);
        //libraryItemModel.setSelectedProxy(library.getScenes().get(0));

        //        try {
        //            getController().getLibraryManager().saveLibrary(library);
        //            getController().getApplication().save();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

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

        //libraryHolder.setBean(getController().getLibraryManager().getSelectedLibrary());
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
    protected void firstRun() {
        super.firstRun();
        //System.out.println("LibraryItemPaneController.firstRun()");
        setToggleBar(toggleBar);
        setStackPane(viewStack);
    }
}
