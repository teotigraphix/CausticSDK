
package com.teotigraphix.caustic.preferences;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import com.cathive.fx.guice.PersistentProperty;
import com.cathive.fx.guice.PersistentProperty.NodeType;
import com.google.inject.Singleton;

@Singleton
public class WindowPreferences {
    @PersistentProperty(clazz = WindowPreferences.class, key = "x", type = NodeType.USER_NODE)
    private DoubleProperty x = new SimpleDoubleProperty();

    public DoubleProperty xProperty() {
        return x;
    }

    public Double getX() {
        return x.get();
    }

    public void setX(Double x) {
        this.x.set(x);
    }

    @PersistentProperty(clazz = WindowPreferences.class, key = "y", type = NodeType.USER_NODE)
    private DoubleProperty y = new SimpleDoubleProperty();

    public DoubleProperty yProperty() {
        return y;
    }

    public Double getY() {
        return y.get();
    }

    public void setY(Double y) {
        this.y.set(y);
    }

    @PersistentProperty(clazz = WindowPreferences.class, key = "width", type = NodeType.USER_NODE)
    private DoubleProperty width = new SimpleDoubleProperty();

    public DoubleProperty widthProperty() {
        return width;
    }

    public Double getWidth() {
        return width.get();
    }

    public void setWidth(Double width) {
        this.width.set(width);
    }

    @PersistentProperty(clazz = WindowPreferences.class, key = "height", type = NodeType.USER_NODE)
    private DoubleProperty height = new SimpleDoubleProperty();

    public DoubleProperty heightProperty() {
        return height;
    }

    public Double getHeight() {
        return height.get();
    }

    public void setHeight(Double height) {
        this.height.set(height);
    }

    public WindowPreferences() {
    }

}
