
package com.teotigraphix.libraryeditor.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    public TextField searchText;

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

    private Map<ItemKind, ListState> state = new HashMap<>();

    class ListState {

        private ItemKind kind;

        public ItemKind getKind() {
            return kind;
        }

        private ListView<LibraryItemProxy> list;

        public ListView<LibraryItemProxy> getList() {
            return list;
        }

        public ListState(ItemKind kind, ListView<LibraryItemProxy> list) {
            this.kind = kind;
            this.list = list;
        }

        private String searchText = "";

        public String getSearchText() {
            return searchText;
        }

        public void setSearchText(String value) {
            searchText = value;
        }
    }

    public LibraryItemPaneController() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();
        //System.out.println("LibraryItemPaneController.registerObservers()");

        // OnLibraryModelRefresh
        getController().register(OnLibraryModelRefresh.class,
                new EventObserver<OnLibraryModelRefresh>() {
                    @Override
                    public void trigger(OnLibraryModelRefresh object) {
                        onLibraryModelRefreshHandler();
                    }
                });

        // OnLibraryManagerSelectedLibraryChange
        getController().register(OnLibraryManagerSelectedLibraryChange.class,
                new EventObserver<OnLibraryManagerSelectedLibraryChange>() {
                    @Override
                    public void trigger(OnLibraryManagerSelectedLibraryChange object) {
                        onLibraryManagerSelectedLibraryChangeHandler();
                    }
                });

        // OnLibraryManagerImportComplete
        getController().register(OnLibraryManagerImportComplete.class,
                new EventObserver<OnLibraryManagerImportComplete>() {
                    @Override
                    public void trigger(OnLibraryManagerImportComplete object) {
                        refreshView();
                    }
                });

        // OnLibraryItemModelSelectedKindChange
        getController().register(OnLibraryModelSelectedKindChange.class,
                new EventObserver<OnLibraryModelSelectedKindChange>() {
                    @Override
                    public void trigger(OnLibraryModelSelectedKindChange object) {
                        selectedKindChangeHandler(object.getKind(), object.getOldKind());
                    }
                });
    }

    protected void refreshView() {
        libraryModel.refresh();
        sceneList.setItems(FXCollections.observableArrayList(libraryModel.getScenes()));
        patchList.setItems(FXCollections.observableArrayList(libraryModel.getPatches()));
        phraseList.setItems(FXCollections.observableArrayList(libraryModel.getPhrases()));
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
        searchText.setText(getListState().getSearchText());
    }

    protected List<LibraryItemProxy> getItems(ItemKind kind) {
        switch (kind) {
            case SCENE:
                return libraryModel.getScenes();
            case PHRASE:
                return libraryModel.getPhrases();
            case PATCH:
                return libraryModel.getPatches();
        }
        return null;
    }

    protected void onLibraryManagerSelectedLibraryChangeHandler() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        titleLabel.setText(library.getName());

        refreshView();
        
        searchText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                getListState().setSearchText(newValue);
                handleSearchByKey(getListState().getList(), oldValue, newValue);
            }
        });

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

        // TEMP
        sceneList.getSelectionModel().select(0);
    }

    protected void onLibraryModelRefreshHandler() {
        getListState().getList().setItems(null);
        getListState().getList().setItems(
                FXCollections.observableArrayList(getItems(libraryModel.getSelectedKind())));
    }

    private ListState getListState() {
        return state.get(libraryModel.getSelectedKind());
    }

    @Override
    public void onRegister() {

    }

    public void handleSearchByKey(ListView<LibraryItemProxy> list, String oldVal, String newVal) {
        // If the number of characters in the text box is less than last time
        // it must be because the user pressed delete
        if (oldVal != null && (newVal.length() < oldVal.length())) {
            // Restore the lists original set of entries
            // and start from the beginning
            list.setItems(FXCollections.observableArrayList(getItems(libraryModel.getSelectedKind())));
        }

        // Break out all of the parts of theO search text
        // by splitting on white space
        String[] parts = newVal.toUpperCase().split(" ");

        // Filter out the entries that don't contain the entered text
        ObservableList<LibraryItemProxy> subentries = FXCollections.observableArrayList();
        for (LibraryItemProxy entry : list.getItems()) {
            boolean match = true;
            // String entryText = (String)entry.getItem().getMetadataInfo().getTagsString();
            String entryText = (String)entry.getItem().toString();
            for (String part : parts) {
                // The entry needs to contain all portions of the
                // search string *but* in any order
                if (!entryText.toUpperCase().contains(part)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                subentries.add(entry);
            }
        }
        list.setItems(subentries);
    }

    @Override
    public void create(Pane root) {
        setToggleBar(toggleBar);
        setStackPane(viewStack);

        state = new HashMap<ItemKind, ListState>();
        state.put(ItemKind.SCENE, new ListState(ItemKind.SCENE, sceneList));
        state.put(ItemKind.PHRASE, new ListState(ItemKind.PHRASE, phraseList));
        state.put(ItemKind.PATCH, new ListState(ItemKind.PATCH, patchList));
    }
}
