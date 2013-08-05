
package com.teotigraphix.libraryeditor.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import org.androidtransfuse.event.EventObserver;

import com.cathive.fx.guice.FXMLController;
import com.google.inject.Inject;
import com.teotigraphix.caustic.model.BeanPathAdapter;
import com.teotigraphix.caustic.model.BeanPathAdapter.FieldPathValue;
import com.teotigraphix.caustic.ui.controller.ViewStackController;
import com.teotigraphix.libraryeditor.model.LibraryModel;
import com.teotigraphix.libraryeditor.model.LibraryModel.LibraryItemProxy;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelRefresh;
import com.teotigraphix.libraryeditor.model.LibraryModel.OnLibraryModelSelectedItemChange;

@FXMLController
public class EditorViewStackController extends ViewStackController {

    private BeanPathAdapter<LibraryItemProxy> libraryItem;

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

    @Override
    public void onRegister() {
        super.onRegister();
    }

    private ChangeListener<String> changedHandler = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue,
                String newValue) {
        }
    };

    @Override
    protected void registerObservers() {
        super.registerObservers();
        //System.out.println("EditorViewStackController.registerObservers()");
        getController().getDispatcher().register(OnLibraryModelSelectedItemChange.class,
                new EventObserver<OnLibraryModelSelectedItemChange>() {
                    @Override
                    public void trigger(OnLibraryModelSelectedItemChange object) {
                        sceneItemChanged(libraryItemModel.getSelectedItem());
                    }
                });
    }

    protected void sceneItemChanged(final LibraryItemProxy libraryItemProxy) {
        if (libraryItemProxy == null) {
            if (libraryItem == null)
                return;
            System.out.println("EditorViewStackController.sceneItemChanged( UNSET )");
            //reset or loading a new library
            libraryItem.unBindBidirectional("name", metaName.textProperty());
            libraryItem.unBindBidirectional("author", metaAuthor.textProperty());
            libraryItem.unBindBidirectional("description", metaDescription.textProperty());
            libraryItem.unBindBidirectional("tagsString", metaTags.textProperty());

            metaName.setText("");
            metaAuthor.setText("");
            metaDescription.setText("");
            metaTags.setText("");

            libraryItem = null;
            return;
        }
        getController().getDispatcher().trigger(new OnLibraryModelRefresh());
        if (libraryItem == null) {

            libraryItem = new BeanPathAdapter<LibraryItemProxy>(libraryItemProxy);
            libraryItem.bindBidirectional("name", metaName.textProperty());
            libraryItem.bindBidirectional("author", metaAuthor.textProperty());
            libraryItem.bindBidirectional("description", metaDescription.textProperty());
            libraryItem.bindBidirectional("tagsString", metaTags.textProperty());
            libraryItem.fieldPathValueProperty().addListener(new ChangeListener<FieldPathValue>() {
                @Override
                public void changed(ObservableValue<? extends FieldPathValue> arg0,
                        FieldPathValue arg1, FieldPathValue arg2) {
                    getController().getDispatcher().trigger(new OnLibraryModelRefresh());
                }
            });

        } else {
            // System.out.println(item);
            System.out.println("EditorViewStackController.sceneItemChanged( SET )");
            libraryItem.setBean(libraryItemProxy);
        }
    }

    @Override
    public void create(Pane root) {
        setStackPane(stackPane);

        metaName.textProperty().addListener(changedHandler);
        metaAuthor.textProperty().addListener(changedHandler);
        metaDescription.textProperty().addListener(changedHandler);
        metaTags.textProperty().addListener(changedHandler);
    }

}
