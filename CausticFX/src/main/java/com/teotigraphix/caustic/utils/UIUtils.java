
package com.teotigraphix.caustic.utils;

import java.util.Collection;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.control.gauge.Led;

public class UIUtils {
    public static Button createControlButton(String id, Group parent, final Runnable action,
            Rectangle2D metrics) {
        Button button = new Button();
        button.setId(id);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                action.run();
            }
        });
        button.setLayoutX(metrics.getMinX());
        button.setLayoutY(metrics.getMinY());
        button.setPrefSize(metrics.getWidth(), metrics.getHeight());
        parent.getChildren().add(button);
        return button;
    }

    public static Button createControlButton(String id, Pane parent, final Runnable action,
            Rectangle2D metrics) {
        Button button = new Button();
        button.setId(id);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                action.run();
            }
        });
        button.resizeRelocate(metrics.getMinX(), metrics.getMinY(), metrics.getWidth(),
                metrics.getHeight());
        parent.getChildren().add(button);
        return button;
    }

    public static ToggleButton createControlToggleButton(String id, Group parent,
            final Runnable action, Rectangle2D metrics) {
        ToggleButton button = new ToggleButton();
        button.setId(id);
        button.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                action.run();
            }
        });
        button.setLayoutX(metrics.getMinX());
        button.setLayoutY(metrics.getMinY());
        button.setPrefSize(metrics.getWidth(), metrics.getHeight());
        parent.getChildren().add(button);
        return button;
    }

    public static void setOn(List<Led> list, int selected) {
        for (Led led : list)
            led.setOn(false);
        list.get(selected).setOn(true);
    }

    public static void createLeds(Pane parent, List<Led> leds, int count, double size, Color color) {
        for (int i = 0; i < count; i++) {
            Led led = new Led();
            led.setMinSize(size, size);
            led.setColor(color);
            led.setFrameVisible(false);
            parent.getChildren().add(led);
            leds.add(led);
        }
    }

    public static void setSelected(Collection<ToggleButton> nodes, boolean selected) {
        for (ToggleButton node : nodes)
            node.setSelected(selected);
    }

    public static void setDisable(Collection<? extends Node> nodes, boolean disable) {
        for (Node node : nodes)
            node.setDisable(disable);
    }

    public static void setVisible(Collection<? extends Node> nodes, boolean visible) {
        for (Node node : nodes)
            node.setVisible(visible);
    }

    public static void layout(Control node, int x, int y, int width, int height) {
        node.setLayoutX(x);
        node.setLayoutY(y);
        node.setPrefWidth(width);
        node.setPrefHeight(height);
    }

}
