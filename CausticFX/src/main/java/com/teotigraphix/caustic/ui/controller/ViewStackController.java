
package com.teotigraphix.caustic.ui.controller;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import com.google.inject.Inject;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.core.MediatorBase;

public class ViewStackController extends MediatorBase {

    @Inject
    ICaustkApplicationProvider provider;

    //----------------------------------
    // selectedIndex
    //----------------------------------

    private int selectedIndex = -1;

    public final int getSelectedIndex() {
        return selectedIndex;
    }

    public final void setSelectedIndex(int value) {
        if (value == selectedIndex)
            return;
        selectedIndex = value;
        setVisibleChild(selectedIndex);
        if (toggleBar != null) {
            ToggleButton button = (ToggleButton)toggleBar.getChildrenUnmodifiable().get(
                    selectedIndex);
            button.setSelected(true);
        }
    }

    //----------------------------------
    // stackPane
    //----------------------------------

    private StackPane stackPane;

    protected StackPane getStackPane() {
        return stackPane;
    }

    protected void setStackPane(StackPane value) {
        stackPane = value;
    }

    //----------------------------------
    // toggleBar
    //----------------------------------

    private Parent toggleBar;

    protected Parent getToggleBar() {
        return toggleBar;
    }

    protected void setToggleBar(Parent value) {
        toggleBar = value;

        // stop double selections
        for (Node child : toggleBar.getChildrenUnmodifiable()) {
            final ToggleButton button = (ToggleButton)child;
            button.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                public void handle(final MouseEvent e) {
                    if (button.isSelected()) {
                        e.consume();
                    }
                }
            });
            button.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                public void handle(final KeyEvent e) {
                    if (button.isSelected() && e.getCode() == KeyCode.SPACE) {
                        e.consume();
                    }
                }
            });
        }
    }

    public ViewStackController() {
    }

    @Inject
    public ViewStackController(ICaustkApplicationProvider provider) {
        super(provider);
    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        // no sub components are created
//        //setController(provider.get().getController());
//    }

    protected Node getChild(int index) {
        return stackPane.getChildren().get(index);
    }

    protected void setVisibleChild(int index) {
        for (Node node : stackPane.getChildren()) {
            node.setVisible(false);
        }
        getChild(index).setVisible(true);
    }

    protected ToggleButton getButton(int index) {
        return (ToggleButton)toggleBar.getChildrenUnmodifiable().get(index);
    }

    protected int getButtonIndex(ToggleButton button) {
        return toggleBar.getChildrenUnmodifiable().indexOf(button);
    }
}
