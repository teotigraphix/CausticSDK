
package com.teotigraphix.libraryeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import org.androidtransfuse.event.EventObserver;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.Inject;
import com.teotigraphix.caustic.model.BeanPathAdapter;
import com.teotigraphix.caustic.ui.controller.ViewStackController;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.libraryeditor.model.LibraryModel;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelSelectedItemChange;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelRefresh;

@FXMLController
public class EditorViewStackController extends ViewStackController {

    private BeanPathAdapter<LibraryItem> libraryItem;

    @FXML
    StackPane stackPane;

    @FXML
    TextField metaName;

    @FXML
    TextField metaAuthor;

    @FXML
    TextArea metaDescription;

    @FXML
    TextField metaTags;

    @Inject
    LibraryModel libraryItemModel;

    public EditorViewStackController() {
    }

    @Inject
    public EditorViewStackController(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    protected void firstRun() {
        super.firstRun();
        //System.out.println("LibraryItemPaneController.firstRun()");
        setStackPane(stackPane);

        metaName.textProperty().addListener(changedHandler);
        metaAuthor.textProperty().addListener(changedHandler);
        metaDescription.textProperty().addListener(changedHandler);
        metaTags.textProperty().addListener(changedHandler);
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
        System.out.println("EditorViewStackController.registerObservers()");
        getController().getDispatcher().register(OnLibraryModelSelectedItemChange.class,
                new EventObserver<OnLibraryModelSelectedItemChange>() {
                    @Override
                    public void trigger(OnLibraryModelSelectedItemChange object) {
                        sceneItemChanged(libraryItemModel.getSelectedItem());
                    }
                });
    }

    protected void sceneItemChanged(final LibraryItem item) {
        if (item == null) {
            if (libraryItem == null)
                return;
            //System.out.println("EditorViewStackController.sceneItemChanged( UNSET )");
            //reset or loading a new library
            libraryItem.unBindBidirectional("metadataInfo.name", metaName.textProperty());
            libraryItem.unBindBidirectional("metadataInfo.author", metaAuthor.textProperty());
            libraryItem.unBindBidirectional("metadataInfo.description",
                    metaDescription.textProperty());
            libraryItem.unBindBidirectional("metadataInfo.tagsString", metaTags.textProperty());

            metaName.setText("");
            metaAuthor.setText("");
            metaDescription.setText("");
            metaTags.setText("");

            libraryItem = null;
            return;
        }

        if (libraryItem == null) {
            //System.out.println("EditorViewStackController.sceneItemChanged( SETUP )");
            libraryItem = new BeanPathAdapter<LibraryItem>(item);
            libraryItem.bindBidirectional("metadataInfo.name", metaName.textProperty());
            libraryItem.bindBidirectional("metadataInfo.author", metaAuthor.textProperty());
            libraryItem.bindBidirectional("metadataInfo.description",
                    metaDescription.textProperty());
            libraryItem.bindBidirectional("metadataInfo.tagsString", metaTags.textProperty());
        } else {
            // System.out.println(item);
            //System.out.println("EditorViewStackController.sceneItemChanged( SET )");
            libraryItem.setBean(item);
        }
    }

}
