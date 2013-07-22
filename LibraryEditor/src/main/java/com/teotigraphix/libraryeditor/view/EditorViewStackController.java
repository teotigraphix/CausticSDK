
package com.teotigraphix.libraryeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import org.androidtransfuse.event.EventObserver;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.Inject;
import com.teotigraphix.caustic.model.BeanPathAdapter;
import com.teotigraphix.caustic.ui.controller.ViewStackController;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.libraryeditor.model.LibraryItemModel;
import com.teotigraphix.libraryeditor.model.LibraryItemModel.OnLibraryItemModelItemChange;
import com.teotigraphix.libraryeditor.model.LibraryModel;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelRefresh;

@FXMLController
public class EditorViewStackController extends ViewStackController {

    @FXML
    StackPane stackPane;

    @Inject
    LibraryItemModel libraryItemModel;

    @Inject
    LibraryModel libraryModel;

    public EditorViewStackController() {
    }

    @Inject
    public EditorViewStackController(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public void initialize() {
        super.initialize();

        setStackPane(stackPane);

        Node editorPane = stackPane.getChildrenUnmodifiable().get(0);
        TextField nameInput = (TextField)editorPane.lookup("#metaName");
        TextField authorInput = (TextField)editorPane.lookup("#metaAuthor");

        nameInput.textProperty().addListener(changedHandler);
        authorInput.textProperty().addListener(changedHandler);
    }

    private ChangeListener<String> changedHandler = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue,
                String newValue) {
            getController().getDispatcher().trigger(new OnLibraryModelRefresh());
        }
    };

    @Override
    protected void registerObservers() {
        super.registerObservers();

        getController().getDispatcher().register(OnLibraryItemModelItemChange.class,
                new EventObserver<OnLibraryItemModelItemChange>() {
                    @Override
                    public void trigger(OnLibraryItemModelItemChange object) {
                        sceneItemChanged(libraryItemModel.getSelectedProxy());
                    }
                });
    }

    private BeanPathAdapter<LibraryItem> libraryItem;

    protected void sceneItemChanged(final LibraryItem item) {
        Node editorPane = stackPane.getChildrenUnmodifiable().get(0);
        TextField nameInput = (TextField)editorPane.lookup("#metaName");
        TextField authorInput = (TextField)editorPane.lookup("#metaAuthor");

        if (libraryItem == null) {
            libraryItem = new BeanPathAdapter<LibraryItem>(item);
            libraryItem.bindBidirectional("metadataInfo.name", nameInput.textProperty());
            libraryItem.bindBidirectional("metadataInfo.author", authorInput.textProperty());
        } else {
            libraryItem.setBean(item);
        }
    }

}
