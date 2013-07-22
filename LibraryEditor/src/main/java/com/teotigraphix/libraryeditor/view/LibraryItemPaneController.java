
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
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.libraryeditor.model.LibraryItemModel;
import com.teotigraphix.libraryeditor.model.LibraryItemModel.ItemKind;
import com.teotigraphix.libraryeditor.model.LibraryItemModel.OnLibraryItemModelSelectedKindChange;
import com.teotigraphix.libraryeditor.model.LibraryModel;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelRefresh;

@FXMLController
public class LibraryItemPaneController extends ViewStackController {

    @Inject
    LibraryItemModel libraryItemModel;

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
    ListView<LibraryScene> sceneList;

    @FXML
    ListView<LibraryPatch> patchList;

    @FXML
    ListView<LibraryPhrase> phraseList;

    public LibraryItemPaneController() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        // OnLibraryModelRefresh
        getController().getDispatcher().register(OnLibraryModelRefresh.class,
                new EventObserver<OnLibraryModelRefresh>() {
                    @Override
                    public void trigger(OnLibraryModelRefresh object) {
                        refreshShabang();
                    }
                });
        // OnLibraryManagerSelectedLibraryChange
        getController().getDispatcher().register(OnLibraryManagerSelectedLibraryChange.class,
                new EventObserver<OnLibraryManagerSelectedLibraryChange>() {
                    @Override
                    public void trigger(OnLibraryManagerSelectedLibraryChange object) {
                        selectedLibraryChanged();
                    }
                });

        // OnLibraryManagerImportComplete
        getController().getDispatcher().register(OnLibraryManagerImportComplete.class,
                new EventObserver<OnLibraryManagerImportComplete>() {
                    @Override
                    public void trigger(OnLibraryManagerImportComplete object) {
                        selectedLibraryChanged();
                    }
                });

        // OnLibraryItemModelSelectedKindChange
        getController().getDispatcher().register(OnLibraryItemModelSelectedKindChange.class,
                new EventObserver<OnLibraryItemModelSelectedKindChange>() {
                    @Override
                    public void trigger(OnLibraryItemModelSelectedKindChange object) {
                        selectedKindChangeHandler(object.getKind(), object.getOldKind());
                    }
                });
    }

    protected void refreshShabang() {
        forceListRefreshOn(sceneList);
    }

    @FXML
    public void buttonClickHandler(ActionEvent event) {
        ToggleButton button = (ToggleButton)event.getSource();
        int index = getButtonIndex(button);
        libraryItemModel.setSelectedKind(ItemKind.fromInt(index));
    }

    protected void selectedKindChangeHandler(ItemKind kind, ItemKind oldKind) {
        setSelectedIndex(kind.getIndex());
    }

    BeanPathAdapter<Library> sceneHolder;

    protected void selectedLibraryChanged() {
        final Library library = getController().getLibraryManager().getSelectedLibrary();
        titleLabel.setText(library.getName());

        //libraryModel.refresh(library);
        sceneHolder = new BeanPathAdapter<Library>(library);

        final ObservableList<LibraryScene> sceneItems = FXCollections.observableArrayList(library
                .getScenes());

        sceneList.setItems(sceneItems);

        sceneHolder.bindContentBidirectional("scenes", "metadataInfo.name", LibraryScene.class,
                sceneList.getItems(), LibraryScene.class, null, null);

        sceneList.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue, Number newValue) {
                        int index = newValue.intValue();
                        if (index >= 0 && index < library.getScenes().size()) {
                            //SceneItem scene = libraryModel.getScenes().get(index);
                            //libraryModel.setSelectedScene(scene);
                            LibraryScene scene = library.getScenes().get(index);
                            libraryItemModel.setSelectedProxy(scene);
                        }
                    }
                });

        //        try {
        //            getController().getLibraryManager().saveLibrary(library);
        //            getController().getApplication().save();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

    }

    private <T> void forceListRefreshOn(ListView<T> lsv) {
        System.out.println("forceListRefreshOn()");
        @SuppressWarnings("unchecked")
        ObservableList<T> items = (ObservableList<T>)FXCollections
                .observableArrayList(getController().getLibraryManager().getSelectedLibrary()
                        .getScenes());
        lsv.<T> setItems(null);
        lsv.<T> setItems(items);
    }

    public void initialize() {
        super.initialize();

        setToggleBar(toggleBar);
        setStackPane(viewStack);

        setupLists();

        libraryItemModel.setSelectedKind(ItemKind.SCENE);
    }

    private void setupLists() {

        patchList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<LibraryPatch>() {
                    @Override
                    public void changed(ObservableValue<? extends LibraryPatch> observable,
                            LibraryPatch oldValue, LibraryPatch newValue) {
                        libraryItemModel.setSelectedProxy(newValue);
                    }
                });

        phraseList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<LibraryPhrase>() {
                    @Override
                    public void changed(ObservableValue<? extends LibraryPhrase> observable,
                            LibraryPhrase oldValue, LibraryPhrase newValue) {
                        libraryItemModel.setSelectedProxy(newValue);
                    }
                });
    }

}
